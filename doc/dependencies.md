# structure-dependencies #
structure-dependencies : 主要整合spring-boot、spring-cloud、alibaba-cloud、k8s等引用依赖的版本，防止在快速构建项目时出现依赖冲突的问题

## 版本依赖对照表 ##
|  structure.version  |  spring-boot.version  |  spring-cloud.version | alibaba-cloud.version | spring-alibaba-cloud.version | kubernetes.version |
|  ----  | ----  | ---- | ---- | ---- |  ---- |
| 1.0.X  | 2.1.X.RELEASE | Greenwich.SR2 | 2.1.2.RELEASE | 0.9.0.RELEASE | 1.1.6.RELEASE|
## structure-dependencies 的使用 ##
### spring-boot 项目中使用 structure-dependencies ###
```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
               <groupId>cn.structured</groupId>
               <artifactId>structure-dependencies</artifactId>
               <version>${last.version}</version>
               <type>pom</type>
               <scope>import</scope>
           </dependency>
        </dependencies>
    </dependencyManagement>
```
### starter 启动器的使用 ### 
```xml
    <parent>
        <groupId>cn.structured</groupId>
        <artifactId>structure-dependencies</artifactId>
        <version>${last.version}</version>
    </parent>
```
