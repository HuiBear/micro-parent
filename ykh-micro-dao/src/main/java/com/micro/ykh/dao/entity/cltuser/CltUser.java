package com.micro.ykh.dao.entity.cltuser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName(value = "clt_user")
public class CltUser {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 昵称,为空时，应返回登录账号
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 电子邮箱, 可选, 可用于密码找回或
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号, 如果登录账号是手机号，则自动填写该字段
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 是否启用(0不启用，1是启用)
     */
    @TableField(value = "enable")
    private Byte enable;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除标记位 0 有效 -1 无效
     */
    @TableField(value = "del_flag")
    private String delFlag;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取昵称,为空时，应返回登录账号
     *
     * @return nick_name - 昵称,为空时，应返回登录账号
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称,为空时，应返回登录账号
     *
     * @param nickName 昵称,为空时，应返回登录账号
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取电子邮箱, 可选, 可用于密码找回或
     *
     * @return email - 电子邮箱, 可选, 可用于密码找回或
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置电子邮箱, 可选, 可用于密码找回或
     *
     * @param email 电子邮箱, 可选, 可用于密码找回或
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取手机号, 如果登录账号是手机号，则自动填写该字段
     *
     * @return phone - 手机号, 如果登录账号是手机号，则自动填写该字段
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号, 如果登录账号是手机号，则自动填写该字段
     *
     * @param phone 手机号, 如果登录账号是手机号，则自动填写该字段
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取是否启用(0不启用，1是启用)
     *
     * @return enable - 是否启用(0不启用，1是启用)
     */
    public Byte getEnable() {
        return enable;
    }

    /**
     * 设置是否启用(0不启用，1是启用)
     *
     * @param enable 是否启用(0不启用，1是启用)
     */
    public void setEnable(Byte enable) {
        this.enable = enable;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取删除标记位 0 有效 -1 无效
     *
     * @return del_flag - 删除标记位 0 有效 -1 无效
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * 设置删除标记位 0 有效 -1 无效
     *
     * @param delFlag 删除标记位 0 有效 -1 无效
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", nickName=").append(nickName);
        sb.append(", email=").append(email);
        sb.append(", phone=").append(phone);
        sb.append(", enable=").append(enable);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append("]");
        return sb.toString();
    }
}