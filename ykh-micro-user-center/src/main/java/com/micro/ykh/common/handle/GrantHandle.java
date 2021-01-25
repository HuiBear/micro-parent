package com.micro.ykh.common.handle;

import com.micro.ykh.common.controller.model.AuthUser;
import com.micro.ykh.common.controller.model.LoginVO;

/**
 * 授权处理
 */
public interface GrantHandle {

    /**
     * 处理
     * @return AuthUser
     */
    AuthUser handle(LoginVO vo);

}
