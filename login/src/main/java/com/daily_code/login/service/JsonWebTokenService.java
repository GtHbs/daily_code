package com.daily_code.login.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.daily_code.login.config.LoginConfigurationParam;
import com.daily_code.login.entity.jwt.JwtContent;
import com.daily_code.login.entity.jwt.JwtPart;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * @author: lizhao
 * @date: 2024/1/31 14:50
 */
@Service
public class JsonWebTokenService {

    @Resource
    private LoginConfigurationParam loginConfigurationParam;

    @Resource(name = "jwtHeadersMap")
    private Map<String, String> jwtHeadersMap;


    private String generateHeaderPart() {
        byte[] headerBytes = JSON.toJSONBytes(jwtHeadersMap);
        return new String(Base64.encodeBase64(headerBytes, false, true), StandardCharsets.US_ASCII);
    }


    private String generatePayloadPart(Map<String, Object> cliams) {
        byte[] payloadBytes = JSON.toJSONBytes(cliams);
        return new String(Base64.encodeBase64(payloadBytes, false, true), StandardCharsets.UTF_8);
    }

    private String generateSignaturePart(String headerPart, String payloadPart) {
        String content = headerPart + loginConfigurationParam.getDot() + payloadPart;
        Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, loginConfigurationParam.getSecretKey().getBytes(StandardCharsets.UTF_8));
        byte[] output = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.encodeBase64(output, false, true), StandardCharsets.UTF_8);
    }


    public String generate(Map<String, Object> claims) {
        String headerPart = generateHeaderPart();
        String payloadPart = generatePayloadPart(claims);
        String signaturePart = generateSignaturePart(headerPart, payloadPart);
        return headerPart + loginConfigurationParam.getDot() + payloadPart + loginConfigurationParam.getDot() + signaturePart;
    }


    public Map<JwtPart, JwtContent> parse(String jwt) {
        Map<JwtPart, JwtContent> map = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(jwt, loginConfigurationParam.getDot());
        String[] parts = new String[3];
        int idx = 0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            parts[idx] = token;
            idx++;
        }
        String headerPart = parts[0];
        JwtContent headerContent = new JwtContent();
        headerContent.setRow(headerPart);
        headerContent.setJwtPart(JwtPart.HEADER);
        headerPart = new String(Base64.decodeBase64(headerPart), StandardCharsets.UTF_8);
        headerContent.setPairs(JSON.parseObject(headerPart, new TypeReference<Map<String, Object>>() {
        }));
        map.put(JwtPart.HEADER, headerContent);

        String payloadPart = parts[1];
        JwtContent payloadContent = new JwtContent();
        payloadContent.setJwtPart(JwtPart.PAYLOAD);
        payloadContent.setRow(payloadPart);
        payloadPart = new String(Base64.decodeBase64(payloadPart), StandardCharsets.UTF_8);
        payloadContent.setPairs(JSON.parseObject(payloadPart, new TypeReference<Map<String, Object>>() {
        }));

        map.put(JwtPart.PAYLOAD, payloadContent);


        String signaturePart = parts[2];
        JwtContent signatureContent = new JwtContent();
        signatureContent.setRow(signaturePart);
        signatureContent.setJwtPart(JwtPart.SIGNATURE);
        map.put(JwtPart.SIGNATURE, signatureContent);
        return map;
    }


    public boolean verify(Map<JwtPart, JwtContent> map) {
        JwtContent payloadContent = map.get(JwtPart.PAYLOAD);
        JwtContent signatureContent = map.get(JwtPart.SIGNATURE);
        String signaturePart = generateSignaturePart(generateHeaderPart(), generatePayloadPart(payloadContent.getPairs()));
        String signature = signatureContent.getRow();
        if (!Objects.equals(signature, signaturePart)) {
            return false;
        }
        Map<String, Object> payloadPairs = payloadContent.getPairs();
        if (payloadPairs.containsKey("iss") && payloadPairs.containsKey("exp")) {
            String iss = payloadPairs.get("iss").toString();
            if (!Objects.equals(iss, "throws")) {
                return false;
            }
            String exp = payloadPairs.get("exp").toString();
            long expire = Long.parseLong(exp);
            return System.currentTimeMillis() - expire < 1000 * 60 * 60 * 24 * 3;
        }
        return false;
    }


}
