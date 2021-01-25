package com.micro.ykh.ppsuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.constant.FwtConstant;

import com.micro.ykh.dao.entity.ppsuser.PpsSysRole;
import com.micro.ykh.dao.entity.ppsuser.PpsSysRoleMenu;
import com.micro.ykh.dao.entity.ppsuser.PpsSysUserRole;

import com.micro.ykh.dao.ppsuser.PpsSysRoleMapper;
import com.micro.ykh.dao.ppsuser.PpsSysRoleMenuMapper;
import com.micro.ykh.dao.ppsuser.PpsSysUserRoleMapper;
import com.micro.ykh.fwtuser.controller.model.MenuVO;
import com.micro.ykh.utils.text.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.ppsuser.PpsSysMenu;
import com.micro.ykh.dao.ppsuser.PpsSysMenuMapper;
import com.micro.ykh.ppsuser.service.PpsSysMenuService;
import org.springframework.util.CollectionUtils;

@Service
public class PpsSysMenuServiceImpl extends ServiceImpl<PpsSysMenuMapper, PpsSysMenu> implements PpsSysMenuService{

    private static final Logger logger = LogManager.getLogger(PpsSysMenuServiceImpl.class);

    @Resource
    private PpsSysRoleMenuMapper ppsSysRoleMenuMapper;

    @Resource
    private PpsSysUserRoleMapper ppsSysUserRoleMapper;

    @Resource
    private PpsSysRoleMapper ppsSysRoleMapper;


    @Override
    public List<PpsSysMenu> getPpsSysMenuListByUserId(Integer userId) {
        List<PpsSysUserRole> ppsSysRoleList = ppsSysUserRoleMapper.selectList(new QueryWrapper<PpsSysUserRole>().eq("user_id", userId));

        if (CollectionUtils.isEmpty(ppsSysRoleList)) {
            logger.warn("根据用户ID:{}查询 用户角色为空", userId);
            return null;
        }

        //迭代循环获取roleIds
        List<Integer> roleIds = new ArrayList<>();
        for (PpsSysUserRole ppsSysUserRole : ppsSysRoleList) {
            roleIds.add(ppsSysUserRole.getRoleId());
        }

        //查询角色 资源关联集合
        List<PpsSysRoleMenu> sysRolePermissionList = ppsSysRoleMenuMapper.findByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(sysRolePermissionList)) {
            logger.warn("根据RoleIds:{}查询SysRolePermission集合为空", roleIds);
            return null;
        }

        //迭代permissionId 加入集合
        List<Integer> ppsSysRoleMenuList = new ArrayList<>();
        for (PpsSysRoleMenu ppsSysRoleMenu : sysRolePermissionList) {
            ppsSysRoleMenuList.add(ppsSysRoleMenu.getMenuId());
        }

        //查询用户的所有资源
        List<PpsSysMenu> ppsSysMenuList = this.baseMapper.findByIds(ppsSysRoleMenuList);
        if (CollectionUtils.isEmpty(ppsSysMenuList)) {
            logger.warn("根据permissionIds:{} 查询PpsSysMenu为空", ppsSysMenuList);
        }

        return ppsSysMenuList;
    }

    @Override
    public List<PpsSysMenu> getAllMenu() {
        return this.baseMapper.selectMenuTreeAll();
    }

    @Override
    public List<PpsSysMenu> getChildPerms(List<PpsSysMenu> list, Integer parentId) {
        List<PpsSysMenu> returnList = new ArrayList<PpsSysMenu>();
        for (PpsSysMenu t : list) {
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
        PpsSysRole role = ppsSysRoleMapper.selectById(roleId);
        return this.baseMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    @Override
    public List<String> selectMenuPermsByUserId(Integer userId) {
        return this.baseMapper.selectMenuPermsByUserId(userId);
    }

    @Override
    public String checkMenuNameUnique(MenuVO vo) {
        Integer menuId = StringUtils.isNull(vo.getMenuId()) ? -1 : vo.getMenuId();
        PpsSysMenu PpsSysMenu = this.baseMapper.selectOne(new QueryWrapper<PpsSysMenu>()
                .eq("menu_name", vo.getMenuName())
                .eq("parent_id", vo.getParentId()));
        if(StringUtils.isNotNull(PpsSysMenu) && !PpsSysMenu.getMenuId().equals(menuId)){
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }

    @Override
    public boolean hasChildByMenuId(Integer menuId) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<PpsSysMenu>().eq("parent_id",menuId));
        return count > 0;
    }

    @Override
    public List<PpsSysMenu> selectMenuList() {
        return this.baseMapper.selectMenuList(new PpsSysMenu());
    }


    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<PpsSysMenu> list, PpsSysMenu t) {
        // 得到子节点列表
        List<PpsSysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (PpsSysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<PpsSysMenu> getChildList(List<PpsSysMenu> list, PpsSysMenu t) {
        List<PpsSysMenu> tlist = new ArrayList<PpsSysMenu>();
        Iterator<PpsSysMenu> it = list.iterator();
        while (it.hasNext()) {
            PpsSysMenu n = (PpsSysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<PpsSysMenu> list, PpsSysMenu t) {
        return getChildList(list, t).size() > 0;
    }
}
