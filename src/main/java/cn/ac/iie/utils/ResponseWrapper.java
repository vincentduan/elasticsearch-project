package cn.ac.iie.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by vincent on 2019-6-21 10:42
 */
public class ResponseWrapper {
    public static HttpServletResponse responseToJson(HttpServletResponse response, Map<String, ? extends Object> map) {
        response.setContentType("application/json; charset=utf-8");
        try {
            JSONObject jsonObject = new JSONObject((Map<String, Object>)map);
            PrintWriter writer = response.getWriter();
            writer.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
