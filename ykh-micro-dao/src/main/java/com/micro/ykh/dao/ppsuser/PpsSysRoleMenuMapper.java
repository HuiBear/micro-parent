package com.micro.ykh.dao.ppsuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import com.micro.ykh.dao.entity.ppsuser.PpsSysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PpsSysRoleMenuMapper extends BaseMapper<PpsSysRoleMenu> {

    List<PpsSysRoleMenu> findByRoleIds(@Param("roleIds") List<Integer> roleIds);

}