package com.micro.ykh.common.extension;

import com.micro.ykh.common.controller.model.LoginVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails customLoadUserByLoginVO(LoginVO loginVO) throws UsernameNotFoundException;
}
