# 前置条件

1、安装 [Node.js](https://nodejs.org/en/download/)，自带了 npm

2、安装 Vue CLI：

```bash
npm install -g @vue/cli
```

# 打包项目

1、进入项目根目录，执行 Maven 打包命令：

```bash
mvn clean package
```

# 部署方式

1、将 `rest.war` 放置于 `tomcat/webapps` 目录中

2、将 `rest-conf` 放置于 `tomcat/lib` 目录中

3、启动 Tomcat
