package com.mmall.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Component
public class DateTimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
    public static final String date_fm_zh_long = "yyyy-MM-dd";
    public static final String date_fm_en_long ="dd/MM/yy";

    public static final String date_fm_zh_short = "yyMM";
    public static final String date_fm_en_short ="MMMyy";

    public static final String format_ZH = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获得当前日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String currentDatetime() {
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(now());
    }

    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatDatetime(Date date) {
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static void main(String[] args) {
        System.out.println(currentDatetime());
        System.out.println(currentDate());
    }

    /**
     * 格式化日期时间
     *
     * @param date
     * @param pattern
     *            格式化模式，详见{@link SimpleDateFormat}构造器
     *            <code>SimpleDateFormat(String pattern)</code>
     * @return
     */
    public static String formatDatetime(Date date, String pattern) {
        SimpleDateFormat customFormat = (SimpleDateFormat) new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss")
                .clone();
        customFormat.applyPattern(pattern);
        if (date==null){
            return customFormat.format(now());
        }
        return customFormat.format(date);
    }

    /**
     * 获得当前日期
     * <p>
     * 日期格式yyyy-MM-dd
     *
     * @return
     */
    public static String currentDate() {
        return new SimpleDateFormat(
                "yyyy-MM-dd").format(now());
    }

    /**
     * 日期类型的当前日期
     * @return
     * @throws ParseException
     */
    public static Date nowDate() throws ParseException {
        return parseDate(now());
    }

    /**
     * 日期类型当前日期时间
     * @return
     * @throws ParseException
     */
    public static Date nowDateTime() throws ParseException {
        return parseDateTime(now());
    }


    /**
     * 格式化日期
     * <p>
     * 日期格式yyyy-MM-dd
     *
     * @return
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat(
                "yyyy-MM-dd").format(date);
    }

    /**
     * 获得当前时间
     * <p>
     * 时间格式HH:mm:ss
     *
     * @return
     */
    public static String currentTime() {
        return new SimpleDateFormat(
                "HH:mm:ss").format(now());
    }

    /**
     * 格式化时间
     * <p>
     * 时间格式HH:mm:ss
     *
     * @return
     */
    public static String formatTime(Date date) {
        return new SimpleDateFormat(
                "HH:mm:ss").format(date);
    }

    /**
     * 获得当前时间的<code>java.util.Date</code>对象
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }

    /**
     * 获得当前时间的毫秒数
     * <p>
     * 详见{@link System#currentTimeMillis()}
     *
     * @return
     */
    public static long millis() {
        return System.currentTimeMillis();
    }

    /**
     *
     * 获得当前Chinese月份
     *
     * @return
     */
    public static int month() {
        return calendar().get(Calendar.MONTH) + 1;
    }

    /**
     * 获得月份中的第几天
     *
     * @return
     */
    public static int dayOfMonth() {
        return calendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天是星期的第几天
     *
     * @return
     */
    public static int dayOfWeek() {
        return calendar().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 今天是年中的第几天
     *
     * @return
     */
    public static int dayOfYear() {
        return calendar().get(Calendar.DAY_OF_YEAR);
    }

    /**
     *判断原日期是否在目标日期之前
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /**
     *判断原日期是否在目标日期之后
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    /**
     *判断两日期是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * 判断某个日期是否在某个日期范围
     *
     * @param beginDate
     *            日期范围开始
     * @param endDate
     *            日期范围结束
     * @param src
     *            需要判断的日期
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    /**
     * 获得当前月的最后一天
     * <p>
     * HH:mm:ss为0，毫秒为999
     *
     * @return
     */
    public static Date lastDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
        cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
        return cal.getTime();
    }

    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     *
     * @return
     */
    public static Date firstDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTime();
    }

    private static Date weekDay(int week) {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTime();
    }

    /**
     * 获得周五日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date friday() {
        return weekDay(Calendar.FRIDAY);
    }

    /**
     * 获得周六日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date saturday() {
        return weekDay(Calendar.SATURDAY);
    }

    /**
     * 获得周日日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date sunday() {
        return weekDay(Calendar.SUNDAY);
    }

    /**
     * 将字符串日期时间转换成java.util.Date类型
     * <p>
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param datetime
     * @return
     */
    public static Date parseDatetime(String datetime) throws ParseException {
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").parse(datetime);
    }

    /**
     * 将字符串日期转换成java.util.Date类型
     *<p>
     * 日期时间格式yyyy-MM-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat(
                "yyyy-MM-dd").parse(date);
    }

    /**
     * 日期格式yyyy-MM-dd
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(Date date) throws ParseException {
        String d = new SimpleDateFormat(
                "yyyy-MM-dd").format(date);
        return new SimpleDateFormat(
                "yyyy-MM-dd").parse(d);

    }

    /**
     * 日期时间格式 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDateTime(Date date)throws ParseException {
        String d = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(date);
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").parse(d);
    }

    /**
     * 将字符串日期转换成java.util.Date类型
     *<p>
     * 时间格式 HH:mm:ss
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String time) throws ParseException {
        return new SimpleDateFormat(
                "HH:mm:ss").parse(time);
    }

    /**
     * 根据自定义pattern将字符串日期转换成java.util.Date类型
     *
     * @param datetime
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDatetime(String datetime, String pattern)
            throws ParseException {
        SimpleDateFormat format = (SimpleDateFormat) new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").clone();
        format.applyPattern(pattern);
        return format.parse(datetime);
    }

    /**
     *
     * 英文日期转换为中文日期
     * @return
     * @throws
     * @since
     * <br><b>作者： 李会军</b>
     * <br>创建时间：2015年9月16日 下午4:58:52
     */
    public static String parseEnDateToCH(String date, String sourceFormat, String descFormat) {
        SimpleDateFormat sdf_ch = new SimpleDateFormat(descFormat);
        SimpleDateFormat sdf_en = new SimpleDateFormat(sourceFormat, Locale.ENGLISH);
        String dateCH = "";
        try {
            dateCH = sdf_ch.format(sdf_en.parse(date));
        } catch (ParseException e) {
            logger.error("parseEnDateToCH_error",e);
        }
        return dateCH;
    }

    /**
     * 获取两个日期之间相差天数
     *<p>
     *
     * @param date1
     * Return
     * @throws ParseException
     */
    public static int  getDasyBetween(String date1, String date2) throws ParseException{
        if(date1 == null || date1.equals("") || date2 == null || date2.equals("")){
            return -10000;
        }
        long mill_start = parseDate(date1).getTime();
        long mill_end = parseDate(date2).getTime();
        long days_between = (mill_end - mill_start)/(1000*60*60*24);
        return Integer.parseInt(String.valueOf(days_between));
    }

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static Date parseDate(String dateValue, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateValue);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date getFristDayOfWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    public static Date getLastDayOfWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    public static Date getFristDayOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static Date getLastDayOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, max);
        return cal.getTime();
    }

    public static boolean isWeekEnd(String tradingDay){
        boolean flag = false;
        Date tradingDayDate = parseDate(tradingDay,DATE_PATTERN);
        Date date = getLastDayOfWeek(tradingDayDate);
        if(tradingDay.equals(format(date,DATE_PATTERN))){
            flag = true;
        }
        return flag;
    }

    public static boolean isMonthEnd(String tradingDay){
        boolean flag = false;
        Date tradingDayDate = parseDate(tradingDay,DATE_PATTERN);
        Date date = getLastDayOfMonth(tradingDayDate);
        if(tradingDay.equals(format(date,DATE_PATTERN))){
            flag = true;
        }
        return flag;
    }

    public static String getDateByFormat(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(now());
    }

    /**
     * @Title: addDate  获取某日期+-天数
     * @param date
     * @param days
     * @return
     * @return String    返回类型
     * @throws
     * @author 周胜兵
     */
    public static String addDate(String date, int days){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl = Calendar.getInstance();
        Date newDate = null;
        try {
            newDate = (Date) sdf.parse(date);
        } catch (ParseException e) {
            logger.error("addDate_error",e);
        }
        cl.setTime(newDate);
        cl.add(Calendar.DAY_OF_MONTH, days);

        newDate = cl.getTime();
        return sdf.format(newDate);
    }

    public static String currentDatetimeMSEC() {
        return new SimpleDateFormat("yyyy-MM-dd HHmmssSSS").format(now());
    }

    /**
     * 检查年月日是否合法
     * @param ymd
     * @return
     */
    public static boolean checkYearMonthDay(String ymd) {
        if (ymd == null || ymd.length() == 0) {
            return false;
        }
        //String s = ymd.replaceAll("[/\\- ]", "");
        String s = ymd;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(s);
            if (!format.format(date).equals(s)) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
