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

    //searchReports original
    //public List<LocationStatusReport> searchReports(List<ReportSearchCriterion> searchCriteria) {
    //    EntityManager em = entityManagerFactory.createEntityManager();
    //    em.getTransaction().begin();
    //
    //    System.out.println("searchCriteria: "+searchCriteria);
    //    String whereClause = ReportsDaoUtils.buildWhereSubClauseFromCriteria(searchCriteria);
    //    //String whereClause = ReportsDaoUtils.;
    //
    //    //ALTERNATIVE SOLUTION: Use 2 queries: 1)find agent id using Call sign  2) add agent id to "where criteria"
    //
    //    TypedQuery<LocationStatusReport> query = em.createQuery("FROM LocationStatusReport " + whereClause, LocationStatusReport.class);
    //    //TypedQuery<LocationStatusReport> query = em.createQuery("SELECT r FROM LocationStatusReport r" + whereClause, LocationStatusReport.class);
    //    //TypedQuery<LocationStatusReport> query = em.createQuery("SELECT r FROM LocationStatusReport r JOIN r.agentId" + whereClause, LocationStatusReport.class);
    //
    //    for (ReportSearchCriterion criterion : searchCriteria) {
    //        for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
    //            System.out.println("A: "+bindingEntry.getKey());
    //            System.out.println("B: " +bindingEntry.getValue());
    //            query = query.setParameter(bindingEntry.getKey(), bindingEntry.getValue());
    //        }
    //    }
    //    //System.out.print("Miro query: "); query.getParameters().forEach(p-> System.out.println(p.getName()));
    //    List<LocationStatusReport> results = query.getResultList();
    //
    //    em.getTransaction().commit();
    //    em.close();
    //
    //    return results;
    //}

    public List<LocationStatusReport> searchReports(List<ReportSearchCriterion> searchCriteria) {
        implementAgentCall_Sign(searchCriteria);
        EntityManager em = entityManagerFactory.createEntityManager();
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

    //implementing stream (and for loop) - final solution(s)
    private void implementAgentCall_Sign(List<ReportSearchCriterion> searchCriteria) {
        String agentCallSign = "";
        ReportSearchCriterion searchCriterionToRemove = null;
        for (ReportSearchCriterion criterion : searchCriteria) {
            for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
                if (bindingEntry.getKey().equals("call_sign_sc_call_sign")) {
                    agentCallSign = (String) bindingEntry.getValue();
                    searchCriterionToRemove = criterion;
                }
            }
        }
        if (agentCallSign.length() > 0) {
            searchCriteria.remove(searchCriterionToRemove);

            //////using stream
            final String AGENT_CALL_SIGN = agentCallSign;
            int agentId = helperAgent
                            .getEntities(Agent.class)
                            .stream()
                            .filter(a -> a.getCallSign().equals(AGENT_CALL_SIGN))
                            .map(agent -> agent.getAgentId())
                            .findFirst()
                            .orElse(-1);


            //using query to get single agent_id;
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


            System.out.println(agentId);
            searchCriteria.add(new AgentIdSearchCriterion(agentId));
        }
    }

    //implementing query receiving single value instead of all agents
    //private void implementAgentCall_Sign(List<ReportSearchCriterion> searchCriteria) {
    //    String agentCallSign = "";
    //    ReportSearchCriterion searchCriterionToRemove = null;
    //    for (ReportSearchCriterion criterion : searchCriteria) {
    //        for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
    //            if (bindingEntry.getKey().equals("call_sign_sc_call_sign")) {
    //                agentCallSign = (String) bindingEntry.getValue();
    //                searchCriterionToRemove = criterion;
    //            }
    //        }
    //    }
    //    if (agentCallSign.length() > 0) {
    //        searchCriteria.remove(searchCriterionToRemove);
    //
    //
    //        List<ReportSearchCriterion> agentSearchCriteria = new ArrayList<>();
    //        agentSearchCriteria.add(new AgentCallSignSearchCriterion(agentCallSign));
    //
    //        EntityManager em2 = entityManagerFactory.createEntityManager();
    //        em2.getTransaction().begin();
    //        String whereClause = ReportsDaoUtils.buildWhereSubClauseFromCriteria(agentSearchCriteria);
    //        TypedQuery<Agent> query = em2.createQuery("FROM Agent" + whereClause, Agent.class);
    //        for (ReportSearchCriterion criterion : agentSearchCriteria) {
    //            for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
    //                query = query.setParameter(bindingEntry.getKey(), bindingEntry.getValue());
    //            }
    //        }
    //        Optional agentOrEmpty = query.getResultStream().map(a->a.getAgentId()).findFirst();
    //        int agentId = agentOrEmpty.isPresent() ? (int)agentOrEmpty.get() : -1;
    //        System.out.println(agentId);
    //        em2.getTransaction().commit();
    //        em2.close();
    //
    //        searchCriteria.add(new AgentIdSearchCriterion(agentId));
    //    }
    //}

}
