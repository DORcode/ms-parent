package com.coin.msdict.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @program: jplatform
 * @description: 中文转拼音工具类
 * @author: kh
 * @create: 2018-12-19 14:58:49
 **/
public class PinyinUtil {
    
    /** 
    * @Description: 获取中文拼音数组 
    * @Param: [ch] 
    * @return: java.lang.String[] 
    * @Author: kh
    * @Date: 2018/12/19 15:23
    */
    public static String[] getPinYin(char ch) throws Exception {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        if(ch>=32 && ch<=125){	//ASCII >=33 ASCII<=125的直接返回 ,ASCII码表：http://www.asciitable.com/
            return new String[]{String.valueOf(ch)};
        }
        return distinct(PinyinHelper.toHanyuPinyinStringArray(ch, outputFormat));
    }
    
    /** 
    * @Description: 获取中文词拼音
    * @Param: [chinese] 
    * @return: java.util.List<java.lang.String> 
    * @Author: kh
    * @Date: 2018/12/19 15:24
    */
    public static List<String> getPinyin(String chinese) throws Exception {
        if(StringUtils.isEmpty(chinese)) {
            return null;
        }
        chinese = chinese.replaceAll("[\\.，\\,！·\\!？\\?；\\;\\(\\)\\（\\）\\［\\］\\【\\】\\{\\}\\[\\]\\:：\\| ]+", " ").trim();
        List<String> pinyins = new ArrayList<>();
        char[] chs = chinese.toCharArray();
        for(char ch : chs) {
            String[] py_arr = getPinYin(ch);
            if(null == py_arr || py_arr.length == 0) {
                throw new IllegalStateException("未获取到中文的拼音");
            }
            pinyins.add(py_arr[0]);
        }
        return pinyins;
    }

    public static String getWholePinyin(String chinese) {
        StringBuffer fpy = new StringBuffer();
        try {
            List<String> pinyins = getPinyin(chinese);
            for(String py : pinyins) {
                fpy.append(py);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fpy.toString();
    }


    public static String getFirstPinyin(String chinese) {
        StringBuffer fpy = new StringBuffer();
        try {
            List<String> pinyins = getPinyin(chinese);
            for(String py : pinyins) {
                fpy.append(py.substring(0, 1).toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fpy.toString();
    }

    public static String[] distinct(String[] arr) {
        if(arr==null || arr.length==0) {
            return arr;
        }
        HashSet<String> set = new HashSet<>();
        for (String str : arr) {
            set.add(str);
        }
        return set.toArray(new String[0]);
    }
}
