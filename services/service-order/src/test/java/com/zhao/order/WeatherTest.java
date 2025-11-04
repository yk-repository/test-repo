package com.zhao.order;

import com.zhao.order.feign.WeatherFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeatherTest {

    @Autowired
    private WeatherFeignClient weatherFeignClient;

    @Test
    public void test(){
        weatherFeignClient.getWeather("123", "456", "101210101");
        System.out.println("weatherFeignClient.getWeather() = " + weatherFeignClient.getWeather("123", "456", "101210101"));
    }
}
