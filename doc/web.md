# structure-restful-web

对应用场景为前后台分离、互联网、以及微服务中。接口 restful 风格进行封装

## 功能介绍

1. 封装 spring-boot-starter-web 启动器
2. 封装公共返回结果简易版和两级 code 码的公共异常处理
3. 对参数校验进行了封装
4. swagger 的开启和配置
5. 开启 fastJson 序列化

## 如何使用

### pom 引用

引用最新文档版本的依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-restful-web-starter</artifactId>
    <version>1.2.3</version>
</dependency>
```

### 选择性开启不同的公共异常拦截也可以自定义

1. 使用@EnableSimpleGlobalException 开启简易版本公共异常

```java
@SpringBootApplication
@EnableSimpleGlobalException
public class WebRestFulApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebRestFulApplication.class,args);
    }
}
```

2. 使用@EnableFatherGlobalException 开启多级 code 码公共异常

```java
@SpringBootApplication
@EnableFatherGlobalException
public class WebRestFulApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebRestFulApplication.class,args);
    }
}
```

### 参数校验开启

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

- 如果您开启了 structure-restful-web-starter 的公共异常拦截则不需要处理参数校验的错误信息
- 如果您使用自己的公共异常则需要自行处理参数校验的异常信息

### 返回结果封装

#### 系统内置 code 码

```java
/**
     * 验证成功
     */
    SUCCESS("成功！", "SUCCESS"),
    /**
     * 内部业务错误
     */
    FAIL("内部业务错误", "FAIL"),
    /**
     * 资源不存在
     */
    NOT_FOUND("资源不存在", "NOT_FOUND"),
    /**
     * 断路
     */
    FALLBACK("断路", "FALLBACK"),
    /**
     * 验证失败
     */
    UNAUTHORIZED("验证失败", "UNAUTHORIZED"),
    /**
     * 格式校验失败
     */
    VERIFICATION_FAILED("格式校验失败", "VERIFICATION_FAILED"),
    /**
     * 未知异常
     */
    ERR("异常", "ERR");
```

#### 简易返回结果实体 ResResultVO

```java
/**
     * 业务状态码
     */
    @ApiModelProperty(value = "业务状态码",example = "SUCCESS")
    private String code;

    /**
     * 返回的消息内容
     */
    @ApiModelProperty(value = "返回的消息内容",example = "成功！")
    private String message;

    /**
     * 业务是否成功
     */
    @ApiModelProperty(value = "业务是否成功",example = "true")
    private Boolean success;

    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的数据",example = "{}")
    private T data;

    /**
     * 系统响应的时间戳
     */
    @ApiModelProperty(value = "系统响应的时间戳",example = "112345644446")
    private Long timestamp;

```

#### 二级 code 码返回结果实体 ResultVO

```java
    /**
     * 系统级别的CODE码
     */
    @ApiModelProperty(value = "系统级别的CODE码",example = "SUCCESS")
    private String code;
    /**
     * 系统级别的消息内容
     */
    @ApiModelProperty(value = "系统级别的消息内容",example = "成功！")
    private String msg;
    /**
     * 业务级别的code码
     */
    @ApiModelProperty(value = "业务级别的code码",example = "SUCCESS")
    private String subCode;
    /**
     * 业务级别的消息内容
     */
    @ApiModelProperty(value = "业务级别的消息内容",example = "成功！")
    private String subMsg;
    /**
     * 系统响应的时间戳
     */
    @ApiModelProperty(value = "系统响应的时间戳",example = "112345644446")
    private long timestamp;
    /**
     * 系统响应的回参数据是一个泛型
     */
    @ApiModelProperty(value = "系统响应的回参数据是一个泛型",example = "{}")
    private T data;
```

### 开启 swagger

#### 使用@EnableSwagger 注解开启 swagger

```java
@EnableSwagger
@SpringBootApplication
public class WebRestFulApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebRestFulApplication.class,args);
    }
}
```

#### swagger 的配置

```yaml
swagger:
  title: 标题
  description: 描述
  version: v1.0.1
```

### 使用@EnableFastJsonHttpConverters 开启 FastJson 序列化

```java
@EnableFastJsonHttpConverters
@SpringBootApplication
public class WebRestFulApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebRestFulApplication.class,args);
    }
}
```

#### @EnableFastJsonHttpConverters 有两个属性

1. longToString 将 long 转换为 String 字符串 js 没有 long 类型会有经度丢失 默认是 false
2. nullShowValue 是否展示值为 null 的 key 默认是 false
