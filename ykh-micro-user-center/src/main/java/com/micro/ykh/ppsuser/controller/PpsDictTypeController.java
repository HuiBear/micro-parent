package com.micro.ykh.ppsuser.controller;


import com.micro.ykh.dao.entity.ppsuser.PpsSysDictData;
import com.micro.ykh.dao.entity.ppsuser.PpsSysDictType;
import com.micro.ykh.ppsuser.service.PpsSysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName PpsDictTypeController
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/12 10:23
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote/pps")
public class PpsDictTypeController {


    @Autowired
    private PpsSysDictTypeService ppsSysDictTypeService;

    @GetMapping(value = "/select_dicttype_by_id")
    public PpsSysDictType selectDictTypeById(@RequestParam("dictTypeId") Integer dictTypeId) {
        return ppsSysDictTypeService.selectDictTypeById(dictTypeId);
    }

    @GetMapping(value = "/all_dict_type")
    public List<PpsSysDictType> getAllDictType() {
        return ppsSysDictTypeService.getAllDictType();
    }

    @GetMapping(value = "/select_dict_data_by_type")
    public List<PpsSysDictData> selectDictDataByType(@RequestParam("dictType") String dictType) {
        return ppsSysDictTypeService.selectDictDataByType(dictType);
    }
}
