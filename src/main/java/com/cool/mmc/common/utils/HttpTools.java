package com.cool.mmc.common.utils;

import com.core.common.Cools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 2019-04-19
 */
public class HttpTools {

    private static Map<HttpServletRequest, String> ipCache = new ConcurrentHashMap<>();

    private static Logger log = LoggerFactory.getLogger(HttpTools.class);

    public static Map<String, String> convertMap(HttpServletRequest request){
        Map<String,String> params = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            String[] values = parameterMap.get(key);
            String valueStr = "";
            // String[] ====>> String
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1)
                        ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(key, valueStr);
        }
        return params;
    }

    public static String gainRealIp(HttpServletRequest request) {
        String ipAddress = "";
        try {
            if (request == null) {
                return ipAddress;
            }
            ipAddress = ipCache.get(request);
            if (ipAddress != null && !ipAddress.isEmpty()) {
                return ipAddress;
            }
            //排除本地测试
            if ("127.0.0.1".equals(request.getServerName()) || "localhost".equals(request.getServerName())) {
                ipAddress = "127.0.0.1";
                return ipAddress;
            }
            ipAddress = request.getRemoteAddr();
            if (Cools.isEmpty(ipAddress)) {
                ipAddress = request.getRemoteHost();
            } else {
                return ipAddress;
            }
            if (!Cools.isEmpty(ipAddress)) {
                return ipAddress;
            }
            // 获取真实ip,排除代理ip
            ipAddress = request.getHeader("Referer");
            // ipAddress = this.getRequest().getRemoteAddr();
            ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                // 更换niginx代理
                // ipAddress = request.getRemoteAddr();
                ipAddress = request.getHeader("X-Real-IP");
                if (ipAddress != null && (ipAddress.equals("" + "") || ipAddress.endsWith("0:0:0:0:0:0:0:1"))) {
                    // 根据网卡取本机配置的IP
                    // linux下也可以获取本地的ip地址
                    Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                    InetAddress ip;
                    while (allNetInterfaces.hasMoreElements()) {
                        NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                        Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            ip = addresses.nextElement();
                            if (ip != null && ip instanceof Inet4Address) {
                                // 获取真实的Ip地址
                                ipAddress = ip.getHostAddress();
                            }
                        }
                    }
                }

            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割"***.***.***.***".length()=15
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            log.warn("ip{},解析异常", ipAddress, e);
        } finally {
            if (!Cools.isEmpty(ipAddress)) {
                ipCache.put(request, ipAddress);
            }
        }
        return ipAddress;
    }

    public static boolean isboolIp(String ipAddress) {
        String zhengze = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(zhengze);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
}
