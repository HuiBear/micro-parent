package com.micro.ykh.ppsuser.controller;

import com.micro.ykh.dao.entity.ppsuser.PpsSysRole;
import com.micro.ykh.dao.entity.ppsuser.PpsSysUser;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsUserDto;
import com.micro.ykh.ppsuser.controller.model.PpsSysRoleVO;
import com.micro.ykh.ppsuser.controller.model.PpsUserVO;
import com.micro.ykh.ppsuser.service.PpsSysUserService;
import com.micro.ykh.type.DelFlagType;
import com.micro.ykh.utils.ListCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PpsUserController
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/12 9:57
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote/pps")
public class PpsUserController {

    @Autowired
    private PpsSysUserService ppsSysUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/pps_sys_user_list")
    public List<PpsUserVO> getPpsSysUserList(@RequestBody PpsUserDto dto) {
        List<PpsSysUser> list = ppsSysUserService.selectUserList(dto);
        List<PpsUserVO> userVOS = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(list, userVOS, PpsUserVO.class);
        return userVOS;
    }

    @GetMapping(value = "/select_user_by_id")
    public PpsUserVO selectUserById(@RequestParam("userId") Integer userId) {
        PpsSysUser ppsSysUser = ppsSysUserService.selectUserById(userId);
        PpsUserVO vo = new PpsUserVO();
        BeanUtils.copyProperties(ppsSysUser, vo);
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<PpsSysRoleVO> roleVOList = new ArrayList<>();
        listCopyUtils.copyList(ppsSysUser.getRoleList(), roleVOList, PpsSysRoleVO.class);
        vo.setRoleVOList(roleVOList);
        return vo;
    }

    @PostMapping(value = "/save_user")
    public Boolean save(@RequestBody PpsUserVO vo) {
        PpsSysUser ppsSysUser = new PpsSysUser();
        BeanUtils.copyProperties(vo, ppsSysUser);
        List<PpsSysRole> roleVOList = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(vo.getRoleVOList(), roleVOList, PpsSysRole.class);
        ppsSysUser.setRoleList(roleVOList);
        return ppsSysUserService.saveUser(ppsSysUser);
    }

    @PutMapping(value = "/edit_user")
    public Boolean edit(@RequestBody PpsUserVO vo) {
        PpsSysUser ppsSysUser = new PpsSysUser();
        BeanUtils.copyProperties(vo, ppsSysUser);
        List<PpsSysRole> roleVOList = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(vo.getRoleVOList(), roleVOList, PpsSysRole.class);
        ppsSysUser.setRoleList(roleVOList);
        return ppsSysUserService.updateUser(ppsSysUser);
    }

    @PutMapping(value = "/edit_user_password")
    public Boolean updatePassword(@RequestBody PpsUserVO vo) {
        PpsSysUser ppsSysUser = new PpsSysUser();
        BeanUtils.copyProperties(vo, ppsSysUser, PpsSysUser.class);
        ppsSysUser.setPassword(passwordEncoder.encode(ppsSysUser.getPassword()));
        return ppsSysUserService.updateById(ppsSysUser);
    }

    @DeleteMapping(value = "/remove_user")
    public Boolean remove(@RequestBody List<Integer> userIds) {
        List<PpsSysUser> list = (List<PpsSysUser>) this.ppsSysUserService.listByIds(userIds);
        if (!CollectionUtils.isEmpty(list)) {
            for (PpsSysUser ppsSysUser : list) {
                ppsSysUser.setDelFlag(DelFlagType.DELETED.getValue());
            }
            return ppsSysUserService.updateBatchById(list);
        }
        return false;
    }

    @GetMapping(value = "/check_user_name_unique")
    public String checkUserNameUnique(@RequestParam("userName") String userName) {
        return ppsSysUserService.checkUserNameUnique(userName);
    }

    @PostMapping(value = "/check_phone_unique")
    public String checkPhoneUnique(@RequestBody PpsUserVO vo) {
        return ppsSysUserService.checkPhoneUnique(vo);
    }

}
