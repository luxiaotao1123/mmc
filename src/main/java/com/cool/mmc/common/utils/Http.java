package com.cool.mmc.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.core.common.R;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by vincent on 2019-08-01
 */
public class Http {

    public static void response(HttpServletResponse response, String baseRes){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            R r = R.parse(baseRes);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", "0");
            jsonObject.put("record", "");
            r.add(jsonObject);
            out.print(JSON.toJSONString(r));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
