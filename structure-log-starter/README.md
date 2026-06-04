# Structure Log Starter

Structure Log Starter 是 Structure Boot 框架的日志处理模块，提供统一的日志切面、请求日志记录和 JSON 格式日志输出。

## 功能特性

- **接口日志切面**: 自动记录 Controller 方法的请求和响应日志
- **方法日志注解**: 通过注解标记需要记录日志的方法
- **JSON格式输出**: 支持结构化的 JSON 日志格式
- **灵活配置**: 支持多种日志配置选项

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-log-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 启用日志功能

在 Spring Boot 启动类上添加 `@EnableWebAopLog` 注解：

```java
@SpringBootApplication
@EnableWebAopLog
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. 配置日志

在 `application.yml` 中添加配置：

```yaml
structure:
  log:
    enabled: true
    # 是否记录请求参数
    request-log: true
    # 是否记录响应参数
    response-log: true
    # 是否记录执行时间
    execution-time: true
    # 忽略的URL路径（正则表达式）
    ignore-patterns:
      - "/actuator/*"
      - "/health/*"
```

## 目录结构

```
structure-log-starter/
├── src/main/java/cn/structure/starter/log/
│   ├── anno/              # 注解定义
│   │   ├── AspectParamLog.java    # 方法日志注解
│   │   └── EnableWebAopLog.java   # 启用日志注解
│   ├── aop/               # AOP切面
│   │   └── ParamLogsAspect.java   # 参数日志切面
│   ├── config/            # 配置类
│   │   ├── AutoLogConfiguration.java
│   │   ├── EnableWebAopConfig.java
│   │   └── WebAopConfig.java
│   ├── filter/            # 过滤器
│   │   └── WebLogAspect.java      # Web日志切面
│   ├── logback/           # Logback扩展
│   │   └── JSONLogLayout.java     # JSON日志布局
│   └── properties/        # 配置属性
│       └── WebAopConfigProperties.java
└── src/main/resources/
    ├── logback-spring.xml      # 默认日志配置
    ├── logback-spring-line.xml # 行式日志配置
    └── structure-boot-aop.xml  # AOP配置
```

## 使用示例

### 方法日志注解

```java
@RestController
public class UserController {
    
    @AspectParamLog(description = "获取用户信息")
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        // 业务逻辑
        return userService.getUserById(id);
    }
    
    @AspectParamLog(description = "创建用户")
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        // 业务逻辑
        return userService.createUser(user);
    }
}
```

### JSON日志格式

使用 `JSONLogLayout` 可以输出结构化的 JSON 日志：

```json
{
  "timestamp": "2024-01-01T12:00:00.000Z",
  "level": "INFO",
  "logger": "cn.structure.example.controller.UserController",
  "method": "getUser",
  "params": {"id": 1},
  "result": {"id": 1, "name": "John"},
  "executionTime": 15
}
```

## 配置项说明

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `structure.log.enabled` | Boolean | 是否启用日志功能 | `true` |
| `structure.log.request-log` | Boolean | 是否记录请求参数 | `true` |
| `structure.log.response-log` | Boolean | 是否记录响应参数 | `true` |
| `structure.log.execution-time` | Boolean | 是否记录执行时间 | `true` |
| `structure.log.ignore-patterns` | List | 忽略的URL路径列表 | 空列表 |

## 核心组件

### ParamLogsAspect
方法日志切面，记录方法的入参、出参和执行时间。

### WebLogAspect
Web请求日志切面，记录HTTP请求的详细信息。

### JSONLogLayout
Logback的JSON日志布局，输出结构化日志。

### AspectParamLog
方法日志注解，标记需要记录日志的方法。

## 许可证

Apache License 2.0