package cn.zjn

import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ListBuffer

/**
   * 方法描述 第二题
 *
   * @param:
   * @return:
   * @author: zjn
   * @date: 2022/6/21
   */
object demo01 {
    def main(args: Array[String]): Unit = {
      val spark = SparkSession.builder()
        .appName(this.getClass.getSimpleName.stripSuffix("$"))
        .master("local[*]")
        .getOrCreate()

        import spark.implicits._
        //读取数据
        spark.sparkContext.setLogLevel("WARN")
        val in = spark.read.textFile("src/data/source.txt")
        val ds = in.map(x => {
            val arr = x.split(",")
            resObj(arr.apply(0), arr.apply(1), arr.apply(2), arr.apply(3))
        })
        val res = ds
            .groupByKey(x => {
                x.id + "_" + x.name
            })
            .mapGroups((k, vs) => {
                val sIter =
                    vs.toList.sortBy(x => { x.date_of_birth > x.date_of_birth }).iterator
                val lis = new ListBuffer[String]
                val cplList = new ListBuffer[resObj]
                val j = sIter.next()
                val one = j.date_of_birth
                val id = j.id
                val name = j.name
              if (one.length > 7) {
                lis.append(j.order_item)
              } else cplList.append(resObj(id, name, one, j.order_item))
                while (sIter.hasNext) {
                    val dd = sIter.next()
                    if (
                      dd.date_of_birth
                          .equalsIgnoreCase("no value") || one.length == dd.date_of_birth.length
                    ) {
                        val date_of_birth = dd.date_of_birth
                        val order_item = dd.order_item
                        cplList.append(resObj(id, name, date_of_birth, order_item))
                    } else {
                      val d = dd.order_item
                      lis.append(d)
                    }
                }
                cplList.append(
                  resObj(id, name, one, lis.toList.mkString(","))
                )
                cplList
            })
            .flatMap(l => l)
        res.orderBy("id").show(false)
    }

  //创建对象接收
    case class resObj(
        id: String,
        name: String,
        date_of_birth: String,
        order_item: String
    )
}
