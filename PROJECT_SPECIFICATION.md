# Structure Boot 项目规范

本文档定义了 Structure Boot 项目的开发规范、代码风格和文档书写规范，是项目开发的统一标准。

---

## 1. 项目概述

### 1.1 项目信息

| 属性 | 值 |
| :--- | :--- |
| 项目名称 | Structure Boot |
| 当前版本 | 1.4.2 |
| Spring Boot | 4.0.6 |
| JDK | 17+ |
| Maven | 3.6+ |
| 开源协议 | Apache License 2.0 |

### 1.2 版本规范

项目采用语义化版本控制（Semantic Versioning）：

```
MAJOR.MINOR.PATCH
```

| 版本类型 | 说明 |
| :--- | :--- |
| **MAJOR** | 不兼容的 API 变更，破坏性更新 |
| **MINOR** | 向后兼容的功能新增或改进 |
| **PATCH** | 向后兼容的 Bug 修复 |

**版本号使用规则：**
- 代码配置（pom.xml）：使用快照版本号 `x.y.z-SNAPSHOT`
- 文档和示例：使用正式版本号 `x.y.z`

---

## 2. 代码风格规范

### 2.1 命名规范

#### 包命名
- 使用小写字母，点分隔符分隔
- 遵循公司域名反转作为前缀
- 示例：`cn.structured.common.entity`

#### 类命名
- 使用 PascalCase（帕斯卡命名）
- 类名应清晰表达其功能
- 示例：`ResultVO`、`DateUtil`、`StructureRedisAutoConfiguration`

#### 方法命名
- 使用 camelCase（驼峰命名）
- 方法名应清晰表达其功能
- 示例：`getNowDateText`、`calculateDiff`、`acquireDistributedLock`

#### 变量命名
- 使用 camelCase
- 变量名应清晰表达其含义
- 示例：`startDate`、`endDate`、`redisTemplate`

#### 常量命名
- 全大写，单词间用下划线分隔
- 示例：`DEFAULT_FORMAT`、`DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS`

### 2.2 代码格式

| 规范 | 要求 |
| :--- | :--- |
| 缩进 | 4 空格，不使用制表符 |
| 行长度 | 最多 120 字符 |
| 左括号 `{` | 与语句在同一行 |
| 右括号 `}` | 单独占一行，对齐 |
| 空行 | 类成员之间、方法之间添加适当空行 |

**代码示例：**

```java
public class DateUtil {

    private DateUtil() {
    }

    public static String getNowDateText(String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(new Date());
    }
}
```

### 2.3 版权声明

每个 Java 文件必须包含 Apache License 2.0 版权声明：

```java
/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

### 2.4 注释规范

#### 类注释
```java
/**
 * <p>
 * 日期工具类
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2025-01-01
 */
public class DateUtil {
}
```

#### 方法注释
```java
/**
 * 获取当前时间格式化后的值
 *
 * @param pattern 格式
 * @return 格式化后的日期字符串
 */
public static String getNowDateText(String pattern) {
}
```

#### 变量注释
```java
/**
 * 默认格式
 */
public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
```

---

## 3. 代码结构规范

### 3.1 包结构

```
cn.structured.{模块名}.{层名}
```

| 层名 | 说明 |
| :--- | :--- |
| `controller` | 控制层 |
| `service` | 业务层 |
| `mapper` | 数据访问层 |
| `entity` | 实体类 |
| `dto` | 数据传输对象 |
| `vo` | 视图对象 |
| `config` | 配置类 |
| `util` | 工具类 |

### 3.2 类命名规范

| 类型 | 命名规范 | 示例 |
| :--- | :--- | :--- |
| Controller | XxxController | UserController |
| Service | XxxService, XxxServiceImpl | UserService, UserServiceImpl |
| Mapper | XxxMapper | UserMapper |
| Entity | Xxx | User, Order |
| DTO | XxxDTO | UserDTO |
| VO | XxxVO | UserVO |
| Config | XxxConfig | RedisConfig |
| 工具类 | XxxUtil / XxxUtils | DateUtil |

### 3.3 方法命名规范

| 操作 | 命名规范 |
| :--- | :--- |
| 查询 | `getXxx`, `findXxx`, `queryXxx` |
| 新增 | `saveXxx`, `addXxx`, `createXxx` |
| 修改 | `updateXxx`, `modifyXxx` |
| 删除 | `deleteXxx`, `removeXxx` |
| 分页查询 | `pageXxx`, `listXxx` |

### 3.4 工具类规范

- 使用私有构造方法避免实例化
- 方法应为静态方法
- 示例：

```java
public class DateUtil {

