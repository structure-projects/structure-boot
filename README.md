# Structure Boot

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-boot-parent.svg)](https://search.maven.org/search?q=g:cn.structured)
[![Java](https://img.shields.io/badge/Java-8+-green.svg)](https://www.oracle.com/java/)

Structure Boot 是一个基于 Spring Boot 的快速开发框架，提供了一系列开箱即用的 Starter 组件，帮助开发者快速构建企业级应用。

## 📌 版本说明

| 版本系列      | Spring Boot 版本  | JDK 版本  | 状态                  |
| --------- | --------------- | ------- | ------------------- |
| **1.3.x** | Spring Boot 3.x | JDK 21+ | ✅ 最新版本，当前 1.3.1     |
| **1.2.x** | Spring Boot 2.x | JDK 8+  | ✅ 持续支持中，最新版本 1.2.10 |

## 🚀 功能特性

- **Web 开发**: RESTful API 支持、统一异常处理、参数校验、Swagger 文档
- **数据访问**: MyBatis 增强、MyBatis-Plus 集成、代码生成器
- **缓存支持**: Redis 分布式锁、Redisson 高级缓存
- **文件存储**: MinIO 对象存储集成
- **日志系统**: 统一日志配置、AOP 日志记录
- **微服务**: RPC 调用支持、OAuth2 客户端
- **多租户**: 多租户上下文管理、多种租户识别方式

## 📦 组件列表

| 组件                               | 描述               | 版本                                                                                                                                                                                                  |
| -------------------------------- | ---------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `structure-common`               | 公共工具类和常量         | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-common.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-common)                             |
| `structure-restful-web-starter`  | Web 开发启动器        | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-restful-web-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-restful-web-starter)   |
| `structure-mybatis-starter`      | MyBatis 启动器      | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-mybatis-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-mybatis-starter)           |
| `structure-mybatis-plus-starter` | MyBatis-Plus 启动器 | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-mybatis-plus-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-mybatis-plus-starter) |
| `structure-redis-starter`        | Redis 启动器        | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-redis-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-redis-starter)               |
| `structure-redisson-starter`     | Redisson 启动器     | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-redisson-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-redisson-starter)         |
| `structure-minio-starter`        | MinIO 启动器        | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-minio-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-minio-starter)               |
| `structure-log-starter`          | 日志启动器            | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-log-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-log-starter)                   |
| `structure-rpc-starter`          | RPC 启动器          | [![Maven Central](https://img.shields.io/maven-central/v/cn.structured/structure-rpc-starter.svg)](https://search.maven.org/search?q=g:cn.structured+AND+a:structure-rpc-starter)                   |

## 🚀 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- Spring Boot 2.7.x+

### 1. 创建 Spring Boot 项目

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>1.0.0</version>

    <properties>
        <spring.boot.version>2.7.18</spring.boot.version>
        <structure.version>1.2.3</structure.version>
    </properties>

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

### 2. 添加依赖

根据需要使用相应的 Starter：

```xml
<!-- Web 开发 -->
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-restful-web-starter</artifactId>
</dependency>

<!-- MyBatis 支持 -->
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-mybatis-starter</artifactId>
</dependency>

<!-- Redis 支持 -->
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-redis-starter</artifactId>
</dependency>
```

### 3. 创建启动类

```java
@SpringBootApplication
@EnableSimpleGlobalException  // 开启统一异常处理
@EnableSwagger               // 开启 Swagger 文档
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 4. 配置文件

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

  redis:
    host: localhost
    port: 6379
    password:
    database: 0

structure:
  mybatis:
    plugin:
      generate-id-type: snowflake
      data-center: 0
      machine: 0
```

## 📚 组件使用指南

### Web 开发 (structure-restful-web-starter)

提供 RESTful API 开发支持，包括统一异常处理、参数校验、Swagger 文档等。

#### 统一异常处理

```java
@SpringBootApplication
@EnableSimpleGlobalException  // 简易版异常处理
// 或者
@EnableFatherGlobalException  // 多级码异常处理
public class Application {
    // ...
}
```

#### 统一返回结果

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users/{id}")
    public ResResultVO<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return IResultUtil.success(user);
    }
}
```

#### 参数校验

```java
@PostMapping("/users")
public ResResultVO<User> createUser(@Valid @RequestBody User user) {
    // 自动进行参数校验，校验失败会返回统一错误信息
    return IResultUtil.success(userService.create(user));
}
```

### MyBatis 增强 (structure-mybatis-starter)

提供 MyBatis 的增强功能，包括自动 ID 生成、创建/更新时间注入等。

#### 自动 ID 生成

```java
@Entity
public class User {
    @Id  // 自动生成 ID
    private Long id;

    @CreateTime  // 自动注入创建时间
    private Date createTime;

    @UpdateTime  // 自动注入更新时间
    private Date updateTime;

    // getters and setters
}
```

#### 配置

```yaml
structure:
  mybatis:
    plugin:
      generate-id-type: snowflake # none, uuid, snowflake
      data-center: 0
      machine: 0
```

### MyBatis-Plus 增强 (structure-mybatis-plus-starter)

基于 MyBatis-Plus 的增强功能，包括批量操作、联表查询、代码生成等。

#### 批量插入

```java
@Mapper
public interface UserMapper extends IBaseMapper<User> {
    // 继承 IBaseMapper 即可使用批量插入方法
}

@Service
public class UserService {

    public void batchInsert(List<User> users) {
        userMapper.insertList(users);  // 批量插入
    }
}
```

#### 联表查询

```java
@Entity
public class User {
    @TableField(exist = false)
    @FieldJoin(value = {
        @Join(group = {UserGroup.class},
              joinTarget = Department.class,
              aliasName = "dept",
              columns = "name",
              value = {
                  @JoinCondition(currentColumn = "dept_id", targetColumn = "id")
              })
    })
    private String departmentName;
}
```

### Redis 分布式锁 (structure-redis-starter)

提供基于 Redis 的分布式锁实现。

#### 注解方式

```java
@Service
public class OrderService {

