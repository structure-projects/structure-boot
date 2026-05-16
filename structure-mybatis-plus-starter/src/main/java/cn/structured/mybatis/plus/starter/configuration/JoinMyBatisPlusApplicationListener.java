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
package cn.structured.mybatis.plus.starter.configuration;

import cn.hutool.core.util.StrUtil;
import cn.structured.mybatis.plus.starter.annotations.*;
import cn.structured.mybatis.plus.starter.core.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 11:59
 */
public class JoinMyBatisPlusApplicationListener implements ApplicationListener<ContextRefreshedEvent>, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(JoinMyBatisPlusApplicationListener.class);

    private MetadataReaderFactory metadataReaderFactory;


    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.debug("JoinMyBatisPlusApplicationListener Started");
        
        // 获取启动类注解所在的包名
        Map<String, Object> beansWithAnnotation = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        Optional<Object> any = beansWithAnnotation.values().stream().findAny();
        String packageName = "";
        if (any.isPresent()) {
            Object declaringClass = any.get();
            packageName = ClassUtils.getPackageName(declaringClass.getClass());
            logger.debug("Found SpringBootApplication package: {}", packageName);
        } else {
            logger.warn("No SpringBootApplication annotation found in context.");
        }

        if (!StrUtil.isBlank(packageName)) {
            String classPathPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";
            logger.debug("Scanning for classes with pattern: {}", classPathPattern);
            
            Resource[] classResources = contextRefreshedEvent.getApplicationContext().getResources(classPathPattern);
            logger.info("Found {} class resources to process.", classResources.length);

            for (Resource classResource : classResources) {
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(classResource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    
                    // 跳过非实体类或内部类等，可根据需要增加过滤逻辑
                    logger.trace("Processing class: {}", className);

                    Class<?> clazz = ClassUtils.forName(className, contextRefreshedEvent.getApplicationContext().getClassLoader());
                    
                    // 检查是否有 TableName 注解，没有则跳过
                    TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
                    if (null == tableNameAnnotation) {
                        continue;
                    }
                    
                    String tableName = tableNameAnnotation.value();
                    logger.debug("Processing entity table: {}, class: {}", tableName, className);

                    // 将object 转换位class
                    JoinTableInfo joinTableInfo = new JoinTableInfo();
                    joinTableInfo.setTableClass(clazz);
                    joinTableInfo.setTableName(tableName);

                    // 表字段信息列表
                    List<JoinTableFieldInfo> fieldList = new ArrayList<>();
                    // 联表信息 k 属性名，v 属性的联表信息
                    Map<String, FieldJoinInfo> fieldJoinInfoHashMap = new HashMap<>();
                    // 搜索关键字集合
                    List<String> keywordList = new ArrayList<>();
                    List<String> dateTimeList = new ArrayList<>();
                    
                    Field[] fields = clazz.getDeclaredFields();
                    logger.trace("Analyzing {} fields for class {}", fields.length, className);

                    for (Field field : fields) {
                        JoinTableFieldInfo joinTableFieldInfo = new JoinTableFieldInfo();
                        String fieldName = field.getName();
                        Class<?> type = field.getType();
                        
                        // 处理主键
                        TableId tableId = field.getAnnotation(TableId.class);
                        if (null != tableId) {
                            joinTableInfo.setPrimaryKey(tableId.value());
                            logger.trace("Field {} is PrimaryKey with value {}", fieldName, tableId.value());
                        }
                        
                        // 处理逻辑删除
                        TableLogic tableLogic = field.getAnnotation(TableLogic.class);
                        if (null != tableLogic) {
                            String value = tableLogic.value();
                            joinTableInfo.setIsDelete(true);
                            joinTableInfo.setLogicDelete(value);
                            String delval = tableLogic.delval();
                            joinTableInfo.setDeleteValue(delval.equals("") ? "1" : delval);
                            logger.trace("Field {} is LogicDelete, logic value: {}, delete value: {}", fieldName, value, joinTableInfo.getDeleteValue());
                        }
                        
                        // 处理普通字段
                        TableField tableField = field.getAnnotation(TableField.class);
                        if (tableField != null) {
                            String column = tableField.value();
                            joinTableFieldInfo.setField(field);
                            joinTableFieldInfo.setColumn(column);
                            joinTableFieldInfo.setProperty(fieldName);
                            joinTableFieldInfo.setPropertyType(type);
                            
                            if (tableField.exist()) {
                                fieldList.add(joinTableFieldInfo);
                            }
                            
                            // 处理关键字搜索
                            Keyword keyword = field.getAnnotation(Keyword.class);
                            if (keyword != null) {
                                String keywordColumn = (!StrUtil.isBlank(keyword.value())) ? keyword.value() : column;
                                keywordList.add(keywordColumn);
                                logger.trace("Field {} added to keyword list as column: {}", fieldName, keywordColumn);
                            }

                            // 处理日期时间格式化
                            DateTime dateTime = field.getAnnotation(DateTime.class);
                            if (dateTime != null) {
                                String dateTimeColumn = (!StrUtil.isBlank(dateTime.value())) ? dateTime.value() : column;
                                dateTimeList.add(dateTimeColumn);
                                logger.trace("Field {} added to dateTime list as column: {}", fieldName, dateTimeColumn);
                            }
                            
                            // 处理 Where 条件
                            Where where = field.getAnnotation(Where.class);
                            if (null != where) {
                                Map<Class<?>, String> conditionGroup = new HashMap<>();
                                Condition[] conditions = where.value();
                                for (Condition condition : conditions) {
                                    Class<?>[] group = condition.group();
                                    String sqlCondition = StrUtil.blankToDefault(condition.condition(), condition.sqlCondition());
                                    for (Class<?> aClass : group) {
                                        conditionGroup.put(aClass, sqlCondition);
                                    }
                                }
                                joinTableFieldInfo.setSqlConditionGroup(conditionGroup);
                                logger.trace("Field {} has Where conditions configured", fieldName);
                            }
                        }
                        
                        // 处理联表查询 FieldJoin
                        FieldJoin fieldJoin = field.getAnnotation(FieldJoin.class);
                        if (null != fieldJoin) {
                            Join[] joins = fieldJoin.value();
                            FieldJoinInfo fieldJoinInfo = new FieldJoinInfo();
                            List<JoinInfo> joinInfoList = new ArrayList<>();
                            
                            for (Join join : joins) {
                                String[] columns = join.columns();
                                JoinInfo joinInfoItem = new JoinInfo();
                                joinInfoItem.setIsResult(join.result());
                                joinInfoItem.setJoinType(join.joinType());
                                joinInfoItem.setAliasName(join.aliasName());
                                joinInfoItem.setJoinTarget(join.joinTarget());
                                joinInfoItem.setGroups(Lists.newArrayList(join.group()));
                                JoinCondition[] value = join.value();
                                joinInfoItem.setJoinConditionInfo(Lists.newArrayList(value));
                                joinInfoItem.setColumns(Lists.newArrayList(columns));
                                joinInfoList.add(joinInfoItem);
                            }
                            
                            fieldJoinInfo.setJoinType(fieldJoin.joinType());
                            fieldJoinInfo.setResult((fieldJoin.result().equals(Object.class)) ? field.getType() : fieldJoin.result());
                            fieldJoinInfo.setResultName(fieldName);
                            fieldJoinInfo.setType(fieldJoin.type());
                            fieldJoinInfo.setJoinInfoList(joinInfoList);
                            fieldJoinInfoHashMap.put(fieldName, fieldJoinInfo);
                            logger.trace("Field {} configured with FieldJoin info, join count: {}", fieldName, joins.length);
                        }
                    }
                    
                    joinTableInfo.setFieldList(fieldList);
                    joinTableInfo.setJoinInfo(fieldJoinInfoHashMap);
                    joinTableInfo.setKeyword(keywordList);
                    joinTableInfo.setTimeList(dateTimeList);
                    
                    JoinHelper.joinTableInfoMap.put(clazz, joinTableInfo);
                    logger.debug("Successfully registered JoinTableInfo for table: {}, class: {}", tableName, className);
                    
                } catch (Exception e) {
                    logger.error("Failed to process class resource: {}", classResource, e);
                }
            }
            logger.info("Finished processing all classes in package: {}", packageName);
        } else {
            logger.warn("Package name is blank, skipping class scanning.");
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.metadataReaderFactory = new SimpleMetadataReaderFactory(resourceLoader);
    }
}
