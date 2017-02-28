package cn.hncu.tool;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: 陈浩翔.
 * Date: 2017/2/27.
 * Time: 下午 6:45.
 * Explain:编码转换
 */
public class Encoding {

    public static String encoding(String str){
        if(str==null){
            return null;
        }
        try {
            str =new String(str.getBytes("GBK"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
