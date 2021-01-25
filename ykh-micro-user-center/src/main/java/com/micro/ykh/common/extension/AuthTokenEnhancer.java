package com.micro.ykh.common.extension;

import com.micro.ykh.common.controller.model.AuthUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuthTokenEnhancer
 * @Description token增强器, 根据自己业务 往token存储业务字段
 * @Author xiongh
 * @Date 2020/11/18 21:17
 * @Version 1.0
 **/
public class AuthTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        AuthUser tulingUser = (AuthUser) oAuth2Authentication.getPrincipal();

        final Map<String, Object> additionalInfo = new HashMap<>();
        final Map<String, Object> retMap = new HashMap<>();

        additionalInfo.put("userId", tulingUser.getUserId());
        additionalInfo.put("nickName", tulingUser.getNickName());

        retMap.put("additionalInfo", additionalInfo);

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(retMap);

        return oAuth2AccessToken;
    }
}
