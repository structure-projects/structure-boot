# Structure Boot 项目代码风格和规范

## 1. 代码风格

### 1.1 命名规范

#### 包命名
- 包名使用小写字母，使用点分隔符
- 遵循 Java 包命名规范，使用公司域名反转作为前缀
- 示例：`cn.structure.common.entity`

#### 类命名
- 使用 PascalCase 命名法（大驼峰命名）
- 类名应该清晰表达其功能
- 示例：`ResultVO`、`DateUtil`、`StructureRedisAutoConfiguration`

#### 方法命名
- 使用 camelCase 命名法（小驼峰命名）
- 方法名应该清晰表达其功能
- 示例：`getNowDateText`、`calDiffs`、`iDistributedLock`

#### 变量命名
- 使用 camelCase 命名法
- 变量名应该清晰表达其含义
- 示例：`startDate`、`endDate`、`redisTemplate`

#### 常量命名
- 使用全大写字母，单词之间用下划线分隔
- 示例：`DEFAULT_FORMAT`、`CAL_MINUTES`、`DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS`

### 1.2 代码格式

#### 缩进
- 使用 4 空格缩进，不使用制表符
- 缩进应该一致，保持代码的可读性

#### 空行
- 类成员之间、方法之间应该有适当的空行
- 逻辑块之间应该有适当的空行，提高代码可读性

#### 括号使用
- 左括号 `{` 应该与语句在同一行
- 右括号 `}` 应该单独占一行，与对应的语句对齐
- 示例：
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

#### 行长度
- 代码行长度应该控制在 120 字符以内，超过应该换行
- 换行时应该保持代码的可读性和逻辑结构

## 2. 代码规范

### 2.1 版权声明
- 每个文件都应该包含标准的 Apache License 2.0 版权声明
- 版权声明应该位于文件的顶部
- 示例：
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

### 2.2 注释规范

#### 类注释
- 每个类都应该有详细的 Javadoc 注释
- 注释应该包括：功能描述、作者、版本、创建日期等
- 示例：
  ```java
  /**
   * <p>
   * 日期工具类
   * </p>
   *
   * @author chuck
   * @version 1.0.1
   * @since 2020-12-26
   */
  public class DateUtil {
      // 类实现
  }
  ```

#### 方法注释
- 每个方法都应该有详细的 Javadoc 注释
- 注释应该包括：功能描述、参数说明、返回值说明、异常说明等
- 示例：
  ```java
  /**
   * 获取当前时间格式化后的值
   *
   * @param pattern 格式
   * @return java.lang.String
   */
  public static String getNowDateText(String pattern) {
      // 方法实现
  }
  ```

#### 变量注释
- 重要变量应该有注释说明
- 示例：
  ```java
  /**
   * 默认格式
   */
  public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
  ```

### 2.3 代码结构

#### 工具类
- 工具类应该使用私有构造方法，避免实例化
- 工具类的方法应该是静态的，方便直接调用
- 示例：
  ```java
  public class DateUtil {
      
      private DateUtil() {
      }
      
      // 静态方法
  }
  ```

#### 配置类
- 使用 Spring Boot 标准的配置注解
- 示例：
  ```java
  @Configuration
  @AutoConfigureAfter(RedisAutoConfiguration.class)
  @Import(DistributedLockAspectConfiguration.class)
  public class StructureRedisAutoConfiguration {
      // 配置方法
  }
  ```

#### 依赖注入
- 使用 Spring 的依赖注入机制
- 优先使用构造函数注入，其次使用 `@Resource` 注解
- 示例：
  ```java
  @Service
  public class FileService {
      
      @Resource
      private MinioTemplate minioTemplate;
      
      // 方法实现
  }
  ```

### 2.4 异常处理
- 方法声明中应该使用 `throws` 关键字声明可能抛出的异常
- 合理处理异常，使用自定义异常类
- 示例：
  ```java
  public static Date getDate(String date, String pattern) throws ParseException {
      SimpleDateFormat sdf = getSimpleDateFormat(pattern);
      return sdf.parse(date);
  }
  ```

### 2.5 日志记录
- 使用统一的日志配置
- 遵循项目的日志规范

## 3. 技术栈使用

### 3.1 核心技术栈
- Spring Boot：作为基础框架
- Lombok：使用注解减少样板代码
- Swagger：生成 API 文档
- Redis：用于缓存和分布式锁
- MyBatis/MyBatis-Plus：用于数据库访问
- MinIO：用于对象存储

### 3.2 注解使用
- 使用 Lombok 注解：`@Getter`、`@Setter`、`@ToString`、`@Builder`、`@EqualsAndHashCode`、`@NoArgsConstructor`、`@AllArgsConstructor`
- 使用 Swagger 注解：`@ApiModel`、`@ApiModelProperty`
- 使用 Spring 注解：`@Configuration`、`@Bean`、`@ConditionalOnBean`、`@AutoConfigureAfter`、`@Import`、`@Service`、`@RestController`、`@RequestMapping`、`@GetMapping`、`@PostMapping`、`@PathVariable`、`@RequestBody`、`@Valid`、`@Resource`

## 4. 项目结构

### 4.1 模块化结构
- 按功能模块划分，如 `structure-common`、`structure-redis-starter`、`structure-mybatis-starter` 等
- 每个模块都有自己的职责和功能

### 4.2 包结构
- 按功能划分包，如 `entity`、`utils`、`configuration`、`lock` 等
- 包结构应该清晰，便于代码管理和维护

### 4.3 资源文件
- 配置文件和资源文件放在 `src/main/resources` 目录下
- 配置文件使用 YAML 格式，如 `application.yml`
- 资源文件应该按照功能分类存放

## 5. 其他规范

### 5.1 版本号
- 使用语义化版本号，如 `1.0.1`
- 版本号应该在类注释和 pom.xml 文件中保持一致

### 5.2 作者信息
- 每个文件都应该有作者信息
- 作者信息应该在类注释中体现

### 5.3 代码质量
- 代码应该简洁、可读性高
- 避免冗余代码和复杂的逻辑结构
- 遵循 SOLID 原则

### 5.4 测试
- 确保代码通过所有测试
- 编写单元测试和集成测试

### 5.5 提交信息
- 提交信息应该清晰明了
- 遵循 Git 提交规范

## 6. 总结

Structure Boot 项目的代码风格和规范遵循了 Java 开发的最佳实践，包括：

- 清晰的命名规范
- 统一的代码格式
- 详细的注释
- 合理的代码结构
- 标准的技术栈使用
- 模块化的项目结构

这些规范有助于提高代码的可读性、可维护性和可扩展性，确保项目的质量和稳定性。