    private DateUtil() {
    }

    public static String format(Date date, String pattern) {
    }
}
```

---

## 4. 配置规范

### 4.1 配置优先级

```
bootstrap.yml → application.yml → application-{profile}.yml
```

### 4.2 主要配置结构

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

  data:
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
  log:
    aop:
      enable: true
```

---

## 5. API 设计规范

### 5.1 响应格式

```json
{
    "code": 200,
    "success": true,
    "message": "操作成功",
    "data": {},
    "timestamp": 1640995200000
}
```

### 5.2 请求映射规范

| 方法 | 路径 | 说明 |
| :--- | :--- | :--- |
| GET | /api/{resource} | 查询列表 |
| GET | /api/{resource}/{id} | 查询详情 |
| POST | /api/{resource} | 新增 |
| PUT | /api/{resource}/{id} | 修改 |
| DELETE | /api/{resource}/{id} | 删除 |

---

## 6. Git 提交规范

### 6.1 提交信息格式

```
<type>(<scope>): <subject>

<body>
```

### 6.2 类型说明

| 类型 | 说明 |
| :--- | :--- |
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `docs` | 文档更新 |
| `style` | 代码格式调整 |
| `refactor` | 重构 |
| `test` | 测试相关 |
| `chore` | 构建工具或辅助工具更新 |

### 6.3 提交示例

```
feat(web): 添加统一异常处理功能
fix(mybatis): 修复批量插入时空指针异常
docs: 更新 README 文档
```

---

## 7. 分支管理规范

| 分支 | 说明 |
| :--- | :--- |
| `main` | 主分支，稳定版本 |
| `develop` | 开发分支，日常开发 |
| `feature/*` | 功能分支，新功能开发 |
| `hotfix/*` | 修复分支，紧急 Bug 修复 |

---

## 8. 变更日志规范

### 8.1 格式要求

```markdown
## [版本号] - YYYY-MM-DD

### 变更类型

- 简短清晰的变更说明
- 涉及的模块或文件
```

### 8.2 变更类型分类

| 分类 | 说明 |
| :--- | :--- |
| **功能新增** | 添加新功能或模块 |
| **功能增强** | 现有功能的改进 |
| **Bug 修复** | 修复已知问题 |
| **配置更新** | 更新配置文件 |
| **文档更新** | 更新文档内容 |
| **依赖升级** | 升级第三方依赖 |

---

## 9. 文档书写规范

### 9.1 文档结构

| 文档位置 | 说明 |
| :--- | :--- |
| 根目录 | 项目级文档（README、CHANGELOG、规约等） |
| `docs/v{x.y.z}/` | 按版本号组织的文档 |
| `docs/archive/` | 历史版本归档文档 |
| 各模块 README.md | 模块级文档 |

### 9.2 文档命名规范

- 使用有意义的文件名
- 使用英文或中文拼音命名
- 版本文档使用版本号作为目录名
- 示例：`v1.4.2/CHANGELOG.md`、`docs/archive/1.3.X.md`

### 9.3 Markdown 格式规范

#### 标题层级
- 一级标题：`# 标题`
- 二级标题：`## 标题`
- 三级标题：`### 标题`
- 避免超过四级标题

#### 列表格式
- 无序列表使用 `-` 或 `*`
- 有序列表使用 `1.` `2.` `3.`
- 列表项缩进使用 2 空格

