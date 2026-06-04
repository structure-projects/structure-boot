# Structure MyBatis Plus Starter

Structure MyBatis Plus Starter 是 Structure Boot 框架的 MyBatis Plus 集成模块，提供 MyBatis Plus 的自动配置和增强功能。

## 功能特性

- **自动配置**: 自动配置 MyBatis Plus
- **CRUD增强**: 提供强大的 CRUD 操作
- **条件构造器**: 支持 Lambda 表达式查询
- **分页支持**: 内置分页插件
- **代码生成**: 支持自动生成实体类、Mapper、Service

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-mybatis-plus-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 配置MyBatis Plus

在 `application.yml` 中添加配置：

```yaml
structure:
  mybatis-plus:
    enabled: true
    # Mapper文件位置
    mapper-locations: classpath:mapper/*.xml
    # 实体类包路径
    type-aliases-package: cn.structure.example.entity
    # 配置ID生成策略
    global-config:
      db-config:
        id-type: auto
```

### 3. 创建实体类

```java
@Data
@TableName("user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private Integer age;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
```

### 4. 创建Mapper接口

```java
public interface UserMapper extends BaseMapper<User> {
    
    // 自定义查询
    List<User> selectByName(@Param("name") String name);
}
```

### 5. 创建Service

```java
public interface UserService extends IService<User> {
    
    User findByName(String name);
}

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Override
    public User findByName(String name) {
        return lambdaQuery()
                .eq(User::getName, name)
                .one();
    }
}
```

## 目录结构

```
structure-mybatis-plus-starter/
├── src/main/resources/
│   └── META-INF/
│       ├── spring/
│       │   └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│       └── spring.factories
```

## 常用操作

### 查询操作

```java
// 按ID查询
User user = userService.getById(1L);

// 条件查询
List<User> users = userService.lambdaQuery()
        .eq(User::getAge, 18)
        .like(User::getName, "张")
        .list();

// 分页查询
Page<User> page = new Page<>(1, 10);
Page<User> result = userService.page(page, 
    new QueryWrapper<User>().eq("status", 1));
```

### 更新操作

```java
// 更新单条记录
userService.updateById(user);

// 条件更新
userService.lambdaUpdate()
        .eq(User::getId, 1L)
        .set(User::getName, "新名字")
        .update();
```

### 删除操作

```java
// 按ID删除
userService.removeById(1L);

// 条件删除
userService.lambdaUpdate()
        .eq(User::getStatus, 0)
        .remove();
```

## 配置项说明

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `structure.mybatis-plus.enabled` | Boolean | 是否启用MyBatis Plus | `true` |
| `structure.mybatis-plus.mapper-locations` | String | Mapper文件位置 | `classpath:mapper/*.xml` |
| `structure.mybatis-plus.type-aliases-package` | String | 实体类包路径 | - |
| `structure.mybatis-plus.global-config.db-config.id-type` | String | ID生成策略 | `auto` |

## ID生成策略

| 策略 | 说明 |
| :--- | :--- |
| `AUTO` | 数据库自增 |
| `NONE` | 无策略 |
| `INPUT` | 用户输入 |
| `ASSIGN_ID` | 雪花算法 |
| `ASSIGN_UUID` | UUID |

## 许可证

Apache License 2.0