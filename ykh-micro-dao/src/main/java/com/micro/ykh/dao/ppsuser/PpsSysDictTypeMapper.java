package com.micro.ykh.dao.ppsuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictType;
import com.micro.ykh.dao.entity.ppsuser.PpsSysDictType;

import java.util.List;

public interface PpsSysDictTypeMapper extends BaseMapper<PpsSysDictType> {


    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    List<PpsSysDictType> selectDictTypeAll();
}