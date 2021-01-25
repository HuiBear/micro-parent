package com.micro.ykh.dao.fwtuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FwtSysMenuMapper extends BaseMapper<FwtSysMenu> {

    List<FwtSysMenu> findByIds(@Param("permissionIds") List<Integer> permissionIds);

    List<FwtSysMenu> selectMenuTreeAll();

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    List<Integer> selectMenuListByRoleId(@Param("roleId") Integer roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    List<String> selectMenuPermsByUserId(@Param("userId") Integer userId);

    List<FwtSysMenu> selectMenuList(FwtSysMenu fwtSysMenu);

}