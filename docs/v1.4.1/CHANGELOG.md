# Structure Boot v1.4.1 变更日志

## 版本信息

- **版本号**: 1.4.1
- **发布日期**: 2026-06-15
- **上一版本**: 1.4.0

---

## 详细变更

### 1. 依赖版本更新

#### 1.1 Spring Boot 版本小版本升级

- **变更类型**: 依赖升级
- **版本**: 4.0.0 → 4.0.6
- **说明**: 包含多个安全修复和问题修复

#### 1.2 Knife4j 版本升级

- **变更类型**: 依赖升级
- **版本**: 4.4.0 → 4.5.0
- **说明**: 新增对 Spring Boot 4.x 的完整支持

---

### 2. 已知问题修复

- 修复了部分starter模块在Spring Boot 4.x环境下的一些兼容性问题
- 更新了部分依赖的安全补丁

---

## 升级指南

### 升级到 1.4.1

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.4.1</version>
</parent>
```

### 兼容性说明

| 兼容性级别 | 状态 |
| :--- | :--- |
| 向后兼容 1.4.x | ✅ 兼容 |
| 向后兼容 1.3.x | ✅ 兼容 |
| 无 API 变更 | ✅ 是 |
| 无破坏性更改 | ✅ 是 |

---

## 相关文档

- [版本概述](./README.md)
- [变更日志](../../CHANGELOG.md)
- [组件使用指南](../v1.4.2/COMPONENT_GUIDE.md)
