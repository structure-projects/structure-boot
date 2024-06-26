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

import java.lang.annotation.Annotation;
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
        //获取启动类注解
        Map<String, Object> beansWithAnnotation = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        Optional<Object> any = beansWithAnnotation.values().stream().findAny();
        String packageName = "";
        if (any.isPresent()) {
            Object declaringClass = any.get();
            packageName = ClassUtils.getPackageName(declaringClass.getClass());
        }
        if (!StrUtil.isBlank(packageName)) {
            Resource[] classResources = contextRefreshedEvent.getApplicationContext().getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class");
            for (Resource classResource : classResources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(classResource);
                String className = metadataReader.getClassMetadata().getClassName();
                Class<?> clazz = ClassUtils.forName(className, contextRefreshedEvent.getApplicationContext().getClassLoader());
                //将object 转换位class
                JoinTableInfo joinTableInfo = new JoinTableInfo();
                joinTableInfo.setTableClass(clazz);
                TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
                String tableName = "";
                if (null != tableNameAnnotation) {
                    tableName = tableNameAnnotation.value();
                } else {
                    continue;
                }
                joinTableInfo.setTableName(tableName);
                //表字段信息列表
                List<JoinTableFieldInfo> fieldList = new ArrayList<>();
                //联表信息 k 属性名，v 属性的联表信息
                Map<String, FieldJoinInfo> fieldJoinInfoHashMap = new HashMap<>();
                //搜索关键字集合
                List<String> keywordList = new ArrayList<>();
                List<String> dateTimeList = new ArrayList<>();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    JoinTableFieldInfo joinTableFieldInfo = new JoinTableFieldInfo();
                    String fieldName = field.getName();
                    Class<?> type = field.getType();
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (null != tableId) {
                        joinTableInfo.setPrimaryKey(tableId.value());
                    }
                    TableLogic tableLogic = field.getAnnotation(TableLogic.class);
                    if (null != tableLogic) {
                        String value = tableLogic.value();
                        joinTableInfo.setIsDelete(true);
                        joinTableInfo.setLogicDelete(value);
                        String delval = tableLogic.delval();
                        joinTableInfo.setDeleteValue(delval.equals("") ? "1" : delval);
                    }
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
                        Keyword keyword = field.getAnnotation(Keyword.class);
                        if (keyword != null) {
                            String keywordColumn = (!StrUtil.isBlank(keyword.value())) ? keyword.value() : column;
                            keywordList.add(keywordColumn);
                        }

                        DateTime dateTime = field.getAnnotation(DateTime.class);
                        if (dateTime != null) {
                            String dateTimeColumn = (!StrUtil.isBlank(dateTime.value())) ? dateTime.value() : column;
                            dateTimeList.add(dateTimeColumn);
                        }
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
                        }
                    }
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
                    }
                }
                joinTableInfo.setFieldList(fieldList);
                joinTableInfo.setJoinInfo(fieldJoinInfoHashMap);
                joinTableInfo.setKeyword(keywordList);
                joinTableInfo.setTimeList(dateTimeList);
                JoinHelper.joinTableInfoMap.put(clazz, joinTableInfo);
            }
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.metadataReaderFactory = new SimpleMetadataReaderFactory(resourceLoader);
    }
}
