package com.micro.ykh.shopuser.service;

import com.micro.ykh.dao.entity.shopuser.ShpShopStaff;
import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.shopuser.dto.ShpShopStaffDto;

import java.util.List;

public interface ShpShopStaffService extends IService<ShpShopStaff>{

    /**
     * 根据手机号码查询店铺用户
     * @param phone 电话号码
     * @return ShpShopStaff
     */
    ShpShopStaff loadShopStaffByPhone(String phone);

    List<ShpShopStaffDto> getShopStaff();

    Boolean saveShopStaff(ShpShopStaffDto dto);

    Boolean checkPhone(String phone);


}
