# Structure Boot v1.5.0 变更日志

## 版本信息

- **版本号**: 1.5.0
- **发布日期**: 2026-08-01
- **上一版本**: 1.4.4

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

### 1. 版本号更新

- **变更类型**: 版本更新
- **说明**: 更新项目版本号至 1.5.0

### 2. Jackson 升级 (Jackson 2 → Jackson 3)

- **变更类型**: 重大升级
- **说明**: 将 Jackson 从 2.x (`com.fasterxml.jackson`) 升级到 Jackson 3 (`tools.jackson`)，适配 Spring Boot 4.x

**变更内容：**

| 模块 | 变更说明 |
| :--- | :--- |
| structure-rpc-starter | `BaseHttpClient`、`RpcProxyHandler` 的 import 从 `com.fasterxml.jackson.core/databind` 迁移至 `tools.jackson.core/databind` |
| structure-rpc-starter | `ObjectMapper` 构建方式从 `new ObjectMapper()` 改为 `JsonMapper.builder().build()`（适配 Jackson 3 不可变对象模式） |
| structure-rpc-starter | 新增 `tools.jackson.core:jackson-databind` 显式依赖 |
| structure-restful-web-starter | `JacksonConfig` 使用 `JsonMapperBuilderCustomizer` 配置 Jackson 3 序列化 |
| 测试代码 | 示例模块测试类同步迁移至 Jackson 3 |

**注意事项：**

- `jackson-annotations` 包名保持不变，仍为 `com.fasterxml.jackson.annotation`
- Jackson 3 异常改为非受检异常（`JacksonException extends RuntimeException`），原有 try-catch 仍兼容
- `ObjectMapper` 在 Jackson 3 中为不可变对象，需通过 Builder 模式创建

### 3. fastjson 替换为 fastjson2 (2.0)

- **变更类型**: 依赖替换
- **说明**: 移除旧的 `com.alibaba:fastjson`，统一使用 `com.alibaba.fastjson2:fastjson2`（版本 2.0.43）

**变更内容：**

| 模块 | 变更说明 |
| :--- | :--- |
| structure-dependencies | 移除 `com.alibaba:fastjson` 兼容模块依赖管理，统一使用 `com.alibaba.fastjson2:fastjson2` |
| structure-log-starter | 依赖从 `com.alibaba:fastjson` 替换为 `com.alibaba.fastjson2:fastjson2` |
| structure-mybatis-starter | 依赖从 `com.alibaba:fastjson` 替换为 `com.alibaba.fastjson2:fastjson2` |
| structure-mybatis-plus-starter | 依赖从 `com.alibaba:fastjson` 替换为 `com.alibaba.fastjson2:fastjson2` |
| WebLogAspect | import 从 `com.alibaba.fastjson.JSON` 迁移至 `com.alibaba.fastjson2.JSON` |
| JoinHelper | import 从 `com.alibaba.fastjson.JSONObject` 迁移至 `com.alibaba.fastjson2.JSONObject` |

### 4. 脚本版本更新

- **变更类型**: 版本更新
- **说明**: 更新构建脚本的默认版本号

| 脚本 | 变更 |
| :--- | :--- |
| scripts/install.sh | 默认版本 → `1.5.0-SNAPSHOT` |
| scripts/update-snapshots.sh | 默认版本 → `1.5.0-SNAPSHOT` |
| scripts/release.sh | 发布版本 → `1.5.0` |

---

## 升级指南

### 升级到 1.5.0

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.5.0</version>
</parent>
```

### 兼容性说明

| 兼容性级别 | 状态 | 说明 |
| :--- | :--- | :--- |
| 向后兼容 1.4.x | ⚠️ 需注意 | Jackson 2 → 3 包名变更，使用 `com.fasterxml.jackson.core/databind` 的代码需迁移 |
| 向后兼容 1.3.x | ⚠️ 需注意 | 同上 |
| fastjson API 兼容 | ✅ 兼容 | fastjson2 的 `JSON`、`JSONObject` API 与 fastjson 1.x 兼容 |
| Jackson 注解兼容 | ✅ 兼容 | `jackson-annotations` 包名不变 |

### 升级建议

1. **Jackson 迁移**：如果项目中直接使用了 `com.fasterxml.jackson.core` 或 `com.fasterxml.jackson.databind` 包下的类，需将 import 替换为 `tools.jackson.core` / `tools.jackson.databind`
2. **ObjectMapper 创建**：`new ObjectMapper()` 需改为 `JsonMapper.builder().build()`
3. **fastjson 迁移**：如果项目中直接引用了 `com.alibaba:fastjson`，需替换为 `com.alibaba.fastjson2:fastjson2`，import 从 `com.alibaba.fastjson` 改为 `com.alibaba.fastjson2`
4. **Jackson 注解**：`@JsonInclude`、`@JsonProperty` 等注解包名不变，无需修改

### 破坏性变更

| 变更项 | 影响范围 | 迁移方式 |
| :--- | :--- | :--- |
| Jackson 2 → 3 包名变更 | 直接使用 Jackson core/databind 的代码 | `com.fasterxml.jackson` → `tools.jackson` |
| ObjectMapper 不可变 | 通过 `new ObjectMapper()` 创建的代码 | 改用 `JsonMapper.builder().build()` |
| fastjson → fastjson2 | 直接引用 `com.alibaba:fastjson` 的代码 | 替换为 `com.alibaba.fastjson2:fastjson2` |

---

## 相关文档

- [版本概述](./README.md)
- [组件使用指南](./COMPONENT_GUIDE.md)
- [变更日志](../../CHANGELOG.md)
