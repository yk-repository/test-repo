package com.zhao.order.service.impl;

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


    @Override
    public Order createOrder(Long productId, Long userId) {
        // 从商品服务远程调用查询商品信息
//        Product product = getProductFromRemote(productId);
        Product product = productFeignClient.getProductById(productId);
        Product productWithLoadBalancer = getProductFromRemoteWithLoadBalancer(productId);
//        Product product = getProductFromRemoteWithLoadBalancerAndRestTemplate(productId);
        log.info("商品信息: {}", product);
        log.info("商品信息（负载均衡）: {}", productWithLoadBalancer);
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
        order.setProductList(Arrays.asList(product,productWithLoadBalancer));
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
}
