package com.micro.ykh.fwtuser.service;

import com.micro.ykh.dao.entity.fwtuser.dto.FwtRoleDto;
import com.micro.ykh.fwtuser.controller.model.FwtSysRoleVO;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

public interface FwtSysRoleService extends IService<FwtSysRole>{

    Set<String> getRolesByUserId(Integer userId);

    List<FwtSysRoleVO> selectRoleList(FwtRoleDto dto);

    List<Integer> selectRoleListByUserId(Integer userId);

    Boolean updateBatchByRoleIds(List<Integer> roleIdList);

    FwtSysRole selectRoleById(Integer roleId);

    String checkRoleNameUnique(FwtSysRoleVO vo);

    String checkRoleKeyUnique(FwtSysRoleVO vo);

    Boolean saveRoleAndMenu(FwtSysRoleVO vo);

    Boolean editRoleAndMenu(FwtSysRoleVO vo);
}
