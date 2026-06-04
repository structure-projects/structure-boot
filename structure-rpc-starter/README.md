# Structure RPC Starter

Structure RPC Starter 是一个基于 Spring Boot 的轻量级远程服务调用组件，提供类似 FeignClient 的动态代理功能，支持多种认证方式。

## 功能特性

- **动态代理**: 基于 JDK 动态代理实现，无需编写实现类
- **RESTful 支持**: 支持 GET、POST、PUT、PATCH、DELETE 等 HTTP 方法
- **多种认证方式**: 支持 NONE、BASIC、BEARER 三种认证模式
- **Token 自动刷新**: Bearer 认证模式下自动管理 Token 生命周期
- **线程安全**: Token 刷新采用锁机制，保证并发安全
- **泛型支持**: 支持泛型返回类型

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-rpc-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 启用 RPC 客户端

在 Spring Boot 启动类上添加 `@EnableRpcClients` 注解：

```java
@SpringBootApplication
@EnableRpcClients(basePackages = "cn.structure.rpc.client")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. 定义 RPC 接口

创建带有 `@RpcClient` 注解的接口：

```java
@RpcClient("user-service")
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable("id") Long id);
    
    @PostMapping("/api/users")
    User createUser(@RequestBody User user);
    
    @PutMapping("/api/users/{id}")
    User updateUser(@PathVariable("id") Long id, @RequestBody User user);
    
    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable("id") Long id);
}
```

### 4. 配置服务

在 `application.yml` 中配置远程服务：

```yaml
structure:
  rpc:
    service-list:
      user-service:
        host: localhost
        port: 8080
        auth-type: BEARER
        access-key: client-id
        secret-key: client-secret
        auth-path: /oauth/token
```

## 配置说明

### 服务配置项

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `host` | String | 服务主机地址 | 必填 |
| `port` | Integer | 服务端口 | 必填 |
| `auth-type` | String | 认证类型：NONE/BASIC/BEARER | NONE |
| `access-key` | String | 访问密钥（用户名） | - |
| `secret-key` | String | 密钥（密码） | - |
| `auth-host` | String | 认证服务地址（Bearer模式） | 使用host |
| `auth-port` | Integer | 认证服务端口（Bearer模式） | 使用port |
| `auth-path` | String | Token获取路径 | /oauth/token |
| `token-basic-auth` | Boolean | 获取Token时是否使用Basic Auth | false |

### 认证方式说明

#### NONE（无认证）
不添加任何认证头，适用于公开接口。

#### BASIC（基本认证）
使用 Base64 编码的用户名密码作为 Authorization 头。

#### BEARER（Bearer认证）
使用 OAuth2 的 Access Token 作为 Authorization 头，支持自动刷新。

## 使用示例

```java
@RestController
public class UserController {
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userServiceClient.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
```

## 项目结构

```
structure-rpc-starter/
├── src/main/java/cn/structured/rpc/
│   ├── annotation/          # 注解定义
│   │   ├── EnableRpcClients.java
│   │   └── RpcClient.java
│   ├── configuration/       # 自动配置
│   │   └── AutoRpcConfiguration.java
│   ├── emuns/               # 枚举类型
│   │   └── AuthType.java
│   ├── entity/              # 实体类
│   │   ├── RemoteService.java
│   │   └── TokenInfo.java
│   ├── handler/             # 处理器
│   │   ├── BaseHttpClient.java
│   │   └── IRpcHandler.java
│   ├── properties/          # 配置类
│   │   └── RpcProperties.java
│   ├── proxy/               # 动态代理
│   │   ├── RpcInterfaceFactoryBean.java
│   │   ├── RpcInterfaceScanner.java
│   │   └── RpcProxyHandler.java
│   └── token/               # Token管理
│       ├── DefaultTokenManager.java
│       ├── DefaultTokenProvider.java
│       ├── TokenManager.java
│       └── TokenProvider.java
└── pom.xml
```

## 核心类说明

### RpcInterfaceScanner
扫描带有 `@RpcClient` 注解的接口并注册为 Spring Bean。

### RpcInterfaceFactoryBean
为每个 RPC 接口创建动态代理实例。

### RpcProxyHandler
动态代理处理器，解析方法注解并执行 HTTP 请求。

### BaseHttpClient
HTTP 客户端实现，支持认证处理和请求执行。

### DefaultTokenManager
Token 管理器，负责 Token 的缓存和自动刷新。

## 许可证

Apache License 2.0

## 联系方式

- 作者: chuck
- 邮箱: 15524415221@163.com