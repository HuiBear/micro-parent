package com.micro.ykh.ppsuser.service;

import com.micro.ykh.dao.entity.ppsuser.PpsSysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
public interface PpsSysRoleMenuService extends IService<PpsSysRoleMenu>{

    boolean checkMenuExistRole(Integer menuId);
}
