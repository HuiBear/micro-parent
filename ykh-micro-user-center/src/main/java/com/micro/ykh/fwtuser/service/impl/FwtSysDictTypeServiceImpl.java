package com.micro.ykh.fwtuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.micro.ykh.dao.entity.fwtuser.FwtSysDictData;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictType;
import com.micro.ykh.dao.fwtuser.FwtSysDictDataMapper;
import com.micro.ykh.dao.fwtuser.FwtSysDictTypeMapper;
import com.micro.ykh.type.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.micro.ykh.fwtuser.service.FwtSysDictTypeService;

@Service
public class FwtSysDictTypeServiceImpl extends ServiceImpl<FwtSysDictTypeMapper, FwtSysDictType> implements FwtSysDictTypeService {

    @Resource
    private FwtSysDictDataMapper fwtSysDictDataMapper;

    @Override
    public List<FwtSysDictType> getAllDictType() {
        return this.baseMapper.selectDictTypeAll();
    }

    @Override
    public FwtSysDictType selectDictTypeById(Integer dictTypeId) {
        return this.baseMapper.selectById(dictTypeId);
    }

    @Override
    public List<FwtSysDictData> selectDictDataByType(String dictType) {
        return this.fwtSysDictDataMapper.selectList(new QueryWrapper<FwtSysDictData>().eq("dict_type", dictType).eq("status", StatusType.ENABLE.getValue()));
    }


}
