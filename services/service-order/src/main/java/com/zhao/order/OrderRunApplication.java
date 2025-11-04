package com.zhao.order;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableDiscoveryClient
@SpringBootApplication
public class OrderRunApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderRunApplication.class, args);
    }

    // 1.项目启动就监听配置中心的配置文件
    // 2.配置中心的配置文件发生变化，配置的内容自动刷新到项目中
    // 3.发送邮件
    // 项目启动监听配置中心的配置文件
    @Bean
    public ApplicationRunner applicationRunner(NacosConfigManager nacosConfigManager) {
//        return new  ApplicationRunner() {
//            @Override
//            public void run(ApplicationArguments args) throws Exception {
//                System.out.println("项目启动了...");
//            }
//        };
        // 简写
        return args -> {
            ConfigService configService = nacosConfigManager.getConfigService();
            // 监听配置中心的配置文件
            configService.addListener("service-order.properties", "DEFAULT_GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newFixedThreadPool(4);
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("变化的配置信息： " + configInfo);
                    // 发送邮件
                    System.out.println("发送邮件...");
                    // TODO 发送邮件的代码...
                }
            });

        };
    }
}
