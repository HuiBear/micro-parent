package com.micro.ykh.dao.entity.ppsuser;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

@TableName(value = "pps_sys_user")
public class PpsSysUser {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 状态（0：锁定，1：解锁）
     */
    @TableField(value = "status")
    private Byte status;

    @TableField(value = "create_user")
    private String createUser;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_user")
    private String updateUser;

    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 电话号码
     */
    @TableField(value = "mobile_phone")
    private String mobilePhone;

    /**
     * 删除标记位：0 未删除 -1 已删除
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    @TableField(select = false,updateStrategy = FieldStrategy.NEVER,insertStrategy = FieldStrategy.NEVER)
    private List<PpsSysRole> roleList;

    public List<PpsSysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<PpsSysRole> roleList) {
        this.roleList = roleList;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取账号
     *
     * @return username - 账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置账号
     *
     * @param username 账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取状态（0：锁定，1：解锁）
     *
     * @return status - 状态（0：锁定，1：解锁）
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态（0：锁定，1：解锁）
     *
     * @param status 状态（0：锁定，1：解锁）
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * @return create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_user
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取电话号码
     *
     * @return mobile_phone - 电话号码
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * 设置电话号码
     *
     * @param mobilePhone 电话号码
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * 获取删除标记位：0 未删除 -1 已删除
     *
     * @return del_flag - 删除标记位：0 未删除 -1 已删除
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * 设置删除标记位：0 未删除 -1 已删除
     *
     * @param delFlag 删除标记位：0 未删除 -1 已删除
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", email=").append(email);
        sb.append(", status=").append(status);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", mobilePhone=").append(mobilePhone);
        sb.append(", delFlag=").append(delFlag);
        sb.append("]");
        return sb.toString();
    }
}