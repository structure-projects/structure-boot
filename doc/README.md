# Structure Boot 组件使用文档

本文档详细介绍了 Structure Boot 框架中各个组件的使用方法、配置选项和示例代码。

## 📚 目录

- [快速开始](#快速开始)
- [Web 开发组件](#web-开发组件)
- [数据访问组件](#数据访问组件)
- [缓存组件](#缓存组件)
- [文件存储组件](#文件存储组件)
- [日志组件](#日志组件)
- [RPC 组件](#rpc-组件)
- [开发工具](#开发工具)

## 🚀 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- Spring Boot 2.7.x+

### 基础配置

在 `pom.xml` 中添加依赖管理：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-boot-parent</artifactId>
            <version>1.2.3</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 启动类配置

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 🌐 Web 开发组件

### structure-restful-web-starter

提供 RESTful API 开发支持，包括统一异常处理、参数校验、FastJson 序列化等。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-restful-web-starter</artifactId>
</dependency>
```

#### 功能特性

1. **统一异常处理**
   - 自动捕获并处理各类异常
   - 返回统一的错误响应格式

2. **统一返回结果**
   - `ResResultVO<T>`: 统一返回结果封装
   - 包含成功状态、错误码、消息和数据

3. **参数校验**
   - 自动参数校验
   - 统一错误信息返回

4. **FastJson 序列化**
   - 自动配置 FastJson 序列化
   - 支持 Long 转 String 和 null 值处理

#### 使用示例

##### 统一返回结果

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResResultVO<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            return ResultUtilSimpleImpl.success(user);
        } else {
            return ResultUtilSimpleImpl.fail("404", "用户不存在");
        }
    }

    @PostMapping
    public ResResultVO<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.create(user);
        return ResultUtilSimpleImpl.success(createdUser);
    }
}
```

## 🗄️ 数据访问组件

### structure-mybatis-starter

提供 MyBatis 的增强功能，包括自动 ID 生成、创建/更新时间注入等。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-mybatis-starter</artifactId>
</dependency>
```

#### 功能特性

1. **自动 ID 生成**
   - 支持雪花算法、UUID、数据库自增

2. **时间字段自动注入**
   - `@CreateTime`: 创建时间自动注入
   - `@UpdateTime`: 更新时间自动注入

#### 配置

```yaml
structure:
  mybatis:
    plugin:
      generate-id-type: snowflake # none, uuid, snowflake
      data-center: 0 # 数据中心码 (0-31)
      machine: 0 # 机器码 (0-31)
```

#### 使用示例

```java
public class User {

    private Long id;

    private String username;

    private String email;

    @CreateTime
    private Date createTime;

    @UpdateTime
    private Date updateTime;

    // getters and setters
}
```

#### 注意事项

1. 禁止使用匿名内部类做插入和修改操作
2. 需要生成时属性必须为 null
3. 注意生成的类型

### structure-mybatis-plus-starter

基于 MyBatis-Plus 的增强功能，包括批量操作、代码生成等。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-mybatis-plus-starter</artifactId>
</dependency>
```

#### 功能特性

1. **批量操作**
   - 继承 `BaseMapper<T>` 获得批量操作能力

2. **代码生成**
   - 基于配置文件的代码生成器
   - 支持自定义模板和策略

#### 使用示例

##### 批量插入

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper 即可使用批量操作方法
}

@Service
public class UserService {

    public void batchInsert(List<User> users) {
        userMapper.insertBatchSomeColumn(users);  // 批量插入
    }
}
```

##### 代码生成器配置

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

tableFill:
  is_deleted: INSERT
  create_time: INSERT
  update_time: INSERT_UPDATE
```

## 🗃️ 缓存组件

### structure-redis-starter

提供基于 Redis 的分布式锁实现。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-redis-starter</artifactId>
</dependency>
```

#### 功能特性

1. **注解式分布式锁**
   - `@RedisLock`: 基于 SpEL 表达式的锁 key 生成

2. **手动分布式锁**
   - `IDistributedLock`: 手动获取和释放锁

#### 使用示例

##### 注解方式

```java
@Service
public class OrderService {

    @RedisLock("#orderId")
    public void processOrder(String orderId) {
        // 业务逻辑
        System.out.println("处理订单: " + orderId);
    }

    @RedisLock("#user.id:_#orderId")
    public void processUserOrder(User user, String orderId) {
        // 多个参数组合作为锁的 key
        System.out.println("用户 " + user.getId() + " 处理订单: " + orderId);
    }
}
```

##### 手动方式

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
                System.out.println("处理订单: " + orderId);
            } finally {
                distributedLock.releaseLock(lockKey);
            }
        } else {
            System.out.println("获取锁失败，订单正在处理中");
        }
    }
}
```

### structure-redisson-starter

基于 Redisson 的高级缓存功能，支持对象缓存、集合缓存、Map 缓存等。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-redisson-starter</artifactId>
</dependency>
```

#### 功能特性

1. **写缓存注解**
   - `@WCache`: 写入缓存，支持对象、集合、Map 缓存

2. **读缓存注解**
   - `@RCache`: 读取对象缓存
   - `@RListCache`: 读取集合缓存
   - `@RCacheMap`: 读取 Map 缓存

3. **Redisson 客户端**
   - 直接使用 `RedissonClient` 进行高级操作

#### 使用示例

##### 写缓存

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
        System.out.println("缓存用户信息: " + user);
        return user;
    }
}
```

##### 读缓存

```java
@RestController
public class CacheController {

    @GetMapping("/users/{id}")
    @RCache(key = "#id")
    public User getUser(@PathVariable String id) {
        // 如果缓存中没有，会执行此方法并将结果写入缓存
        User user = new User();
        user.setId(id);
        user.setName("从数据库获取的用户");
        return user;
    }
}
```

##### Redisson 客户端使用

```java
@Service
public class CacheService {

    @Resource
    private RedissonClient redissonClient;

    public void setValue(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, 1, TimeUnit.HOURS);
    }

    public String getValue(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }
}
```

## 📁 文件存储组件

### structure-minio-starter

提供 MinIO 对象存储的集成支持。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-minio-starter</artifactId>
</dependency>
```

#### 功能特性

1. **自动端点**
   - 开启后自动提供基本的 MinIO 操作接口

2. **MinioTemplate**
   - 提供便捷的文件操作方法

#### 配置

```yaml
structure:
  minio:
    url: http://localhost:9000
    access-key: your-access-key
    secret-key: your-secret-key
    endpoint-enable: true # 开启自动端点
```

#### 使用示例

##### 使用 MinioTemplate

```java
@Service
public class FileService {

    @Resource
    private MinioTemplate minioTemplate;

    public void uploadFile(String bucketName, String fileName, InputStream inputStream, long size) {
        try {
            minioTemplate.putObject(bucketName, fileName, inputStream, size, "application/octet-stream");
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    public String getFileUrl(String bucketName, String fileName, int expires) {
        try {
            return minioTemplate.getObjectURL(bucketName, fileName, expires);
        } catch (Exception e) {
            throw new RuntimeException("获取文件URL失败", e);
        }
    }

    public void deleteFile(String bucketName, String fileName) {
        try {
            minioTemplate.removeObject(bucketName, fileName);
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }
}
```

## 📝 日志组件

### structure-log-starter

提供统一的日志配置和 AOP 日志记录功能。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-log-starter</artifactId>
</dependency>
```

#### 功能特性

1. **统一日志配置**
   - 提供标准的 logback 配置模板

2. **AOP 日志记录**
   - 自动记录方法调用日志
   - 支持自定义日志级别

#### 配置

```yaml
structure:
  log:
    level: INFO
    enable-aop: true
```

#### 使用示例

##### 基础日志配置

```xml
<!-- logback-spring.xml -->
<configuration>
    <include resource="classpath:logback-spring-line.xml"/>

    <springProfile name="dev">
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>

    <springProfile name="prod">
        <root level="WARN">
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>
</configuration>
```

##### AOP 日志记录

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users/{id}")
    public ResResultVO<User> getUser(@PathVariable Long id) {
        // 方法调用会自动记录日志
        User user = userService.getById(id);
        return ResultUtilSimpleImpl.success(user);
    }
}
```

## 🔌 RPC 组件

### structure-rpc-starter

提供 RPC 调用支持。

#### 依赖引入

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-rpc-starter</artifactId>
</dependency>
```

#### 功能特性

1. **RPC 调用**
   - 支持 HTTP 协议的 RPC 调用
   - 自动处理请求和响应

#### 使用示例

##### RPC 服务调用

```java
@Service
public class UserService {

    @Resource
    private UserClient userClient;

    public User getUserInfo(Long userId) {
        return userClient.getUserById(userId);
    }
}

@RpcClient(serviceUrl = "http://localhost:8080")
public interface UserClient {

    @RequestMapping("/api/users/{id}")
    User getUserById(@PathVariable("id") Long id);
}
```

## 🛠️ 开发工具

### 代码生成器

使用 MyBatis-Plus 代码生成器快速生成 CRUD 代码：

#### Maven 插件配置

```xml
<build>
    <plugins>
        <plugin>
            <groupId>cn.structured</groupId>
            <artifactId>structure-mybatis-plus-generate</artifactId>
            <configuration>
                <configurationFile>${basedir}/src/main/resources/generator-config.yaml</configurationFile>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

#### 生成命令

```bash
mvn structure-mybatis-plus-generate:generate
```

### 版本管理

使用 `structure-dependencies` 统一管理依赖版本：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-dependencies</artifactId>
            <version>1.1.0.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### 版本对照表

| structure.version | spring-boot.version |
|-------------------|---------------------|
| 1.0.X             | 2.1.X.RELEASE       |
| 1.2.X             | 2.7.X.RELEASE       |

## 📖 示例项目

项目提供了丰富的示例代码，位于 `structure-example` 目录下：

### 基础示例

- **structure-boot-example**: 基础启动示例
  - 展示如何配置和使用基础组件

### 数据访问示例

- **structure-mybatis-starter-example**: MyBatis 使用示例
  - 包含 MyBatis 和 MyBatis-Plus 的使用示例
- **structure-tk-mapper-starter-example**: TK Mapper 使用示例

### 缓存示例

- **structure-redis-example**: Redis 使用示例
  - 展示分布式锁的使用方法
- **structure-redisson-starter-example**: Redisson 使用示例
  - 展示高级缓存功能

### 存储示例

- **structure-minio-example**: MinIO 使用示例
  - 展示文件上传、下载、管理等操作

### 日志示例

- **structure-log-example**: 日志配置示例
  - 展示不同环境的日志配置

### Web 示例

- **structure-restful-web-example**: RESTful API 示例
  - 展示 Web 开发的最佳实践

### RPC 示例

- **structure-rpc-example**: RPC 调用示例
  - 展示微服务间的调用方式

## 🔧 配置参考

### 完整配置示例

```yaml
server:
  port: 8080

spring:
  application:
    name: structure-boot-demo

  datasource:
    url: jdbc:mysql://localhost:3306/demo?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
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

  minio:
    url: http://localhost:9000
    access-key: minioadmin
    secret-key: minioadmin
    endpoint-enable: true

  log:
    level: INFO
    enable-aop: true
```

## 🚨 常见问题

### 1. 依赖冲突

**问题**: 出现依赖版本冲突
**解决**: 使用 `structure-dependencies` 统一管理版本

### 2. 配置不生效

**问题**: 自定义配置没有生效
**解决**: 检查配置前缀是否正确，确保配置项在正确的命名空间下

### 3. 注解不工作

**问题**: 自定义注解没有生效
**解决**: 确保启动类上添加了相应的 `@Enable*` 注解

### 4. 缓存问题

**问题**: 缓存没有按预期工作
**解决**: 检查 Redis 连接配置，确保缓存注解配置正确

## 📞 技术支持

如果您在使用过程中遇到问题，请通过以下方式获取帮助：

1. 查看本文档和示例代码
2. 在 GitHub 上创建 Issue
3. 查看项目 Wiki 页面
4. 联系项目维护者

---

**Structure Boot** - 让 Spring Boot 开发更简单、更高效！
