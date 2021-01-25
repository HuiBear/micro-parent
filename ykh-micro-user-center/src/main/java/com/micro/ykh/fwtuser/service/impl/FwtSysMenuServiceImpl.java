package com.micro.ykh.fwtuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.fwtuser.controller.model.MenuVO;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUserRole;
import com.micro.ykh.dao.fwtuser.FwtSysRoleMapper;
import com.micro.ykh.dao.fwtuser.FwtSysRoleMenuMapper;
import com.micro.ykh.dao.fwtuser.FwtSysUserRoleMapper;
import com.micro.ykh.fwtuser.service.FwtSysMenuService;
import com.micro.ykh.utils.text.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.fwtuser.FwtSysMenu;
import com.micro.ykh.dao.fwtuser.FwtSysMenuMapper;

import org.springframework.util.CollectionUtils;

@Service
public class FwtSysMenuServiceImpl extends ServiceImpl<FwtSysMenuMapper, FwtSysMenu> implements FwtSysMenuService {

    private static final Logger logger = LogManager.getLogger(FwtSysMenuServiceImpl.class);

    @Resource
    private FwtSysRoleMenuMapper fwtSysRoleMenuMapper;

    @Resource
    private FwtSysUserRoleMapper fwtSysUserRoleMapper;

    @Resource
    private FwtSysRoleMapper fwtSysRoleMapper;


    @Override
    public List<FwtSysMenu> getFwtSysMenuListByUserId(Integer userId) {
        List<FwtSysUserRole> fwtSysRoleList = fwtSysUserRoleMapper.selectList(new QueryWrapper<FwtSysUserRole>().eq("user_id", userId));

        if (CollectionUtils.isEmpty(fwtSysRoleList)) {
            logger.warn("根据用户ID:{}查询 用户角色为空", userId);
            return null;
        }

        //迭代循环获取roleIds
        List<Integer> roleIds = new ArrayList<>();
        for (FwtSysUserRole fwtSysUserRole : fwtSysRoleList) {
            roleIds.add(fwtSysUserRole.getRoleId());
        }

        //查询角色 资源关联集合
        List<FwtSysRoleMenu> sysRolePermissionList = fwtSysRoleMenuMapper.findByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(sysRolePermissionList)) {
            logger.warn("根据RoleIds:{}查询SysRolePermission集合为空", roleIds);
            return null;
        }

        //迭代permissionId 加入集合
        List<Integer> fwtSysRoleMenuList = new ArrayList<>();
        for (FwtSysRoleMenu fwtSysRoleMenu : sysRolePermissionList) {
            fwtSysRoleMenuList.add(fwtSysRoleMenu.getMenuId());
        }

        //查询用户的所有资源
        List<FwtSysMenu> fwtSysMenuList = this.baseMapper.findByIds(fwtSysRoleMenuList);
        if (CollectionUtils.isEmpty(fwtSysMenuList)) {
            logger.warn("根据permissionIds:{} 查询FwtSysMenu为空", fwtSysMenuList);
        }

        return fwtSysMenuList;
    }

    @Override
    public List<FwtSysMenu> getAllMenu() {
        return this.baseMapper.selectMenuTreeAll();
    }

    @Override
    public List<FwtSysMenu> getChildPerms(List<FwtSysMenu> list, Integer parentId) {
        List<FwtSysMenu> returnList = new ArrayList<FwtSysMenu>();
        for (FwtSysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    @Override
    public List<Integer> selectMenuListByRoleId(Integer roleId) {
        FwtSysRole role = fwtSysRoleMapper.selectById(roleId);
        return this.baseMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    @Override
    public List<String> selectMenuPermsByUserId(Integer userId) {
        return this.baseMapper.selectMenuPermsByUserId(userId);
    }

    @Override
    public String checkMenuNameUnique(MenuVO vo) {
        Integer menuId = StringUtils.isNull(vo.getMenuId()) ? -1 : vo.getMenuId();
        FwtSysMenu fwtSysMenu = this.baseMapper.selectOne(new QueryWrapper<FwtSysMenu>()
                                               .eq("menu_name", vo.getMenuName())
                                               .eq("parent_id", vo.getParentId()));
        if(StringUtils.isNotNull(fwtSysMenu) && !fwtSysMenu.getMenuId().equals(menuId)){
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }

    @Override
    public boolean hasChildByMenuId(Integer menuId) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<FwtSysMenu>().eq("parent_id",menuId));
        return count > 0;
    }

    @Override
    public List<FwtSysMenu> selectMenuList() {
        return this.baseMapper.selectMenuList(new FwtSysMenu());
    }


    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<FwtSysMenu> list, FwtSysMenu t) {
        // 得到子节点列表
        List<FwtSysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (FwtSysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<FwtSysMenu> getChildList(List<FwtSysMenu> list, FwtSysMenu t) {
        List<FwtSysMenu> tlist = new ArrayList<FwtSysMenu>();
        Iterator<FwtSysMenu> it = list.iterator();
        while (it.hasNext()) {
            FwtSysMenu n = (FwtSysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<FwtSysMenu> list, FwtSysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }


}
