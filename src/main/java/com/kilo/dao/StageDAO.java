
package com.kilo.dao;

import java.util.List;

import com.kilo.domain.MotleyObject;
import com.kilo.domain.StageResult;

public interface StageDAO {

    public static final int batchSize = 10000;

    public static final String BULK_INSERT_ROW_SEPARATOR = "\r\n";

    /**
     * Stages the given records using the given template table in the template
     * DB
     * 
     * @param records
     *            given records
     * @param templateDB
     * @param templateTable
     * @return stage result containing details of where the data was staged
     */
    StageResult stage(List<MotleyObject> records, String templateDB,
            String templateTable);

    void dropStageTable(StageResult stageResult);
}
