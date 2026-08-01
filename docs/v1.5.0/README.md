# Structure Boot v1.5.0

## 版本信息

- **版本号**: 1.5.0
- **发布日期**: 2026-08-01
- **父版本**: 1.4.4

---

## 文档目录

| 文档 | 说明 |
| :--- | :--- |
| [README.md](./README.md) | 版本概述、快速开始、环境要求 |
| [COMPONENT_GUIDE.md](./COMPONENT_GUIDE.md) | 各组件详细使用说明 |
| [CHANGELOG.md](./CHANGELOG.md) | 版本详细变更记录 |

---

## 版本概述

v1.5.0 是依赖升级版本，主要包含 Jackson 2 → Jackson 3 升级和 fastjson → fastjson2 替换。

### 主要更新

- **Jackson 升级**
  - 从 Jackson 2.x (`com.fasterxml.jackson`) 升级到 Jackson 3 (`tools.jackson`)
  - `ObjectMapper` 改用 Builder 模式创建 (`JsonMapper.builder().build()`)
  - 适配 Spring Boot 4.x 的 Jackson 3 自动配置
- **fastjson 替换为 fastjson2**
  - 移除 `com.alibaba:fastjson`，统一使用 `com.alibaba.fastjson2:fastjson2` (2.0.43)
  - 所有模块的 fastjson 依赖和 import 迁移至 fastjson2

### 破坏性变更

- Jackson core/databind 包名从 `com.fasterxml.jackson` 变更为 `tools.jackson`
- `ObjectMapper` 在 Jackson 3 中为不可变对象，需通过 Builder 模式创建
- `com.alibaba:fastjson` 依赖被移除，替换为 `com.alibaba.fastjson2:fastjson2`

---

## 快速开始

### 环境要求

| 要求 | 版本 |
| :--- | :--- |
| JDK | 17+ |
| Maven | 3.6+ |
| Spring Boot | 4.0.6 |
| Jackson | 3.x |
| fastjson2 | 2.0.43 |

### 基础配置

在 `pom.xml` 中添加依赖管理：

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.5.0</version>
</parent>
```

---

## 相关文档

| 文档 | 说明 |
| :--- | :--- |
| [变更日志](../../CHANGELOG.md) | 项目变更日志索引 |
| [用户开发指南](../../USER_GUIDE.md) | 快速开始和开发指南 |
| [v1.4.4 文档](../v1.4.4/) | 上一版本文档 |

---

## 更新日志

| 日期 | 更新内容 |
| :--- | :--- |
| 2026-08-01 | 发布 v1.5.0 版本，Jackson 升级至 3.x，fastjson 替换为 fastjson2 |
