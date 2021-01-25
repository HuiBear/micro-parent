package com.micro.ykh.common.extension;

import com.micro.ykh.common.controller.model.LoginVO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * @ClassName CustomUserDetailsByNameServiceWrapper
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/17 13:11
 * @Version 1.0
 **/
public class CustomUserDetailsByNameServiceWrapper<T extends Authentication> extends UserDetailsByNameServiceWrapper<T> {

    private CustomOauthUserDetailService customOauthUserDetailService;

    public CustomUserDetailsByNameServiceWrapper(CustomOauthUserDetailService customOauthUserDetailService) {
        this.customOauthUserDetailService = customOauthUserDetailService;
    }

    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        AbstractAuthenticationToken principal = (AbstractAuthenticationToken) authentication.getPrincipal();
        Map<String, String> map = (Map<String, String>) principal.getDetails();
        LoginVO vo = new LoginVO();
        vo.setLoginMode(map.get("loginMode"));
        vo.setPlatformType(map.get("platformType"));
        vo.setMobilePhone(map.get("mobilePhone"));
        vo.setUsername(map.get("username"));
        return this.customOauthUserDetailService.customLoadUserByLoginVO(vo);
    }


}
