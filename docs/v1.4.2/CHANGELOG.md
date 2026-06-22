# Structure Boot v1.4.2 变更日志

## 版本信息

- **版本号**: 1.4.2
- **发布日期**: 2026-06-23
- **上一版本**: 1.4.1

---

## 变更类型说明

| 类型 | 说明 |
| :--- | :--- |
| 新增功能 | 新添加的功能 |
| 功能改进 | 现有功能的优化或增强 |
| 问题修复 | Bug 或缺陷的修复 |
| 兼容性问题 | 兼容性相关的修复或调整 |
| 安全修复 | 安全漏洞的修复 |

---

## 详细变更

### 1. Spring Boot 4.x 兼容性修复

#### 1.1 GlobalControllerAdvice 注解修复

- **变更类型**: 兼容性问题
- **文件**: `structure-restful-web-starter/.../filter/GlobalControllerAdvice.java`
- **问题**: 缺少 `@RestControllerAdvice` 注解，导致全局异常处理不生效
- **修复**: 将 `@ResponseBody` 替换为 `@RestControllerAdvice`

#### 1.2 ExceptionHandler 处理顺序修复

- **变更类型**: 问题修复
- **文件**: `structure-restful-web-starter/.../filter/GlobalControllerAdvice.java`
- **问题**: 同时存在 `@ExceptionHandler(Exception.class)` 和 `@ExceptionHandler(Throwable.class)`，由于 `Exception` 是 `Throwable` 的子类，导致 `Throwable` 处理方法永远不会被触发
- **修复**: 移除冗余的 `@ExceptionHandler(Exception.class)`，统一使用 `@ExceptionHandler(Throwable.class)` 处理

#### 1.3 FastJsonHttpMessageConverters ImportSelector 修复

- **变更类型**: 兼容性问题
- **文件**: `structure-restful-web-starter/.../filter/FastJsonHttpMessageConverters.java`
- **问题**: `ImportSelector` 实现类的 `selectImports()` 返回空数组，且 `@Bean` 方法在 `ImportSelector` 中不会被 Spring 正常处理
- **修复**: 新建 `FastJsonHttpMessageConvertersConfiguration.java` 配置类，正确注册 `FastJsonHttpMessageConverter` Bean

---

### 2. 配置类优化

#### 2.1 MybatisProperties 配置优化

- **变更类型**: 功能改进
- **文件**: `structure-mybatis-starter/.../configuration/MybatisProperties.java`
- **问题**: `@Configuration` 和 `@ConfigurationProperties` 混用，且使用不当的 `@ImportAutoConfiguration`
- **修复**: 移除冗余注解，在 `MybatisAutoConfiguration` 中使用 `@Import` 导入配置类

#### 2.2 RpcProperties 配置优化

- **变更类型**: 功能改进
- **文件**: `structure-rpc-starter/.../properties/RpcProperties.java`
- **问题**: 冗余的 `@Configuration` 注解
- **修复**: 移除 `@Configuration`，在 `AutoRpcConfiguration` 中使用 `@EnableConfigurationProperties` 和 `@Import`

---

### 3. 新增依赖版本管理

| 依赖 | 版本 | 变更类型 | 说明 |
| :--- | :--- | :--- | :--- |
| `springdoc-openapi` | 3.0.3 | 依赖升级 | 升级自 2.3.0，支持 Spring Boot 4.x |
| `knife4j-openapi3-jakarta-spring-boot-starter` | 4.5.0 | 新增 | Spring Boot 3.x 兼容的 API 文档 |
| `testcontainers` | 1.20.6 | 新增 | 测试容器支持 |
| `h2` | 2.2.224 | 新增 | H2 数据库驱动 |
| `bouncycastle` | 1.84 | 新增 | CVE-2026-0636 安全修复 |
| `commons-fileupload` | 1.6.0 | 新增 | CVE-2025-48976 安全修复 |

---

### 4. 安全漏洞修复

#### 4.1 CVE-2026-0636

- **变更类型**: 安全修复
- **影响组件**: BouncyCastle
- **修复版本**: 1.84
- **添加依赖**:
  - `bcprov-jdk18on`
  - `bcpkix-jdk18on`

#### 4.2 CVE-2025-48976

- **变更类型**: 安全修复
- **影响组件**: Apache Commons FileUpload
- **修复版本**: 1.6.0
- **添加依赖**: `commons-fileupload`

---

## 升级指南

### 升级到 1.4.2

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.4.2</version>
</parent>
```

### 兼容性说明

| 兼容性级别 | 状态 |
| :--- | :--- |
| 向后兼容 1.4.x | ✅ 兼容 |
| 向后兼容 1.3.x | ✅ 兼容 |
| 无 API 变更 | ✅ 是 |
| 无破坏性更改 | ✅ 是 |

### 升级建议

1. 建议所有用户升级到此版本，以获取 Spring Boot 4.x 的完整兼容性支持
2. 如遇到问题，可参考上述修复详情进行排查

---

## 相关文档

- [版本概述](./README.md)
- [组件使用指南](./COMPONENT_GUIDE.md)
- [变更日志](../../CHANGELOG.md)
