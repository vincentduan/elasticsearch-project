package cn.ac.iie.authorization.model;

public class TokenModel {
    //用户UserName
    private String userName;

    //随机生成的uuid
    private String token;

    public TokenModel(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
