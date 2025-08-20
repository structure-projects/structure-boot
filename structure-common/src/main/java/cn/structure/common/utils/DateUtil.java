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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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

    private DateUtil() {
    }

    /**
     * 默认根式
     */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式，年份，例如：2004，2008
     */
    public static final String DATE_FORMAT_YYYY = "yyyy";

    /**
     * 日期格式，年份和月份，例如：200707，200808
     */
    public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

    /**
     * 日期格式，年份和月份，例如：200707，2008-08
     */
    public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";

    /**
     * 日期格式，年月日，例如：050630，080808
     */
    public static final String DATE_FORMAT_YYMMDD = "yyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
     */
    public static final String DATE_FORMAT_YY_MM_DD = "yy-MM-dd";

    /**
     * 日期格式，年月日，例如：20050630，20080808
     */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
     */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式，年月日，例如：2016.10.05
     */
    public static final String DATE_FORMAT_POINTYYYYMMDD = "yyyy.MM.dd";

    /**
     * 日期格式，年月日，例如：2016年10月05日
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD = "yyyy年MM月dd日";

    /**
     * 日期格式，年月日时分，例如：200506301210，200808081210
     */
    public static final String DATE_FORMAT_YYYYMMDDHHmm = "yyyyMMddHHmm";

    /**
     * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm";

    /**
     * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

    /**
     * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
     * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";

    /**
     * 日期格式，月日时分，例如：10-05 12:00
     */
    public static final String DATE_FORMAT_MMDDHHMI = "MM-dd HH:mm";

    /**
     * 年月日格式 例如：20221202
     */
    public static final String YMD = "yyyyMMdd";

    /**
     * 年份格式 例如：2022
     */
    public static final String YMD_YEAR = "yyyy";

    /**
     * 年月日格式 例如：2022-12-02
     */
    public static final String YMD_BREAK = "yyyy-MM-dd";

    /**
     * 年月日时分秒格式 例如：20221202123050
     */
    public static final String YMDHMS = "yyyyMMddHHmmss";

    /**
     * 年月日时分秒格式 例如：2022-12-02 12:30:50
     */
    public static final String YMDHMS_BREAK = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年月日时分格式 例如：2022-12-02 12:30
     */
    public static final String YMDHMS_BREAK_HALF = "yyyy-MM-dd HH:mm";

    /**
     * 年月日格式 例如：2022年12月02日
     */
    public static final String PP = "YYYY年MM月dd日";

    /**
     * 分钟
     */
    public static final long CAL_MINUTES = 1000 * 60;
    /**
     * 小时
     */
    public static final long CAL_HOURS = 1000 * 60 * 60;
    /**
     * 天
     */
    public static final long CAL_DAYS = 1000 * 60 * 60 * 24;
    /**
     * 月
     */
    public static final long CAL_MONTHS = 1000 * 60 * 60 * 24 * 30L;

    /**
     * 获取当前时间格式化后的值
     *
     * @param pattern 格式
     * @return java.lang.String
     */
    public static String getNowDateText(String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * 获取日期格式化后的值
     *
     * @param date    时间
     * @param pattern 格式
     * @return java.lang.String
     */
    public static String getDateText(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串时间转换成Date格式
     *
     * @param date    日期
     * @param pattern 格式
     * @return java.text.SimpleDateFormat
     */
    public static Date getDate(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    /**
     * 获取日期格式
     *
     * @param pattern 格式
     * @return java.text.SimpleDateFormat
     */
    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取时间戳
     *
     * @param date 时间
     * @return java.lang.String
     */
    public static Long getTime(Date date) {
        return date.getTime();
    }

    /**
     * 计算时间差
     *
     * @param startDate 开始时间
     * @param endDate   截至时间
     * @param calType   计算类型,按分钟、小时、天数计算
     * @return 返回时间差值
     */
    public static int calDiffs(Date startDate, Date endDate, long calType) {
        Long start = DateUtil.getTime(startDate);
        Long end = DateUtil.getTime(endDate);
        return (int) ((end - start) / calType);
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param startDate 开始时间
     * @param endDate   截至时间
     * @return java.lang.String
     */
    public static String timeDiffText(Date startDate, Date endDate) {
        long calDiffs = DateUtil.calDiffs(startDate, endDate, DateUtil.CAL_MINUTES);
        if (calDiffs == 0) {
            return "刚刚";
        }
        if (calDiffs < 60) {
            return calDiffs + "分钟前";
        }
        calDiffs = DateUtil.calDiffs(startDate, endDate, DateUtil.CAL_HOURS);
        if (calDiffs < 24) {
            return calDiffs + "小时前";
        }
        calDiffs = DateUtil.calDiffs(startDate, endDate, DateUtil.CAL_DAYS);
        if (calDiffs < 4) {
            return calDiffs + "天前";
        }
        return parseDateToStr(startDate, DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
    }

    /**
     * 显示某种约定后的时间值,类似微信朋友圈发布说说显示的时间那种
     *
     * @param date 时间
     * @return java.lang.String
     */
    public static String showTimeText(Date date) {
        return DateUtil.timeDiffText(date, new Date());
    }


    /**
     * 格式化时间
     *
     * @param time       时间
     * @param timeFormat 格式化格式
     * @return java.lang.String
     **/
    public static String parseDateToStr(Date time, String timeFormat) {
        if (null == time) {
            time = new Date();
        }
        if (StringUtil.isBlank(timeFormat)) {
            timeFormat = DEFAULT_FORMAT;
        }
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        return dateFormat.format(time);
    }


    /**
     * 将长整型转换为日期对象
     *
     * @param time 当前时间毫秒数
     * @return java.util.Date
     **/
    public static Date toDate(long time) {
        return new Date(time);
    }

    /**
     * 将日期转换为长整型（毫秒）
     *
     * @param date 时间
     * @return long
     **/
    public static long toLong(Date date) {
        return (date == null) ? 0 : date.getTime();
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime   当前时间
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return boolean
     **/
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }

    /**
     * 根据日历的规则，为给定的日历字段添加或减去指定的时间
     *
     * @param field 指定的日历字段
     * @param date  需要操作的日期对象
     * @param value 更改的时间值
     * @return java.util.Date
     **/
    public static Date add(int field, Date date, int value) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(field, value);
        return ca.getTime();
    }

    /**
     * 获取本周第一天凌晨0点
     *
     * @return java.util.Date
     **/
    public static Date getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        return cal.getTime();

    }

    /**
     * 获取今日凌晨0点
     *
     * @return java.util.Date
     */
    public static Date getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
