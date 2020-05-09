package com.qtimes.utils.android;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * Created by gufei on 2016/8/22 0022.
 * 货币转换
 */

public class MoneyUtils {
    private static int defType = MoneyType.LONGBI;//默认龙币
    private static int scaleLongbi = 100;//1:100 元宝龙币比例

    public static String formatYuanBao(Object yuanbao) {
        return formatYuanBao(defType, yuanbao, true);
    }

    public static String formatYuanBao(Object yuanbao, boolean isdecimal) {
        return formatYuanBao(defType, yuanbao, isdecimal);
    }

    /**
     * @param type
     * @param yuanbao
     * @param isdecimal 是否保留小数
     * @return
     */
    public static String formatYuanBao(int type, Object yuanbao, boolean isdecimal) {
        String money = "0";
        if (yuanbao == null || TextUtils.isEmpty(yuanbao.toString())) return money;
        switch (type) {
            case MoneyType.YUANBAO:
                break;
            case MoneyType.LONGBI:
                Double tempYuanBao;
                if (yuanbao instanceof Double) {
                    tempYuanBao = (Double) yuanbao;
                } else {
                    tempYuanBao = Double.valueOf(yuanbao.toString());
                }
                tempYuanBao = tempYuanBao * scaleLongbi;
                BigDecimal b = new BigDecimal(tempYuanBao);
                tempYuanBao = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                money = isdecimal ? String.valueOf(tempYuanBao) : String.valueOf(tempYuanBao.intValue());
                break;
        }
        return money;
    }


    /**
     * 把送完礼物后返回的balance转化成龙币，取整，去掉小数位
     *
     * @param balance
     * @return
     */
    public static String formatBalance(Double balance) {
        String money = "0";
        if (balance == null || TextUtils.isEmpty(balance.toString())) return money;
        java.text.DecimalFormat df = new java.text.DecimalFormat("###########");
        Double tempYuanBao;
        tempYuanBao = balance * scaleLongbi;
        money = df.format(tempYuanBao);
        return money;
    }

    /**
     * 龙币转化为金额，只保留有效位
     * <p>
     * 例如：100.0-->1; 112-->1.12
     */
    public static String longBiToYuanBao(String longbi) {
        try {
            double double_longbi = !TextUtils.isEmpty(longbi) ? Double.valueOf(longbi) : 0.0;
            double yuanbao = double_longbi / 100.0;
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(yuanbao);
//            return String.valueOf(yuanbao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    public static int yuanBaoTrans(String userBalance){
//        try {
//            int price=Integer.parseInt(userBalance);
//            return price*scaleLongbi;
//        }catch (Exception e){
//        }
//        return 0;
//    }

    public static double yuanbaoParseInt(String userBalance){
        try {
            double price= Double.parseDouble(userBalance);
            return price;
        }catch (Exception e){
            return 0;
        }
    }


    public static String numberFormat(String number) {
        try {
            double double_number = !TextUtils.isEmpty(number) ? Double.valueOf(number) : 0.0;
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(double_number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 龙币转化为金额，只保留有效位
     * <p>
     * 例如：100.0-->1; 112-->1.12
     */
    @NonNull
    public static String money2longBi(String money) {
        try {
            int double_money = !TextUtils.isEmpty(money) ? Integer.valueOf(money) : 0;
            int longbi = double_money * 100;
            return String.valueOf(longbi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public interface MoneyType {
        int YUANBAO = 0;//元宝
        int LONGBI = 1;//龙币
    }

    /**
     * 将元单位数字转成int类型的元
     *
     * @param numStr
     */
    public static int numStrToInt(String numStr) {
        int num = 0;
        try {
            if (!TextUtils.isEmpty(numStr)) {
                num = Integer.valueOf(numStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }
}
