package cn.zjn

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
   * 方法描述
   *
   * 如果员工表的数据大小为100M，员工工资表的数据大小为200G。
   * 如何通过Spark RDD或DataFrame/DataSet实现上述查询?
   * 避免数据倾斜
   */
object demo03 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .set("spark.sql.codegen", "true")
      .set("spark.sql.inMemoryColumnStorage.compressed","true")
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .set("spark.sql.inMemoryColumnStorage.batchSize","5000")
      .set("spark.sql.crossJoin.enabled", "true")
      // .set("spark.sql.autoBroadcastJoinThreshold","31457280")   // 30M 以下的表自动广播
      .set("spark.sql.autoBroadcastJoinThreshold","-1")
      .set("spark.debug.maxToStringFields", "100")
      .set("spark.port.maxRetries", "100")      //
//      .set("spark.sql.baroadcastTimeout","36000") //防止单个DF过大，报错
//      .set("spark.storage.memoryFraction","0.3")  //降低cache 内存 避免OOM
//      .set("spark.default.parallelism", "24")    //设置为core的2-3倍
//      .set("spark.reducer.maxSizeInFlight","12") //控制shuffle reduce端缓冲大小，避免OOM  cache()内存为24M,默认48

    val sparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .master("local[*]")
      .config(sparkConf)
      .getOrCreate()

    val sc = sparkSession.sparkContext
    sc.setLogLevel("WARN")

    //读取表格 , 预处理
    val empDF = sparkSession.read.parquet("/dwd/employee")
    val salaryDF = sparkSession.read.parquet("/dwd/salary").select("employee_id","salary","date")
    empDF.createOrReplaceTempView("employee")
    salaryDF.createOrReplaceTempView("salary")

    //  实现sum/count
    import org.apache.spark.sql.functions._
    val avg_udf = udf((sm:Long,cnt:Long)=>sm.toDouble/cnt.toDouble)

    //todo 1.按月计算，谁是工资最高的经理?
    var sql :String= null
    //step-one:先计算出经理的id 与薪资
    sql =
      """
        |SELECT * FROM salary
        |WHERE employee_id
        |IN ( SELECT employee_id FROM employee WHERE manager_id IS NOT NULL AND manager_id <> '' )
        |
        |""".stripMargin
    val oneDF = sparkSession.sql(sql)
    //step-two 获取平均工资,并与员工表关联,获取名称
    val tmp2DF = oneDF.withColumn("avgSalary", avg_udf(col("salary"), col("salary")))
      .join(empDF, Seq("employee_id"), "left")
    //step-three 获取最高工资经理
      tmp2DF.orderBy(desc("avgSalary")).take(1)

    //todo 2.谁是年薪最高的非经理?
    //setp-one 1.先计算出非经理的id 与薪资
    sql =
      """
        |SELECT * FROM salary
        |WHERE employee_id
        |IN ( SELECT employee_id FROM employee WHERE manager_id IS  NULL or manager_id = '' )
        |
        |""".stripMargin
    val twoDF = sparkSession.sql(sql)

  }
}
