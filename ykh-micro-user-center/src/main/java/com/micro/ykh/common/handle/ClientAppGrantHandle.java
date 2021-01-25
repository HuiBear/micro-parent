package com.micro.ykh.common.handle;

import com.micro.ykh.cltuser.service.CltUserService;
import com.micro.ykh.common.controller.model.AuthUser;
import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.cltuser.CltUser;
import com.micro.ykh.error.UserCenterErrorType;
import com.micro.ykh.exception.CustomException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ClientAppGrantHandle
 * @Description 客户端授权
 * @Author xiongh
 * @Date 2021/1/13 11:09
 * @Version 1.0
 **/
@Component
public class ClientAppGrantHandle implements GrantHandle {

    private static final Logger logger = LogManager.getLogger(ClientAppGrantHandle.class);

    @Autowired
    private CltUserService cltUserService;

    @Override
    public AuthUser handle(LoginVO vo) {
        String mobilePhone = vo.getMobilePhone();
        CltUser cltUser = cltUserService.loadCltUserByPhone(mobilePhone);
        if (ObjectUtils.isEmpty(cltUser)) {
            logger.warn("客户端此用户不存在，手机号码：{}", mobilePhone);
            throw new CustomException(UserCenterErrorType.NO_QUERY_CLIENT_USER);
        }
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(mobilePhone));
        AuthUser authUser = new AuthUser(mobilePhone, mobilePhone, authorityList);
        authUser.setUserId(cltUser.getId());
        if (StringUtils.isNotBlank(cltUser.getNickName())) {
            authUser.setNickName(cltUser.getNickName());
        } else {
            authUser.setNickName("");
        }


        return authUser;
    }
}
