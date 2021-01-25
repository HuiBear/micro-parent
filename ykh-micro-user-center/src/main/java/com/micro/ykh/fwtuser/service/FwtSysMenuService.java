package com.micro.ykh.fwtuser.service;

import com.micro.ykh.fwtuser.controller.model.MenuVO;
import com.micro.ykh.dao.entity.fwtuser.FwtSysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FwtSysMenuService extends IService<FwtSysMenu>{

    List<FwtSysMenu> getFwtSysMenuListByUserId(Integer userId);

    List<FwtSysMenu> getAllMenu();

    List<FwtSysMenu> getChildPerms(List<FwtSysMenu> list,Integer parentId);

    List<Integer> selectMenuListByRoleId(Integer roleId);

    List<String> selectMenuPermsByUserId(Integer userId);

    String checkMenuNameUnique(MenuVO vo);

    boolean hasChildByMenuId(Integer menuId);

    List<FwtSysMenu> selectMenuList();
}
