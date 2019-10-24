package com.liyiwei.baselibrary.util.apputil;

import com.sie.mp.space.utils.SettingSp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateUtil {
    /**
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_week = seconds_of_1day * 7;
    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;

    //是否7天内不弹窗
    public static boolean isDialogShow() {
        //SettingSp.getInstance().putString(IDUtils.ACCOUNT_INFO_TOKEN, token);
        boolean status = SettingSp.getInstance().getBoolean("ACCOUNT_UPDATESEVENDAY_CHECKBOXSTATUS", false);
        if (status){
            if (isSevenDay()){
                return true;
            }
        }else {
            return false;
        }
        return false;
    }

    //是否七天内
    public static boolean isSevenDay() {
        //SettingSp.getInstance().putString(IDUtils.ACCOUNT_INFO_TOKEN, token);
        String saveTime = SettingSp.getInstance().getString("ACCOUNT_UPDATE_REMINTIME", null);
        if (saveTime != null){
            /**除以1000是为了转换成秒*/
            long between = (getCurrentlong() - getTimelong(saveTime)) / 1000;

            int elapsedTime = (int) (between);
            if (elapsedTime < seconds_of_week) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    //更新提醒时间和提醒状态
    public static void updateReminTime(boolean status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        SettingSp.getInstance().putBoolean("ACCOUNT_UPDATESEVENDAY_CHECKBOXSTATUS", status);
        if (status){
            SettingSp.getInstance().putString("ACCOUNT_UPDATE_REMINTIME", dataStrNew);
        }else {
            SettingSp.getInstance().remove("ACCOUNT_UPDATE_REMINTIME");
        }

    }

    public static long getTimelong(String mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = null;
        try {
            /**将时间转化成Date*/
            startTime = sdf.parse(mTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startTime.getTime();
    }

    public static long getCurrentlong() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        try {
            /**将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curDate.getTime();
    }

    /**
     * 格式化时间
     *
     * @param mTime
     * @return
     */
    public static String getTimeRange(String mTime) {

        /**除以1000是为了转换成秒*/
        long between = (getCurrentlong() - getTimelong(mTime)) / 1000;

        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {

            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {

            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {

            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {

            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {

            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {

            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {

            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {

            return "半年前";
        }
        if (elapsedTime >= seconds_of_1year) {

            return elapsedTime / seconds_of_1year + "年前";
        }
        return "";
    }
}
