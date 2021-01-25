package com.micro.ykh.fwtuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtRoleDto;
import com.micro.ykh.fwtuser.controller.model.FwtSysRoleVO;

import com.micro.ykh.dao.entity.fwtuser.FwtSysRoleMenu;
import com.micro.ykh.dao.fwtuser.FwtSysRoleMenuMapper;
import com.micro.ykh.fwtuser.service.FwtSysRoleService;
import com.micro.ykh.type.DelFlagType;
import com.micro.ykh.type.StatusType;
import com.micro.ykh.utils.ListCopyUtils;
import com.micro.ykh.utils.text.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.fwtuser.FwtSysRole;
import com.micro.ykh.dao.fwtuser.FwtSysRoleMapper;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FwtSysRoleServiceImpl extends ServiceImpl<FwtSysRoleMapper, FwtSysRole> implements FwtSysRoleService {

    @Resource
    private FwtSysRoleMenuMapper fwtSysRoleMenuMapper;

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
        List<FwtSysRole> roleList = this.baseMapper.getFwtSysRoleList(userId);
        Set<String> roleSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(roleList)) {
            for (FwtSysRole role : roleList) {
                if (ObjectUtils.isNotNull(role)) {
                    roleSet.addAll(Arrays.asList(role.getRoleCode().trim().split(",")));
                }
            }
        }
        return roleSet;
    }

    @Override
    public List<FwtSysRoleVO> selectRoleList(FwtRoleDto dto) {
        List<FwtSysRole> list = this.baseMapper.selectRoleList(dto);
        List<FwtSysRoleVO> roleVOList = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(list, roleVOList, FwtSysRoleVO.class);
        return roleVOList;
    }

    @Override
    public List<Integer> selectRoleListByUserId(Integer userId) {
        return this.baseMapper.selectRoleListByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateBatchByRoleIds(List<Integer> roleIdList) {
        List<FwtSysRole> list = (List<FwtSysRole>) this.listByIds(roleIdList);
        if (!CollectionUtils.isEmpty(list)) {
            for (FwtSysRole role : list) {
                role.setDelFlag(DelFlagType.DELETED.getValue());
                this.fwtSysRoleMenuMapper.delete(new QueryWrapper<FwtSysRoleMenu>().eq("role_id", role.getId()));
            }
            this.updateBatchById(list);
            return true;
        }
        // 删除 role和menu相关联数据
        return false;
    }

    @Override
    public FwtSysRole selectRoleById(Integer roleId) {
        return this.baseMapper.selectRoleById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param vo 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(FwtSysRoleVO vo) {
        Integer roleId = StringUtils.isNull(vo.getId()) ? -1 : vo.getId();
        FwtSysRole role = this.baseMapper.selectOne(new QueryWrapper<FwtSysRole>()
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
    public String checkRoleKeyUnique(FwtSysRoleVO vo) {
        Integer roleId = StringUtils.isNull(vo.getId()) ? -1 : vo.getId();
        FwtSysRole info = this.baseMapper.selectOne(new QueryWrapper<FwtSysRole>()
                .eq("role_code", vo.getRoleCode())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue())
                .eq("status", StatusType.ENABLE.getValue()));
        if (StringUtils.isNotNull(info) && !info.getId().equals(roleId)) {
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }

    @Override
    public Boolean saveRoleAndMenu(FwtSysRoleVO vo) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            FwtSysRole fwtSysRole = new FwtSysRole();
            BeanUtils.copyProperties(vo, fwtSysRole);
            // 保存角色
            this.save(fwtSysRole);
            // 保存角色与菜单相关数据
            Integer[] menuIds = vo.getMenuIds();
            if (menuIds != null && menuIds.length != 0) {
                for (Integer menuId : menuIds) {
                    FwtSysRoleMenu obj = new FwtSysRoleMenu();
                    obj.setRoleId(fwtSysRole.getId());
                    obj.setMenuId(menuId);
                    this.fwtSysRoleMenuMapper.insert(obj);
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
    public Boolean editRoleAndMenu(FwtSysRoleVO vo) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            FwtSysRole fwtSysRole = new FwtSysRole();
            BeanUtils.copyProperties(vo, fwtSysRole);
            this.baseMapper.updateById(fwtSysRole);
            Integer[] menuIds = vo.getMenuIds();
            if (menuIds != null && menuIds.length != 0) {
                for (Integer menuId : menuIds) {
                    FwtSysRoleMenu obj = new FwtSysRoleMenu();
                    obj.setRoleId(fwtSysRole.getId());
                    obj.setMenuId(menuId);
                    this.fwtSysRoleMenuMapper.updateById(obj);
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
