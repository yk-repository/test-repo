package com.zhao.order.service.impl;

import com.zhao.order.bean.Order;
import com.zhao.order.service.OrderService;
import com.zhao.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Order createOrder(Long productId, Long userId) {
        // 从商品服务远程调用查询商品信息
        Product product = getProductFromRemote(productId);
        log.info("商品信息: {}", product);

        Order order = new Order();
        // 订单号
        order.setOrderId(1L);
        order.setUserId(userId);
        // 总金额（需要远程调用商品进行计算）
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setNickName("张三");
        order.setAddress("北京市海淀区");
        // 商品列表 （需要远程调用商品进行查询）
        order.setProductList(Arrays.asList(product));
        return order;
    }

    // 从商品服务远程调用查询商品信息
    private Product getProductFromRemote(Long productId) {
        // 1. 远程调用（获取商品服务所在的地址：从注册中心获取【IP+ Port】）
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance serviceInstance = instances.get(0);
        // 拼接URL（http://IP:Port/product/1）
        String url =
                "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/product/get/" + productId;
        log.info("远程请求url: {}", url);
        // 2. 远程调用（根据URL进行远程调用）,给远程发送请求
//         RestTemplate restTemplate = new RestTemplate(); // 线程安全
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }
}
