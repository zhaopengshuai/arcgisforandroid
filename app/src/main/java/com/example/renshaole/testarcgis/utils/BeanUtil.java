package com.example.renshaole.testarcgis.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

/**
 * 判断字符串对象是否为null
 * Created by dell on 2016/12/22.
 */
public class BeanUtil {



    public BeanUtil() {
    }

    /**
     * 可以用于判断 Map,Collection,String,Array,Long是否为空
     * @param o java.lang.Object.
     * @return boolean.
     */
    @SuppressWarnings("unused")
    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String)  {
            if (((String) o).trim().length() == 0 || "null".equals(o)) {
                return true;
            }
        } else if (o instanceof Collection) {
            if (((Collection) o).isEmpty()) {
                return true;
            }
        } else if (o.getClass().isArray()) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).isEmpty()) {
                return true;
            }
        } else if (o instanceof Long) {
            if(((Long)o)==null) {
                return true;
            }
        } else if (o instanceof Short) {
            if(((Short)o)==null) {
                return true;
            }
        } else {
            return false;
        }
        return false;

    }


    public static boolean isNotEmpty(Object o)
    {
        return !isEmpty(o);
    }

    /**
     * @category 获取try-catch中的异常内容
     * @param e Exception
     * @return  异常内容
     */
    public static String getExceptionInfo(Throwable e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception ex) {

        }
        return ret;
    }
    public static boolean validClass(String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (ClassNotFoundException e) {}
        return false;
    }
}
