package com.micro.ykh.common.handle;

import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.fwtuser.service.FwtSysMenuService;
import com.micro.ykh.fwtuser.service.FwtSysUserService;
import com.micro.ykh.type.PlatformTypeEnum;
import com.micro.ykh.utils.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @ClassName GrantHandleContext
 * @Description 授权处理上下文
 * @Author xiongh
 * @Date 2020/12/23 10:07
 * @Version 1.0
 **/
@Component
public class GrantHandleContext {

    @Autowired
    private PlatformGrantHandle platformGrantHandle;

    @Autowired
    private PropertyServiceGrantHandle propertyServiceGrantHandle;

    @Autowired
    private ClientAppGrantHandle clientAppGrantHandle;

    @Autowired
    private ShopAppGrantHandle shopAppGrantHandle;

    public GrantHandle initGrantHandle(LoginVO vo) {
        GrantHandle grantHandle = null;
        switch (getPlatformTypeEnum(vo)) {
            case PLATFORM:
                grantHandle = platformGrantHandle;
                break;
            case PROPERTY_SERVICE:
                grantHandle = propertyServiceGrantHandle;
                break;
            case CLIENT_APP:
                grantHandle = clientAppGrantHandle;
                break;
            case SHOP_APP:
                grantHandle = shopAppGrantHandle;
                break;
            default:
                break;
        }
        return grantHandle;
    }

    /**
     * 根据平台类型获得platformTypeEnum类
     *
     * @return PlatformTypeEnum
     */
    private PlatformTypeEnum getPlatformTypeEnum(LoginVO vo) {
        Optional<PlatformTypeEnum> optionalPlatformTypeEnum = EnumUtils.getEnumObject(PlatformTypeEnum.class, e -> e.getValue().equals(vo.getPlatformType()));
        return optionalPlatformTypeEnum.orElse(null);
    }

}
