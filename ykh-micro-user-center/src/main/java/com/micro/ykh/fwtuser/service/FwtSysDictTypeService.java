package com.micro.ykh.fwtuser.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictData;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictType;

import java.util.List;

public interface FwtSysDictTypeService extends IService<FwtSysDictType>{

    List<FwtSysDictType> getAllDictType();

    FwtSysDictType selectDictTypeById(Integer dictTypeId);

    List<FwtSysDictData> selectDictDataByType(String dictType);
}
