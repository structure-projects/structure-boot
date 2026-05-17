# structure-dependencies

structure-dependencies : 主要整合 spring-boot、spring-cloud、alibaba-cloud、k8s 等引用依赖的版本，防止在快速构建项目时出现依赖冲突的问题

## 版本依赖对照表

| structure.version | spring-boot.version | spring-cloud.version | alibaba-cloud.version | spring-alibaba-cloud.version | kubernetes.version |
| ----------------- | ------------------- | -------------------- | --------------------- | ---------------------------- | ------------------ |
| 1.0.X             | 2.1.X.RELEASE       | Greenwich.SR2        | 2.1.2.RELEASE         | 0.9.0.RELEASE                | 1.1.6.RELEASE      |
| 1.2.X             | 2.7.X.RELEASE       | 2021.0.5.0           | 2021.0.5.0            | 2021.0.5.0                   | 5.12.2             |
| 1.3.X             | 3.2.X.RELEASE       | 2023.0.1.0           | 2023.0.1.0            | 2023.0.1.0                   | 6.8.0              |

## structure-dependencies 的使用

### spring-boot 项目中使用 structure-dependencies

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
               <groupId>cn.structured</groupId>
               <artifactId>structure-dependencies</artifactId>
               <version>1.3.1</version>
               <type>pom</type>
               <scope>import</scope>
           </dependency>
        </dependencies>
    </dependencyManagement>
```

### starter 启动器的使用

```xml
    <parent>
        <groupId>cn.structured</groupId>
        <artifactId>structure-dependencies</artifactId>
        <version>1.3.1</version>
    </parent>
```
