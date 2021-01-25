package com.micro.ykh.fwtuser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.ykh.constant.GenConstants;

import com.micro.ykh.dao.entity.generator.GenTable;
import com.micro.ykh.dao.entity.generator.GenTableColumn;
import com.micro.ykh.dao.generator.GenTableColumnMapper;
import com.micro.ykh.dao.generator.GenTableMapper;

import com.micro.ykh.fwtuser.service.GenTableService;
import com.micro.ykh.fwtuser.utils.GenUtils;
import com.micro.ykh.utils.text.Convert;
import com.micro.ykh.utils.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName GenTableServiceImpl
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/28 13:45
 * @Version 1.0
 **/
@Service
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

    @Resource
    private GenTableColumnMapper genTableColumnMapper;

    @Autowired
    private DataSourceTransactionManager txManager;

    @Override
    public List<GenTable> selectGenTable(GenTable genTable) {
        return this.baseMapper.selectGenTableList(genTable);
    }

    @Override
    public GenTable selectGenTableById(Long id) {
        GenTable genTable = this.baseMapper.selectGenTableById(id);
        setTableFromOptions(genTable);
        return genTable;
    }

    @Override
    public GenTable selectGenTableByName(String tableName) {
        return this.baseMapper.selectGenTableByName(tableName);
    }

    @Override
    public List<GenTable> selectDbTableList(GenTable genTable) {
        return this.baseMapper.selectDbTableList(genTable);
    }

    @Override
    public List<GenTable> selectDbTableListByNames(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        return this.baseMapper.selectDbTableListByNames(tableNames);
    }

    @Override
    public Boolean importGenTable(String tables) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        List<GenTable> tableList = this.selectDbTableListByNames(tables);
        String operName = "admin";
        try {
            for (GenTable table : tableList) {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                int row = this.baseMapper.insertGenTable(table);
                if (row > 0) {
                    // 保存列信息
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns) {
                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            txManager.rollback(status);
            return false;
        }
    }

    @Override
    public Boolean updateGenTable(GenTable genTable) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String options = null;
            try {
                options = objectMapper.writeValueAsString(genTable.getParams());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
            genTable.setOptions(options);
            int row = this.baseMapper.updateGenTable(genTable);
            if (row > 0) {
                for (GenTableColumn cenTableColumn : genTable.getColumns()) {
                    genTableColumnMapper.updateGenTableColumn(cenTableColumn);
                }
            }
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            txManager.rollback(status);
            return false;
        }

    }

    @Override
    public Boolean deleteGenTableByIds(Long[] tableIds) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            this.baseMapper.deleteGenTableByIds(tableIds);
            genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            txManager.rollback(status);
            return false;
        }

    }

    @Override
    public Map<String, GenTable> previewCode(Long tableId) {
        Map<String, GenTable> dataMap = new LinkedHashMap<>();
        // 查询表信息
        GenTable table = this.baseMapper.selectGenTableById(tableId);
        // 查询列信息
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);
        dataMap.put("table", table);
        return dataMap;
    }

    /**
     * 同步数据库
     *
     * @param tableName 表名称
     */
    @Override
    public Boolean syncDb(String tableName) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = txManager.getTransaction(def);
        try {
            GenTable table = this.baseMapper.selectGenTableByName(tableName);
            List<GenTableColumn> tableColumns = table.getColumns();
            List<String> tableColumnNames = tableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

            List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
            List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

            dbTableColumns.forEach(column -> {
                if (!tableColumnNames.contains(column.getColumnName())) {
                    GenUtils.initColumnField(column, table);
                    genTableColumnMapper.insertGenTableColumn(column);
                }
            });

            List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(delColumns)) {
                genTableColumnMapper.deleteGenTableColumns(delColumns);
            }
            txManager.commit(status);
            return true;
        } catch (Exception e) {
            txManager.rollback(status);
            return false;
        }

    }

    /**
     * 设置代码生成其他选项值
     *
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        Map<String, String> paramsObj = null;
        try {
            if(!ObjectUtils.isEmpty(genTable.getOptions())){
                paramsObj = objectMapper.readValue(genTable.getOptions(), javaType);
                if (!CollectionUtils.isEmpty(paramsObj)) {
                    String treeCode = paramsObj.get(GenConstants.TREE_CODE);
                    String treeParentCode = paramsObj.get(GenConstants.TREE_PARENT_CODE);
                    String treeName = paramsObj.get(GenConstants.TREE_NAME);
                    String parentMenuId = paramsObj.get(GenConstants.PARENT_MENU_ID);
                    String parentMenuName = paramsObj.get(GenConstants.PARENT_MENU_NAME);

                    genTable.setTreeCode(treeCode);
                    genTable.setTreeParentCode(treeParentCode);
                    genTable.setTreeName(treeName);
                    genTable.setParentMenuId(parentMenuId);
                    genTable.setParentMenuName(parentMenuName);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置主键列信息
     *
     * @param table   业务表信息
     * @param columns 业务字段列表
     */
    public void setPkColumn(GenTable table, List<GenTableColumn> columns) {
        for (GenTableColumn column : columns) {
            if (column.isPk()) {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn())) {
            table.setPkColumn(columns.get(0));
        }
    }
}
