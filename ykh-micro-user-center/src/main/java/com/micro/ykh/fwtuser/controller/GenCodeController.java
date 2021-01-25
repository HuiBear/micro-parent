package com.micro.ykh.fwtuser.controller;

import com.micro.ykh.dao.entity.generator.GenTable;
import com.micro.ykh.dao.entity.generator.GenTableColumn;
import com.micro.ykh.fwtuser.service.GenTableColumnService;
import com.micro.ykh.fwtuser.service.GenTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenCodeController
 * @Description 代码生成
 * @Author xiongh
 * @Date 2020/12/28 11:31
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/remote")
public class GenCodeController {

    @Autowired
    private GenTableService genTableService;

    @Autowired
    private GenTableColumnService genTableColumnService;


    @PostMapping(value = "/list")
    public List<GenTable> selectGenTableList(@RequestBody GenTable genTable) {
        return genTableService.selectGenTable(genTable);
    }

    @GetMapping(value = "/select_table_by_name")
    public GenTable selectTableByName(@RequestParam("tableName") String tableName) {
        return genTableService.selectGenTableByName(tableName);
    }

    @GetMapping(value = "/gen_table_info")
    public Map<String, Object> getTableInfo(@RequestParam("tableId") Long tableId) {
        GenTable genTable = genTableService.selectGenTableById(tableId);
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<>();
        map.put("info", genTable);
        map.put("rows", list);
        return map;
    }

    @PostMapping(value = "/data_list")
    public List<GenTable> dataList(@RequestBody GenTable genTable) {
        return genTableService.selectDbTableList(genTable);
    }

    @GetMapping(value = "/gen_table_column_list")
    public List<GenTableColumn> genTableColumns(@RequestParam("tableId") Long tableId) {
        return genTableColumnService.selectGenTableColumnListByTableId(tableId);
    }

    @GetMapping(value = "/save_table_info")
    public Boolean saveTableInfo(@RequestParam("tables") String tables) {
        return genTableService.importGenTable(tables);
    }

    @PutMapping(value = "/table_info")
    public Boolean editTableInfo(@RequestBody GenTable genTable) {
        return genTableService.updateGenTable(genTable);
    }

    @DeleteMapping(value = "/table_info")
    public Boolean removeTableInfo(@RequestBody Long[] tableIds) {
        return genTableService.deleteGenTableByIds(tableIds);
    }

    @GetMapping(value = "/preview")
    public Map<String, GenTable> preview(@RequestParam("tableId") Long tableId) {
        return genTableService.previewCode(tableId);
    }

    @GetMapping(value = "/sync_db")
    public Boolean syncDb(@RequestParam("tableName") String tableName) {
        return genTableService.syncDb(tableName);
    }


}
