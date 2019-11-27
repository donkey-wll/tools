package com.zbba.tool;

import com.jxzx.common.redis.service.RedisUtils;
import com.jxzx.common.service.impl.SyncSb2YwTableServiceImpl;
import com.jxzx.common.utils.DownStreamUtils;
import com.jxzx.common.web.LoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"com.zbba.tool"})
@MapperScan({"com.zbba.tool.dao","com.jxzx.common.*.dao","com.jxzx.common.dao"})
@EnableFeignClients(basePackages = {"com.jxzx.comms", "com.zbba.tool"})
@EnableDiscoveryClient
@EnableSwagger2
public class ToolApplication {

    public static void main(String[] args) {

        System.setProperty("spring.devtools.restart.enabled", "false");

        LoginInterceptor.addExcludeUrl("/swagger-ui.html");
        LoginInterceptor.addExcludeUrl("/swagger-resources/**");
        LoginInterceptor.addExcludeUrl("/swagger/**");
//		LoginInterceptor.addExcludeUrl("/**");
        SpringApplication.run(ToolApplication.class, args);
    }

    @Bean
    public SyncSb2YwTableServiceImpl syncSb2YwTableService(){
        return new SyncSb2YwTableServiceImpl();
    }

    @Bean
    public RedisUtils buildRedisUtils(RedisTemplate redisTemplate) {
        return new RedisUtils(redisTemplate);
    }

    @Bean
    public DownStreamUtils downStreamUtils() {
        return new DownStreamUtils();
    }
}
