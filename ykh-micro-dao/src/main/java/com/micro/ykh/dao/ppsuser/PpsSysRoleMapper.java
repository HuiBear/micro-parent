package com.micro.ykh.dao.ppsuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtRoleDto;
import com.micro.ykh.dao.entity.ppsuser.PpsSysRole;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsRoleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PpsSysRoleMapper extends BaseMapper<PpsSysRole> {

    List<PpsSysRole> getPpsSysRoleList(@Param("userId") Integer userId);

    List<PpsSysRole> selectRoleList(@Param("ppsRoleDto") PpsRoleDto dto);

    PpsSysRole  selectRoleById(@Param("roleId") Integer roleId);

    List<Integer> selectRoleListByUserId(Integer userId);

}