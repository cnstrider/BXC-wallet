package com.landis.eoswallet.util.eos;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final String FULL_DATE = "yyyyMMddHHmmss";
    private static final Random rand = new Random();
    private static final Gson gson = new Gson();
    private static final int DEFAULT_SEED = 10000;
    public static SimpleDateFormat fullDateFormat_no_xxx = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final Random random = new Random();
    private static final String pattern_regex_float = "(-?\\d+.?\\d+)";


    /**
     * 获取完整日期
     *
     * @return
     */
    public static String getFullDate() {
        return new SimpleDateFormat(FULL_DATE).format(new Date());
    }

    /**
     * 生成一个随机数字，在种子范围内
     *
     * @param seed
     * @return
     */
    public static int getRandNumber(int seed) {
        return rand.nextInt(seed);
    }

    /**
     * 生成一个随机数字,在10000范围内
     *
     * @return
     */
    public static int getRandNumber() {
        return getRandNumber(DEFAULT_SEED);
    }

    /**
     * 生成一个随机数字, 大于等于seed, 小于等于seed+rand
     *
     * @param seed
     * @param rand
     * @return
     */
    public static int getRandNumber(int seed, int rand) {
        return seed + getRandNumber(rand);
    }

    public static String toJsonByGson(Object o) {
        return gson.toJson(o);
    }


    public static String getCode() {
        return String.format("%04d", random.nextInt(9999));
    }


    public static BigDecimal convertBigDecimal(String d) {
        BigDecimal d1 = new BigDecimal(d);
        return d1;
    }

    public static String match(String patternRegex, String source) {
        Pattern pattern = Pattern.compile(patternRegex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static float matchFloat(String source) {
        String result = match(pattern_regex_float, source);
        if (result != null) {
            return Float.parseFloat(result);
        }
        return 0;
    }

    public static Date parse(SimpleDateFormat format, String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 格式化四位小数
     *
     * @param balance
     * @return
     */
    public static String formatFour(double balance) {
        //4位小数
        DecimalFormat fourdf = new DecimalFormat("0.0000");
        return fourdf.format(balance);
    }


    public static String formatEosBalance(double balance) {
        DecimalFormat fnum = new DecimalFormat("##0.0000");
        return fnum.format(balance);
    }

    public static String getEosBalance(double balance) {
        return formatEosBalance(balance) + " EOS";
    }

    public static String UTCToCST(String UTCStr, String format) throws ParseException {
        SimpleDateFormat sLoggingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        date = sdf.parse(UTCStr);
        LogUtils.d("UTC时间: " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return sLoggingFormat.format(calendar.getTime());// 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳
//        System.out.println("北京时间: " + calendar.getTime());
    }

    /**
     * 获取两个日期之间的间隔天数是否大于7天
     *
     * @return
     */
    public static boolean isSevenDayDate(String startDate) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date_start = null;
        Date date_end = new Date();
        String endDate = simpleDateFormat.format(date_end);

        try {
            date_start = simpleDateFormat.parse(startDate);
            date_end = simpleDateFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date_start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date_end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24)) > 7;
    }

    /**
     * 将毫秒数转换成时间
     *
     * @param longTime
     * @return
     */
    public static String formatLongTime(Long longTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime = simpleDateFormat.format(longTime);
        return formatTime;
    }

    /**
     * 指定日期加任意天数后返回相加后的日期
     *
     * @param num
     * @param newDate
     * @return
     */
    public static String plusDay(int num, String newDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = null;
        try {
            date = sdf.parse(newDate);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_YEAR, +num);
            Date time = rightNow.getTime();
            String format = sdf.format(time);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getDate() {
        String currentDateTimeString =
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINESE).format(new Date());
        return currentDateTimeString;
    }

    /**
     * 获取现在时间
     *
     * @return返回短时间格式 yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 判断2个时间大小
     * @param startTime
     * @param endTime
     * @return
     */
    public static int timeCompare(String startTime, String endTime) {

        int i = 0;
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //结束时间小于开始时间
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                //开始时间与结束时间相同
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //结束时间大于开始时间
                i = 3;
            }

        } catch (Exception e) {
        }
        return i;
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getYear(){

        Calendar cd = Calendar.getInstance();

        return  cd.get(Calendar.YEAR);

    }
}
