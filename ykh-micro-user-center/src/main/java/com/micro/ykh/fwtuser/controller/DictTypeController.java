package com.micro.ykh.fwtuser.controller;


import com.micro.ykh.dao.entity.fwtuser.FwtSysDictData;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictType;
import com.micro.ykh.fwtuser.service.FwtSysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DictController
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/29 15:12
 * @Version 1.0
 **/
@RestController
@RequestMapping("/remote")
public class DictTypeController {

    @Autowired
    private FwtSysDictTypeService fwtSysDictTypeService;

    @GetMapping(value = "/select_dicttype_by_id")
    public FwtSysDictType selectDictTypeById(@RequestParam("dictTypeId") Integer dictTypeId) {
        return fwtSysDictTypeService.selectDictTypeById(dictTypeId);
    }

    @GetMapping(value = "/all_dict_type")
    public List<FwtSysDictType> getAllDictType() {
        return fwtSysDictTypeService.getAllDictType();
    }

    @GetMapping(value = "/select_dict_data_by_type")
    public List<FwtSysDictData> selectDictDataByType(@RequestParam("dictType") String dictType) {
        return fwtSysDictTypeService.selectDictDataByType(dictType);
    }
}
