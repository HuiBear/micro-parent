package com.micro.ykh.dao.entity.ppsuser.dto;

/**
 * @ClassName PpsRoleDto
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/11 17:24
 * @Version 1.0
 **/
public class PpsRoleDto {

    private String roleName;
    private Integer status;
    private String roleCode;
    private String beginTime;
    private String endTime;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "PpsRoleDto{" +
                "roleName='" + roleName + '\'' +
                ", status=" + status +
                ", roleCode='" + roleCode + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
