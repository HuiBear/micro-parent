package com.micro.ykh.common.handle;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

/**
 * @ClassName ClientAppUrlPermission
 * @Description 客户端URL鉴权，默认通过
 * @Author xiongh
 * @Date 2021/1/13 15:50
 * @Version 1.0
 **/
@Component
public class ClientAppUrlPermission implements UrlPermissionInterface{
    @Override
    public boolean checkUrlPermission(Claims claims, String currentUrl) {
        return true;
    }
}
