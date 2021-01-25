package com.micro.ykh.ppsuser.controller;


import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.ppsuser.PpsSysMenu;
import com.micro.ykh.fwtuser.controller.model.MenuVO;
import com.micro.ykh.ppsuser.service.PpsSysMenuService;
import com.micro.ykh.ppsuser.service.PpsSysRoleMenuService;
import com.micro.ykh.type.StatusType;
import com.micro.ykh.utils.ListCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PpsMenuController
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/12 10:10
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote/pps")
public class PpsMenuController {

    @Autowired
    private PpsSysMenuService ppsSysMenuService;

    @Autowired
    private PpsSysRoleMenuService ppsSysRoleMenuService;


    @GetMapping(value = "/menus")
    public List<MenuVO> getMenuInfo(@RequestParam("userId") Integer userId) {
        ListCopyUtils utils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<PpsSysMenu> list;
        if (userId.equals(FwtConstant.PLATFORM_USER_ID)) {
            list = ppsSysMenuService.getAllMenu();
        } else {
            list = ppsSysMenuService.getPpsSysMenuListByUserId(userId);
        }
        list = ppsSysMenuService.getChildPerms(list, 0);
        List<MenuVO> menuVOS = new ArrayList<>();
        utils.copyList(list, menuVOS, MenuVO.class);
        return menuVOS;
    }

    @GetMapping(value = "/tree_select")
    public List<MenuVO> treeSelect(@RequestParam("userId") Integer userId) {
        ListCopyUtils utils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<PpsSysMenu> list;
        if (userId == 1) {
            list = ppsSysMenuService.getAllMenu();
        } else {
            list = ppsSysMenuService.getPpsSysMenuListByUserId(userId);
        }
        List<MenuVO> menuVOS = new ArrayList<>();
        utils.copyList(list, menuVOS, MenuVO.class);
        return menuVOS;
    }


    @GetMapping("/menu_info")
    public MenuVO menuInfo(@RequestParam("menuId") Integer menuId) {
        PpsSysMenu ppsSysMenu = ppsSysMenuService.getById(menuId);
        MenuVO vo = new MenuVO();
        BeanUtils.copyProperties(ppsSysMenu, vo);
        return vo;
    }

    @GetMapping("/checked_keys")
    public List<Integer> checkedKeys(@RequestParam("roleId") Integer roleId) {
        return ppsSysMenuService.selectMenuListByRoleId(roleId);
    }

    @PostMapping("/save_menu")
    public Boolean insertMenu(@RequestBody MenuVO vo) {
        PpsSysMenu ppsSysMenu = new PpsSysMenu();
        BeanUtils.copyProperties(vo, ppsSysMenu);
        return ppsSysMenuService.save(ppsSysMenu);
    }

    @PutMapping("/edit_menu")
    public Boolean editMenu(@RequestBody MenuVO vo) {
        PpsSysMenu ppsSysMenu = new PpsSysMenu();
        BeanUtils.copyProperties(vo, ppsSysMenu);
        return ppsSysMenuService.updateById(ppsSysMenu);
    }

    @DeleteMapping("/remove_menu")
    public Boolean removeMenu(@RequestParam("menuId") Integer menuId) {
        PpsSysMenu ppsSysMenu = ppsSysMenuService.getById(menuId);
        ppsSysMenu.setStatus(StatusType.DISABLE.getValue());
        return ppsSysMenuService.removeById(menuId);
    }

    @PostMapping(value = "/check_menu_name_unique")
    public String checkMenuNameUnique(@RequestBody MenuVO vo) {
        return ppsSysMenuService.checkMenuNameUnique(vo);
    }

    @GetMapping(value = "/has_child_by_menu")
    public Boolean hasChildByMenuId(@RequestParam("menuId") Integer menuId) {
        return ppsSysMenuService.hasChildByMenuId(menuId);
    }

    @GetMapping(value = "/check_menu_exist_role")
    public Boolean checkMenuExistRole(@RequestParam("menuId") Integer menuId) {
        return ppsSysRoleMenuService.checkMenuExistRole(menuId);
    }

    @GetMapping(value = "/select_menu_perms_by_user_id")
    public List<String> selectMenuPermsByUserId(@RequestParam("userId") Integer userId) {
        return ppsSysMenuService.selectMenuPermsByUserId(userId);
    }

    @GetMapping(value = "/list")
    public List<MenuVO> getMenuList(@RequestParam("userId") Integer userId){
        ListCopyUtils utils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<PpsSysMenu> list;
        if (userId.equals(FwtConstant.PLATFORM_USER_ID)) {
            list = ppsSysMenuService.selectMenuList();
        } else {
            list = ppsSysMenuService.getPpsSysMenuListByUserId(userId);
        }
        list = ppsSysMenuService.getChildPerms(list, 0);
        List<MenuVO> menuVOS = new ArrayList<>();
        utils.copyList(list, menuVOS, MenuVO.class);
        return menuVOS;
    }

}
