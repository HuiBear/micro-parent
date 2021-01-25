package com.micro.ykh.cltuser.controller;

import com.micro.ykh.cltuser.service.CltUserService;
import com.micro.ykh.dao.entity.cltuser.CltUser;
import com.micro.ykh.dao.entity.cltuser.dto.CltUserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CltUserController
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/19 16:33
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote/cltuser")
public class CltUserController {

    @Autowired
    private CltUserService cltUserService;

    @GetMapping(value = "/list")
    public List<CltUserDto> getCltUserList() {
        return cltUserService.getCltUserDtoList();
    }

    @PostMapping(value = "/save")
    public Boolean saveCltUser(@RequestBody CltUserDto dto) {
        CltUser cltUser = new CltUser();
        BeanUtils.copyProperties(dto, cltUser, CltUser.class);
        return cltUserService.save(cltUser);
    }

    @PutMapping(value = "/edit")
    public Boolean editCltUser(@RequestBody CltUserDto dto) {
        CltUser cltUser = new CltUser();
        BeanUtils.copyProperties(dto, cltUser, CltUser.class);
        return cltUserService.updateById(cltUser);
    }

    @GetMapping(value = "/check_phone")
    public Boolean checkPhone(@RequestParam("phone") String phone) {
        return cltUserService.checkPhone(phone);
    }
}
