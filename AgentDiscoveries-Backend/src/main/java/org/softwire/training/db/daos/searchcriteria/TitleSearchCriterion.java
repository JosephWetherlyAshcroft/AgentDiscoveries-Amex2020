package org.softwire.training.db.daos.searchcriteria;

import org.softwire.training.db.daos.LocationReportsDao;
import java.util.Collections;
import java.util.Map;


public final class TitleSearchCriterion extends ReportSearchCriterion {

    private static final String TITLE_BINDING_NAME = "title_sc_title";
    private final String title;

    public TitleSearchCriterion(String title) {
        this.title = title;
    }
    @Override
    public String getSqlForWhereClause() { return("title LIKE : title");
    }
    @Override
    public Map<String, Object> getBindingsForSql() {
        return Collections.singletonMap("title", "%"+title+"%");
    }
}
