package com.micro.ykh.cltuser.service;

import com.micro.ykh.dao.entity.cltuser.CltUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.cltuser.dto.CltUserDto;

import java.util.List;

public interface CltUserService extends IService<CltUser>{

    /**
     * 根据电话号码查询用户
     * @param phone
     * @return
     */
    CltUser loadCltUserByPhone(String phone);

    List<CltUserDto> getCltUserDtoList();

    Boolean checkPhone(String phone);

}
