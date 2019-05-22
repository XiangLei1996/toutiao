package com.nowcoder.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Author: XiangL
 * Date: 2019/5/21 15:02
 * Version 1.0
 */
public class ToutiaoUtil {
    private final static Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);

    /**
     * 工具类里的方法写成静态方法
     */
    public static String getJsonString(int code){
        JSONObject json = new JSONObject();
        json.put("code", code);

        return json.toJSONString();
    }

    public static String getJsonString(int code, String msg){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);

        return json.toJSONString();
    }

    /**
     *  将返回数据转换为Json字符串
     * @param code 表示本次服务是否正确，如0表示正确，非0表示错误
     * @param map 存储了返回视图中需要的信息
     * @return
     */
    public static String getJsonString(int code, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code", code);

        //Map的迭代方式(通过entrySet)
        for(Map.Entry<String, Object> entry : map.entrySet()){
            json.put(entry.getKey(), entry.getValue());
        }

        return json.toJSONString();
    }

    /**
     * MD5加密算法，用于对密码进行加密，代码为网上拷贝所得
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}
