package com.micro.ykh.fwtuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.ykh.dao.entity.generator.GenTable;

import java.util.List;
import java.util.Map;

public interface GenTableService extends IService<GenTable> {

    List<GenTable> selectGenTable(GenTable genTable);

    GenTable selectGenTableById(Long id);

    GenTable selectGenTableByName(String tableName);

    List<GenTable> selectDbTableList(GenTable genTable);

    List<GenTable> selectDbTableListByNames(String tables);

    Boolean importGenTable(String tables);

    Boolean updateGenTable(GenTable genTable);

    Boolean deleteGenTableByIds(Long[] tableIds);

    Map<String,GenTable> previewCode(Long tableId);

    Boolean syncDb(String tableName);

}
