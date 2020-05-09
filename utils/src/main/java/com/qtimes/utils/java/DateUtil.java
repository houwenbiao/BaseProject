package com.qtimes.utils.java;

import android.text.TextUtils;
import android.text.format.DateUtils;

import com.qtimes.utils.android.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 *
 * @author 以后
 */
public class DateUtil extends DateUtils {
    private static final String SIMPLEDATE = "yyyy-MM-dd";
    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 将日期转换成字符串
     */
    public static String getDateString(Date d, String pattern) {
        if (d == null || "".equals(d)) {
            return "";
        }
        SimpleDateFormat simple = new SimpleDateFormat(
                pattern == null ? SIMPLEDATE : pattern);
        String date = simple.format(d);
        return date;
    }

    public static String getDateTip(Date d, String pattern) {
        if (d == null || "".equals(d)) {
            return "";
        }
        if (getDaysBetween(d, new Date()) >= 1 && getDaysBetween(d, new Date()) <= 3) {
            return "一天前";
        } else if (getDaysBetween(d, new Date()) > 3 && getDaysBetween(d, new Date()) <= 7) {
            return "三天前";
        }
        SimpleDateFormat simple = new SimpleDateFormat(
                pattern == null ? SIMPLEDATE : pattern);
        String date = simple.format(d);
        return date;
    }

    /**
     * 将时间类型的字符串转成int类型
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static int getDateInByString(String dateStr, String pattern) {
        Date date = getDate(dateStr, pattern);
        return date == null ? 0 : getDateInt(date);
    }

    /**
     * 将整数转换成字符串日期类型
     */
    public static String getDateStringByInt(int dateInt) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = simple.parse(dateInt + "");
            String dateStr = getDateString(date, null);
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将字符串转换成日期
     */
    public static Date getDate(String d, String pattern) {
        SimpleDateFormat simple = new SimpleDateFormat(
                pattern == null ? SIMPLEDATE : pattern);
        Date date = null;
        try {
            date = simple.parse(d);
        } catch (Exception e) {
            date=new Date();
        }
        return date;
    }


    /**
     * 获得当前时间int类型格式为yyyyMMdd
     *
     * @return
     */
    public static int getDateInt(Date d) {
        return StringUtil.String2Integer(getDateString(d, "yyyyMMdd"), 0);
    }

