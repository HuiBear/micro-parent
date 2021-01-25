package com.micro.ykh.fwtuser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.ykh.dao.entity.generator.GenTableColumn;
import com.micro.ykh.dao.generator.GenTableColumnMapper;
import com.micro.ykh.fwtuser.service.GenTableColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GenTableColumnServiceImpl
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/28 14:11
 * @Version 1.0
 **/
@Service
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumn> implements GenTableColumnService {

    @Override
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId) {
        return this.baseMapper.selectGenTableColumnListByTableId(tableId);
    }
}
