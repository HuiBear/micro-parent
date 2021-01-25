package com.micro.ykh.dao.fwtuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtRoleDto;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FwtSysRoleMapper extends BaseMapper<FwtSysRole> {

    List<FwtSysRole> getFwtSysRoleList(@Param("userId") Integer userId);

    List<FwtSysRole> selectRoleList(@Param("fwtRoleDto") FwtRoleDto dto);

    FwtSysRole  selectRoleById(@Param("roleId") Integer roleId);

    List<Integer> selectRoleListByUserId(Integer userId);

}