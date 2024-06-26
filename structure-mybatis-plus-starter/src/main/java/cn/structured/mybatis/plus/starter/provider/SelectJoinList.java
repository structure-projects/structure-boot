package cn.structured.mybatis.plus.starter.provider;

import cn.hutool.core.util.StrUtil;
import cn.structured.mybatis.plus.starter.core.*;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 联表分页查询
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 14:59
 */
public class SelectJoinList {

    public String selectJoinList(HashMap condition) {
        QueryJoinPageListWrapper queryWrapper = (QueryJoinPageListWrapper) condition.get("wrapper");
        Object entity = queryWrapper.getEntity();
        boolean isJoin = queryWrapper.getIsJoin();
        TableInfo tableInfo = SqlHelper.table(queryWrapper.getClazz());
        List<String> selectColumn = queryWrapper.getSelectColumn();
        StringBuilder sqlBuilder = new StringBuilder();
        JoinTableInfo joinTableInfo = JoinHelper.getTableInfo(queryWrapper.getClazz());
        sqlBuilder.append("<script> ");
        sqlBuilder.append("SELECT ");
        if (selectColumn.isEmpty()) {
            String columns = JoinHelper.getColumns(joinTableInfo);
            sqlBuilder.append(columns);
        } else {
            for (int i = 0; i < selectColumn.size(); i++) {
                String column = selectColumn.get(i);
                if (i != 0) {
                    sqlBuilder.append(" , ");
                }
                if (column.indexOf(".") > 0) {
                    sqlBuilder.append(column);
                } else {
                    sqlBuilder.append(joinTableInfo.getTableName());
                    sqlBuilder.append(".");
                    sqlBuilder.append(column);
                    sqlBuilder.append(" as ");
                    sqlBuilder.append(StrUtil.toCamelCase(column));
                }
            }
        }

        //获取联表
        Map<String, FieldJoinInfo> joinInfoList = joinTableInfo.getJoinInfo();
        if (isJoin) {
            joinInfoList.values().forEach(joinInfoItem -> {
                String joinColumnsSql = JoinHelper.getJoinColumnsSql(joinInfoItem, queryWrapper.getJoinGroup());
                sqlBuilder.append(joinColumnsSql);
            });
        }

        sqlBuilder.append(" FROM ");
        sqlBuilder.append(joinTableInfo.getTableName());
        if (isJoin) {
            joinInfoList.values().forEach(joinInfoItem -> {
                String joinSql = JoinHelper.getJoinSql(joinTableInfo, joinInfoItem, queryWrapper.getJoinGroup());
                sqlBuilder.append(" \n");
                sqlBuilder.append(joinSql);
            });
        }

        List<String> searchList = queryWrapper.getSearchList();

        List<String> timeList = queryWrapper.getTimeList();

        sqlBuilder.append("<trim prefix=\"where (\" suffix=\")\" suffixOverrides=\" AND|OR \">");
        boolean withLogicDelete = tableInfo.isWithLogicDelete();
        if (withLogicDelete) {
            TableFieldInfo logicDeleteFieldInfo = tableInfo.getLogicDeleteFieldInfo();
            sqlBuilder.append(tableInfo.getTableName());
            sqlBuilder.append(".");
            sqlBuilder.append(logicDeleteFieldInfo.getColumn());
            sqlBuilder.append("=");
            sqlBuilder.append(logicDeleteFieldInfo.getLogicNotDeleteValue());
            sqlBuilder.append(" ");
        } else {
            sqlBuilder.append(" 1 = 1 ");
        }
        sqlBuilder.append("<trim suffixOverrides=\"AND\">");
        sqlBuilder.append("<if test= 'wrapper.entity.");
        sqlBuilder.append(tableInfo.getKeyProperty());
        sqlBuilder.append("!= null' >");
        sqlBuilder.append(" AND ");
        sqlBuilder.append(tableInfo.getTableName());
        sqlBuilder.append(".");
        sqlBuilder.append(tableInfo.getKeyColumn());
        sqlBuilder.append("=#{");
        sqlBuilder.append("wrapper.entity.");
        sqlBuilder.append(tableInfo.getKeyProperty());
        sqlBuilder.append("}");
        sqlBuilder.append("</if>");

        //可以执行得组
        Class<?> joinGroup = queryWrapper.getJoinGroup();

        //缓存得字段信息
        List<JoinTableFieldInfo> joinTableInfoFieldList = joinTableInfo.getFieldList();
        if (null != entity) {
            joinTableInfoFieldList
                    .forEach(tableFieldInfo -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append("<if test= '");
                        sb.append("wrapper.entity.");
                        sb.append(tableFieldInfo.getProperty());
                        sb.append(" != null' > ");
                        Map<Class<?>, String> sqlConditionGroup = tableFieldInfo.getSqlConditionGroup();
                        String columnName = tableInfo.getTableName() + StrUtil.C_DOT + tableFieldInfo.getColumn();
                        String em = "wrapper.entity." + tableFieldInfo.getProperty();
                        if (null != sqlConditionGroup && sqlConditionGroup.containsKey(joinGroup)) {
                            sb.append(" AND ");
                            String sqlCondition = sqlConditionGroup.get(joinGroup);
                            sb.append(String.format(sqlCondition, columnName, em));
                        } else {
                            sb.append(" AND ");
                            sb.append(String.format(SqlCondition.EQUAL, columnName, em));
                        }
                        sb.append("</if>");
                        if (!searchList.contains(tableFieldInfo.getColumn()) && !timeList.contains(tableFieldInfo.getColumn())) {
                            sqlBuilder.append(sb);
                        }
                    });
        }
        sqlBuilder.append("</trim>");

