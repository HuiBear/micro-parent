package com.micro.ykh.common.handle;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

/**
 * @ClassName ShopAppUrlPermission
 * @Description 店铺端URL鉴权，默认通过
 * @Author xiongh
 * @Date 2021/1/13 15:51
 * @Version 1.0
 **/
@Component
public class ShopAppUrlPermission implements UrlPermissionInterface{
    @Override
    public boolean checkUrlPermission(Claims claims, String currentUrl) {
        return true;
    }
}
