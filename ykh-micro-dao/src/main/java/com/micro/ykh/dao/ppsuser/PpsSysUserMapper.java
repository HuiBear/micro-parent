package com.micro.ykh.dao.ppsuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUser;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtUserDto;
import com.micro.ykh.dao.entity.ppsuser.PpsSysUser;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsUserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PpsSysUserMapper extends BaseMapper<PpsSysUser> {

    List<PpsSysUser> selectUserList(@Param("ppsUserDto") PpsUserDto dto);

    PpsSysUser selectUserById(@Param("userId") Integer userId);
}