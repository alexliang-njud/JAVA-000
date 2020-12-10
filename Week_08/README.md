WEEK 08

## 一、split-db 分库分表练习

作业说明

设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表并在新结构在演示常见的增删改查操作

### 环境配置  
**设置MySQL**
   
   创建两个数据库
   `create database demo_ds_0`
   `create database demo_ds_1`
   
ShardingSphere Proxy 5.0.0 alpha 设置

1.下载ShardingSphere-Proxy，下载完成后放到自己相应的目录下

2.下载MySQL-connect.jar,下载完成后将jar文件放到Sharding根目录的lib目录下
    下面需要配置两个文件：server.yaml、config-sharding.yaml

配置好以后，使用直接进入sharding的根目录下的bin目录中运行：start.bat,看到start success表示运行成功

使用mysql命令或者mysqlworkbench连接上sharding，运行下面的SQL语句生成测试的表，运行成功看到日志中一大批SQL语句，
    
    CREATE TABLE IF NOT EXISTS `t_order` (
        `order_id` int(11) NOT NULL,
        `user_id` int(11) NOT NULL,
        PRIMARY KEY (`order_id`)
    ) ENGINE=InnoD

实体类、Mapper设置这里就不详细赘述了，看具体工程即可



