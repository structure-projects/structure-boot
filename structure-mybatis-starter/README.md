# Structure MyBatis Starter

Structure MyBatis Starter 是 Structure Boot 框架的 MyBatis 集成模块，提供 MyBatis 自动配置、分页插件、分表插件等功能。

## 功能特性

- **自动配置**: 自动配置 MyBatis 数据源和 SqlSessionFactory
- **分页支持**: 集成 PageHelper 分页插件
- **分表支持**: 支持按日期自动分表
- **自动填充**: 支持创建时间、更新时间自动填充
- **ID生成**: 支持多种ID生成策略

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-mybatis-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 配置MyBatis

在 `application.yml` 中添加配置：

```yaml
structure:
  mybatis:
    enabled: true
    # Mapper文件位置
    mapper-locations: classpath:mapper/*.xml
    # 实体类包路径
    type-aliases-package: cn.structure.example.entity
    # 分页插件配置
    page-helper:
      enabled: true
      reasonable: true
      support-methods-arguments: true
```

### 3. 创建Mapper接口

```java
public interface UserMapper {
    
    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Long id);
    
    @Insert("INSERT INTO user(name, age) VALUES(#{name}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    @Update("UPDATE user SET name = #{name}, age = #{age} WHERE id = #{id}")
    int update(User user);
    
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);
}
```

## 目录结构

```
structure-mybatis-starter/
├── src/main/java/cn/structure/starter/mybatis/
│   ├── annotation/         # 注解定义
│   │   ├── CreateTime.java      # 创建时间注解
│   │   ├── EnableSplitTable.java # 启用分表注解
│   │   ├── SplitTable.java      # 分表注解
│   │   └── UpdateTime.java      # 更新时间注解
│   ├── configuration/      # 配置类
│   │   ├── EnableSplitDateSource.java
│   │   ├── MybatisAutoConfiguration.java
│   │   ├── MybatisProperties.java
│   │   └── PageHelperProperties.java
│   ├── enums/              # 枚举类型
│   │   ├── GenerateIdType.java   # ID生成类型
│   │   └── SplitTableEnum.java   # 分表类型
│   └── plugin/             # 插件
│       ├── OverWritePluginParameter.java
│       └── SplitDateSourcePlugin.java
```

## 分表功能

### 启用分表

```java
@SpringBootApplication
@EnableSplitTable
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 分表注解

```java
@SplitTable(tableName = "order", splitType = SplitTableEnum.MONTH)
public interface OrderMapper {
    
    @Select("SELECT * FROM ${tableName} WHERE id = #{id}")
    Order selectById(Long id);
    
    @Insert("INSERT INTO ${tableName}(order_no, amount) VALUES(#{orderNo}, #{amount})")
    int insert(Order order);
}
```

### 分表类型

| 类型 | 说明 | 表名示例 |
| :--- | :--- | :--- |
| `DAY` | 按天分表 | order_20240101 |
| `MONTH` | 按月分表 | order_202401 |
| `YEAR` | 按年分表 | order_2024 |

## 自动填充

### 使用注解

```java
public class User {
    
    private Long id;
    
    private String name;
    
    @CreateTime
    private Date createTime;
    
    @UpdateTime
    private Date updateTime;
}
```

## 分页查询

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public PageInfo<User> getUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectAll();
        return new PageInfo<>(users);
    }
}
```

## 配置项说明

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `structure.mybatis.enabled` | Boolean | 是否启用MyBatis | `true` |
| `structure.mybatis.mapper-locations` | String | Mapper文件位置 | `classpath:mapper/*.xml` |
| `structure.mybatis.type-aliases-package` | String | 实体类包路径 | - |
| `structure.mybatis.page-helper.enabled` | Boolean | 是否启用分页插件 | `true` |
| `structure.mybatis.page-helper.reasonable` | Boolean | 分页合理化 | `true` |
| `structure.mybatis.page-helper.support-methods-arguments` | Boolean | 支持方法参数 | `true` |

## 许可证

Apache License 2.0