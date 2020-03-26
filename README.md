# 前置条件

1、安装 [Node.js](https://nodejs.org/en/download/)，自带了 npm

2、安装 Vue CLI：

```bash
npm install -g @vue/cli
```

# 运行项目

1、进入项目根目录，执行 Maven 打包命令：

```bash
mvn clean package
```

2、运行完整（包含前端内容）的 Spring Boot 项目：

```bash
mvn --projects backend spring-boot:run
```
