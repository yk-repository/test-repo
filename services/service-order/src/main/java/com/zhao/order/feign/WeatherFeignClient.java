package com.zhao.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-weather", url="http://aliv8.data.moji.com")
public interface WeatherFeignClient {

    @GetMapping("/whapi/json/alicityweather/condition")
    public String getWeather(@RequestParam("Authorization") String auth,
                             @RequestParam("token") String token,
                             @RequestParam("cityId") String cityId);
}
