package org.softwire.training.db.daos.searchcriteria;

import java.util.Collections;
import java.util.Map;

public final class ResultsRangeSearchCriterion extends ReportSearchCriterion {

    private static final String RESULTS_RANGE_BINDING_NAME = "results_range_sc_results_range";
    private final String resultsRange;

    public ResultsRangeSearchCriterion(String resultsRange) {
        this.resultsRange = resultsRange;
    }

    @Override
    public String getSqlForWhereClause() {
        return "results_range = :" + RESULTS_RANGE_BINDING_NAME;
    }

    @Override
    public Map<String, Object> getBindingsForSql() {
        return Collections.singletonMap(RESULTS_RANGE_BINDING_NAME, resultsRange);
    }
}
