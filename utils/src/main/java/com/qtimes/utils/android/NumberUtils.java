package com.qtimes.utils.android;

/**
 * Author: JackHou
 * Date: 2019/12/16.
 */
public class NumberUtils {

    /**
     * 格式化Number
     *
     * @param input
     * @return
     */
    public static String DecimalFormat(double input) {
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("0.00");
        return decimalFormat.format(input);
    }

    /**
     * 格式化Number
     *
     * @param input
     * @param format
     * @return
     */
    public static String DecimalFormat(double input, String format) {
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat(format);
        return decimalFormat.format(input);
    }
}
