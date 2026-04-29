# Structure Tenant Starter

多租户上下文管理 Starter，支持多种租户识别方式。

## 功能特性

- **多种租户识别方式**
  - HTTP请求头识别
  - 请求参数识别
  - 默认租户ID
  - 可扩展的自定义识别器

- **灵活配置**
  - 支持配置识别器顺序
  - 可配置请求头和参数名称
  - 可配置默认租户ID

- **多种场景支持**
  - HTTP Web请求
  - XXL-Job定时任务
  - 消息队列消费
  - 其他异步处理场景

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-tenant-starter</artifactId>
    <version>1.3.1</version>
</dependency>
```

### 2. 配置应用

在 `application.yml` 中添加配置：

```yaml
structure:
  tenant:
    enabled: true
    # 默认租户ID，所有识别方式都失败时使用
    default-tenant-id: "default"
    # 请求头识别配置
    header:
      enabled: true
      name: "X-Tenant-Id"
    # 请求参数识别配置
    param:
      enabled: true
      name: "tenantId"
    # 识别器顺序，值越小优先级越高
    resolver-order:
      - "header"
      - "param"
```

### 3. 配置Filter（Web应用）

在配置类中注册租户Filter：

```java
@Configuration
public class TenantConfig {

    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilterRegistration() {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TenantFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setName("tenantFilter");
        return registration;
    }
}
```

## 使用说明

### HTTP场景使用

通过请求头传递租户ID：

```bash
curl -H "X-Tenant-Id: tenant1" http://localhost:8080/api
```

或者通过请求参数传递：

```bash
curl http://localhost:8080/api?tenantId=tenant1
```

在代码中获取租户ID：

```java
@RestController
public class MyController {

    @GetMapping("/api")
    public Result doSomething() {
        // 获取当前租户ID
        String tenantId = TenantContextHolder.getTenantId();
        
        // 检查是否有租户
        if (TenantContextHolder.hasTenant()) {
            // 业务逻辑
        }
        
        return Result.success();
    }
}
```

### XXL-Job场景使用

```java
@JobHandler("myJob")
public class MyJob extends IJobHandler {

    @Override
    public void execute(String param) {
        try {
            // 从参数中解析租户ID
            String tenantId = parseTenantId(param);
            TenantContextHolder.setTenantId(tenantId);
            
            // 业务逻辑
            String currentTenant = TenantContextHolder.getTenantId();
            doBusiness(currentTenant);
        } finally {
            // 清理租户上下文
            TenantContextHolder.clear();
        }
    }
}
```

### 消息队列场景使用

```java
@Component
public class MessageConsumer {

    @RabbitListener(queues = "myQueue")
    public void consume(Message message) {
        try {
            // 从消息中解析租户ID
            String tenantId = parseTenantId(message);
            TenantContextHolder.setTenantId(tenantId);
            
            // 业务逻辑
            String currentTenant = TenantContextHolder.getTenantId();
            doBusiness(currentTenant);
        } finally {
            // 清理租户上下文
            TenantContextHolder.clear();
        }
    }
}
```

## 自定义识别器

如果需要自定义租户识别方式，实现 `TenantResolver` 接口：

```java
@Component
public class CustomTenantResolver implements TenantResolver {

    @Override
    public String getName() {
        return "custom";
    }

    @Override
    public String resolve() {
        // 自定义识别逻辑
        // 例如：从Session中获取、从Token中解析、从数据库配置中获取等
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession(false);
            if (session != null) {
                return (String) session.getAttribute("tenantId");
            }
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void cleanup() {
        // 可选的清理逻辑
    }
}
```

在配置中添加自定义识别器：

```yaml
structure:
  tenant:
    resolver-order:
      - "custom"
      - "header"
      - "param"
```

## TenantContextHolder API

| 方法 | 说明 |
|------|------|
| `setTenantId(String tenantId)` | 设置租户ID |
| `getTenantId()` | 获取租户ID（会触发识别） |
| `set(String key, Object value)` | 设置其他租户相关信息 |
| `get(String key)` | 获取其他租户相关信息 |
| `clear()` | 清理租户上下文 |
| `hasTenant()` | 检查是否有租户信息 |
| `getAll()` | 获取所有租户信息 |

## 配置项说明

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `structure.tenant.enabled` | 是否启用多租户功能 | `true` |
| `structure.tenant.default-tenant-id` | 默认租户ID | `null` |
| `structure.tenant.header.enabled` | 是否启用请求头识别 | `true` |
| `structure.tenant.header.name` | 请求头名称 | `X-Tenant-Id` |
| `structure.tenant.param.enabled` | 是否启用请求参数识别 | `true` |
| `structure.tenant.param.name` | 请求参数名称 | `tenantId` |
| `structure.tenant.resolver-order` | 识别器顺序 | 按Bean顺序 |

## 注意事项

1. **非HTTP场景必须手动清理**
   - 在非HTTP场景（如XXL-Job、消息队列）中，使用完毕后必须调用 `TenantContextHolder.clear()` 清理上下文，否则会造成内存泄漏

2. **Filter由应用层控制**
   - Filter不会自动注册，需要应用层在配置类中手动注册，可以灵活控制Filter的顺序和作用范围

3. **识别器优先级**
   - 识别器按照 `resolver-order` 配置的顺序执行，先返回非空值的识别器胜出

4. **默认租户的使用**
   - 默认租户仅在所有识别器都返回null时才会使用，谨慎使用默认租户功能

## 示例项目

完整的示例项目见 `structure-tenant-example` 模块，包含：
- HTTP场景演示
- XXL-Job场景演示
- 消息队列场景演示
- 自定义识别器示例

## 许可证

Apache License 2.0
