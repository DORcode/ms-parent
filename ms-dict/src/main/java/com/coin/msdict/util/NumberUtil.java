package com.coin.msdict.util;

import java.text.DecimalFormat;

/**
 * @ClassName NumberUtil
 * @Description: TODO
 * @Author kh
 * @Date 2020/1/3 16:26
 * @Version V1.0
 **/
public class NumberUtil {
    /**
     * @MethodName percent
     * @Description 计算百分比
     * @param count
     * @param total
     * @return java.lang.String
     * @throws
     * @author kh
     * @date 2020/1/3 16:28
     */
    public static String percent(int count, int total) {
        DecimalFormat df1 = new DecimalFormat("0.00%");
        if(total != 0) {
            String format = df1.format((float) count / total);
            if(format.substring(format.indexOf("."), format.length()).equals(".00%")) {
                return format.substring(0, format.indexOf(".")) + "%";
            } else {
                return format;
            }

        } else {
            return "-";
        }

    }

    public static boolean isNumeric(final String str) {
        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException ex) {
                try {
                    Float.parseFloat(str);
                    return true;
                } catch (NumberFormatException exx) {
                    return false;
                }
            }
        }
    }

    public static void main(String[] args) {
        String a = "10%";
        System.out.println(isNumeric(a));

        System.out.println(percent(1, 2));
    }

}
