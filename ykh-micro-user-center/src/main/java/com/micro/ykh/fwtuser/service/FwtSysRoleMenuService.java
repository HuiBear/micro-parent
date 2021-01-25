package com.micro.ykh.fwtuser.service;

import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
public interface FwtSysRoleMenuService extends IService<FwtSysRoleMenu>{

    boolean checkMenuExistRole(Integer menuId);

}
