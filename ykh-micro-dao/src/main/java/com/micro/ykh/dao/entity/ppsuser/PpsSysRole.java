package com.micro.ykh.dao.entity.ppsuser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName(value = "pps_sys_role")
public class PpsSysRole {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    private String roleName;

    @TableField(value = "role_code")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField(value = "role_description")
    private String roleDescription;

    /**
     * 显示顺序
     */
    @TableField(value = "role_sort")
    private Integer roleSort;

    @TableField(value = "create_user")
    private String createUser;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_user")
    private String updateUser;

    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 0 有效 -1 删除
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    /**
     * 菜单树选择项是否关联显示
     */
    private Boolean menuCheckStrictly;

    /**
     * 角色状态（0停用 1正常）
     */
    @TableField(value = "status")
    private Integer status;

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
     * 获取角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return role_code
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * @param roleCode
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * 获取角色描述
     *
     * @return role_description - 角色描述
     */
    public String getRoleDescription() {
        return roleDescription;
    }

    /**
     * 设置角色描述
     *
     * @param roleDescription 角色描述
     */
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    /**
     * 获取显示顺序
     *
     * @return role_sort - 显示顺序
     */
    public Integer getRoleSort() {
        return roleSort;
    }

    /**
     * 设置显示顺序
     *
     * @param roleSort 显示顺序
     */
    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
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
     * 获取0 有效 -1 删除
     *
     * @return del_flag - 0 有效 -1 删除
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * 设置0 有效 -1 删除
     *
     * @param delFlag 0 有效 -1 删除
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public boolean isMenuCheckStrictly()
    {
        return menuCheckStrictly;
    }

    /**
     * 设置菜单树选择项是否关联显示
     *
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     */
    public void setMenuCheckStrictly(Boolean menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    /**
     * 获取角色状态（0停用 1正常）
     *
     * @return status - 角色状态（0停用 1正常）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置角色状态（0停用 1正常）
     *
     * @param status 角色状态（0停用 1正常）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleName=").append(roleName);
        sb.append(", roleCode=").append(roleCode);
        sb.append(", roleDescription=").append(roleDescription);
        sb.append(", roleSort=").append(roleSort);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", menuCheckStrictly=").append(menuCheckStrictly);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}