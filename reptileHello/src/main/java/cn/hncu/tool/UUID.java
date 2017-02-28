package cn.hncu.tool;

/**
 * Created with IntelliJ IDEA.
 * User: 陈浩翔.
 * Date: 2017/2/27.
 * Time: 下午 7:36.
 * Explain:
 */
public class UUID {
    public static String getId(){
        return java.util.UUID.randomUUID().toString().replaceAll("-","");
    }
}
