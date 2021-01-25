package com.micro.ykh.common.handle;

import com.micro.ykh.common.controller.model.AuthUser;
import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.shopuser.ShpShopStaff;
import com.micro.ykh.error.UserCenterErrorType;
import com.micro.ykh.exception.CustomException;
import com.micro.ykh.shopuser.service.ShpShopStaffService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName ShopAppGrantHandle
 * @Description 店铺端授权
 * @Author xiongh
 * @Date 2021/1/13 11:10
 * @Version 1.0
 **/
@Component
public class ShopAppGrantHandle implements GrantHandle {

    private static final Logger logger = LogManager.getLogger(ShopAppGrantHandle.class);

    @Autowired
    private ShpShopStaffService shpShopStaffService;

    @Override
    public AuthUser handle(LoginVO vo) {
        String mobilePhone = vo.getMobilePhone();
        // 是手机测试帐号，不做校验
        ShpShopStaff shpShopStaff = new ShpShopStaff();
//        if (!FwtConstant.SHOP_APP_TEST_ACCOUNT.equals(mobilePhone)) {
            shpShopStaff = shpShopStaffService.loadShopStaffByPhone(mobilePhone);
            if (ObjectUtils.isEmpty(shpShopStaff)) {
                logger.warn("没有此用户，手机号码：{}", mobilePhone);
                throw new CustomException(UserCenterErrorType.NO_QUERY_SHOP_STAFF);
            }
//        }
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(mobilePhone));
        AuthUser authUser = new AuthUser(mobilePhone, mobilePhone, authorityList);
        authUser.setUserId(shpShopStaff.getId());
        authUser.setNickName("");
        return authUser;
    }
}
