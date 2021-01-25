package com.micro.ykh.ppsuser.service;

import com.micro.ykh.dao.entity.ppsuser.PpsSysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.fwtuser.controller.model.MenuVO;

import java.util.List;

public interface PpsSysMenuService extends IService<PpsSysMenu>{


    List<PpsSysMenu> getPpsSysMenuListByUserId(Integer userId);

    List<PpsSysMenu> getAllMenu();

    List<PpsSysMenu> getChildPerms(List<PpsSysMenu> list,Integer parentId);

    List<Integer> selectMenuListByRoleId(Integer roleId);

    List<String> selectMenuPermsByUserId(Integer userId);

    String checkMenuNameUnique(MenuVO vo);

    boolean hasChildByMenuId(Integer menuId);

    List<PpsSysMenu> selectMenuList();
}
