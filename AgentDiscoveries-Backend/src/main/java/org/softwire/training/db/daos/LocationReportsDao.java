package org.softwire.training.db.daos;

import org.softwire.training.db.daos.searchcriteria.AgentIdSearchCriterion;
import org.softwire.training.db.daos.searchcriteria.ReportSearchCriterion;
import org.softwire.training.models.Agent;
import org.softwire.training.models.LocationStatusReport;
import org.softwire.training.models.LocationStatusReport2;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalTime;
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
        //List <LocationStatusReport> reports = helper.getEntities(LocationStatusReport.class);
        //reports.forEach(r-> System.out.println(r.toString()));

        ///////MIRO 2 queries section
        //String agentCallSign = "";
        //ReportSearchCriterion searchCriterionToRemove = null;
        //for (ReportSearchCriterion criterion : searchCriteria) {
        //
        //    for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
        //        //System.out.println("A: "+bindingEntry.getKey());
        //        //System.out.println("B: " +bindingEntry.getValue());
        //        if (bindingEntry.getKey().equals("call_sign_sc_call_sign")) {
        //            agentCallSign = (String) bindingEntry.getValue();
        //            searchCriterionToRemove = criterion;
        //        }
        //    }
        //}
        ////System.out.println("Agent wanted: "+agentCallSign);
        //if (agentCallSign.length() > 0) {
        //    searchCriteria.remove(searchCriterionToRemove);
        //    List<Agent> agents = helperAgent.getEntities(Agent.class);
        //    int agentId = -1;
        //    for (int i = 0; i < agents.size(); i++) {
        //        if (agents.get(i).getCallSign().equals(agentCallSign)) agentId = agents.get(i).getAgentId();
        //    }
        //    if (agentId>=0) searchCriteria.add(new AgentIdSearchCriterion(agentId));
        //    System.out.println("agent id from given call sign: " + agentCallSign + " " + agentId);
        //}
        ////////END OF "S I M P L E - 2queries" implementation

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        System.out.println("searchCriteria2: " + searchCriteria);
        String whereClause = ReportsDaoUtils.buildWhereSubClauseFromCriteria(searchCriteria);
        //String whereClause = ReportsDaoUtils.;


        //TypedQuery<LocationStatusReport> query = em.createQuery("FROM LocationStatusReport" + whereClause, LocationStatusReport.class);
        TypedQuery<LocationStatusReport2> query = em.createQuery("FROM LocationStatusReport2" + whereClause, LocationStatusReport2.class);
        //TypedQuery<LocationStatusReport> query = em.createQuery("SELECT r FROM LocationStatusReport r" + whereClause, LocationStatusReport.class);
        //TypedQuery<LocationStatusReport> query = em.createQuery("SELECT r FROM LocationStatusReport r JOIN r.agentId" + whereClause, LocationStatusReport.class);

        for (ReportSearchCriterion criterion : searchCriteria) {
            for (Map.Entry<String, Object> bindingEntry : criterion.getBindingsForSql().entrySet()) {
                System.out.println("A: " + bindingEntry.getKey());
                System.out.println("B: " + bindingEntry.getValue());
                query = query.setParameter(bindingEntry.getKey(), bindingEntry.getValue());
            }
        }
        //System.out.print("Miro query: "); query.getParameters().forEach(p-> System.out.println(p.getName()));
        List<LocationStatusReport2> results = query.getResultList();
        //List<LocationStatusReport> results = query.getResultList();
        results.forEach(System.out::println);

        em.getTransaction().commit();
        em.close();

        List<LocationStatusReport> results2 = new ArrayList<>();
        //results.forEach(r->results2.add((LocationStatusReport)r));
        //LocationStatusReport2 r1 = results.get(0);
        //LocationStatusReport r2 = (LocationStatusReport)r1;




        //return results;
        return results2;
    }
}