    @RedisLock("#orderId")  // 使用订单ID作为锁的key
    public void processOrder(String orderId) {
        // 业务逻辑
    }

    @RedisLock("#user.id:_#orderId")  // 多个参数组合作为key
    public void processUserOrder(User user, String orderId) {
        // 业务逻辑
    }
}
```

#### 手动方式

```java
@Service
public class OrderService {

    @Resource
    private IDistributedLock distributedLock;

    public void processOrder(String orderId) {
        String lockKey = "order:" + orderId;

        if (distributedLock.lock(lockKey)) {
            try {
                // 业务逻辑
            } finally {
                distributedLock.releaseLock(lockKey);
            }
        }
    }
}
```

### Redisson 高级缓存 (structure-redisson-starter)

基于 Redisson 的高级缓存功能，支持对象缓存、集合缓存、Map 缓存等。

#### 写缓存

```java
@RestController
public class CacheController {

    @PostMapping("/cache")
    @WCache(key = "#user.id",
            isObjCache = true,
            list = @CList(listKeyName = "user-list",
                         isList = true,
                         size = 100,
                         time = @CTime(isTime = true, time = 10)),
            map = @CMap(mapKey = "user-map",
                        isMap = true,
                        time = @CTime(isTime = true, time = 100)))
    public User cacheUser(@RequestBody User user) {
        return user;
    }
}
```

#### 读缓存

```java
@RestController
public class CacheController {

    @GetMapping("/users/{id}")
    @RCache(key = "#id")
    public User getUser(@PathVariable String id) {
        // 如果缓存中没有，会执行此方法并将结果写入缓存
        return userService.getById(id);
    }

    @GetMapping("/users")
    @RListCache(key = "user-list",
                mapKey = "user-map",
                value = CList.ListType.MAP)
    public List<User> getAllUsers() {
        return userService.list();
    }
}
```

### MinIO 文件存储 (structure-minio-starter)

提供 MinIO 对象存储的集成支持。

#### 配置

```yaml
structure:
  minio:
    url: http://localhost:9000
    access-key: your-access-key
    secret-key: your-secret-key
    endpoint-enable: true # 开启自动端点
```

#### 使用

```java
@Service
public class FileService {

    @Resource
    private MinioTemplate minioTemplate;

    public void uploadFile(String bucketName, String fileName, InputStream inputStream, long size) {
        minioTemplate.putObject(bucketName, fileName, inputStream, size, "application/octet-stream");
    }

    public String getFileUrl(String bucketName, String fileName, int expires) {
        return minioTemplate.getObjectURL(bucketName, fileName, expires);
    }
}
```

## 🔧 开发工具

### 代码生成器

使用 MyBatis-Plus 代码生成器快速生成 CRUD 代码：

```xml
<plugin>
    <groupId>cn.structured</groupId>
    <artifactId>structure-mybatis-plus-generate</artifactId>
    <configuration>
        <configurationFile>${basedir}/src/main/resources/generator-config.yaml</configurationFile>
    </configuration>
</plugin>
```

配置文件示例：

```yaml
globalConfig:
  author: your-name
  open: false
  idType: NONE
  dateType: ONLY_DATE
  enableCache: false
  activeRecord: false
  baseResultMap: true
  baseColumnList: true
  swagger2: false
  fileOverride: true

dataSourceConfig:
  url: jdbc:mysql://localhost:3306/demo
  driverName: com.mysql.cj.jdbc.Driver
  username: root
  password: password

packageConfig:
  parent: com.example.demo
  entity: entity
  service: service
  serviceImpl: service.impl
  mapper: mapper
  xml: mapper
  controller: controller

strategyConfig:
  naming: underline_to_camel
  columnNaming: underline_to_camel
  entityLombokModel: true
  superMapperClass: com.baomidou.mybatisplus.core.mapper.BaseMapper
  superServiceClass: com.baomidou.mybatisplus.extension.service.IService
  superServiceImplClass: com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
  controllerMappingHyphenStyle: true
  restControllerStyle: true
```

## 📖 示例项目

项目提供了丰富的示例代码，位于 `structure-example` 目录下：

- `structure-boot-example`: 基础启动示例
- `structure-redis-example`: Redis 使用示例
- `structure-mybatis-starter-example`: MyBatis 使用示例
- `structure-minio-example`: MinIO 使用示例
- `structure-log-example`: 日志配置示例

## 📄 开源协议

本项目采用 [Apache License 2.0](LICENSE) 开源协议。

## 🤝 贡献指南

我们欢迎所有形式的贡献，包括但不限于：

- 🐛 Bug 报告
- 💡 功能建议
- 📝 文档改进
- 🔧 代码贡献

### 贡献步骤

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

### 代码规范

- 遵循 Java 编码规范
- 添加必要的注释和文档
- 确保代码通过所有测试
- 提交信息要清晰明了

### 问题反馈

如果您在使用过程中遇到问题，请通过以下方式反馈：

- 在 GitHub 上创建 Issue
- 提供详细的错误信息和复现步骤
- 包含您的环境信息（Java 版本、Spring Boot 版本等）

## 📞 联系我们

- 项目地址: <https://github.com/structure-projects/structure-boot>
- 问题反馈: [Issues](https://github.com/structure-projects/structure-boot/issues)
- 邮箱: <361648887@qq.com>

## ⭐ 支持我们

如果这个项目对您有帮助，请给我们一个 ⭐ Star！

***

**Structure Boot** - 让 Spring Boot 开发更简单、更高效！
