package com.daily_code.login.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: lizhao
 * @date: 2024/1/31 14:46
 */
@Data
@Component
@PropertySource("classpath:application_param.properties")
@ConfigurationProperties(prefix = "param")
public class LoginConfigurationParam {

    private String secretKey;

    private String dot;

    private String algorithm;

    private String type;
}
