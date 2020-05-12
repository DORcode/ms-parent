package com.coin.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @ClassName BeanUtil
 * @Description: TODO
 * @Author kh
 * @Date 2020/3/5 14:27
 * @Version V1.0
 **/
public class BeanUtil {
    
    /**
     * @MethodName copyProperties
     * @Description TODO
     * @param dest
     * @param orig 
     * @return void
     * @throws 
     * @author kh
     * @date 2020/3/5 14:32
     */
    public static void copyProperties(final Object dest, final Object orig) {
        BeanUtils.copyProperties(orig, dest);
    }
    
    /**
     * @MethodName describe
     * @Description TODO
     * @param bean 
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @throws 
     * @author kh
     * @date 2020/3/5 14:33
     */
    public static Map<String, String> describe(final Object bean) {

        try {
            return BeanUtilsBean.getInstance().describe(bean);
        } catch (IllegalAccessException e) {
            throw new BaseException();
        } catch (InvocationTargetException e) {
            throw new BaseException();
        } catch (NoSuchMethodException e) {
            throw new BaseException();
        }
    }
}
