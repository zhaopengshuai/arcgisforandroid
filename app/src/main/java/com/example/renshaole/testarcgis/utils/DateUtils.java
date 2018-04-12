package com.example.renshaole.testarcgis.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.jucheng.jclibs.tools.SubStringUtils;

/**
 * Created by w on 2017-11-23.
 */

public class DateUtils {


    public static String getCurrentTimeZone()
    {
        TimeZone tz = TimeZone.getDefault();
        return createGmtOffsetString(true,true,tz.getRawOffset());
    }

    public static String createGmtOffsetString(boolean includeGmt,
                                               boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }

    /**
     * 获取当前时间
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前时间
     */
    public static String getDates() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static String timeLogic(String dateStr) {
        String timeDate = "";
        String times = "";
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time > 0 && time <= 600) {//十分钟内
            timeDate = "十分钟内";
            times = "10";
            return times;
        } else if (time > 60 && time <= 3600) {//一个小时内
            timeDate = sb.append(time / 60 + "分钟前").toString();

            times = "60";
            return times;
        } else if (time >= 3600 && time <= 3600 * 36) {//36小时内
            times = sb.append(time / 3600 + "小时前").toString();
            times = "36";
            return times;
        } else if (time >= 3600 * 72) {
            timeDate = "相差72小时或以上";
            times = "72";
            return times;
        }
        return timeDate;
    }

    public static String getminDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        String times = "";
        try {
            date = new Date();
            times = sdf.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return times;

    }

    /*
    *年
     */
    public static String getYear(){
        Calendar calendar= Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return year;
    }

    /**
     * 月
     * @return
     */
    public static String getMonth(){
        Calendar calendar= Calendar.getInstance();
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        return month;
    }

    /**
     * 日
     * @return
     */
    public static String getDay(){
        Calendar calendar= Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return day;
    }

    /**
     * 计算时间差
     * @param date1
     * @param date2
     * @return
     */
    public static String getTimeLong(Date date1, Date date2){
        String number = "";
        int day = 0;
        int hour = 0;
        int minute = 0;
        long times= date1.getTime() - date2.getTime();//这样得到的差值是微秒级别
        long days = times/ (1000 * 60* 60 * 24); //换算成天数
        long hours =(times-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60); //换算成小时
        long minutes =(times-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60); //换算成分钟
        day = (int) days;
        hour = (int) hours;
        minute = (int) minutes;
        if(day < 0 || hour < 0 || minute < 0){//负数
            number = "您的设备时间慢" + Math.abs(day) + "天" + Math.abs(hour) + "小时";
        }else {//正数
            if (day >= 1) {
                number = "您的设备时间快" + day + "天" + hour + "小时";
            }
            if (day < 1 && hour >= 1) {
                number = "您的设备时间快" + hour + "小时";
            }
//            if (hour < 1 && minute >= 10) {
//                number = "您的设备时间快" + minute + "分钟,";
//            }

            if(day <1 && hour <1 && minute < 10){
                number = "";
            }
        }

        return number;

    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 得到一个格式化的时间
     *
     * @param time 时间 毫秒
     *
     * @return 时：分：秒
     */
    public static String getFormatTime(long time) {
        time = time/1000;
        long second = time % 60;
        long minute = (time % 3600) / 60;
        long hour = time / 3600;

        // 秒显示两位
        String strSecond = ("00" + second).substring(("00" + second).length() - 2);
        // 分显示两位
        String strMinute = ("00" + minute).substring(("00" + minute).length() - 2);
        // 时显示两位
        String strHour = ("00" + hour).substring(("00" + hour).length() - 2);

        return strHour + ":" + strMinute + ":" + strSecond;
    }
    /**
     * 将字符串转化为以秒表示的时间
     * @param time
     * @return  sum
     * */
    public static long getTimeLong(String time){
        int h= Integer.parseInt(SubStringUtils.substring(time,0,2),16);
        int m= Integer.parseInt(SubStringUtils.substring(time,2,4),16);
        int s= Integer.parseInt(SubStringUtils.substring(time,4,6),16);
        long sum=h*3600+m*60+s;
        return sum;
    }
    /**
     * 求最小公倍数，貌似不对，求得不是最小公倍数
     *
     * */
    public static int getMinDividend(int Num1,int Num2){
        if(Num1==0 || Num2==0){
            Log.v("求最小公倍数出错","有数为0");
            return 0;
        }else{
            if (Num1==Num2){
                return Num1;
            }else if(Num1*Num2%2!=0) //这是俩数相乘为奇数的情况
            {
                return Num1*Num2;
            } else{//俩数字都不为零切互不相等
                //俩数互为倍数关系
                int cs ,bcs;
                bcs = Num1>Num2?Num1:Num2;
                cs = Num1>Num2?Num2:Num1;
                if(bcs%cs==0){
                    return bcs;
                }

                //这是俩数相乘为偶数的情况
                int amass = Num1*Num2;
                while (amass %2 ==0&&amass/2%Num1==0&&amass/2%Num2==0){
                    amass = amass/2;
                }
                return amass;
            }
        }
    }
}
