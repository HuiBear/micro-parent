package com.micro.ykh.ppsuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictData;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictType;
import com.micro.ykh.dao.entity.ppsuser.PpsSysDictData;
import com.micro.ykh.dao.fwtuser.FwtSysDictDataMapper;
import com.micro.ykh.dao.ppsuser.PpsSysDictDataMapper;
import com.micro.ykh.type.StatusType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.ppsuser.PpsSysDictType;
import com.micro.ykh.dao.ppsuser.PpsSysDictTypeMapper;
import com.micro.ykh.ppsuser.service.PpsSysDictTypeService;

@Service
public class PpsSysDictTypeServiceImpl extends ServiceImpl<PpsSysDictTypeMapper, PpsSysDictType> implements PpsSysDictTypeService {

    @Resource
    private PpsSysDictDataMapper ppsSysDictDataMapper;

    @Override
    public List<PpsSysDictType> getAllDictType() {
        return this.baseMapper.selectDictTypeAll();
    }

    @Override
    public PpsSysDictType selectDictTypeById(Integer dictTypeId) {
        return this.baseMapper.selectById(dictTypeId);
    }

    @Override
    public List<PpsSysDictData> selectDictDataByType(String dictType) {
        return this.ppsSysDictDataMapper.selectList(new QueryWrapper<PpsSysDictData>().eq("dict_type", dictType).eq("status", StatusType.ENABLE.getValue()));
    }


}
