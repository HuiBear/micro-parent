package com.micro.ykh.common.extension;


import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.common.handle.GrantHandle;
import com.micro.ykh.common.handle.GrantHandleContext;
import com.micro.ykh.fwtuser.service.FwtSysMenuService;
import com.micro.ykh.fwtuser.service.FwtSysUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;

/**
 * @ClassName CustomOauthUserDetailService
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/17 10:43
 * @Version 1.0
 **/
@Component("customOauthUserDetailService")
public class CustomOauthUserDetailService implements CustomUserDetailService {

    @Autowired
    private GrantHandleContext grantHandleContext;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails customLoadUserByLoginVO(LoginVO loginVO) throws UsernameNotFoundException {
        // 先判断平台类型，根据平台类型不同，读取不同的用户
        GrantHandle grantHandle = grantHandleContext.initGrantHandle(loginVO);
        return grantHandle.handle(loginVO);
    }


}
