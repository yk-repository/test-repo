package com.zhao.order;

import com.alibaba.nacos.shaded.io.grpc.LoadBalancer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

@SpringBootTest
public class LoadBalancerTest {

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    private DiscoveryClient discoveryClient;

    @Test
    void test(){
        ServiceInstance choose = loadBalancerClient.choose("service-product");
        System.out.println("choose: "+choose.getHost()+" : "+choose.getPort());
        choose = loadBalancerClient.choose("service-product");
        System.out.println("choose: "+choose.getHost()+" : "+choose.getPort());
        choose = loadBalancerClient.choose("service-product");
        System.out.println("choose: "+choose.getHost()+" : "+choose.getPort());
        choose = loadBalancerClient.choose("service-product");
        System.out.println("choose: "+choose.getHost()+" : "+choose.getPort());
        choose = loadBalancerClient.choose("service-product");
        System.out.println("choose: "+choose.getHost()+" : "+choose.getPort());
        choose = loadBalancerClient.choose("service-product");
        System.out.println("choose: "+choose.getHost()+" : "+choose.getPort());
    }

}
