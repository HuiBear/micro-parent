package com.micro.ykh.dao.entity.shopuser.dto;


import java.util.Date;

/**
 * @ClassName ShpShopStaffDto
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/19 15:26
 * @Version 1.0
 **/
public class ShpShopStaffDto {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺编码(来自店铺表的编码)
     */
    private String shopCode;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工电话
     */
    private String phone;

    /**
     * 员工角色(普通员工 employee, 店铺管理员 store_manager)
     */
    private String role;

    /**
     * 员工状态(0是禁用；1是启用)
     */
    private Byte enable;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志位
     */
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
     * 获取店铺编码(来自店铺表的编码)
     *
     * @return shop_code - 店铺编码(来自店铺表的编码)
     */
    public String getShopCode() {
        return shopCode;
    }

    /**
     * 设置店铺编码(来自店铺表的编码)
     *
     * @param shopCode 店铺编码(来自店铺表的编码)
     */
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    /**
     * 获取员工姓名
     *
     * @return name - 员工姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置员工姓名
     *
     * @param name 员工姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取员工电话
     *
     * @return phone - 员工电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置员工电话
     *
     * @param phone 员工电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取员工角色(普通员工 employee, 店铺管理员 store_manager)
     *
     * @return role - 员工角色(普通员工 employee, 店铺管理员 store_manager)
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置员工角色(普通员工 employee, 店铺管理员 store_manager)
     *
     * @param role 员工角色(普通员工 employee, 店铺管理员 store_manager)
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取员工状态(0是禁用；1是启用)
     *
     * @return enable - 员工状态(0是禁用；1是启用)
     */
    public Byte getEnable() {
        return enable;
    }

    /**
     * 设置员工状态(0是禁用；1是启用)
     *
     * @param enable 员工状态(0是禁用；1是启用)
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
     * 获取删除标志位
     *
     * @return del_flag - 删除标志位
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * 设置删除标志位
     *
     * @param delFlag 删除标志位
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
        sb.append(", shopCode=").append(shopCode);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", role=").append(role);
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
