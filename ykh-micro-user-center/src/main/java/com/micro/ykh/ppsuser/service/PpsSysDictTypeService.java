package com.micro.ykh.ppsuser.service;

import com.micro.ykh.dao.entity.ppsuser.PpsSysDictData;
import com.micro.ykh.dao.entity.ppsuser.PpsSysDictType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PpsSysDictTypeService extends IService<PpsSysDictType>{

    List<PpsSysDictType> getAllDictType();

    PpsSysDictType selectDictTypeById(Integer dictTypeId);

    List<PpsSysDictData> selectDictDataByType(String dictType);
}
