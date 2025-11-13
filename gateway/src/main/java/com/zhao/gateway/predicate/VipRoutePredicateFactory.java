package com.zhao.gateway.predicate;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component // 注册为组件，交给Spring容器管理
public class VipRoutePredicateFactory extends AbstractRoutePredicateFactory<VipRoutePredicateFactory.Config> {

    public VipRoutePredicateFactory() {
        super(Config.class);
    }


    /**
     * 自定义断言，判断请求参数中是否包含指定的vip参数，vip参数的值必须包含指定的值
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                // 获取请求对象中的路由信息
                ServerHttpRequest request = serverWebExchange.getRequest();
                // 从请求路由信息中获取vip参数的值，param的值是user
                String first = request.getQueryParams().getFirst(config.param);
                // 校验vip参数的值是否包含指定的字符串，value的值是admin
                if (StringUtils.hasText(first) && first.equals(config.value)) {
                    return true;
                }
                // 校验失败，返回false
                return false;
            }
        };
    }

    /**
     * 定义断言的参数顺序，必须和Config类中的字段顺序一致
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("param", "value");
    }

    /**
     * 自定义断言，判断请求参数中是否包含指定的vip参数，vip参数的值必须包含指定的值
     * 可以配置的参数：
     * param：vip参数的名称
     * value：vip参数的值必须包含的字符串
     */
    @Validated
    public static class  Config {

        @NotEmpty
        private String param;

        @NotEmpty
        private String value;

        public @NotEmpty String getParam() {
            return param;
        }

        public void setParam(@NotEmpty String param) {
            this.param = param;
        }

        public @NotEmpty String getValue() {
            return value;
        }

        public void setValue(@NotEmpty String value) {
            this.value = value;
        }
    }

}
