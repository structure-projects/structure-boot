# structure-redission

一个对 redission 的封装,redission 要比 redis 功能更强大

## Spring Boot 3.0 兼容性说明

本项目已适配 Spring Boot 3.0，SpEL 表达式解析支持两种写法：

1. **使用真实参数名**（推荐，需要 Maven 编译配置保留参数名）
2. **使用 p0/p1/p2... 索引形式**（兼容性更好，随时可用）

## redisson 的使用

### POM 依赖###

```xml
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-redisson-starter</artifactId>
            <version>1.3.1</version>
        </dependency>
```

### redisson 的使用

#### 使用 redissonClient

```java
     @Resource
     private RedissonClient redissonClient;
```

#### 使用 RedissonClient 存储对象

```java
    RBucket<String> test = redissonClient.getBucket("test");
    test.set("test redissonClient");
    System.out.println("test = " + test.get());
```

#### 使用 RedissonClient Map List 对象结构请到 redisson 官网查看

[redisson Github 地址](https://github.com/redisson/redisson)

### redisson 锁的使用

```java
    @RequestMapping("/testLock")
    @Lock(keys = "#key")  // 使用真实参数名
    // @Lock(keys = "#p0")  // 或使用索引形式
    public void testLock(@RequestParam("key") String key) throws InterruptedException {
        System.out.println("key = " + key);
        Thread.sleep(10000L);
    }

    @RequestMapping("/testLock2")
    @Lock(keys = "#testVO.id")  // 使用真实参数名
    // @Lock(keys = "#p0.id")  // 或使用索引形式
    public void testLockObject(@RequestBody TestVO testVO ) throws InterruptedException {
        System.out.println("key = " + testVO.getId());
        Thread.sleep(10000L);
    }
```

### 缓存结构说明

#### 1. @WCache 写缓存注解

写缓存注解是对要要执行的方法返回的结果写入缓存中

```java
	@WCache(key = "#giftId",isObjCache = false,
			map = @CMap(isMap = true ,mapKey = "ACCOUNT-GIFT:_#accountId"),
			time = @CTime(isTime = true,time = 4,timeType = TimeUnit.HOURS))
```

@WCache 支持 map 缓存策略 list 缓存策略，时间缓存策略，是否为对象缓存

#### 2. @RCache 读缓存注解

读对象缓存注解通过代理实现无则写入，有则读取如果读取成功不会执行要代理的方法只有在写入缓存是会指定要代理的方法取得返回结果写入缓存中

```java
    @RCache(key = "GIFT-TYPE:_#giftType",time = 1)
```

#### 3. @RListCache 集合缓存注解

集合缓存注解是对 redisList 存储结构封装 list 缓存注解 可以搭配 map 结构和对象结构混合使用

#### 4. @RCacheMa pMap 缓存注解

Map 缓存注解 是对 redis-Map 存储结构封装 map 缓存注解 可以搭配 list 结构和对象结构混合使用

```java
    @RCacheMap(mapKey = "ACCOUNT-GIFT:_#accountId",key = "#giftId",isTime = true,time = 4,timeType = TimeUnit.HOURS)
```

读取存储在 map 中的单条数据

#### 5. @RMapAllCache 读全部 map 缓存注解

```java
    @RMapAllCache(mapKey = "ACCOUNT-GIFT:_#accountId",keyName = "giftId",time = @CTime(isTime = true,time = 4,timeType = TimeUnit.HOURS))
```

读取 map 中全部的数据

### redisson 缓存的使用

```java
    /**
     * 写入缓存,同时构建 object 对象和 集合对象,以及map对象
     * @param testVO
     * @return
     */
    @RequestMapping(value = "cache")
    @WCache(key = "#testVO.id",isObjCache = true
            ,list = @CList(listKeyName = "test-list",isList = true,size = 100,time = @CTime(isTime = true,time = 10))
            ,map = @CMap(mapKey = "test-map",isMap = true,time = @CTime(isTime = true,time = 100))
    )
    // 或使用索引形式
    // @WCache(key = "#p0.id",isObjCache = true
    public TestVO cache(TestVO testVO){
        System.out.println("testVO = " + testVO);
        return testVO;
    }

    /**
     * 读缓存 如果没有读到则更新缓存 object
     * @param id
     * @return
     */
    @RCache(key = "#id")
    // 或使用索引形式
    // @RCache(key = "#p0")
    @RequestMapping("/getCache")
    public TestVO getCache(@RequestParam("id") String id){
        TestVO testVO = new TestVO();
        testVO.setId(id);
        testVO.setName("没有读取到");
        System.out.println("需要写入缓存,如果缓存中有值则不会写入缓存");
        return testVO;
    }

    /**
     * map和list配合使用
     * @param testVO
     * @return
     */
    @RequestMapping(value = "cacheMapList")
    @WCache(key = "#testVO.id",isObjCache = false
            ,list = @CList(listKeyName = "cache-list",isList = true,size = 100
            ,time = @CTime(isTime = true,time = 10),mapKey = "cache-map",value = CList.ListType.MAP)
            ,map = @CMap(mapKey = "cache-map",isMap = true,time = @CTime(isTime = true,time = 100))
    )
    // 或使用索引形式
    // @WCache(key = "#p0.id",isObjCache = false
    public TestVO cacheMapList(TestVO testVO){
        System.out.println("testVO = " + testVO);
        return testVO;
    }

    /**
     * list配合object使用
     * @param testVO
     * @return
     */
    @RequestMapping(value = "cacheList")
    @WCache(key = "#testVO.id",isObjCache = true
            ,list = @CList(listKeyName = "cache-list-key",isList = true,size = 100
            ,time = @CTime(isTime = true,time = 10),value = CList.ListType.KEY))
    // 或使用索引形式
    // @WCache(key = "#p0.id",isObjCache = true
    public TestVO cacheList(TestVO testVO){
        System.out.println("testVO = " + testVO);
        return testVO;
    }

    /**
     * list和map关联的数据结构
     * 该结构需要手动补偿缓存值
     * @return
     */
    @RListCache(key = "cache-list",mapKey = "cache-map",value = CList.ListType.MAP)
    public List<TestVO> getMapList(){
        return new ArrayList<>();
    }

```

## Maven 编译配置说明

为确保能正确获取参数名，请确保项目的 `pom.xml` 中 maven-compiler-plugin 配置了 `<parameters>true</parameters>`：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <parameters>true</parameters>
    </configuration>
</plugin>
```
