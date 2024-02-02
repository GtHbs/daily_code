package com.daily_code.login.module;

import com.daily_code.login.entity.jwt.JwtContent;
import com.daily_code.login.entity.jwt.JwtPart;
import com.daily_code.login.service.JsonWebTokenService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: lizhao
 * @date: 2024/1/31 15:04
 */
@Component
public class StartFunction implements ApplicationRunner {

    @Resource
    private JsonWebTokenService jsonWebTokenService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("iss", "throws");
        map.put("exp", "1706547600000");
        String jwt = jsonWebTokenService.generate(map);
        Map<JwtPart, JwtContent> contentMap = jsonWebTokenService.parse(jwt);
        boolean verify = jsonWebTokenService.verify(contentMap);
        System.out.println(verify);
    }
}
