package com.qtimes.utils.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xutao0 on 2016/10/12.
 */

public class DigitUtils {

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
