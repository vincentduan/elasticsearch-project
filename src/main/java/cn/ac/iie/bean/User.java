package cn.ac.iie.bean;

public class User {
    private String userName;
    private String passwordEn;

    public User(String userName, String passwordEn) {
        this.userName = userName;
        this.passwordEn = passwordEn;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passwordEn='" + passwordEn + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordEn() {
        return passwordEn;
    }

    public void setPasswordEn(String passwordEn) {
        this.passwordEn = passwordEn;
    }
}
