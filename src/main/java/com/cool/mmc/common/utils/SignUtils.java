package com.cool.mmc.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import static javax.xml.bind.JAXBIntrospector.getValue;

/**
 * Created by vincent on 2020-01-06
 */
public class SignUtils {

    /**
     * 生成签名
     * （规则如下：
     * 第一步：
     *  设所有发送或者接收到的数据为集合M，
     *  将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），
     *  使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA
     *
     * 第二步：
     *  在stringA最后拼接上key得到stringSignTemp字符串，
     *  并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，
     *  得到sign值signValue
     *  ）
     * @param map 参数集合
     * @param secret 密钥
     * @return the sign 签名
     */
    public static String sign(Map<String, Object> map, String secret) {
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            if (!key.equals("sign") && map.get(key) != null) {
                list.add(key + "=" + getValue(key) + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String stringA = sb.toString();
        stringA += "key=" + secret;
        return md5(stringA).toUpperCase();
    }

    private static String md5(String string){
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] bytes=md5.digest(string.getBytes(StandardCharsets.UTF_8));
            char[] chars = new char[bytes.length * 2];
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i];
                chars[i * 2] = hexDigits[(b & 0xF0) >> 4];
                chars[i * 2 + 1] = hexDigits[b & 0x0F];
            }
            return new String(chars);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5加密失败,str=".concat(string));
        }
    }

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

}
