package com.micro.ykh.common.handle;

import com.micro.ykh.constant.FwtConstant;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PlatformUrlPermission
 * @Description 平台端访问URL鉴权
 * @Author xiongh
 * @Date 2021/1/8 14:23
 * @Version 1.0
 **/
@Component
public class PlatformUrlPermission implements UrlPermissionInterface {

    @Override
    public boolean checkUrlPermission(Claims claims, String currentUrl) {
        Map<String, Object> map = claims.get("additionalInfo", Map.class);
        Integer userId = (Integer) map.get("userId");
        // 假如是管理员直接跳过
        if (ObjectUtils.equals(userId, FwtConstant.PLATFORM_USER_ID)) {
            return true;
        }
        // 假如是平台的获得路由和获得用户信息 数据字典的请求也直接放过
        if (currentUrl.contains(FwtConstant.PLATFORM_GET_USER_INFO_URL)
                || currentUrl.contains(FwtConstant.PLATFORM_GET_ROUTES_URL)
                || currentUrl.contains(FwtConstant.PLATFORM_DICT_URL)) {
            return true;
        }
        //登陆用户的权限集合判断
        List<String> permissionList = claims.get("authorities", List.class);
        for (String url : permissionList) {
            if (currentUrl.contains(url)) {
                return true;
            }
        }
        return false;
    }
}
