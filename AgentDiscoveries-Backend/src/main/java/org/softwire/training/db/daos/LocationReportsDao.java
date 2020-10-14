package org.softwire.training.db.daos;

import org.softwire.training.db.daos.searchcriteria.AgentCallSignSearchCriterion;
import org.softwire.training.db.daos.searchcriteria.AgentIdSearchCriterion;
import org.softwire.training.db.daos.searchcriteria.ReportSearchCriterion;
import org.softwire.training.models.Agent;
import org.softwire.training.models.LocationStatusReport;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LocationReportsDao implements ReportsDao<LocationStatusReport> {

    private EntityManagerFactory entityManagerFactory;
    private DaoHelper<LocationStatusReport> helper;
    private DaoHelper<Agent> helperAgent;

    @Inject
    public LocationReportsDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.helper = new DaoHelper<>(entityManagerFactory);
        this.helperAgent = new DaoHelper<>(entityManagerFactory);
    }

    public Optional<LocationStatusReport> getReport(int reportId) {
        return helper.getEntity(LocationStatusReport.class, reportId);
    }

    public int createReport(LocationStatusReport report) {
        helper.createEntity(report);
        return report.getReportId();
    }

    public void deleteReport(int reportId) {
        helper.deleteEntity(LocationStatusReport.class, reportId);
    }

    public List<LocationStatusReport> searchReports(List<ReportSearchCriterion> searchCriteria) {



        EntityManager em = entityManagerFactory.createEntityManager();

        System.out.println("ahoj");

        implementAgentCall_Sign(searchCriteria);
        //int[] resultsRange = extractPagination(searchCriteria);
        //no error registered

        em.getTransaction().begin();
        String whereClause = ReportsDaoUtils.buildWhereSubClauseFromCriteria(searchCriteria);
        TypedQuery<LocationStatusReport> query = em.createQuery("FROM LocationStatusReport" + whereClause, LocationStatusReport.class);
        for (ReportSearchCriterion criterion : searchCriteria) {
            for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
                query = query.setParameter(bindingEntry.getKey(), bindingEntry.getValue());
            }
        }
        List<LocationStatusReport> results = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return results;
    }

    private void implementAgentCall_Sign(List<ReportSearchCriterion> searchCriteria) {
        String agentCallSign = "";
        ReportSearchCriterion searchCriterionToRemove = null;
        for (ReportSearchCriterion criterion : searchCriteria) {
            for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
                //System.out.println(bindingEntry.getKey());
                //System.out.println(bindingEntry.getValue());
                if (bindingEntry.getKey().equals("call_sign_sc_call_sign")) {
                    agentCallSign = (String) bindingEntry.getValue();
                    searchCriterionToRemove = criterion;
                }
            }
        }
        if (agentCallSign.length() > 0) {
            searchCriteria.remove(searchCriterionToRemove);
            List<ReportSearchCriterion> agentSearchCriteria = new ArrayList<>();
            agentSearchCriteria.add(new AgentCallSignSearchCriterion(agentCallSign));

            EntityManager emForAgentQuery = entityManagerFactory.createEntityManager();
            emForAgentQuery.getTransaction().begin();

            String whereClause = ReportsDaoUtils.buildWhereSubClauseFromCriteria(agentSearchCriteria);
            TypedQuery<Agent> query = emForAgentQuery.createQuery("FROM Agent" + whereClause, Agent.class);

            for (ReportSearchCriterion criterion : agentSearchCriteria) {
                for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
                    query = query.setParameter(bindingEntry.getKey(), bindingEntry.getValue());
                }
            }

            Optional agentOrEmpty = query.getResultStream().map(a->a.getAgentId()).findFirst();
            int agentId = agentOrEmpty.isPresent() ? (int)agentOrEmpty.get() : -1;

            emForAgentQuery.getTransaction().commit();
            emForAgentQuery.close();
            searchCriteria.add(new AgentIdSearchCriterion(agentId));
        }
    }
}
