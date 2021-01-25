package com.micro.ykh.ppsuser.service;


import com.micro.ykh.dao.entity.ppsuser.PpsSysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsRoleDto;
import com.micro.ykh.ppsuser.controller.model.PpsSysRoleVO;

import java.util.List;
import java.util.Set;

public interface PpsSysRoleService extends IService<PpsSysRole>{
    
    Set<String> getRolesByUserId(Integer userId);

    List<PpsSysRoleVO> selectRoleList(PpsRoleDto dto);

    List<Integer> selectRoleListByUserId(Integer userId);

    Boolean updateBatchByRoleIds(List<Integer> roleIdList);

    PpsSysRole selectRoleById(Integer roleId);

    String checkRoleNameUnique(PpsSysRoleVO vo);

    String checkRoleKeyUnique(PpsSysRoleVO vo);

    Boolean saveRoleAndMenu(PpsSysRoleVO vo);

    Boolean editRoleAndMenu(PpsSysRoleVO vo);

}
