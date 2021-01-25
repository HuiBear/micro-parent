package com.micro.ykh.fwtuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.generator.GenTableColumn;

import java.util.List;

public interface GenTableColumnService extends IService<GenTableColumn> {

    List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId);

}
