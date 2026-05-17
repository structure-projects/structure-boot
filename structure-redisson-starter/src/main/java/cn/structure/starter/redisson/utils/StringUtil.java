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
package cn.structure.starter.redisson.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * string 工具类
 * </p>
 *
 * @author chuck
 * @since 2020-12-26
 */
@Slf4j
public class StringUtil {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("#([a-zA-Z_][a-zA-Z0-9_.]*)");

    /**
     * <p>
     * 通过spel表达式获取理想的key值
     * 支持格式：#key, #redisLockBo.key, #redisLockBo.key:_#key
     * </p>
     *
     * @param key            key 参数
     * @param parameterNames 参数列表名
     * @param values         参数列表值
     * @return java.lang.String
     */
    public static String getValueBySpelKey(String key, String[] parameterNames, Object[] values) {
        log.debug("[StringUtil] 解析SpEL表达式 - spelKey: {}, parameterNames: {}, values: {}", key, Arrays.toString(parameterNames), Arrays.toString(values));
        //不存在表达式返回
        if (!key.contains("#")) {
            log.debug("[StringUtil] SpEL表达式不包含变量，直接返回 - key: {}", key);
            return key;
        }

        try {
            //创建 SpEL 上下文并设置变量
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterNames.length; i++) {
                //同时设置两种变量名：参数名和p0/p1/p2...，提高兼容性
                if (parameterNames[i] != null && !parameterNames[i].isEmpty()) {
                    context.setVariable(parameterNames[i], values[i]);
                }
                context.setVariable("p" + i, values[i]);
                log.debug("[StringUtil] 设置SpEL变量 - 参数名: {}, p{}: {}", parameterNames[i], i, values[i]);
            }

            //使用正则匹配变量并替换
            StringBuffer result = new StringBuffer();
            Matcher matcher = VARIABLE_PATTERN.matcher(key);
            while (matcher.find()) {
                String variable = matcher.group(1);
                try {
                    Object value = parser.parseExpression("#" + variable).getValue(context);
                    String replacement = value != null ? value.toString() : "";
                    matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                } catch (Exception e) {
                    log.warn("[StringUtil] 解析变量失败 - variable: {}, error: {}", variable, e.getMessage());
                    matcher.appendReplacement(result, "");
                }
            }
            matcher.appendTail(result);
            
            String resultKey = result.toString();
            log.debug("[StringUtil] SpEL表达式解析完成 - originalKey: {}, resultKey: {}", key, resultKey);
            return resultKey;
        } catch (Exception e) {
            log.error("[StringUtil] SpEL表达式解析失败 - key: {}, error: {}", key, e.getMessage(), e);
            return key;
        }
    }

    /**
     * <p>判断字符串是否为空</p>
     *
     * @param string
     * @return boolean
     **/
    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!StringUtil.isWhitespace(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * 判断是否为空白字符
     * </p>
     *
     * @param c
     * @return boolean
     **/
    private static boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    /**
     * <p>
     * unicode 转字符串
     * </p>
     *
     * @param unicode
     * @return java.lang.String
     **/
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    /**
     * <p>
     * 去掉英文单引号以及首尾空格
     * </p>
     *
     * @param string
     * @return java.lang.String
     **/
    public static String trimAndRemoveQuot(String string) {
        return string.replaceAll("['*]*", "").trim();
    }

    /**
     * <p>
     * 去掉英文单引号以及所有空格
     * </p>
     *
     * @param string
     * @return java.lang.String
     **/
    public static String removeAllBlankAndQuot(String string) {
        return string.replaceAll("['*| *|　*|\\s*]*", "").trim();
    }

    /**
     * <p>
     * 拼接redis带前缀的地址
     * </p>
     *
     * @param address redis 地址
     * @return 新的地址
     */
    public static String prefixAddress(String address) {
        if (!StringUtils.isEmpty(address) && !address.startsWith("redis")) {
            return "redis://" + address;
        }
        return address;
    }

}
