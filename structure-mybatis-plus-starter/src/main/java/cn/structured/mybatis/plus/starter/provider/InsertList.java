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
package cn.structured.mybatis.plus.starter.provider;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 批量插入实现类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/6/28 17:18
 */
public class InsertList {

    Log log = LogFactory.getLog(this.getClass());

    /**
     * 批量插入的实现
     *
     * @param condition
     * @return String
     */
    public String insertList(HashMap condition) {
        //获取到列表
        List list = (List) condition.get("list");
        Object object = SqlHelper.getObject(log, list);
        TableInfo table = SqlHelper.table(object.getClass());
        List<TableFieldInfo> fieldList = table.getFieldList();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("<script>");
        sqlSb.append("INSERT INTO ");
        sqlSb.append(table.getTableName());
        sqlSb.append("(");

        String columns = fieldList.stream()
                .map(TableFieldInfo::getColumn)
                .collect(Collectors.joining(","));
        if (IdType.AUTO != table.getIdType()) {
            sqlSb.append(table.getKeyColumn());
            sqlSb.append(",");
        }

        sqlSb.append(columns);
        sqlSb.append(")");
        sqlSb.append(" VALUES ");
        sqlSb.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sqlSb.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");

        if (IdType.AUTO != table.getIdType()) {
            sqlSb.append("#{record.");
            sqlSb.append(table.getKeyProperty());
            sqlSb.append("}");
            sqlSb.append(",");
        }

        String property = fieldList.stream()
                .map(field -> "#{record." + field.getProperty() + "}")
                .collect(Collectors.joining(","));

        sqlSb.append(property);
        sqlSb.append("</trim>");
        sqlSb.append("</foreach>");
        sqlSb.append("</script>");
        return sqlSb.toString();
    }

}
