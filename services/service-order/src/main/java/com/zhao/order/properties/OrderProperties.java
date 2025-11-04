package com.zhao.order.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// 动态配置中心的配置类
@Component // 组件扫描，将OrderProperties类注册到Spring容器中
// 配置批量绑定在nacos下，可以无需使用@RefreshScope注解实现自动刷新
@ConfigurationProperties(prefix = "order") // 前缀为order的配置属性会自动绑定到OrderProperties类的属性上
@Data
public class OrderProperties {
    private String timeout;
    private String autoConfirm;
}
