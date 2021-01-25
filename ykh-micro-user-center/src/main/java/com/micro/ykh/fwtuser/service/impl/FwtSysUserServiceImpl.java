package com.micro.ykh.fwtuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.dao.entity.fwtuser.dto.FwtUserDto;
import com.micro.ykh.fwtuser.controller.model.UserVO;

import com.micro.ykh.fwtuser.service.FwtSysRoleService;
import com.micro.ykh.type.DelFlagType;
import com.micro.ykh.type.FwtUserStatusEnum;
import com.micro.ykh.utils.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.fwtuser.FwtSysUser;
import com.micro.ykh.dao.fwtuser.FwtSysUserMapper;
import com.micro.ykh.fwtuser.service.FwtSysUserService;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FwtSysUserServiceImpl extends ServiceImpl<FwtSysUserMapper, FwtSysUser> implements FwtSysUserService {

    @Resource
    private FwtSysRoleService fwtSysRoleService;

    @Autowired
    private DataSourceTransactionManager txManager;

    @Override
    public FwtSysUser loadFwtSysUserByUsername(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<FwtSysUser>().eq("username", username).eq("status", FwtUserStatusEnum.UN_LOCKED.getStatus()));
    }

    @Override
    public FwtSysUser loadFwtSysUserByMobilePhone(String mobilePhone) {
        return this.baseMapper.selectOne(new QueryWrapper<FwtSysUser>().eq("mobile_phone", mobilePhone).eq("status", FwtUserStatusEnum.UN_LOCKED.getStatus()));
    }

    @Override
    public List<FwtSysUser> selectUserList(FwtUserDto fwtUserDto) {
        return this.baseMapper.selectUserList(fwtUserDto);
    }

    @Override
    public FwtSysUser selectUserById(Integer userId) {
        return this.baseMapper.selectUserById(userId);
    }

    @Override
    public Boolean saveUser(FwtSysUser fwtSysUser) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            fwtSysUser.setPassword(passwordEncoder.encode(fwtSysUser.getPassword()));
            this.save(fwtSysUser);
//            fwtSysRoleService.saveBatch(fwtSysUser.getRoleList());
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            txManager.rollback(status);
            return false;
        }
    }

    @Override
    public Boolean updateUser(FwtSysUser fwtSysUser) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            this.updateById(fwtSysUser);
            if(!CollectionUtils.isEmpty(fwtSysUser.getRoleList())){
                fwtSysRoleService.updateBatchById(fwtSysUser.getRoleList());
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
        int count = this.baseMapper.selectCount(new QueryWrapper<FwtSysUser>()
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
    public String checkPhoneUnique(UserVO vo) {
        Integer userId = StringUtils.isNull(vo.getId()) ? -1 : vo.getId();
        FwtSysUser fwtSysUser = this.baseMapper.selectOne(new QueryWrapper<FwtSysUser>()
                .eq("mobile_phone",vo.getMobilePhone())
                .eq("del_flag",DelFlagType.UN_DELETED.getValue()));
        if(StringUtils.isNotNull(fwtSysUser) && !fwtSysUser.getId().equals(userId)){
            return FwtConstant.NOT_UNIQUE;
        }
        return FwtConstant.UNIQUE;
    }

}
