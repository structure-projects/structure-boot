# structure-boot

structure-boot

## 功能介绍

对structure生态依赖的封装

- structure-restful-web
- structure-mybatis
- structure-redis
- structure-redisson
- structure-log
- structure-jwt-security

## 如何使用

### pom引用 ###

引用最新文档版本的依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.structured</groupId>
    <artifactId>structure-boot-example</artifactId>
    <version>1.0.1</version>

    <properties>
        <spring.boot.version>2.1.4.RELEASE</spring.boot.version>
        <!--<structure.version>1.1.0.RELEASE</structure.version>-->
        <structure.version>1.1.0.SNAPSHOT</structure.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-restful-web-starter</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-parent</artifactId>
                <version>${spring.boot.version}</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-boot-parent</artifactId>
                <version>${structure.version}</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```