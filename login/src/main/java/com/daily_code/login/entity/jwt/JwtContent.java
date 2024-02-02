package com.daily_code.login.entity.jwt;

import lombok.Data;

import java.util.Map;

/**
 * @author: lizhao
 * @date: 2024/1/31 15:48
 */
@Data
public class JwtContent {

    private JwtPart jwtPart;

    private String row;

    private Map<String, Object> pairs;
}
