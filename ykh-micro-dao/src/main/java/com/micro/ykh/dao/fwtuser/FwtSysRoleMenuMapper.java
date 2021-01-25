package com.micro.ykh.dao.fwtuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FwtSysRoleMenuMapper extends BaseMapper<FwtSysRoleMenu> {

    List<FwtSysRoleMenu> findByRoleIds(@Param("roleIds") List<Integer> roleIds);

}