    /**
     * 获取两个时间相差的天数
     *
     * @param startDate
     * @param endDate
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(startDate);
        caled.setTime(endDate);
        // 设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);

        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }

    /**
     * 将两个时间中的每一天返回组成一个数组,包括开始和结束当天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getDates(Date startDate, Date endDate) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(startDate);
        caled.setTime(endDate);
        // 设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        long start = calst.getTimeInMillis();
        long end = caled.getTimeInMillis();
        List<Date> date = new ArrayList<Date>();
        do {
            date.add(new Date(start));
        } while ((start += 1000 * 60 * 60 * 24) <= end);
        return date;
    }

    /**
     * 将两个时间中的每一天返回组成一个数组,包括开始,不包括结束当天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getDatesWithoutEnd(Date startDate, Date endDate) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(startDate);
        caled.setTime(endDate);
        // 设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        long start = calst.getTimeInMillis();
        long end = caled.getTimeInMillis();
        List<Date> date = new ArrayList<Date>();
        do {
            date.add(new Date(start));
        } while ((start += 1000 * 60 * 60 * 24) < end);
        return date;
    }

    /**
     * 将int类型的时间转换成yyyy-MM-DD
     *
     * @param dateInt
     * @return
     */
    public static String getDateString(Integer dateInt) {
        if (dateInt == null || "".equals(dateInt)) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(dateInt.toString().subSequence(0, 4));
            sb.append("-");
            sb.append(dateInt.toString().subSequence(4, 6));
            sb.append("-");
            sb.append(dateInt.toString().subSequence(6, 8));
            return sb.toString();
        }
    }

    /**
     * 判断是否是24小时之前
     *
     * @param startDate
     * @param endDate
     */
    public static boolean isBefore24Hours(Date startDate, Date endDate) {
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(startDate);
        caled.setTime(endDate);

        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;
        return days >= 1;
    }

    public static String unixToDate(long unix) {
        Date nowTime = new Date();
        Date unixTime = new Date(unix * 1000);

        if (nowTime.getDate() == unixTime.getDate()) {
            return "今天";
        } else if ((nowTime.getDate() - unixTime.getDate()) == 1) {
            return "昨天";
        } else {
            long difference = nowTime.getTime() - unixTime.getTime();
            if (difference < 0)
                return "日期异常";
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormater.format(unixTime);
        }
    }

    public static Date now() {
        return new Date();
    }

    /**
     * 截取Date(1418784200000-0000)的固定长度
     */
    public static String spliteTime(String dateStr) {
        String sequence = dateStr.replace("/Date(", "");
        return sequence.substring(0, 13).trim();
    }

    /**
     * 判断是否是日期格式字符
     * yyyy-mm-dd
     *
     * @param data
     * @return
     */
    public static boolean isDateStr(String data) {
        if (TextUtils.isEmpty(data)) {
            return false;
        }
        try {
            String year = data.substring(0, 4);
            String month = data.substring(5, 7);
            String day = data.substring(8, 10);
            Integer.parseInt(year);
            Integer.parseInt(month);
            Integer.parseInt(day);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    /**
     * 判断是否是8位的出生日期
     * @param date
     * @return
     */
    public static  boolean isDate(String date){
       if(TextUtils.isEmpty(date)){
           return false;
       }
        if(date.length()!=8){
            return false;
        }
        int d=StringUtil.strToInt(date,0);
        if(d==0){
            return false;
        }
        return true;
    }

    /**
     * 截取指定长度 从0开始，包左不包右
     */
    public static String spliteTime(String dateStr, int start, int end) {
        CharSequence sequence = dateStr.subSequence(start, end);
        return sequence.toString();
    }
    /**
     *
     * 计算两个日期相差的月份数
     *
     * @param pattern  日期1和日期2的日期格式
     * @return  相差的月份数
     * @throws ParseException
     */
    public static int countMonths(String date, String pattern) {
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);

        Calendar c1= Calendar.getInstance();
        Calendar c2= Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(date));
            int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
            //开始日期若小月结束日期
            if(year<0){
                year=-year;
                return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
            }

            return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
        } catch (ParseException e) {
            return -1;
        }
    }
    public static int getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                return -1;
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }

    /**
     * @param @return 设定文件
     * @return Long 返回类型
     * @throws
     * @Title: getCurrentSystemTime
     * @Description: 获取当前系统时间
     */
    public static Long getCurrentSystemTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getCurrentSystemTimeStr(){
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return simple.format(date);
    }

    /**
     * 将时间戳转成格式化时间
     *
     * @param milliseconds
     */
    public static String getFormatTimeYY(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(milliseconds));
    }


    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(long date) {
        if (date == 0) {
            return "";
        }
        date = date * 1000;
        long diff = new Date().getTime() - date;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    private static final long EIGHT_COMPLEMENT_MILLISECONDS = 8 * 60 * 60 * 1000L;
    private static final long MILLISECONDS_OF_DAY = 24 * 60 * 60 * 1000L;

    public static boolean isSameDay(long timestamp1, long timestamp2) {
        return ((timestamp1 + EIGHT_COMPLEMENT_MILLISECONDS) / MILLISECONDS_OF_DAY)
                == ((timestamp2 + EIGHT_COMPLEMENT_MILLISECONDS) / MILLISECONDS_OF_DAY);
    }

    public static String timestampToDateString(long second) {
        long timestamp = second * 1000L;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String foredata = format.format(new Date(timestamp));
        return foredata;
    }
}
