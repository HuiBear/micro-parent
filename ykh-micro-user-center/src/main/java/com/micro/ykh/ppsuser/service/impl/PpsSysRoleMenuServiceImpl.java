package com.micro.ykh.ppsuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.ppsuser.PpsSysRoleMenuMapper;
import com.micro.ykh.dao.entity.ppsuser.PpsSysRoleMenu;
import com.micro.ykh.ppsuser.service.PpsSysRoleMenuService;
@Service
public class PpsSysRoleMenuServiceImpl extends ServiceImpl<PpsSysRoleMenuMapper, PpsSysRoleMenu> implements PpsSysRoleMenuService{

    @Override
    public boolean checkMenuExistRole(Integer menuId) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<PpsSysRoleMenu>().eq("menu_id", menuId));
        return count > 0;
    }
}
