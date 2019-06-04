package cn.ac.iie.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("es")
public class EsConf {

//    @Value("${es.ips}")
    private List<String> ips;
//    @Value("${es.clusterName}")
    private String clusterName;
//    @Value("${es.port}")
    private int port;

    @Override
    public String toString() {
        return "EsConf{" +
                "ips=" + ips +
                ", clusterName='" + clusterName + '\'' +
                ", port=" + port +
                '}';
    }

    public List<String> getIps() {
        return ips;
    }

    public String getClusterName() {
        return clusterName;
    }

    public int getPort() {
        return port;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
