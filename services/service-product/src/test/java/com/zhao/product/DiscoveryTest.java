package com.zhao.product;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest
public class DiscoveryTest {

    // spring家的API
    @Autowired
    private DiscoveryClient discoveryClient;

    // 只能引用nacos的API
    @Autowired
    private NacosDiscoveryClient nacosDiscoveryClient;

    @Test
    void discoveryTest(){
        for(String service : discoveryClient.getServices()){
            System.out.println("service" + service);
            // 获取ip + port
            List<ServiceInstance> instances = discoveryClient.getInstances(service);

            for(ServiceInstance instance : instances){
                System.out.println("ip: " + instance.getHost() + ", port: " + instance.getPort());
            }
        }
    }


    @Test
    void nacosDiscoveryTest(){
        for(String service : nacosDiscoveryClient.getServices()){
            System.out.println("service" + service);
            // 获取ip + port
            List<ServiceInstance> instances = nacosDiscoveryClient.getInstances(service);

            for(ServiceInstance instance : instances){
                System.out.println("ip: " + instance.getHost() + ", port: " + instance.getPort());
            }
        }
    }
}
