package com.micro.ykh.common.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.ykh.common.controller.model.AuthUser;
import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.dao.entity.fwtuser.FwtSysMenu;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUser;
import com.micro.ykh.dao.entity.ppsuser.PpsSysMenu;
import com.micro.ykh.dao.entity.ppsuser.PpsSysUser;
import com.micro.ykh.exception.CustomException;
import com.micro.ykh.fwtuser.service.FwtSysMenuService;
import com.micro.ykh.ppsuser.service.PpsSysMenuService;
import com.micro.ykh.ppsuser.service.PpsSysUserService;
import com.micro.ykh.type.UserLoginMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PropertyServiceGrantHandle
 * @Description 物业端授权处理
 * @Author xiongh
 * @Date 2021/1/12 11:15
 * @Version 1.0
 **/
@Component
public class PropertyServiceGrantHandle implements GrantHandle {

    private static final Logger logger = LogManager.getLogger(PropertyServiceGrantHandle.class);


    @Autowired
    private PpsSysUserService ppsSysUserService;

    @Autowired
    private PpsSysMenuService ppsSysMenuService;

    @Override
    public AuthUser handle(LoginVO vo) {
        // 密码登录模式
        PpsSysUser ppsSysUser = new PpsSysUser();
        if (UserLoginMode.PASSWORD.getLoginMode().equals(vo.getLoginMode())) {
            ppsSysUser = ppsSysUserService.loadPpsSysUserByUsername(vo.getUsername());
            if (ObjectUtils.isEmpty(ppsSysUser)) {
                logger.warn("根据用户{}，所查用户信息为空！", vo.getUsername());
                throw new CustomException("没有此用户：" + vo.getUsername());
            }
        }

        // 手机登录模式
        if (UserLoginMode.MOBILE_PHONE.getLoginMode().equals(vo.getLoginMode())) {
            ppsSysUser = ppsSysUserService.loadPpsSysUserByMobilePhone(vo.getMobilePhone());
            if (ObjectUtils.isEmpty(ppsSysUser)) {
                logger.warn("根据手机{}，所查用户信息为空！", vo.getMobilePhone());
                throw new CustomException("没有此手机号码：" + vo.getMobilePhone());
            }
        }
        // 获得该用户权限
        List<PpsSysMenu> ppsSysMenuList = ppsSysMenuService.getPpsSysMenuListByUserId(ppsSysUser.getId());
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        // 转换格式
        if (!CollectionUtils.isEmpty(ppsSysMenuList)) {
            for (PpsSysMenu ppsSysMenu : ppsSysMenuList) {
                if (StringUtils.isNotBlank(ppsSysMenu.getUri())) {
                    authorityList.add(new SimpleGrantedAuthority(ppsSysMenu.getUri()));
                }
            }
        }

        AuthUser authUser = new AuthUser(ppsSysUser.getUsername(), ppsSysUser.getPassword(), authorityList);
        authUser.setUserId(ppsSysUser.getId());
        if (StringUtils.isNotBlank(ppsSysUser.getNickname())) {
            authUser.setNickName(ppsSysUser.getNickname());
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
}
