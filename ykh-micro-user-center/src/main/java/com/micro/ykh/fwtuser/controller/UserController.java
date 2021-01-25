package com.micro.ykh.fwtuser.controller;

import com.micro.ykh.dao.entity.fwtuser.dto.FwtUserDto;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUser;

import com.micro.ykh.fwtuser.controller.model.FwtSysRoleVO;
import com.micro.ykh.fwtuser.controller.model.UserVO;
import com.micro.ykh.fwtuser.service.FwtSysUserService;
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
 * @ClassName UserController
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/29 17:05
 * @Version 1.0
 **/
@RestController
@RequestMapping("/remote")
public class UserController {

    @Autowired
    private FwtSysUserService fwtSysUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/fwt_sys_user_list")
    public List<UserVO> getFwtSysUserList(@RequestBody FwtUserDto dto) {
        List<FwtSysUser> list = fwtSysUserService.selectUserList(dto);
        List<UserVO> userVOS = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(list, userVOS, UserVO.class);
        return userVOS;
    }

    @GetMapping(value = "/select_user_by_id")
    public UserVO selectUserById(@RequestParam("userId") Integer userId) {
        FwtSysUser fwtSysUser = fwtSysUserService.selectUserById(userId);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(fwtSysUser, vo);
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<FwtSysRoleVO> roleVOList = new ArrayList<>();
        listCopyUtils.copyList(fwtSysUser.getRoleList(), roleVOList, FwtSysRoleVO.class);
        vo.setRoleVOList(roleVOList);
        return vo;
    }

    @PostMapping(value = "/save_user")
    public Boolean save(@RequestBody UserVO vo) {
        FwtSysUser fwtSysUser = new FwtSysUser();
        BeanUtils.copyProperties(vo, fwtSysUser);
        List<FwtSysRole> roleVOList = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(vo.getRoleVOList(), roleVOList, FwtSysRole.class);
        fwtSysUser.setRoleList(roleVOList);
        return fwtSysUserService.saveUser(fwtSysUser);
    }

    @PutMapping(value = "/edit_user")
    public Boolean edit(@RequestBody UserVO vo) {
        FwtSysUser fwtSysUser = new FwtSysUser();
        BeanUtils.copyProperties(vo, fwtSysUser);
        List<FwtSysRole> roleVOList = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(vo.getRoleVOList(), roleVOList, FwtSysRole.class);
        fwtSysUser.setRoleList(roleVOList);
        return fwtSysUserService.updateUser(fwtSysUser);
    }

    @PutMapping(value = "/edit_user_password")
    public Boolean updatePassword(@RequestBody UserVO vo) {
        FwtSysUser fwtSysUser = new FwtSysUser();
        BeanUtils.copyProperties(vo, fwtSysUser, FwtSysUser.class);
        fwtSysUser.setPassword(passwordEncoder.encode(fwtSysUser.getPassword()));
        return fwtSysUserService.updateById(fwtSysUser);
    }

    @DeleteMapping(value = "/remove_user")
    public Boolean remove(@RequestBody List<Integer> userIds) {
        List<FwtSysUser> list = (List<FwtSysUser>) this.fwtSysUserService.listByIds(userIds);
        if (!CollectionUtils.isEmpty(list)) {
            for (FwtSysUser fwtSysUser : list) {
                fwtSysUser.setDelFlag(DelFlagType.DELETED.getValue());
            }
            return fwtSysUserService.updateBatchById(list);
        }
        return false;
    }

    @GetMapping(value = "/check_user_name_unique")
    public String checkUserNameUnique(@RequestParam("userName") String userName) {
        return fwtSysUserService.checkUserNameUnique(userName);
    }

    @PostMapping(value = "/check_phone_unique")
    public String checkPhoneUnique(@RequestBody UserVO vo) {
        return fwtSysUserService.checkPhoneUnique(vo);
    }


}
