package cn.structured.mybatis.plus.starter.core;

import cn.hutool.core.util.StrUtil;
import cn.structured.mybatis.plus.starter.annotations.JoinCondition;
import cn.structured.mybatis.plus.starter.enums.JoinResultEnum;
import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 联表工具类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 14:42
 */
public class JoinHelper {

    /**
     * 实体反射信息
     */
    public static Map<Class, JoinTableInfo> joinTableInfoMap = new ConcurrentHashMap();

    /**
     * 获取表信息
     *
     * @param clazz 类型
     * @return {@link JoinTableInfo} 返回这个表的信息
     */
    public static JoinTableInfo getTableInfo(Class clazz) {
        return joinTableInfoMap.get(clazz);
    }

    /**
     * 获取所有的列
     *
     * @param joinTableInfo 联表查询的基本信息
     * @return {@link String}
     */
    public static String getColumns(JoinTableInfo joinTableInfo) {
        //表名
        String tableName = joinTableInfo.getTableName();
        //列名
        List<String> columnList = new ArrayList<>();
        //添加主键
        columnList.add(tableName + "." + joinTableInfo.getPrimaryKey());
        //添加全部列
        columnList.addAll(joinTableInfo.getFieldList().stream().map(joinTableFieldInfo ->
                tableName + "." + joinTableFieldInfo.getColumn() + " AS " + joinTableFieldInfo.getProperty()
        ).collect(Collectors.toList()));
        //转换为逗号分割
        return StrUtil.join(",", columnList);
    }

