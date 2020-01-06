package com.cool.mmc.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import static javax.xml.bind.JAXBIntrospector.getValue;

/**
 * Created by vincent on 2020-01-06
 */
public class SignUtils {

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 生成签名
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
        String result = sb.toString();
        result += "key=" + secret;
        return md5(result).toUpperCase();
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

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("isd", "2321");
        map.put("osd", "2321");
        map.put("asd", "2321");
        map.put("zsd", "2321");
        map.put("fsd", "2321");
        sign(map, "sds");
    }

}
