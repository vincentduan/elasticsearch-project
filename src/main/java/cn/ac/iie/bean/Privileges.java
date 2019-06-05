package cn.ac.iie.bean;

import java.util.List;

public class Privileges {

    private String userName;
    private String authorityType;
    private List<String> authorityApps;

    public Privileges(String userName, String authorityType, List<String> authorityApps) {
        this.userName = userName;
        this.authorityType = authorityType;
        this.authorityApps = authorityApps;
    }

    @Override
    public String toString() {
        return "Privileges{" +
                "userName='" + userName + '\'' +
                ", authorityType='" + authorityType + '\'' +
                ", authorityApps=" + authorityApps +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    public List<String> getAuthorityApps() {
        return authorityApps;
    }

    public void setAuthorityApps(List<String> authorityApps) {
        this.authorityApps = authorityApps;
    }
}