    /**
     * 获取关联sql
     *
     * @param joinTableInfo 联表信息
     * @param fieldJoinInfo 字段信息
     * @return 返回对应的SQL
     */
    public static String getJoinSql(JoinTableInfo joinTableInfo, FieldJoinInfo fieldJoinInfo, Class<?> group) {
        StringBuilder stringBuilder = new StringBuilder();
        fieldJoinInfo.getJoinInfoList().stream()
                .filter(joinInfo -> (group == null || joinInfo.getGroups().contains(group)))
                .forEach(joinInfo -> {
                    JoinTableInfo targetJoinTableInfo = joinTableInfoMap.get(joinInfo.getJoinTarget());
                    String tableName = targetJoinTableInfo.getTableName();
                    String currentTableName = joinTableInfo.getTableName();
                    JoinTypeEnum joinType = joinInfo.getJoinType();
                    stringBuilder.append(joinType.getKeyword());
                    stringBuilder.append(" ");
                    stringBuilder.append(targetJoinTableInfo.getTableName());
                    if (!StrUtil.isBlank(joinInfo.getAliasName())) {
                        stringBuilder.append(" ");
                        stringBuilder.append(joinInfo.getAliasName());
                    }
                    stringBuilder.append(" ON ");
                    List<JoinCondition> joinConditionInfo = joinInfo.getJoinConditionInfo();
                    joinConditionInfo.stream().forEach(joinCondition -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append(joinCondition.value());
                        if (joinCondition.targetColumn().indexOf(".") < 0) {
                            if (!StrUtil.isBlank(joinInfo.getAliasName())) {
                                sb.append(joinInfo.getAliasName());
                            } else {
                                sb.append(tableName);
                            }
                            sb.append(".");
                        }

                        sb.append(joinCondition.targetColumn());

                        sb.append(joinCondition.joinKeyword().getKeyword());

                        if (joinCondition.currentColumn().indexOf(".") < 0) {
                            sb.append(currentTableName);
                            sb.append(".");
                        }

                        sb.append(joinCondition.currentColumn());

                        stringBuilder.append((!StrUtil.isBlank(joinCondition.condition())) ? joinCondition.condition() : sb.toString());

                    });
                    stringBuilder.append(" \n ");
                });
        return stringBuilder.toString();
    }

    /**
     * 获取管理列sql
     *
     * @param fieldJoinInfo 字段信息
     * @return 返回联表字段的SQL
     */
    public static String getJoinColumnsSql(FieldJoinInfo fieldJoinInfo, Class<?> group) {
        StringBuilder columns = new StringBuilder();
        String resultName = fieldJoinInfo.getResultName();
        Class<?> result = fieldJoinInfo.getResult();
        //判断是否为对象类型
        final boolean isObject = !(result.equals(String.class) || result.equals(Boolean.class)
                || result.equals(Double.class) || result.equals(Float.class)
                || result.equals(Integer.class) || result.equals(Long.class)
                || result.equals(BigDecimal.class) || result.equals(BigInteger.class)
                || result.equals(Date.class) || result.equals(LocalDateTime.class)
                || result.equals(LocalTime.class) || result.equals(LocalDate.class)
                || result.equals(Byte.class) || result.equals(Short.class)
                || result.equals(Character.class)
        );
        fieldJoinInfo.getJoinInfoList().stream().filter(joinInfo -> (group == null || joinInfo.getGroups().contains(group))).forEach(joinInfo -> {
            Class<?> joinTarget = joinInfo.getJoinTarget();
            JoinTableInfo joinTableInfo = joinTableInfoMap.get(joinTarget);
            if (joinInfo.getColumns().size() <= 0 && joinInfo.getIsResult()) {
                joinTableInfo.getFieldList().forEach(joinTableFieldInfo -> joinInfo.getColumns().add(joinTableFieldInfo.getColumn()));
            }
            joinInfo.getColumns().forEach(column -> {
                columns.append(", ");
                if (!StrUtil.isBlank(joinInfo.getAliasName())) {
                    columns.append(joinInfo.getAliasName());
                } else {

                    columns.append(joinTableInfo.getTableName());
                }
                columns.append(".");
                columns.append(column);
                columns.append(" AS ");
                if (!StrUtil.isBlank(resultName)) {
                    columns.append(resultName);
                } else {
                    columns.append(StrUtil.toCamelCase(joinTableInfo.getTableName()));
                }

                if (isObject) {
                    columns.append("_");
                    columns.append(StrUtil.toCamelCase(column));
                }
            });
        });
        return columns.toString();
    }

    /**
     * map结果响应listObject
     *
     * @param records     结果集
     * @param entityClass 类型
     * @return 返回对应类型的结果
     */
    public static <R> List<R> getList(List<HashMap<String, Object>> records, Class<R> entityClass) {
        //设置新的结果集合
        Map<Object, Map<String, Object>> newResultMap = Maps.newHashMap();
        //缓存子项目结果集合
        Map<String, Map<String, Object>> subListMap = Maps.newHashMap();
        //缓存子项目结果
        Map<String, Map<String, Object>> subObjectMap = Maps.newHashMap();
        //获取联表信息
        JoinTableInfo tableInfo = JoinHelper.getTableInfo(entityClass);
        //获取联表字段
        Map<String, FieldJoinInfo> joinInfo = tableInfo.getJoinInfo();
        //遍历结果
        records.stream().forEach(stringObjectHashMap -> {
            //构建新的实体对象
            Map newEntity = new HashMap();
            //遍历属性为当前实体属性
            stringObjectHashMap.keySet().stream().filter(key -> !(key.indexOf("_") > 0)).forEach(key -> {
                newEntity.put(key, stringObjectHashMap.get(key));
            });
            //获取hash
            final String hashCode = newEntity.hashCode() + "";
            //判断响应结果中是否存在当前对象hash
            if (!newResultMap.containsKey(hashCode)) {
                newResultMap.put(hashCode + "", newEntity);
            }
            //遍历带有子级的映射
            stringObjectHashMap.keySet().stream().filter(key -> (key.indexOf("_") > 0)).forEach(key -> {
                //分解父子属性
                String[] keys = key.split("_");
                //父
                String parent = keys[0];
                //子
                String sub = keys[1];
                //判断是否为映射关系
                if (joinInfo.containsKey(parent)) {
                    FieldJoinInfo fieldJoinInfo = joinInfo.get(parent);
                    JoinResultEnum type = fieldJoinInfo.getType();
                    //判断是多的情况返回值应该是个list
                    if (type.equals(JoinResultEnum.MANY)) {
                        //从缓存中获取
                        Map<String, Object> stringObjectMap = subListMap.get(hashCode + "_" + parent);
                        //判断是否为空
                        if (null == stringObjectMap) {
                            stringObjectMap = new HashMap<>();
                            subListMap.put(hashCode + "_" + parent, stringObjectMap);
                        }
                        stringObjectMap.put(getNewKey(stringObjectMap, sub + "_0"), stringObjectHashMap.get(key));
                    } else {
                        //parentField.set(finalGetObject, objects);
                        Map<String, Object> stringObjectMap = subObjectMap.get(hashCode + "_" + parent);
                        if (null == stringObjectMap) {
                            stringObjectMap = new HashMap<>();
                            subObjectMap.put(hashCode + "_" + parent, stringObjectMap);
                        }
                        stringObjectMap.put(sub, stringObjectHashMap.get(key));
                    }
                }
            });
        });
        //解析对象类型
        subObjectMap.keySet().stream().forEach(key -> {
            String[] keys = key.split("_");
            String hashCode = keys[0];
            String resultName = keys[1];
            Map obj = newResultMap.get(hashCode);
            FieldJoinInfo fieldJoinInfo = joinInfo.get(resultName);
            Map<String, Object> stringObjectMap = subObjectMap.get(key);
            Object resultObj = JSONObject.parseObject(JSONObject.toJSONString(stringObjectMap), fieldJoinInfo.getResult());
            obj.put(resultName, resultObj);
        });
        //存储集合的列表
        Map<String, List> subObject = new HashMap<>();
        //存储集合中对象的列表
        Map<String, Object> subListObject = new HashMap<>();
        //解析集合类型
        subListMap.keySet().stream().forEach(key -> {
            String[] keys = key.split("_");
            String resultName = keys[1];
            List list = subObject.get(key);
            if (null == list) {
                list = new ArrayList();
                subObject.put(key, list);
            }
            List arrList = list;
            subListObject.get(key);
            Map<String, Object> stringObjectMap = subListMap.get(key);
            stringObjectMap.keySet().stream().forEach(subKey -> {
                try {
                    String[] subKeys = subKey.split("_");
                    String fieldName = subKeys[0];
                    String number = subKeys[1];
                    Object subObj = subListObject.get(key + "_" + number);
                    if (null == subObj) {
                        subObj = joinInfo.get(resultName).getResult().newInstance();
                        arrList.add(subObj);
                        subListObject.put(key + "_" + number, subObj);
                    }
                    Field declaredField = subObj.getClass().getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    declaredField.set(subObj, stringObjectMap.get(subKey));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            });
        });
        //设置集合到实体里
        for (String key : subObject.keySet()) {
            String[] keys = key.split("_");
            String hashCode = keys[0];
            String resultName = keys[1];
            Map obj = newResultMap.get(hashCode);
            obj.put(resultName, subObject.get(key));
        }
        return newResultMap.values().stream().map(map -> JSONObject.parseObject(JSONObject.toJSONString(map), entityClass)).collect(Collectors.toList());
    }

    public static String getNewKey(Map map, String key) {
        if (map.containsKey(key)) {
            Integer index = key.lastIndexOf("_") + 1;
            Integer num = Integer.parseInt(key.substring(index));
            num++;
            key = key.substring(0, index) + num;
            getNewKey(map, key);
        }
        return key;
    }
}
