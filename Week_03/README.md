# 作业项目简介：

使用netty实现httpclient功能。
实现Filter功能。
实现Router功能

访问地址：http://localhost:8888/api/hello

filter功能：过滤所有访问非“/api/hello”的请求，其他请求返回404

Router功能：随机的跳转到http://localhost:8088/api/hello或http://www.baidu.com网址