package com.daily_code.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: lizhao
 * @date: 2024/1/31 14:51
 */
@Configuration
public class LoginConfiguration {

    @Resource
    private LoginConfigurationParam loginConfigurationParam;

    @Bean(name = "jwtHeadersMap")
    public Map<String, String> jwtHeadersMap() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("algorithm", loginConfigurationParam.getAlgorithm());
        map.put("type", loginConfigurationParam.getType());
        return map;
    }
}
