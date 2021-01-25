package com.micro.ykh.ppsuser.controller;


import com.micro.ykh.dao.entity.ppsuser.PpsSysRole;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsRoleDto;
import com.micro.ykh.ppsuser.controller.model.PpsSysRoleVO;
import com.micro.ykh.ppsuser.service.PpsSysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @ClassName PpsRoleController
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/12 10:07
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote/pps")
public class PpsRoleController {

    @Autowired
    private PpsSysRoleService ppsSysRoleService;


    @GetMapping(value = "/roles")
    public Set<String> getRoleList(@RequestParam("userId") Integer userId) {
        return ppsSysRoleService.getRolesByUserId(userId);
    }

    @PostMapping(value = "/select_role_list")
    public List<PpsSysRoleVO> selectRoleList(@RequestBody PpsRoleDto dto) {
        return ppsSysRoleService.selectRoleList(dto);
    }

    @GetMapping(value = "/select_role_by_id")
    public PpsSysRoleVO selectRoleById(@RequestParam("roleId") Integer roleId) {
        PpsSysRoleVO vo = new PpsSysRoleVO();
        PpsSysRole ppsSysRole = ppsSysRoleService.selectRoleById(roleId);
        BeanUtils.copyProperties(ppsSysRole, vo);
        return vo;
    }

    @PostMapping(value = "/add_role_info")
    public Boolean addRoleInfo(@RequestBody PpsSysRoleVO vo) {
        return ppsSysRoleService.saveRoleAndMenu(vo);
    }

    @PutMapping(value = "/edit_role_info")
    public Boolean editRoleInfo(@RequestBody PpsSysRoleVO vo) {
        return ppsSysRoleService.editRoleAndMenu(vo);
    }

    @DeleteMapping(value = "/remove_roles")
    public Boolean removeRoles(@RequestBody List<Integer> roleIds) {
        return ppsSysRoleService.updateBatchByRoleIds(roleIds);
    }

    @PostMapping(value = "/check_role_name_unique")
    public String checkRoleNameUnique(@RequestBody PpsSysRoleVO vo) {
        return ppsSysRoleService.checkRoleNameUnique(vo);
    }

    @PostMapping(value = "/check_role_key_unique")
    public String checkRoleKeyUnique(@RequestBody PpsSysRoleVO vo) {
        return ppsSysRoleService.checkRoleKeyUnique(vo);
    }

    @GetMapping(value = "/select_role_list_by_user_id")
    public List<Integer> selectRoleListByUserId(@RequestParam("userId") Integer userId) {
        return ppsSysRoleService.selectRoleListByUserId(userId);
    }
}
