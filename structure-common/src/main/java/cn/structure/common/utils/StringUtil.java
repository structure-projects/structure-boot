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
package cn.structure.common.utils;

/**
 * @author chuck
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     **/
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        int l = str.length();
        for (int i = 0; i < l; i++) {
            if (!StringUtil.isWhitespace(str.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为空白字符
     *
     * @param c 字符
     * @return boolean
     **/
    private static boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    /**
     * unicode 转字符串
     *
     * @param unicode unicode
     * @return java.lang.String
     **/
    public static String unicode2String(String unicode) {

        StringBuilder string = new StringBuilder();

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
     * 去掉英文单引号以及首尾空格
     *
     * @param str 字符串
     * @return java.lang.String
     **/
    public static String trimAndRemoveQuot(String str) {
        return str.replaceAll("['*]*", "").trim();
    }

    /**
     * 去掉英文单引号以及所有空格
     *
     * @param str 字符串
     * @return java.lang.String
     **/
    public static String removeAllBlankAndQuot(String str) {
        return str.replaceAll("['*| *|　*|\\s*]*", "").trim();
    }

    /**
     * 手机号中间4位替换为掩码
     *
     * @param phone 手机号
     * @return java.lang.String
     **/
    public static String maskReplaceStar(String phone) {
        String result = null;
        if (!isBlank(phone)) {
            if (phone.length() < 7) {
                result = phone;
            } else {
                String start = phone.substring(0, 3);
                String end = phone.substring(phone.length() - 4, phone.length());
                result = start + "****" + end;
            }
        }
        return result;
    }
}
