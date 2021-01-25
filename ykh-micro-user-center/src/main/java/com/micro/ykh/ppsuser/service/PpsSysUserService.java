package com.micro.ykh.ppsuser.service;


import com.micro.ykh.dao.entity.ppsuser.PpsSysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsUserDto;
import com.micro.ykh.ppsuser.controller.model.PpsUserVO;

import java.util.List;

public interface PpsSysUserService extends IService<PpsSysUser>{

    PpsSysUser loadPpsSysUserByUsername(String username);

    PpsSysUser loadPpsSysUserByMobilePhone(String mobilePhone);

    List<PpsSysUser> selectUserList(PpsUserDto ppsUserDto);

    PpsSysUser selectUserById(Integer userId);

    Boolean saveUser(PpsSysUser ppsSysUser);

    Boolean updateUser(PpsSysUser ppsSysUser);

    String checkUserNameUnique(String userName);

    String checkPhoneUnique(PpsUserVO vo);

}
