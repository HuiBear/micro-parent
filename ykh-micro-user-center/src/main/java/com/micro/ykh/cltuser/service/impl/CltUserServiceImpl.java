package com.micro.ykh.cltuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.dao.entity.cltuser.dto.CltUserDto;
import com.micro.ykh.dao.entity.shopuser.ShpShopStaff;
import com.micro.ykh.type.DelFlagType;
import com.micro.ykh.type.StatusType;
import com.micro.ykh.utils.ListCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.cltuser.CltUser;
import com.micro.ykh.dao.cltuser.CltUserMapper;
import com.micro.ykh.cltuser.service.CltUserService;
import org.springframework.util.ObjectUtils;

@Service
public class CltUserServiceImpl extends ServiceImpl<CltUserMapper, CltUser> implements CltUserService {

    @Override
    public CltUser loadCltUserByPhone(String phone) {
        return this.baseMapper.selectOne(new QueryWrapper<CltUser>()
                .eq("phone", phone)
                .eq("enable", StatusType.ENABLE.getValue())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue()));
    }

    @Override
    public List<CltUserDto> getCltUserDtoList() {
        List<CltUser> list = this.baseMapper.selectList(new QueryWrapper<CltUser>()
                .eq("enable", StatusType.ENABLE.getValue())
                .eq("del_flag", DelFlagType.UN_DELETED.getValue()));
        List<CltUserDto> cltUserDtos = new ArrayList<>();
        ListCopyUtils listCopyUtils = BeanUtils.instantiateClass(ListCopyUtils.class);
        listCopyUtils.copyList(list, cltUserDtos, CltUserDto.class);
        return cltUserDtos;
    }

    @Override
    public Boolean checkPhone(String phone) {
        CltUser cltUser = loadCltUserByPhone(phone);
        return ObjectUtils.isEmpty(cltUser);
    }
}
