package cn.ac.iie.bean;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class MyConfiguration {

    @Autowired
    private EsConf esConf;

    @Bean(name = "EsClient")
    public Client createClient() {
        Settings settings = Settings.builder()
                .put("cluster.name", esConf.getClusterName()).build();
        Client client = new PreBuiltTransportClient(settings);
        for (String ip : esConf.getIps()) {
            try {
                // es 6.2.3版本
                ((TransportClient) client)
                        .addTransportAddress(new TransportAddress(InetAddress.getByName(ip), esConf.getPort()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return client;
    }

}
