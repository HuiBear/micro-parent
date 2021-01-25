package com.micro.ykh.ppsuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.ppsuser.dto.PpsUserDto;
import com.micro.ykh.ppsuser.controller.model.PpsUserVO;
import com.micro.ykh.ppsuser.service.PpsSysRoleService;
import com.micro.ykh.type.DelFlagType;
import com.micro.ykh.type.FwtUserStatusEnum;
import com.micro.ykh.utils.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.ppsuser.PpsSysUser;
import com.micro.ykh.dao.ppsuser.PpsSysUserMapper;
import com.micro.ykh.ppsuser.service.PpsSysUserService;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

@Service
public class PpsSysUserServiceImpl extends ServiceImpl<PpsSysUserMapper, PpsSysUser> implements PpsSysUserService {


    @Resource
    private PpsSysRoleService ppsSysRoleService;

    @Autowired
    private DataSourceTransactionManager txManager;

    @Override
    public PpsSysUser loadPpsSysUserByUsername(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<PpsSysUser>().eq("username", username).eq("status", FwtUserStatusEnum.UN_LOCKED.getStatus()));
    }

    @Override
    public PpsSysUser loadPpsSysUserByMobilePhone(String mobilePhone) {
        return this.baseMapper.selectOne(new QueryWrapper<PpsSysUser>().eq("mobile_phone", mobilePhone).eq("status", FwtUserStatusEnum.UN_LOCKED.getStatus()));
    }

    @Override
    public List<PpsSysUser> selectUserList(PpsUserDto ppsUserDto) {
        return this.baseMapper.selectUserList(ppsUserDto);
    }

    @Override
    public PpsSysUser selectUserById(Integer userId) {
        return this.baseMapper.selectUserById(userId);
    }

    @Override
    public Boolean saveUser(PpsSysUser ppsSysUser) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            ppsSysUser.setPassword(passwordEncoder.encode(ppsSysUser.getPassword()));
            this.save(ppsSysUser);
//            fwtSysRoleService.saveBatch(PpsSysUser.getRoleList());
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return false;
        }
    }

    @Override
    public Boolean updateUser(PpsSysUser ppsSysUser) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            this.updateById(ppsSysUser);
            if (!CollectionUtils.isEmpty(ppsSysUser.getRoleList())) {
                ppsSysRoleService.updateBatchById(ppsSysUser.getRoleList());
            }
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return false;
        }
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = this.baseMapper.selectCount(new QueryWrapper<PpsSysUser>()
                .eq("username", userName)
                .eq("del_flag", DelFlagType.UN_DELETED.getValue()));
        if (count > 0) {
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param vo 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(PpsUserVO vo) {
        Integer userId = StringUtils.isNull(vo.getId()) ? -1 : vo.getId();
        PpsSysUser ppsSysUser = this.baseMapper.selectOne(new QueryWrapper<PpsSysUser>()
                .eq("mobile_phone", vo.getMobilePhone())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue()));
        if (StringUtils.isNotNull(ppsSysUser) && !ppsSysUser.getId().equals(userId)) {
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }
}
