package org.softwire.training.db.daos.searchcriteria;

import java.util.Collections;
import java.util.Map;

public class ReportIdSearchCriterion extends ReportSearchCriterion{

    private static final String REPORT_ID_BINDING_NAME = "report_id_sc_report_id";
    private final int reportId;

    public ReportIdSearchCriterion(int reportId) {
        this.reportId = reportId;
    }

    @Override
    public String getSqlForWhereClause() {
        return "report_id = :" + REPORT_ID_BINDING_NAME;
    }

    @Override
    public Map<String, Object> getBindingsForSql() {
        return Collections.singletonMap(REPORT_ID_BINDING_NAME, reportId);
    }
}
