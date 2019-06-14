package cn.ac.iie.utils;

import cn.ac.iie.vo.ResponseResult;

public class ResponseVOUtil {

    public static ResponseResult success(Object data) {
        ResponseResult responseResult = new ResponseResult();
        //responseResult.setCode(HttpStatus.OK);
        responseResult.setMessage("success");
        responseResult.setData(data);
        return responseResult;
    }

    public static ResponseResult failed(Object data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(500);
        responseResult.setMessage("failed");
        responseResult.setData(data);
        return responseResult;
    }

    public static ResponseResult exception(String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(501);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult error(String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(501);
        responseResult.setMessage(message);
        return responseResult;
    }

}
