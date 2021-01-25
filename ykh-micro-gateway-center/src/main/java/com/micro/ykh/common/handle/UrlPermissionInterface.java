package com.micro.ykh.common.handle;

import io.jsonwebtoken.Claims;

public interface UrlPermissionInterface {

    boolean checkUrlPermission(Claims claims, String currentUrl);
}
