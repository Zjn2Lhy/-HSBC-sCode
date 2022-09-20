# HSBC-sCode
3.What do you think about coding in Spark SQL, RDD, DataFrame and DataSet? 
	1).RDD是Spark对于分布式数据模型的抽象，DF是带数据模式的结构化分布式数据集，类似于传统数据库中的一张表，RDD不带数据模式或者说是泛型的
	2).RDD API的执行引擎是Spark Core，其Spark Core的本质是负责任务的调度、计算、及存储;DF API优化引擎是SparkSQL，包括Catalyst执行过程优化和Tungsten数据结构优化。两者API的区别在于一个提供标量算子一个高阶算子和两者底层优化引擎不一致。
	3).之前子框架如Streaming，mlib,graph都是采用RDD API来编写，现在都是采用DF API来重新编写。
	4).调用DF API生成DF，但DF 的action算子触发执行后最终还是生成RDD，通过Spark Core框架来进行调度计算。DF API+SparkSQL代替之前的RDD API,目的就是为了提供更简单的API，让Spark做统一优化，在rdd计算时更高效.

4.Given 10 boxes (or 10 nodes), there are 64G memory, 4 vcores, 256G hard disk for each box. How would you like to set up a cluster for above jobs? What will be your spark-submit looks like? Why do you do so? (There is no standard answer)

I will set up a cluster for above jobs .

MY Answer:

spark-submit  \
--master yarn --deploy-mode cluster \
--num-executors 10 \
--executor-cores 2 \
--executor-memory 32g \
--driver-memory 2g \
--class cn.zjn.demo03 \
--name Cal_MaxSalary \
/opt/airflow/jar/zjn/bigdata.jar \/opt/airflow/jar/zjn/bigdata.jar \

Analysis:

Step-1:总配置 10个节点,每个节点64G,4Vcores ,256G 硬盘
memory :10*64 640G
disk :10*256  2560G
core :   4*10 40core  

Step-2: 考虑linux运行及程序,Hadoop,Yarn等守护进程等,约占5%-10% ,没太预留1vcore 和 4G memory
为每个执行器分配 3vcore
--executor-cores = 3
每个节点除去预留核心 ,剩下 : 4-1 = 3 ;
集群中核心的可用总数 : 10 * 3vcores = 30 vcores;

-Step-3:-num-executors = 集群中的核心可用总数 / 每个executors分配核心数	
				=30Vcore / 3
				=10
每个节点的executors数目 : 3 / 3 = 1

Step-4:集群中每个节点可使用的总内存数: 64G - 4G = 60G
--executor-memory = 每个executos的内存 = 60 /  每个节点的executors数目
				  = 60 / 1 =  60G			  
预留 off heap overhead = 60g * 7% = 4.2 G
实际 --executor-memory = 60G - 4.2G = 35.8G
