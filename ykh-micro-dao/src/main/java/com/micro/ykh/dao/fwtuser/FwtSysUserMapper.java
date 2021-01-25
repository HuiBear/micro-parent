package com.micro.ykh.dao.fwtuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtUserDto;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FwtSysUserMapper extends BaseMapper<FwtSysUser> {

    List<FwtSysUser> selectUserList(@Param("fwtUserDto") FwtUserDto dto);

    FwtSysUser selectUserById(@Param("userId") Integer userId);

}