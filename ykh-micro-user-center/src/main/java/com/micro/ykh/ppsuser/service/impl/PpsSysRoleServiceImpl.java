package com.micro.ykh.ppsuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtRoleDto;
import com.micro.ykh.dao.entity.ppsuser.PpsSysRoleMenu;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsRoleDto;
import com.micro.ykh.dao.fwtuser.FwtSysRoleMenuMapper;
import com.micro.ykh.dao.ppsuser.PpsSysRoleMenuMapper;
import com.micro.ykh.fwtuser.controller.model.FwtSysRoleVO;
import com.micro.ykh.ppsuser.controller.model.PpsSysRoleVO;
import com.micro.ykh.type.DelFlagType;
import com.micro.ykh.type.StatusType;
import com.micro.ykh.utils.ListCopyUtils;
import com.micro.ykh.utils.text.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.ppsuser.PpsSysRoleMapper;
import com.micro.ykh.dao.entity.ppsuser.PpsSysRole;
import com.micro.ykh.ppsuser.service.PpsSysRoleService;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

@Service
public class PpsSysRoleServiceImpl extends ServiceImpl<PpsSysRoleMapper, PpsSysRole> implements PpsSysRoleService{
    @Resource
    private PpsSysRoleMenuMapper ppsSysRoleMenuMapper;

    @Autowired
    private DataSourceTransactionManager txManager;

    /**
     * 根据用户获取角色
     *
     * @param userId 用户ID
     * @return List<FwtSysRoleVO>
     */
    @Override
    public Set<String> getRolesByUserId(Integer userId) {
        List<PpsSysRole> roleList = this.baseMapper.getPpsSysRoleList(userId);
        Set<String> roleSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(roleList)) {
            for (PpsSysRole role : roleList) {
                if (ObjectUtils.isNotNull(role)) {
                    roleSet.addAll(Arrays.asList(role.getRoleCode().trim().split(",")));
                }
            }
        }
        return roleSet;
    }

    @Override
    public List<PpsSysRoleVO> selectRoleList(PpsRoleDto dto) {
        List<PpsSysRole> list = this.baseMapper.selectRoleList(dto);
        List<PpsSysRoleVO> roleVOList = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(list, roleVOList, PpsSysRoleVO.class);
        return roleVOList;
    }

    @Override
    public List<Integer> selectRoleListByUserId(Integer userId) {
        return this.baseMapper.selectRoleListByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateBatchByRoleIds(List<Integer> roleIdList) {
        List<PpsSysRole> list = (List<PpsSysRole>) this.listByIds(roleIdList);
        if (!CollectionUtils.isEmpty(list)) {
            for (PpsSysRole role : list) {
                role.setDelFlag(DelFlagType.DELETED.getValue());
                this.ppsSysRoleMenuMapper.delete(new QueryWrapper<PpsSysRoleMenu>().eq("role_id", role.getId()));
            }
            this.updateBatchById(list);
            return true;
        }
        // 删除 role和menu相关联数据
        return false;
    }

    @Override
    public PpsSysRole selectRoleById(Integer roleId) {
        return this.baseMapper.selectRoleById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param vo 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(PpsSysRoleVO vo) {
        Integer roleId = StringUtils.isNull(vo.getId()) ? -1 : vo.getId();
        PpsSysRole role = this.baseMapper.selectOne(new QueryWrapper<PpsSysRole>()
                .eq("role_name", vo.getRoleName())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue())
                .eq("status", StatusType.ENABLE.getValue()));
        if (StringUtils.isNotNull(role) && !role.getId().equals(roleId)) {
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param vo 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(PpsSysRoleVO vo) {
        Integer roleId = StringUtils.isNull(vo.getId()) ? -1 : vo.getId();
        PpsSysRole info = this.baseMapper.selectOne(new QueryWrapper<PpsSysRole>()
                .eq("role_code", vo.getRoleCode())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue())
                .eq("status", StatusType.ENABLE.getValue()));
        if (StringUtils.isNotNull(info) && !info.getId().equals(roleId)) {
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }

    @Override
    public Boolean saveRoleAndMenu(PpsSysRoleVO vo) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            PpsSysRole ppsSysRole = new PpsSysRole();
            BeanUtils.copyProperties(vo, ppsSysRole);
            // 保存角色
            this.save(ppsSysRole);
            // 保存角色与菜单相关数据
            Integer[] menuIds = vo.getMenuIds();
            if (menuIds != null && menuIds.length != 0) {
                for (Integer menuId : menuIds) {
                    PpsSysRoleMenu obj = new PpsSysRoleMenu();
                    obj.setRoleId(ppsSysRole.getId());
                    obj.setMenuId(menuId);
                    this.ppsSysRoleMenuMapper.insert(obj);
                }
            }
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return false;
        }
    }

    @Override
    public Boolean editRoleAndMenu(PpsSysRoleVO vo) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            PpsSysRole ppsSysRole = new PpsSysRole();
            BeanUtils.copyProperties(vo, ppsSysRole);
            this.baseMapper.updateById(ppsSysRole);
            Integer[] menuIds = vo.getMenuIds();
            if (menuIds != null && menuIds.length != 0) {
                for (Integer menuId : menuIds) {
                    PpsSysRoleMenu obj = new PpsSysRoleMenu();
                    obj.setRoleId(ppsSysRole.getId());
                    obj.setMenuId(menuId);
                    this.ppsSysRoleMenuMapper.updateById(obj);
                }
            }
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return false;
        }
    }
}
