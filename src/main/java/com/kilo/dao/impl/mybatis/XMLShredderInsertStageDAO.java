
package com.kilo.dao.impl.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.kilo.dao.StageDAO;
import com.kilo.dao.StageUtils;
import com.kilo.domain.MotleyObject;
import com.kilo.domain.StageResult;

public class XMLShredderInsertStageDAO extends SqlSessionDaoSupport implements
        StageDAO {

    @Override
    @Transactional
    public StageResult stage(List<MotleyObject> records, String templateDB,
            String templateTable) {
        // Create the table from the template
        String stageTableName = StageUtils.getStageTableName(templateTable);

        Map<String, Object> stageTableCreationParamMap = new HashMap<>();
        stageTableCreationParamMap.put("templateDB", templateDB);
        stageTableCreationParamMap.put("templateTable", templateTable);
        stageTableCreationParamMap.put("stageTableName", stageTableName);
        getSqlSession().insert(
                "com.kilo.dao.mybatis.mapper.Motley.createStageTable",
                stageTableCreationParamMap);

        StringBuilder content = new StringBuilder();
        for (MotleyObject rec : records) {
            content.append(rec.toXMLInsertString());
        }

        // Use T-SQL to bulk XML insert
        Map<String, Object> stageParamMap = new HashMap<>();
        stageParamMap.put("stageTableName", stageTableName);
        stageParamMap.put("xml", content.toString());
        getSqlSession().insert(
                "com.kilo.dao.mybatis.mapper.Motley.xmlInsertStage",
                stageParamMap);

        StageResult result = new StageResult();
        result.setDbName(templateDB);
        result.setTableName(stageTableName);
        return result;
    }

    @Override
    public void dropStageTable(StageResult stageResult) {
        Map<String, Object> stageParamMap = new HashMap<>();
        stageParamMap.put("stageDBName", stageResult.getDbName());
        stageParamMap.put("stageTableName", stageResult.getTableName());
        getSqlSession().delete(
                "com.kilo.dao.mybatis.mapper.Motley.insertStageDrop",
                stageParamMap);
    }

}
