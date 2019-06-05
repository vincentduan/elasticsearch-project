package cn.ac.iie.web;

public class ResponseResult {

    private String code;
    private String message;
    private Object data;

    public ResponseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }


}
