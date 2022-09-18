package cn.zjn

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object demo02 {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .master("local[*]")
      .getOrCreate()

    val sc = sparkSession.sparkContext
    sc.setLogLevel("WARN")

    //获取 df
    val orderRDD: RDD[String] = sc.textFile("src/data/source.txt")
    val tupleRDD = orderRDD.map(line => {
      val arr = line.split(",")
      val id = arr(0)
      val name = arr(1)
      val date_of_birth = arr(2)
      val order_item = arr(3)
      Tuple4(id, name, date_of_birth, order_item)
    })
    import sparkSession.implicits._
    val orderDF = tupleRDD.toDF("id", "name", "date_of_birth", "order_item")
    orderDF.createOrReplaceTempView("order")
    orderDF.show(20)
    //no value
    val temp = orderDF.where("date_of_birth = 'no value'")

    //3:使用sql分割-自定义
    sparkSession.udf.register(
      "order",
      (dataStr1:String,dataStr2:String,it1:String,it2:String)=>{
        val str =List
        if(dataStr1.equals(dataStr2) && dataStr1.length==dataStr2.length) {

           }else if(dataStr1.length!=dataStr2.length && dataStr1.contains(dataStr2)){

        }
        }

    )

    sparkSession.sql(
      """
        |select t2.id,t2.name,t2.date_of_birth,concat_ws(',',t1.order_item,t2.order_item) as order_item from
        |temp_01 t1 join order t2 t1.id = t2.id and t1.name = t2.name and t1.order_item = t2.order_item
        |where t1.date_of_birth
        |
        |""".stripMargin)

  }
}
