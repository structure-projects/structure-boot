# structure-mybatis-plus

structure-mybatis-plus 对mybatis进行部分功能的扩展

## 功能介绍

1. 集成mybatis-plus
2. 批量插入
3. 联表查询
4. 代码生成

## 如何使用

### 引用POM ###

```xml
    <dependency>
        <groupId>cn.structured</groupId>
        <artifactId>structure-mybatis-plus-starter</artifactId>
        <version>${last.version}</version>
    </dependency>
```

### yaml 配置 ### 

```yaml
server:
  port: 18003
spring:
  datasource:
    username: root
    password: test
    url: jdbc:mysql://yourServerAddress:3306/mybatis?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 批量插入展示 ###

Mapper 需要继承 cn.structured.mybatis.plus.starter.base.IBaseMapper

```java
package cn.structured.mybatisplus.generate.example.dao;

import cn.structured.mybatis.plus.starter.base.IBaseMapper;
import cn.structured.mybatisplus.generate.example.entity.OrgPost;

/**
 * <p>
 * 组织职务 Mapper 接口
 * </p>
 *
 * @author chuck
 * @since 2021-08-02
 */
public interface OrgPostMapper extends IBaseMapper<OrgPost> {

}
```

调用法即可调用批量插入

```java
  public void insertList(){

        List<OrgPost> orgPostList = new ArrayList<>();

        OrgPost orgPost = new OrgPost();
        orgPost.setId(11);
        orgPost.setPostName("orgPost");
        orgPost.setParentId(0);
        orgPost.setSort(0);
        orgPostList.add(orgPost);

        OrgPost orgPost2 = new OrgPost();
        orgPost2.setId(12);
        orgPost.setPostName("orgPost2");
        orgPost.setParentId(0);
        orgPost.setSort(0);
        orgPostList.add(orgPost2);

        orgPostMapper.insertList(orgPostList);
    }
```

### 关表查询 ### 

联表查询主要维护数据实体关系即可

```java
    /**
     * 创建人
     */
    @TableField(value = "name", exist = false)
    @FieldJoin(value = {
            @Join(group = {UserGroup.class, PostGroup.class}, joinTarget = Staff.class, aliasName = "ss", columns = "name", value = {
                    @JoinCondition(currentColumn = "create_id", targetColumn = "id"),
            })
    })
    private String createBy;

    /**
     * 职务列表
     */
    @TableField(exist = false)
    @FieldJoin(type = JoinResultEnum.MANY, result = OrgPost.class, value = {
            @Join(group = {PostGroup.class, StaffPostGroup.class}, joinType = JoinTypeEnum.LEFT_JOIN, joinTarget = StaffPost.class, aliasName = "sp", value = {
                    @JoinCondition(currentColumn = "id", targetColumn = "staff_id")
            }),
            @Join(group = {StaffPostGroup.class}, result = true, joinType = JoinTypeEnum.LEFT_JOIN, joinTarget = OrgPost.class, aliasName = "org", columns = {"id", "post_name"}, value = {
                    @JoinCondition(condition = "sp.post_id = org.id")
            })
    })
    private List<OrgPost> orgPost;

    /**
     * 修改人
     */
    @TableField(exist = false)
    @FieldJoin(value = {
            @Join(group = {UserGroup.class}, joinTarget = Staff.class, aliasName = "us", columns = {"id", "name"}, value = {
                    @JoinCondition(currentColumn = "update_id", targetColumn = "id"),
            })
    })
    private Staff updateBy;
```

### 代码生成 ### 

#### POM 配置 ####

```xml
        <buil>
            <plugin>
                <groupId>cn.structured</groupId>
                <artifactId>structure-mybatis-plus-generate</artifactId>
                <configuration>
                    <configurationFile>${basedir}/src/main/resources/generator-config.yaml</configurationFile>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                    </dependency>
                </dependencies>
            </plugin>
        </buil>
```

#### 代码生成配置 #### 

```yaml
globalConfig:
  author: chuck
  open: false
  idType: NONE
  dateType: ONLY_DATE
  enableCache: false
  activeRecord: false
  baseResultMap: true
  baseColumnList: true
  swagger2: false
  fileOverride: true
dataSourceConfig:
  url: jdbc:mysql://39.97.124.2:3306/gcxm?characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai
  driverName: com.mysql.cj.jdbc.Driver
  username: gcxm
  password: WFJ4ekTwkhRwAiHZ
packageConfig:
  parent: cn.structured.mybatisplus.generate.example
  moduleName:
  entity: entity
  service: service
  serviceImpl: service.impl
  mapper: dao
  xml: mapper
  controller: controller
  pathInfo:
    entity_path: .\src\main\java\cn\structured\mybatisplus\generate\example\entity
    service_path: .\src\main\java\cn\structured\mybatisplus\generate\example\service
    service_impl_path: .\src\main\java\cn\structured\mybatisplus\generate\example\service\impl
    mapper_path: .\src\main\java\cn\structured\mybatisplus\generate\example\dao
    xml_path: .\src\main\resources\mapper
    controller_path: .\src\main\java\cn\structured\mybatisplus\generate\example\controller
strategyConfig:
  naming: underline_to_camel
  columnNaming: underline_to_camel
  entityLombokModel: true
  superMapperClass: com.baomidou.mybatisplus.core.mapper.BaseMapper
  superServiceClass: com.baomidou.mybatisplus.extension.service.IService
  superServiceImplClass: com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
  controllerMappingHyphenStyle: true
  restControllerStyle: true
  tablePrefix:
  include:
  - user_info
tableFill:
  is_deleted: INSERT
  create_time: INSERT
  update_time: INSERT_UPDATE

```