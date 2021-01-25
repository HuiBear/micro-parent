package com.micro.ykh.dao.entity.fwtuser;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

/**
    * 菜单权限表
    */
@TableName(value = "fwt_sys_menu")
public class FwtSysMenu {
    /**
     * 菜单ID
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组件路径
     */
    @TableField(value = "component")
    private String component;

    /**
     * 是否为外链（0是 1否）
     */
    @TableField(value = "is_frame")
    private Integer isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @TableField(value = "is_cache")
    private Integer isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @TableField(value = "menu_type")
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @TableField(value = "visible")
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 权限标识
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 访问接口路径
     */
    @TableField(value = "uri")
    private String uri;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(select = false,updateStrategy = FieldStrategy.NEVER,insertStrategy = FieldStrategy.NEVER)
    private List<FwtSysMenu> children;

    public List<FwtSysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<FwtSysMenu> children) {
        this.children = children;
    }

    /**
     * 获取菜单ID
     *
     * @return menu_id - 菜单ID
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * 设置菜单ID
     *
     * @param menuId 菜单ID
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取菜单名称
     *
     * @return menu_name - 菜单名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置菜单名称
     *
     * @param menuName 菜单名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * 获取父菜单ID
     *
     * @return parent_id - 父菜单ID
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父菜单ID
     *
     * @param parentId 父菜单ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取显示顺序
     *
     * @return order_num - 显示顺序
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * 设置显示顺序
     *
     * @param orderNum 显示顺序
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取路由地址
     *
     * @return path - 路由地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置路由地址
     *
     * @param path 路由地址
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取组件路径
     *
     * @return component - 组件路径
     */
    public String getComponent() {
        return component;
    }

    /**
     * 设置组件路径
     *
     * @param component 组件路径
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 获取是否为外链（0是 1否）
     *
     * @return is_frame - 是否为外链（0是 1否）
     */
    public Integer getIsFrame() {
        return isFrame;
    }

    /**
     * 设置是否为外链（0是 1否）
     *
     * @param isFrame 是否为外链（0是 1否）
     */
    public void setIsFrame(Integer isFrame) {
        this.isFrame = isFrame;
    }

    /**
     * 获取是否缓存（0缓存 1不缓存）
     *
     * @return is_cache - 是否缓存（0缓存 1不缓存）
     */
    public Integer getIsCache() {
        return isCache;
    }

    /**
     * 设置是否缓存（0缓存 1不缓存）
     *
     * @param isCache 是否缓存（0缓存 1不缓存）
     */
    public void setIsCache(Integer isCache) {
        this.isCache = isCache;
    }

    /**
     * 获取菜单类型（M目录 C菜单 F按钮）
     *
     * @return menu_type - 菜单类型（M目录 C菜单 F按钮）
     */
    public String getMenuType() {
        return menuType;
    }

    /**
     * 设置菜单类型（M目录 C菜单 F按钮）
     *
     * @param menuType 菜单类型（M目录 C菜单 F按钮）
     */
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    /**
     * 获取菜单状态（0显示 1隐藏）
     *
     * @return visible - 菜单状态（0显示 1隐藏）
     */
    public String getVisible() {
        return visible;
    }

    /**
     * 设置菜单状态（0显示 1隐藏）
     *
     * @param visible 菜单状态（0显示 1隐藏）
     */
    public void setVisible(String visible) {
        this.visible = visible;
    }

    /**
     * 获取菜单状态（0正常 1停用）
     *
     * @return status - 菜单状态（0正常 1停用）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置菜单状态（0正常 1停用）
     *
     * @param status 菜单状态（0正常 1停用）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取权限标识
     *
     * @return perms - 权限标识
     */
    public String getPerms() {
        return perms;
    }

    /**
     * 设置权限标识
     *
     * @param perms 权限标识
     */
    public void setPerms(String perms) {
        this.perms = perms;
    }

    /**
     * 获取访问接口路径
     *
     * @return uri - 访问接口路径
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置访问接口路径
     *
     * @param uri 访问接口路径
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 获取菜单图标
     *
     * @return icon - 菜单图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置菜单图标
     *
     * @param icon 菜单图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取创建者
     *
     * @return create_by - 创建者
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建者
     *
     * @param createBy 创建者
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
     * 获取更新者
     *
     * @return update_by - 更新者
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新者
     *
     * @param updateBy 更新者
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
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}