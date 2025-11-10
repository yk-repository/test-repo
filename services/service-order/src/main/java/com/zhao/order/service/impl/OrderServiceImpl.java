package com.zhao.order.service.impl;

import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zhao.order.bean.Order;
import com.zhao.order.feign.ProductFeignClient;
import com.zhao.order.service.OrderService;
import com.zhao.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    // 负载均衡客户端
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    // 声明式的远程调用
    @Autowired
    public ProductFeignClient productFeignClient;


    @SentinelResource(value = "createOrder", blockHandler = "createOrderFallBack")
    @Override
    public Order createOrder(Long productId, Long userId) {
        // 从商品服务远程调用查询商品信息
//        Product product = getProductFromRemote(productId);
        Product product = productFeignClient.getProductById(productId);
//        Product productWithLoadBalancer = getProductFromRemoteWithLoadBalancer(productId);
//        Product product = getProductFromRemoteWithLoadBalancerAndRestTemplate(productId);
        log.info("商品信息: {}", product);
//        log.info("商品信息（负载均衡）: {}", productWithLoadBalancer);
//        log.info("商品信息（负载均衡 + RestTemplate 注解式）: {}", product);

        Order order = new Order();
        // 订单号
        order.setOrderId(1L);
        order.setUserId(userId);
        // 总金额（需要远程调用商品进行计算）
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setNickName("张三");
        order.setAddress("北京市海淀区");
        // 商品列表 （需要远程调用商品进行查询）
//        order.setProductList(Arrays.asList(product, productWithLoadBalancer, productWithLoadBalancerAndRestTemplate));
//        order.setProductList(Arrays.asList(product,productWithLoadBalancer));
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
        // RestTemplate restTemplate = new RestTemplate(); // 线程安全
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }


    // 2.进阶：从商品服务远程调用查询商品信息（使用负载均衡客户端）
    private Product getProductFromRemoteWithLoadBalancer(Long productId) {
        // 1. 远程调用（获取商品服务所在的地址：从注册中心获取【IP+ Port】）
        // 使用负载均衡客户端
        ServiceInstance choose = loadBalancerClient.choose("service-product");
        // 使用负载均衡客户端选择的实例
        String url =
                "http://" + choose.getHost() + ":" + choose.getPort() + "/product/get/" + productId;
        log.info("远程请求url: {}", url);
        // 2. 远程调用（根据URL进行远程调用）,给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

    // 3. 进阶：从商品服务远程调用查询商品信息（使用负载均衡客户端 + RestTemplate 注解式）
    // 当前方式使用后，以上两种方式会失效（因为负载均衡客户端 + RestTemplate 注解式会自动根据服务名称进行负载均衡）
    private Product getProductFromRemoteWithLoadBalancerAndRestTemplate(Long productId) {
        // 1.设置动态的IP地址
        // 负载均衡 + RestTemplate 注解式, 自动根据服务名称进行负载均衡
        String url = "http://service-product/product/get/" + productId;
        log.info("远程请求url: {}", url);
        // 2. 远程调用（根据URL进行远程调用）,给远程发送请求，使用负载均衡客户端 + RestTemplate 注解式
        // service-product会被动态替换为实际的服务实例（根据服务名称进行负载均衡）
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

    // 处理 Sentinel 阻塞异常, 当 Sentinel 阻塞时, 会调用这个方法, 并返回一个默认值, 而不是抛出异常, 从而避免了服务雪崩
    // 这是服务的异常的兜底回调
    private Order createOrderFallBack(Long productId, Long userId, BlockException e) {
        Order order = new Order();
        // SphU异常处理
        try {
            SphU.asyncEntry("createOrder");
        } catch (BlockException ex) {
            throw new RuntimeException(ex);
        }

        order.setOrderId(0L);
        order.setUserId(userId);
        order.setNickName("未知用户");
        order.setAddress("异常现象: " + e.getClass());
        order.setTotalAmount(new BigDecimal("0.00"));
        order.setProductList(Collections.emptyList());
        return order;
    }
}
