package com.micro.ykh.fwtuser.controller;

import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.fwtuser.FwtSysMenu;
import com.micro.ykh.fwtuser.controller.model.MenuVO;
import com.micro.ykh.fwtuser.service.FwtSysMenuService;
import com.micro.ykh.fwtuser.service.FwtSysRoleMenuService;
import com.micro.ykh.type.StatusType;
import com.micro.ykh.utils.ListCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MenuController
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/30 9:03
 * @Version 1.0
 **/
@RestController
@RequestMapping("/remote")
public class MenuController{

    @Autowired
    private FwtSysMenuService fwtSysMenuService;

    @Autowired
    private FwtSysRoleMenuService fwtSysRoleMenuService;


    @GetMapping(value = "/menus")
    public List<MenuVO> getMenuInfo(@RequestParam("userId") Integer userId) {
        ListCopyUtils utils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<FwtSysMenu> list;
        if (userId.equals(FwtConstant.PLATFORM_USER_ID)) {
            list = fwtSysMenuService.getAllMenu();
        } else {
            list = fwtSysMenuService.getFwtSysMenuListByUserId(userId);
        }
        list = fwtSysMenuService.getChildPerms(list, 0);
        List<MenuVO> menuVOS = new ArrayList<>();
        utils.copyList(list, menuVOS, MenuVO.class);
        return menuVOS;
    }

    @GetMapping(value = "/tree_select")
    public List<MenuVO> treeSelect(@RequestParam("userId") Integer userId) {
        ListCopyUtils utils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<FwtSysMenu> list;
        if (userId == 1) {
            list = fwtSysMenuService.getAllMenu();
        } else {
            list = fwtSysMenuService.getFwtSysMenuListByUserId(userId);
        }
        List<MenuVO> menuVOS = new ArrayList<>();
        utils.copyList(list, menuVOS, MenuVO.class);
        return menuVOS;
    }


    @GetMapping("/menu_info")
    public MenuVO menuInfo(@RequestParam("menuId") Integer menuId) {
        FwtSysMenu fwtSysMenu = fwtSysMenuService.getById(menuId);
        MenuVO vo = new MenuVO();
        BeanUtils.copyProperties(fwtSysMenu, vo);
        return vo;
    }

    @GetMapping("/checked_keys")
    public List<Integer> checkedKeys(@RequestParam("roleId") Integer roleId) {
        return fwtSysMenuService.selectMenuListByRoleId(roleId);
    }

    @PostMapping("/save_menu")
    public Boolean insertMenu(@RequestBody MenuVO vo) {
        FwtSysMenu fwtSysMenu = new FwtSysMenu();
        BeanUtils.copyProperties(vo, fwtSysMenu);
        return fwtSysMenuService.save(fwtSysMenu);
    }

    @PutMapping("/edit_menu")
    public Boolean editMenu(@RequestBody MenuVO vo) {
        FwtSysMenu fwtSysMenu = new FwtSysMenu();
        BeanUtils.copyProperties(vo, fwtSysMenu);
        return fwtSysMenuService.updateById(fwtSysMenu);
    }

    @DeleteMapping("/remove_menu")
    public Boolean removeMenu(@RequestParam("menuId") Integer menuId) {
        FwtSysMenu fwtSysMenu = fwtSysMenuService.getById(menuId);
        fwtSysMenu.setStatus(StatusType.DISABLE.getValue().toString());
        return fwtSysMenuService.removeById(menuId);
    }

    @PostMapping(value = "/check_menu_name_unique")
    public String checkMenuNameUnique(@RequestBody MenuVO vo) {
        return fwtSysMenuService.checkMenuNameUnique(vo);
    }

    @GetMapping(value = "/has_child_by_menu")
    public Boolean hasChildByMenuId(@RequestParam("menuId") Integer menuId) {
        return fwtSysMenuService.hasChildByMenuId(menuId);
    }

    @GetMapping(value = "/check_menu_exist_role")
    public Boolean checkMenuExistRole(@RequestParam("menuId") Integer menuId) {
        return fwtSysRoleMenuService.checkMenuExistRole(menuId);
    }

    @GetMapping(value = "/select_menu_perms_by_user_id")
    public List<String> selectMenuPermsByUserId(@RequestParam("userId") Integer userId) {
        return fwtSysMenuService.selectMenuPermsByUserId(userId);
    }

    @GetMapping(value = "/list")
    public List<MenuVO> getMenuList(@RequestParam("userId") Integer userId){
        ListCopyUtils utils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<FwtSysMenu> list;
        if (userId.equals(FwtConstant.PLATFORM_USER_ID)) {
            list = fwtSysMenuService.selectMenuList();
        } else {
            list = fwtSysMenuService.getFwtSysMenuListByUserId(userId);
        }
        list = fwtSysMenuService.getChildPerms(list, 0);
        List<MenuVO> menuVOS = new ArrayList<>();
        utils.copyList(list, menuVOS, MenuVO.class);
        return menuVOS;
    }
}
