package com.micro.ykh.fwtuser.controller;

import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtRoleDto;
import com.micro.ykh.domain.AjaxResult;
import com.micro.ykh.fwtuser.controller.model.FwtSysRoleVO;
import com.micro.ykh.fwtuser.service.FwtSysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @ClassName RoleController
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/30 11:11
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote")
public class RoleController {

    @Autowired
    private FwtSysRoleService fwtSysRoleService;


    @GetMapping(value = "/roles")
    public Set<String> getRoleList(@RequestParam("userId") Integer userId) {
//        System.out.println(1/0);
        Set<String> roles = fwtSysRoleService.getRolesByUserId(userId);
        return roles;
    }

    @PostMapping(value = "/select_role_list")
    public List<FwtSysRoleVO> selectRoleList(@RequestBody FwtRoleDto dto) {
        return fwtSysRoleService.selectRoleList(dto);
    }

    @GetMapping(value = "/select_role_by_id")
    public FwtSysRoleVO selectRoleById(@RequestParam("roleId") Integer roleId) {
        FwtSysRoleVO vo = new FwtSysRoleVO();
        FwtSysRole fwtSysRole = fwtSysRoleService.selectRoleById(roleId);
        BeanUtils.copyProperties(fwtSysRole, vo);
        return vo;
    }

    @PostMapping(value = "/add_role_info")
    public Boolean addRoleInfo(@RequestBody FwtSysRoleVO vo) {
        return fwtSysRoleService.saveRoleAndMenu(vo);
    }

    @PutMapping(value = "/edit_role_info")
    public Boolean editRoleInfo(@RequestBody FwtSysRoleVO vo) {
        return fwtSysRoleService.editRoleAndMenu(vo);
    }

    @DeleteMapping(value = "/remove_roles")
    public Boolean removeRoles(@RequestBody List<Integer> roleIds) {
        return fwtSysRoleService.updateBatchByRoleIds(roleIds);
    }

    @PostMapping(value = "/check_role_name_unique")
    public String checkRoleNameUnique(@RequestBody FwtSysRoleVO vo) {
        return fwtSysRoleService.checkRoleNameUnique(vo);
    }

    @PostMapping(value = "/check_role_key_unique")
    public String checkRoleKeyUnique(@RequestBody FwtSysRoleVO vo) {
        return fwtSysRoleService.checkRoleKeyUnique(vo);
    }

    @GetMapping(value = "/select_role_list_by_user_id")
    public List<Integer> selectRoleListByUserId(@RequestParam("userId") Integer userId) {
        return fwtSysRoleService.selectRoleListByUserId(userId);
    }


}
