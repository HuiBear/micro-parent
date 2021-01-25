package com.micro.ykh.fwtuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.fwtuser.FwtSysRoleMenuMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import com.micro.ykh.fwtuser.service.FwtSysRoleMenuService;

@Service
public class FwtSysRoleMenuServiceImpl extends ServiceImpl<FwtSysRoleMenuMapper, FwtSysRoleMenu> implements FwtSysRoleMenuService {

    @Override
    public boolean checkMenuExistRole(Integer menuId) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<FwtSysRoleMenu>().eq("menu_id", menuId));
        return count > 0;
    }
}