#### 代码块
- 使用三个反引号包裹
- 指定语言标识符
- 代码块前后留空行

```markdown
示例：

```java
public class Example {
}
`` `
```

#### 表格格式
- 使用 `|` 分隔列
- 使用 `-` 分隔表头和内容
- 列对齐使用 `:`（可选）

```markdown
| 列1 | 列2 | 列3 |
| :--- | :---: | ---: |
| 左对齐 | 居中 | 右对齐 |
```

#### 链接和图片
- 链接使用 `[文字](URL)` 格式
- 图片使用 `![文字](图片URL)` 格式
- 优先使用相对路径

### 9.4 文档内容规范

#### 标题规范
- 简洁明了，不超过 50 字
- 使用中文描述
- 避免使用无意义的标题（如"介绍"、"概述"）

#### 内容规范
- 简洁清晰，避免冗余
- 使用主动语态
- 适当使用列表和表格
- 代码示例要完整可运行

#### 示例规范
- 示例代码要完整
- 添加必要的注释
- 使用合适的字体大小

### 9.5 版本文档规范

每个版本的详细变更应包含：

1. **版本信息**：版本号、发布日期、上一版本
2. **变更类型**：问题修复/功能新增/依赖升级等
3. **详细变更**：具体变更内容
4. **升级指南**：升级步骤和注意事项
5. **兼容性说明**：与其他版本的兼容性

---

## 10. 异常处理规范

### 10.1 统一异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVO<Void> handleBusinessException(BusinessException e) {
        return ResultVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResultVO<Void> handleException(Throwable e) {
        return ResultVO.fail("系统异常，请稍后重试");
    }
}
```

### 10.2 日志级别使用

| 级别 | 使用场景 |
| :--- | :--- |
| `ERROR` | 系统错误，需要人工处理 |
| `WARN` | 警告信息，不影响系统运行 |
| `INFO` | 关键业务信息 |
| `DEBUG` | 调试信息 |

---

## 11. 安全规范

### 11.1 敏感数据处理
- 密码必须加密存储
- 个人信息需要脱敏处理
- 重要操作记录审计日志

### 11.2 SQL 注入防护
- 使用 MyBatis 的 `#{}` 占位符
- 禁止拼接 SQL 语句
- 参数校验和过滤

---

## 12. 数据库设计规范

### 12.1 表字段规范

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `id` | BIGINT | 主键，自增 |
| `deleted` | TINYINT | 逻辑删除，默认 0 |
| `create_time` | DATETIME | 创建时间 |
| `update_time` | DATETIME | 更新时间 |
| `version` | INT | 乐观锁版本号 |

### 12.2 实体类规范

```java
@Data
@TableName("t_user")
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
```

---

## 13. 技术栈

### 13.1 核心框架

| 框架 | 版本 |
| :--- | :--- |
| Spring Boot | 4.0.6 |
| Spring Security | 6.x |
| Java | 17+ |
| Maven | 3.6+ |

### 13.2 数据持久层

| 组件 | 版本 |
| :--- | :--- |
| MyBatis | 3.5.x |
| MyBatis-Plus | 3.5.x |
| PageHelper | 6.x |

### 13.3 缓存与分布式

| 组件 | 版本 |
| :--- | :--- |
| Redis | Spring Data Redis |
| Redisson | 3.x |

### 13.4 常用组件

| 组件 | 说明 |
| :--- | :--- |
| Lombok | 代码简化 |
| Hutool | 工具类库 |
| FastJSON | JSON 处理 |
| springdoc-openapi | API 文档 |
| Knife4j | Swagger UI 增强 |

---

## 更新履历

| 版本 | 日期 | 说明 |
| :--- | :--- | :--- |
| 1.0 | 2025-01-01 | 初始规范文档 |
| 2.0 | 2026-06-23 | 整合规范文档，添加文档书写规范，更新技术栈版本 |