        //搜索部分
        sqlBuilder.append("<trim prefix=\" AND (\" suffix=\")\" suffixOverrides=\" OR \">");
        searchList.stream().forEach(searchStr -> {
            sqlBuilder.append("<if test=\" wrapper.search!=null and wrapper.search!=''\">");
            if (searchStr.indexOf(".") < 0) {
                sqlBuilder.append(tableInfo.getTableName());
                sqlBuilder.append(".");
            }
            sqlBuilder.append(searchStr);
            sqlBuilder.append(" LIKE CONCAT('%',#{wrapper.search},'%') OR ");
            sqlBuilder.append("</if>");
        });
        sqlBuilder.append("</trim>");

        //时间部分
        sqlBuilder.append("<trim prefix=\" AND (\" suffix=\")\" suffixOverrides=\" OR \">");
        timeList.stream()
                .filter(timeStr -> queryWrapper.getBeginTime() != null || queryWrapper.getEndTime() != null)
                .forEach(timeStr -> {
                    sqlBuilder.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\" AND \">");
                    sqlBuilder.append("<if test=\" wrapper.beginTime!=null \">\n");
                    sqlBuilder.append("<![CDATA[ DATE_FORMAT(");

                    if (timeStr.indexOf(".") < 0) {
                        sqlBuilder.append(tableInfo.getTableName());
                        sqlBuilder.append(".");
                    }

                    sqlBuilder.append(timeStr);
                    sqlBuilder.append(", '%Y-%m-%d')>=  DATE_FORMAT(#{wrapper.beginTime}, '%Y-%m-%d')   ]]>\n");
                    sqlBuilder.append(" AND \n");
                    sqlBuilder.append("</if>");
                    sqlBuilder.append("<if test=\" wrapper.endTime!=null \">\n");
                    sqlBuilder.append("<![CDATA[ DATE_FORMAT(");

                    if (timeStr.indexOf(".") < 0) {
                        sqlBuilder.append(tableInfo.getTableName());
                        sqlBuilder.append(".");
                    }

                    sqlBuilder.append(timeStr);
                    sqlBuilder.append(", '%Y-%m-%d') <=  DATE_FORMAT(#{wrapper.endTime}, '%Y-%m-%d')   ]]>\n");
                    sqlBuilder.append("</if>");
                    sqlBuilder.append("</trim>");
                    sqlBuilder.append(" OR ");
                });
        sqlBuilder.append("</trim>");

        if (StrUtil.isNotBlank(queryWrapper.getCondition())) {
            sqlBuilder.append(" AND ( ");
            sqlBuilder.append(queryWrapper.getCondition());
            sqlBuilder.append(")");
        }

        sqlBuilder.append("</trim>");

        sqlBuilder.append("</script> ");
        return sqlBuilder.toString();
    }

}
