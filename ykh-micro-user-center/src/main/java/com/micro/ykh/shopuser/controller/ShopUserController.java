package com.micro.ykh.shopuser.controller;

import com.micro.ykh.dao.entity.shopuser.ShpShopStaff;
import com.micro.ykh.dao.entity.shopuser.dto.ShpShopStaffDto;
import com.micro.ykh.shopuser.service.ShpShopStaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ShopUserController
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/19 15:12
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote/shopuser")
public class ShopUserController {

    @Autowired
    private ShpShopStaffService shpShopStaffService;

    @GetMapping(value = "/list")
    public List<ShpShopStaffDto> getShopStaff() {
        return shpShopStaffService.getShopStaff();
    }

    @PostMapping(value = "/save")
    public Boolean saveShopStaff(@RequestBody ShpShopStaffDto dto) {
        return shpShopStaffService.saveShopStaff(dto);
    }

    @PutMapping(value = "/edit")
    public Boolean editShopStaff(@RequestBody ShpShopStaffDto dto) {
        ShpShopStaff shpShopStaff = new ShpShopStaff();
        BeanUtils.copyProperties(dto, shpShopStaff, ShpShopStaff.class);
        return shpShopStaffService.updateById(shpShopStaff);
    }

    @GetMapping(value = "/check_phone")
    public Boolean checkPhone(@RequestParam("phone") String phone) {
        return shpShopStaffService.checkPhone(phone);
    }

    @GetMapping(value = "/shp_shop_staff_info")
    public ShpShopStaffDto getShpShopStaffDto(@RequestParam Integer userId){
        ShpShopStaff shpShopStaff = this.shpShopStaffService.getById(userId);
        ShpShopStaffDto dto = new ShpShopStaffDto();
        BeanUtils.copyProperties(shpShopStaff,dto,ShpShopStaffDto.class);
        return dto;
    }






}
