# Structure Boot 快速开始指南

本指南将帮助您快速上手 Structure Boot 框架，从零开始构建一个完整的 Spring Boot 应用。

## 🚀 环境准备

### 必需环境

- **JDK**: 8 或更高版本
- **Maven**: 3.6 或更高版本
- **IDE**: IntelliJ IDEA 或 Eclipse
- **数据库**: MySQL 5.7+ 或 PostgreSQL
- **Redis**: 5.0+ (可选，用于缓存)

### 环境检查

```bash
# 检查 Java 版本
java -version

# 检查 Maven 版本
mvn -version

# 检查 Git 版本
git --version
```

## 📦 创建项目

### 方式一：使用 Spring Initializr

1. 访问 [Spring Initializr](https://start.spring.io/)
2. 选择以下配置：

   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 2.1.x
   - **Group**: com.example
   - **Artifact**: demo
   - **Package name**: com.example.demo
   - **Packaging**: Jar
   - **Java**: 8

3. 添加依赖：

   - Spring Web
   - Spring Data JPA
   - MySQL Driver
   - Spring Boot DevTools

4. 点击 "Generate" 下载项目

### 方式二：手动创建

创建以下目录结构：

```
demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   └── resources/
│   └── test/
│       └── java/
└── pom.xml
```

## 🔧 配置依赖

### 1. 父 POM 配置

在 `pom.xml` 中添加 Structure Boot 依赖管理：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Structure Boot Demo</name>
    <description>Structure Boot 框架使用示例</description>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring.boot.version>2.7.18</spring.boot.version>
        <structure.version>1.2.3</structure.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- Spring Boot 依赖管理 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-parent</artifactId>
                <version>${spring.boot.version}</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>

            <!-- Structure Boot 依赖管理 -->
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-boot-parent</artifactId>
                <version>${structure.version}</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!-- Spring Boot Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- Structure Boot Web Starter -->
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-restful-web-starter</artifactId>
        </dependency>

        <!-- Structure Boot MyBatis Starter -->
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-mybatis-starter</artifactId>
        </dependency>

        <!-- Structure Boot Redis Starter -->
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-redis-starter</artifactId>
        </dependency>

        <!-- MySQL 驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!-- 测试依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. 依赖说明

- **structure-restful-web-starter**: Web 开发支持，包含统一异常处理、Swagger 文档等
- **structure-mybatis-starter**: MyBatis 增强功能，包含自动 ID 生成、时间注入等
- **structure-redis-starter**: Redis 分布式锁支持

## 🏃‍♂️ 创建启动类

### 1. 基础启动类

```java
package com.example.demo;

import cn.structure.starter.web.annotation.EnableSimpleGlobalException;
import cn.structure.starter.web.annotation.EnableSwagger;
import cn.structure.starter.web.annotation.EnableFastJsonHttpConverters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序启动类
 *
 * @author your-name
 * @since 1.0.0
 */
@SpringBootApplication
@EnableSimpleGlobalException  // 开启统一异常处理
@EnableSwagger               // 开启 Swagger 文档
@EnableFastJsonHttpConverters // 开启 FastJson 序列化
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 2. 注解说明

- `@EnableSimpleGlobalException`: 开启简易版全局异常处理
- `@EnableSwagger`: 开启 Swagger API 文档
- `@EnableFastJsonHttpConverters`: 开启 FastJson 序列化支持

## ⚙️ 配置文件

### 1. 基础配置

创建 `src/main/resources/application.yml` 文件：

```yaml
server:
  port: 8080

spring:
  application:
    name: structure-boot-demo

  # 数据源配置
  datasource:
    url: jdbc:mysql://localhost:3306/demo?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

  # Redis 配置
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

# Structure Boot 配置
structure:
  # MyBatis 配置
  mybatis:
    plugin:
      generate-id-type: snowflake # 使用雪花算法生成 ID
      data-center: 0 # 数据中心码
      machine: 0 # 机器码

  # MinIO 配置（可选）
  minio:
    url: http://localhost:9000
    access-key: minioadmin
    secret-key: minioadmin
    endpoint-enable: true

# Swagger 配置
swagger:
  title: Structure Boot Demo API
  description: Structure Boot 框架使用示例 API 文档
  version: v1.0.0
  contact:
    name: Your Name
    email: your-email@example.com
```

### 2. 环境配置

创建不同环境的配置文件：

**application-dev.yml** (开发环境):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo_dev
    username: dev_user
    password: dev_password

  redis:
    host: localhost
    port: 6379

logging:
  level:
    com.example.demo: DEBUG
    cn.structure: DEBUG
```

**application-prod.yml** (生产环境):

```yaml
spring:
  datasource:
    url: jdbc:mysql://prod-server:3306/demo_prod
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  redis:
    host: prod-redis-server
    port: 6379
    password: ${REDIS_PASSWORD}

logging:
  level:
    com.example.demo: INFO
    cn.structure: WARN
```

## 🗄️ 创建数据模型

### 1. 用户实体类

```java
package com.example.demo.entity;

import cn.structure.common.entity.BaseEntity;
import cn.structure.starter.mybatis.annotation.CreateTime;
import cn.structure.starter.mybatis.annotation.Id;
import cn.structure.starter.mybatis.annotation.UpdateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户实体类
 *
 * @author your-name
 * @since 1.0.0
 */
@Entity
@Table(name = "sys_user")
@ApiModel(description = "用户信息")
public class User extends BaseEntity {

    @Id
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    @Column(name = "username", unique = true, nullable = false, length = 20)
    @ApiModelProperty(value = "用户名", example = "admin", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    @Column(name = "password", nullable = false, length = 100)
    @ApiModelProperty(value = "密码", example = "123456", required = true)
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", unique = true, nullable = false, length = 100)
    @ApiModelProperty(value = "邮箱", example = "admin@example.com", required = true)
    private String email;

    @Column(name = "nickname", length = 50)
    @ApiModelProperty(value = "昵称", example = "管理员")
    private String nickname;

    @Column(name = "phone", length = 20)
    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    @Column(name = "status", nullable = false)
    @ApiModelProperty(value = "状态", example = "1", notes = "1:启用,0:禁用")
    private Integer status = 1;

    @CreateTime
    @Column(name = "create_time", nullable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @UpdateTime
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    // 构造函数
    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
```

### 2. 基础实体类

```java
package com.example.demo.entity;

import cn.structure.common.entity.BaseEntity;

/**
 * 基础实体类
 *
 * @author your-name
 * @since 1.0.0
 */
public abstract class BaseEntity {

    private String createBy;
    private String updateBy;
    private Integer isDeleted = 0;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
```

## 🗃️ 创建数据访问层

### 1. 用户 Mapper 接口

```java
package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问接口
 *
 * @author your-name
 * @since 1.0.0
 */
@Mapper
public interface UserMapper {

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> selectAll();

    /**
     * 插入用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户名和邮箱查询用户
     *
     * @param username 用户名
     * @param email 邮箱
     * @return 用户列表
     */
    List<User> selectByUsernameAndEmail(@Param("username") String username, @Param("email") String email);
}
```

### 2. MyBatis XML 映射文件

创建 `src/main/resources/mapper/UserMapper.xml` 文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.demo.mapper.UserMapper">

    <!-- 结果映射 -->
    <resultMap id="BaseResultMap" type="com.example.demo.entity.User">
        <id column="id" property="id" jdbcType="BIGINT"/>
        <result column="username" property="username" jdbcType="VARCHAR"/>
        <result column="password" property="password" jdbcType="VARCHAR"/>
        <result column="email" property="email" jdbcType="VARCHAR"/>
        <result column="nickname" property="nickname" jdbcType="VARCHAR"/>
        <result column="phone" property="phone" jdbcType="VARCHAR"/>
        <result column="status" property="status" jdbcType="INTEGER"/>
        <result column="create_time" property="createTime" jdbcType="TIMESTAMP"/>
        <result column="update_time" property="updateTime" jdbcType="TIMESTAMP"/>
        <result column="create_by" property="createBy" jdbcType="VARCHAR"/>
        <result column="update_by" property="updateBy" jdbcType="VARCHAR"/>
        <result column="is_deleted" property="isDeleted" jdbcType="INTEGER"/>
    </resultMap>

    <!-- 基础字段 -->
    <sql id="Base_Column_List">
        id, username, password, email, nickname, phone, status,
        create_time, update_time, create_by, update_by, is_deleted
    </sql>

    <!-- 根据ID查询用户 -->
    <select id="selectById" parameterType="java.lang.Long" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM sys_user
        WHERE id = #{id} AND is_deleted = 0
    </select>

    <!-- 根据用户名查询用户 -->
    <select id="selectByUsername" parameterType="java.lang.String" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM sys_user
        WHERE username = #{username} AND is_deleted = 0
    </select>

    <!-- 根据邮箱查询用户 -->
    <select id="selectByEmail" parameterType="java.lang.String" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM sys_user
        WHERE email = #{email} AND is_deleted = 0
    </select>

    <!-- 查询所有用户 -->
    <select id="selectAll" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM sys_user
        WHERE is_deleted = 0
        ORDER BY create_time DESC
    </select>

    <!-- 插入用户 -->
    <insert id="insert" parameterType="com.example.demo.entity.User" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO sys_user (
            username, password, email, nickname, phone, status,
            create_time, update_time, create_by, update_by, is_deleted
        ) VALUES (
            #{username}, #{password}, #{email}, #{nickname}, #{phone}, #{status},
            #{createTime}, #{updateTime}, #{createBy}, #{updateBy}, #{isDeleted}
        )
    </insert>

    <!-- 更新用户 -->
    <update id="update" parameterType="com.example.demo.entity.User">
        UPDATE sys_user
        <set>
            <if test="username != null">username = #{username},</if>
            <if test="password != null">password = #{password},</if>
            <if test="email != null">email = #{email},</if>
            <if test="nickname != null">nickname = #{nickname},</if>
            <if test="phone != null">phone = #{phone},</if>
            <if test="status != null">status = #{status},</if>
            <if test="updateTime != null">update_time = #{updateTime},</if>
            <if test="updateBy != null">update_by = #{updateBy},</if>
        </set>
        WHERE id = #{id} AND is_deleted = 0
    </update>

    <!-- 根据ID删除用户（逻辑删除） -->
    <update id="deleteById" parameterType="java.lang.Long">
        UPDATE sys_user
        SET is_deleted = 1, update_time = NOW()
        WHERE id = #{id}
    </update>

    <!-- 根据用户名和邮箱查询用户 -->
    <select id="selectByUsernameAndEmail" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM sys_user
        WHERE is_deleted = 0
        <if test="username != null and username != ''">
            AND username LIKE CONCAT('%', #{username}, '%')
        </if>
        <if test="email != null and email != ''">
            AND email LIKE CONCAT('%', #{email}, '%')
        </if>
        ORDER BY create_time DESC
    </select>

</mapper>
```

## 🏗️ 创建业务逻辑层

### 1. 用户服务接口

```java
package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author your-name
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User getById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getByEmail(String email);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 创建后的用户
     */
    User createUser(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 更新后的用户
     */
    User updateUser(User user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Long id);

    /**
     * 根据条件查询用户
     *
     * @param username 用户名（可选）
     * @param email 邮箱（可选）
     * @return 用户列表
     */
    List<User> searchUsers(String username, String email);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean isUsernameExists(String username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean isEmailExists(String email);
}
```

### 2. 用户服务实现类

```java
package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户服务实现类
 *
 * @author your-name
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Long id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectById(id);
    }

    @Override
    public User getByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        return userMapper.selectByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }

        // 检查用户名是否已存在
        if (isUsernameExists(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (isEmailExists(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 设置创建时间和更新时间
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsDeleted(0);

        // 插入用户
        userMapper.insert(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 检查用户是否存在
        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否已被其他用户使用
        User userWithSameUsername = getByUsername(user.getUsername());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(user.getId())) {
            throw new RuntimeException("用户名已被其他用户使用");
        }

        // 检查邮箱是否已被其他用户使用
        User userWithSameEmail = getByEmail(user.getEmail());
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(user.getId())) {
            throw new RuntimeException("邮箱已被其他用户使用");
        }

        // 设置更新时间
        user.setUpdateTime(new Date());

        // 更新用户
        userMapper.update(user);
        return getById(user.getId());
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }

        // 检查用户是否存在
        User existingUser = getById(id);
        if (existingUser == null) {
            return false;
        }

        // 逻辑删除用户
        int result = userMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public List<User> searchUsers(String username, String email) {
        return userMapper.selectByUsernameAndEmail(username, email);
    }

    @Override
    public boolean isUsernameExists(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        User user = getByUsername(username);
        return user != null;
    }

    @Override
    public boolean isEmailExists(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        User user = getByEmail(email);
        return user != null;
    }
}
```

## 🌐 创建控制器层

### 1. 用户控制器

```java
package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import cn.structure.starter.web.vo.ResResultVO;
import cn.structure.starter.web.util.IResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户管理控制器
 *
 * @author your-name
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/users")
@Api(tags = "用户管理", description = "用户相关的增删改查操作")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询用户")
    public ResResultVO<User> getUserById(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable @NotNull(message = "用户ID不能为空") Long id) {

        User user = userService.getById(id);
        if (user != null) {
            return IResultUtil.success(user);
        } else {
            return IResultUtil.fail("用户不存在");
        }
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    @ApiOperation("根据用户名查询用户")
    public ResResultVO<User> getUserByUsername(
            @ApiParam(value = "用户名", required = true, example = "admin")
            @PathVariable @NotNull(message = "用户名不能为空") String username) {

        User user = userService.getByUsername(username);
        if (user != null) {
            return IResultUtil.success(user);
        } else {
            return IResultUtil.fail("用户不存在");
        }
    }

    /**
     * 查询所有用户
     */
    @GetMapping
    @ApiOperation("查询所有用户")
    public ResResultVO<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return IResultUtil.success(users);
    }

    /**
     * 创建用户
     */
    @PostMapping
    @ApiOperation("创建用户")
    public ResResultVO<User> createUser(
            @ApiParam(value = "用户信息", required = true)
            @Valid @RequestBody User user) {

        try {
            User createdUser = userService.createUser(user);
            return IResultUtil.success(createdUser);
        } catch (Exception e) {
            return IResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    @ApiOperation("更新用户")
    public ResResultVO<User> updateUser(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable @NotNull(message = "用户ID不能为空") Long id,
            @ApiParam(value = "用户信息", required = true)
            @Valid @RequestBody User user) {

        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return IResultUtil.success(updatedUser);
        } catch (Exception e) {
            return IResultUtil.fail(e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public ResResultVO<Boolean> deleteUser(
            @ApiParam(value = "用户ID", required = true, example = "1")
            @PathVariable @NotNull(message = "用户ID不能为空") Long id) {

        boolean result = userService.deleteUser(id);
        if (result) {
            return IResultUtil.success(true);
        } else {
            return IResultUtil.fail("删除用户失败");
        }
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    @ApiOperation("搜索用户")
    public ResResultVO<List<User>> searchUsers(
            @ApiParam(value = "用户名", example = "admin")
            @RequestParam(required = false) String username,
            @ApiParam(value = "邮箱", example = "admin@example.com")
            @RequestParam(required = false) String email) {

        List<User> users = userService.searchUsers(username, email);
        return IResultUtil.success(users);
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/username/{username}")
    @ApiOperation("检查用户名是否存在")
    public ResResultVO<Boolean> checkUsernameExists(
            @ApiParam(value = "用户名", required = true, example = "admin")
            @PathVariable @NotNull(message = "用户名不能为空") String username) {

        boolean exists = userService.isUsernameExists(username);
        return IResultUtil.success(exists);
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check/email/{email}")
    @ApiOperation("检查邮箱是否存在")
    public ResResultVO<Boolean> checkEmailExists(
            @ApiParam(value = "邮箱", required = true, example = "admin@example.com")
            @PathVariable @NotNull(message = "邮箱不能为空") String email) {

        boolean exists = userService.isEmailExists(email);
        return IResultUtil.success(exists);
    }
}
```

## 🗄️ 创建数据库

### 1. 创建数据库

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE demo;
```

### 2. 创建用户表

```sql
-- 创建用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(20) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    nickname VARCHAR(50) COMMENT '昵称',
    phone VARCHAR(20) COMMENT '手机号',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted INT NOT NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email),
    KEY idx_create_time (create_time),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入测试数据
INSERT INTO sys_user (username, password, email, nickname, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@example.com', '系统管理员', 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'user1@example.com', '测试用户1', 1),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'user2@example.com', '测试用户2', 1);
```

## 🚀 运行项目

### 1. 启动应用

```bash
# 进入项目目录
cd demo

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 2. 访问应用

- **应用地址**: http://localhost:8080
- **Swagger 文档**: http://localhost:8080/swagger-ui.html
- **健康检查**: http://localhost:8080/actuator/health

### 3. 测试 API

使用 Swagger 文档或 Postman 测试以下接口：

#### 查询所有用户

```bash
GET http://localhost:8080/api/users
```

#### 根据 ID 查询用户

```bash
GET http://localhost:8080/api/users/1
```

#### 创建用户

```bash
POST http://localhost:8080/api/users
Content-Type: application/json

{
    "username": "newuser",
    "password": "123456",
    "email": "newuser@example.com",
    "nickname": "新用户"
}
```

#### 更新用户

```bash
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{
    "nickname": "更新后的昵称",
    "phone": "13900139000"
}
```

#### 删除用户

```bash
DELETE http://localhost:8080/api/users/1
```

## 🔧 添加更多功能

### 1. 添加 Redis 分布式锁

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @RedisLock("#user.username")
    @Override
    public User createUser(User user) {
        // 创建用户的逻辑
        // 使用用户名作为分布式锁的 key
        return userService.createUser(user);
    }
}
```

### 2. 添加缓存支持

```java
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @RCache(key = "#id")
    public ResResultVO<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return IResultUtil.success(user);
    }
}
```

### 3. 添加文件上传

```java
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private MinioTemplate minioTemplate;

    @PostMapping("/upload")
    public ResResultVO<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            minioTemplate.putObject("user-avatars", fileName, file.getInputStream(), file.getSize(), file.getContentType());
            String url = minioTemplate.getObjectURL("user-avatars", fileName, 3600);
            return IResultUtil.success(url);
        } catch (Exception e) {
            return IResultUtil.fail("文件上传失败: " + e.getMessage());
        }
    }
}
```

## 📚 下一步

恭喜！您已经成功创建了一个基于 Structure Boot 的完整应用。接下来可以：

1. **学习更多组件**: 查看其他 Starter 的使用方法
2. **添加更多功能**: 实现用户认证、权限管理等
3. **优化性能**: 添加缓存、连接池等优化
4. **部署应用**: 学习如何部署到生产环境
5. **贡献代码**: 参与项目开发，提交 Pull Request

## 🆘 遇到问题？

如果在使用过程中遇到问题，请：

1. 查看项目文档和示例代码
2. 在 GitHub 上创建 Issue
3. 查看常见问题解答
4. 联系项目维护者

---

**祝您使用愉快！** 🎉
