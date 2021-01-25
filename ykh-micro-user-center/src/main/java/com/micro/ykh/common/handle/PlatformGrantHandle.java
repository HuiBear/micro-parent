package com.micro.ykh.common.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.ykh.common.controller.model.AuthUser;
import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.fwtuser.FwtSysMenu;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUser;
import com.micro.ykh.exception.CustomException;
import com.micro.ykh.fwtuser.service.FwtSysMenuService;
import com.micro.ykh.fwtuser.service.FwtSysUserService;
import com.micro.ykh.type.UserLoginMode;
import com.micro.ykh.utils.sign.AESUtil;
import com.micro.ykh.utils.sign.Des64Utils;
import com.micro.ykh.utils.sign.ParseSystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @ClassName PlatformGrantHandle
 * @Description 平台端授权
 * @Author xiongh
 * @Date 2020/12/23 10:18
 * @Version 1.0
 **/
@Component
public class PlatformGrantHandle implements GrantHandle {

    private static final Logger logger = LogManager.getLogger(PlatformGrantHandle.class);

    @Autowired
    private FwtSysUserService fwtSysUserService;

    @Autowired
    private FwtSysMenuService fwtSysMenuService;

    @Override
    public AuthUser handle(LoginVO vo) {
        // 密码登录模式
        FwtSysUser fwtSysUser = new FwtSysUser();
        if (UserLoginMode.PASSWORD.getLoginMode().equals(vo.getLoginMode())) {
            fwtSysUser = fwtSysUserService.loadFwtSysUserByUsername(vo.getUsername());
            if (ObjectUtils.isEmpty(fwtSysUser)) {
                logger.warn("根据用户{}，所查用户信息为空！", vo.getUsername());
                throw new CustomException("没有此用户：" + vo.getUsername());
            }
        }
        // 手机登录模式
        if (UserLoginMode.MOBILE_PHONE.getLoginMode().equals(vo.getLoginMode())) {
            fwtSysUser = fwtSysUserService.loadFwtSysUserByMobilePhone(vo.getMobilePhone());
            if (ObjectUtils.isEmpty(fwtSysUser)) {
                logger.warn("根据手机{}，所查用户信息为空！", vo.getMobilePhone());
                throw new CustomException("没有此手机号码：" + vo.getMobilePhone());
            }
        }


        // 获得该用户权限
        List<FwtSysMenu> fwtSysMenuList = fwtSysMenuService.getFwtSysMenuListByUserId(fwtSysUser.getId());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        // 转换格式
        if (!CollectionUtils.isEmpty(fwtSysMenuList)) {
            for (FwtSysMenu fwtSysMenu : fwtSysMenuList) {
                if (StringUtils.isNotBlank(fwtSysMenu.getUri())) {
                    authorityList.add(new SimpleGrantedAuthority(fwtSysMenu.getUri()));
                }

            }
        }

        AuthUser authUser = new AuthUser(fwtSysUser.getUsername(), fwtSysUser.getPassword(), authorityList);
        authUser.setUserId(fwtSysUser.getId());
        if (StringUtils.isNotBlank(fwtSysUser.getNickname())) {
            authUser.setNickName(fwtSysUser.getNickname());
        } else {
            authUser.setNickName("");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("用户登录成功：{}", objectMapper.writeValueAsString(authUser));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return authUser;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String passwd = "123456";
        String clientId = "property-service";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("BCryptPasswordEncoder:" + passwordEncoder.encode(passwd));
        System.out.println("clientSecret的DES密码：" + Des64Utils.des64(passwd, FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI));
        System.out.println("clientId的DES密码：" + Des64Utils.des64(clientId, FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI));
//        System.out.println(passwordEncoder.matches("ykh-gateway", "$2a$10$Ng0ZhFRNQiNXwnbAYky1HusmPX4Ozvpanlqj3qEZiL1LJox9acT1O"));
        System.out.println(Des64Utils.unDes64(Des64Utils.des64(clientId, FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI), FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI));
    }
}
