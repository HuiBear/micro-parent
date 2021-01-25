package com.micro.ykh.fwtuser.service;

import com.micro.ykh.fwtuser.controller.model.UserVO;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtUserDto;

import java.util.List;

public interface FwtSysUserService extends IService<FwtSysUser> {

    FwtSysUser loadFwtSysUserByUsername(String username);

    FwtSysUser loadFwtSysUserByMobilePhone(String mobilePhone);

    List<FwtSysUser> selectUserList(FwtUserDto fwtUserDto);

    FwtSysUser selectUserById(Integer userId);

    Boolean saveUser(FwtSysUser fwtSysUser);

    Boolean updateUser(FwtSysUser fwtSysUser);

    String checkUserNameUnique(String userName);

    String checkPhoneUnique(UserVO vo);

}
