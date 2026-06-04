# Structure MyBatis Plus Generate

Structure MyBatis Plus Generate 是 Structure Boot 框架的 MyBatis Plus 代码生成器模块，支持自动生成实体类、Mapper、Service 和 Controller 代码。

## 功能特性

- **代码生成**: 自动生成实体类、Mapper接口、Mapper.xml、Service、Controller
- **自定义模板**: 支持自定义代码模板
- **灵活配置**: 支持多种配置选项
- **数据库支持**: 支持 MySQL、Oracle、PostgreSQL 等主流数据库

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-mybatis-plus-generate</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 配置生成器

在 `application.yml` 中添加配置：

```yaml
structure:
  mybatis-plus:
    generate:
      enabled: true
      # 数据库配置
      datasource:
        url: jdbc:mysql://localhost:3306/example_db
        username: root
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
      # 生成配置
      global-config:
        output-dir: src/main/java
        author: chuck
        open: false
        file-override: true
      # 包配置
      package-config:
        parent: cn.structure.example
        entity: entity
        mapper: mapper
        service: service
        service-impl: service.impl
        controller: controller
      # 策略配置
      strategy-config:
        include: user,order,product
        table-prefix: t_
        entity-lombok: true
        rest-controller-style: true
```

### 3. 创建配置文件

创建 `generator-config.yaml` 配置文件：

```yaml
datasource:
  url: jdbc:mysql://localhost:3306/example_db
  username: root
  password: password
  driver-class-name: com.mysql.cj.jdbc.Driver

global-config:
  output-dir: src/main/java
  author: chuck
  open: false
  file-override: true

package-config:
  parent: cn.structure.example
  entity: entity
  mapper: mapper
  service: service
  service-impl: service.impl
  controller: controller

strategy-config:
  include:
    - user
    - order
    - product
  table-prefix: t_
  entity-lombok: true
  rest-controller-style: true
```

### 4. 运行生成器

```java
public class CodeGenerator {
    
    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        
        // 配置数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/example_db");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("password");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        generator.setDataSource(dataSourceConfig);
        
        // 配置全局设置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        globalConfig.setAuthor("chuck");
        globalConfig.setOpen(false);
        generator.setGlobalConfig(globalConfig);
        
        // 配置包
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("cn.structure.example");
        generator.setPackageInfo(packageConfig);
        
        // 执行生成
        generator.execute();
    }
}
```

## 目录结构

```
structure-mybatis-plus-generate/
├── src/main/resources/
│   └── mp-code-generator-config.yaml
```

## 配置项说明

### 数据源配置

| 配置项 | 说明 |
| :--- | :--- |
| `datasource.url` | 数据库连接URL |
| `datasource.username` | 数据库用户名 |
| `datasource.password` | 数据库密码 |
| `datasource.driver-class-name` | 数据库驱动类 |

### 全局配置

| 配置项 | 说明 | 默认值 |
| :--- | :--- | :--- |
| `global-config.output-dir` | 输出目录 | `src/main/java` |
| `global-config.author` | 作者 | - |
| `global-config.open` | 生成后打开目录 | `false` |
| `global-config.file-override` | 是否覆盖已有文件 | `false` |

### 包配置

| 配置项 | 说明 | 默认值 |
| :--- | :--- | :--- |
| `package-config.parent` | 父包名 | - |
| `package-config.entity` | 实体类包名 | `entity` |
| `package-config.mapper` | Mapper包名 | `mapper` |
| `package-config.service` | Service包名 | `service` |
| `package-config.service-impl` | Service实现类包名 | `service.impl` |
| `package-config.controller` | Controller包名 | `controller` |

### 策略配置

| 配置项 | 说明 | 默认值 |
| :--- | :--- | :--- |
| `strategy-config.include` | 要生成的表名列表 | - |
| `strategy-config.table-prefix` | 表名前缀 | - |
| `strategy-config.entity-lombok` | 是否使用Lombok | `true` |
| `strategy-config.rest-controller-style` | 是否生成REST风格Controller | `true` |

## 生成文件说明

| 文件类型 | 说明 |
| :--- | :--- |
| `Entity.java` | 实体类，包含字段和注解 |
| `Mapper.java` | Mapper接口，继承BaseMapper |
| `Mapper.xml` | Mapper XML文件 |
| `Service.java` | Service接口，继承IService |
| `ServiceImpl.java` | Service实现类 |
| `Controller.java` | Controller类，提供REST API |

## 许可证

Apache License 2.0