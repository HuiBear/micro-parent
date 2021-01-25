package com.micro.ykh.shopuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.dao.entity.shopuser.dto.ShpShopStaffDto;
import com.micro.ykh.type.DelFlagType;
import com.micro.ykh.type.StatusType;
import com.micro.ykh.utils.ListCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.shopuser.ShpShopStaffMapper;
import com.micro.ykh.dao.entity.shopuser.ShpShopStaff;
import com.micro.ykh.shopuser.service.ShpShopStaffService;
import org.springframework.util.ObjectUtils;

@Service
public class ShpShopStaffServiceImpl extends ServiceImpl<ShpShopStaffMapper, ShpShopStaff> implements ShpShopStaffService {

    @Override
    public ShpShopStaff loadShopStaffByPhone(String phone) {
        return this.baseMapper.selectOne(new QueryWrapper<ShpShopStaff>()
                .eq("phone", phone)
                .eq("enable", StatusType.ENABLE.getValue())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue()));
    }

    @Override
    public List<ShpShopStaffDto> getShopStaff() {
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        List<ShpShopStaff> list = this.baseMapper.selectList(new QueryWrapper<ShpShopStaff>()
                .eq("enable", StatusType.ENABLE.getValue())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue()));
        List<ShpShopStaffDto> shpShopStaffDtos = new ArrayList<>();
        listCopyUtils.copyList(list,shpShopStaffDtos,ShpShopStaffDto.class);
        return shpShopStaffDtos;
    }

    @Override
    public Boolean saveShopStaff(ShpShopStaffDto dto) {
        ShpShopStaff shpShopStaff = new ShpShopStaff();
        BeanUtils.copyProperties(dto,shpShopStaff,ShpShopStaff.class);
        return this.save(shpShopStaff);
    }

    @Override
    public Boolean checkPhone(String phone) {
        ShpShopStaff shpShopStaff = loadShopStaffByPhone(phone);
        return ObjectUtils.isEmpty(shpShopStaff);
    }
}
