package com.micro.ykh.dao.fwtuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.ykh.dao.entity.fwtuser.FwtSysDictType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FwtSysDictTypeMapper extends BaseMapper<FwtSysDictType> {

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    List<FwtSysDictType> selectDictTypeAll();

}