/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.DocumentControlEntity;
import entity.EmployeeEntity;
import entity.MachineEntity;
import entity.Project;
import entity.WeldJob;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author: jitcheong
 */
@Stateless
public class ProjectSessionBean implements ProjectSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    public void saveWeldJob(WeldJob job) {
        Integer id = job.getWeldJobId();
        System.out.println(id);
        try {
            WeldJob job1 = new WeldJob();
            Query q = em.createQuery("select job1 from WeldJob job1 where job1.weldJobId=:id ");
            q.setParameter("id", id);
            job1 = (WeldJob) q.getSingleResult();
            job1.setDuration(job.getDuration());
            em.merge(job1);
        } catch (Exception ex) {

        }
    }

    @Override
    public void createProject(Project project) {
        //initialise document entity for new project
        DocumentControlEntity d = new DocumentControlEntity();
        em.persist(d);
        project.setDocuments(d);
        //create project
        em.persist(project);
    }

    @Override
    public void createWeldJob(WeldJob weldJob) {
        em.persist(weldJob);
    }

    @Override
    public void conductWeldJobMerge(WeldJob weldJob) {
        em.merge(weldJob);
    }

    @Override
    public void conductProjectMerge(Project p) {
        em.merge(p);
    }

    @Override
    public void conductMachineMerge(MachineEntity m) {
        em.merge(m);
    }

    @Override
    public void conductEmployeeMerge(EmployeeEntity e) {
        em.merge(e);
    }

    public void setMachineAvailability(String number, String status) {
        MachineEntity machine = null;
        try {
            Query q = em.createQuery("select machine from MachineEntity machine where machine.machine_number =:id");
            q.setParameter("id", number);
            machine = (MachineEntity) q.getSingleResult();
            machine.setStatus(status);
            em.merge(machine);
        } catch (Exception ex) {

        }
    }

    @Override
    public void setEmployeeAvailability(String name, Boolean availiability) {
        EmployeeEntity e = null;
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name = :name ");
            q.setParameter("name", name);

            e = (EmployeeEntity) q.getSingleResult();
            e.setAvailability(availiability);
            em.merge(e);

        } catch (Exception ex) {

        }
    }

    //return null if there is no available employee
    @Override
    public EmployeeEntity getAvailableEmployee() {
        Query query = em.createQuery("Select e FROM EmployeeEntity AS e where e.availability=true AND e.employee_account_status=:status");
        query.setParameter("status", "staff");
        List<EmployeeEntity> employees = query.getResultList();
        if (employees.isEmpty()) {
            return null;
        } else {
            return employees.get(0);
        }
    }

    //return null if there is no available employee
    @Override
    public List<EmployeeEntity> getAllEmployees() {
        Query query = em.createQuery("Select e FROM EmployeeEntity AS e where e.employee_account_status=:status");
        query.setParameter("status", "staff");
        List<EmployeeEntity> employees = query.getResultList();
        if (employees.isEmpty()) {
            return null;
        } else {
            return employees;
        }
    }

    //return null if there is no available machine
    @Override
    public MachineEntity getAvailableMachine(String machineType) {
        Query query = em.createQuery("Select m FROM MachineEntity AS m where m.status='Available' AND m.machine_type=:machineType");
        query.setParameter("machineType", machineType);

        List<MachineEntity> machines = query.getResultList();
        if (machines.isEmpty()) {
            return null; //no available machine
        } else {
            return machines.get(0);
        }
    }

    //get duration (days) from past weldJobs of same nature
    //return -1 if there is no similar weldJobs
    @Override
    public Integer getSimilarPastProjectDuration(String metal1, String metal2) {
        System.out.println("getSimilarPastProjectDuration: Start");
        Integer days = -1;
        //find weldJobs with two similar metals for welding 
        Query query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND "
                + "( (w.metal1=:metal1 OR w.metal1=:metal2) AND (w.metal2=:metal1 OR w.metal2=:metal2) ) ");
        query.setParameter("metal1", metal1);
        query.setParameter("metal2", metal2);

        List<WeldJob> weldJobs = query.getResultList();
        //if not, find weldJobs with one similar metal for welding
        if (weldJobs.isEmpty()) {
            query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND "
                    + "( (w.metal1=:metal1 OR w.metal1=:metal2) OR (w.metal2=:metal1 OR w.metal2=:metal2) ) ");

            query.setParameter("metal1", metal1);
            query.setParameter("metal2", metal2);

            weldJobs = query.getResultList();
        }
        int avgDays = 0;
        System.out.println("Size of projects: " + weldJobs.size());
        //if there are references to such weldJobs, we get the longest duration among these weldJobs
        if (!weldJobs.isEmpty()) {
            //get the average duration from these weldJobs (ActualEnd - ActualStart)
            days = 0;
            for (int i = 0; i < weldJobs.size(); i++) {
                //if (days == -1) {
                //    days = getDifferenceDays(p.getActualStart(), p.getActualEnd());
                //}
                //if (days < getDifferenceDays(p.getActualStart(), p.getActualEnd())) {
                //    days = getDifferenceDays(p.getActualStart(), p.getActualEnd());
                //}
                days += getDifferenceDays(weldJobs.get(i).getProject().getActualStart(), weldJobs.get(i).getProject().getActualEnd());
            }
        }
        if (days != -1 && !weldJobs.isEmpty()) {
            avgDays = days / weldJobs.size();
        }
        System.out.println("Average days is " + avgDays);
        return avgDays;
    }

    private double roundUp(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.CEILING);
        return bd.doubleValue();
    }

    @Override
    public List<WeldJob> getSimilarPastProjects(String metal1, String metal2, String weldingType) {
        System.out.println("getSimilarPastProjectDuration: Start");
        Integer days = -1;
        //find weldJobs with two similar metals for welding 
        Query query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND "
                + "( (w.metal1=:metal1 OR w.metal1=:metal2) AND (w.metal2=:metal1 OR w.metal2=:metal2) AND (w.weldingType = :weldingType) ) ");
        query.setParameter("metal1", metal1);
        query.setParameter("metal2", metal2);
        query.setParameter("weldingType", weldingType);

        List<WeldJob> weldJobs = query.getResultList();
        //if not, find weldJobs with one similar metal for welding
        if (weldJobs.isEmpty()) {
            query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND (w.weldingType = :weldingType) AND "
                    + "( (w.metal1=:metal1 OR w.metal1=:metal2) OR (w.metal2=:metal1 OR w.metal2=:metal2) ) ");

            query.setParameter("metal1", metal1);
            query.setParameter("metal2", metal2);
            query.setParameter("weldingType", weldingType);

            weldJobs = query.getResultList();
        }
        return weldJobs;
    }

    //return -1 if there is no weld job references
    @Override
    public Double deriveAverageDuration(ArrayList<WeldJob> similarWeldJobs) {

        double totalMins = 0;
        if (similarWeldJobs == null || similarWeldJobs.isEmpty()) {
            return (double) -1;
        } else {
            for (int i = 0; i < similarWeldJobs.size(); i++) {
                int totalQtyWelded = similarWeldJobs.get(i).getTotalQuantity();
                double surfaceArea = similarWeldJobs.get(i).getSurfaceArea();
                int daysTook = similarWeldJobs.get(i).getDuration();
                double weldingDurationPerCm3 = (daysTook * 24 * 60) / (totalQtyWelded * surfaceArea); //go by minutes
                totalMins += weldingDurationPerCm3;
            }
            return ((totalMins / similarWeldJobs.size()) / 24 / 60);
        }
    }

    @Override
    public Project getProjectWithEarliestCompletionDate() {
        System.out.println("getProjectWithEarliestCompletionDate: Start");
        Query query = em.createQuery("Select p FROM Project AS p where p.actualStart IS NOT NULL AND p.plannedEnd = "
                + "(Select min(p.plannedEnd) FROM Project AS p where p.projectCompletion = false )");

        try {
            Project p = (Project) query.getSingleResult();
            System.out.println("getProjectWithEarliestCompletionDate: End; - A");
            return p;
        } catch (NoResultException e) {
            System.out.println("getProjectWithEarliestCompletionDate: End - B");
            return null;
        }
    }

    //get project with project slacks which can accomodate the durations of new weldJobs
    @Override
    public Project getProjectDurationWithSlack(Integer days) {
        System.out.println("getProjectDurationWithSlack: Start");
        Query query = em.createQuery("Select p FROM Project AS p where p.actualEnd IS NOT NULL and p.projectCompletion = true AND p.plannedEnd > p.actualEnd AND p.plannedEnd > CURRENT_TIMESTAMP");
        List<Project> projects = query.getResultList();

        int diff = 0;
        int chosenProjectIndex = 0;

        if (!projects.isEmpty()) {
            int minslack = -1;
            for (int i = 0; i < projects.size(); i++) {
                diff = getDifferenceDays(projects.get(i).getActualEnd(), projects.get(i).getPlannedEnd());
                if (diff >= days) {
                    if (minslack == -1) {//assignment of first chosen project index
                        minslack = diff;
                        chosenProjectIndex = i;
                    }
                    if (minslack > diff) {
                        minslack = diff;
                        chosenProjectIndex = i;
                    }
                }
            }
            System.out.println("getProjectDurationWithSlack: End - A");
            return projects.get(chosenProjectIndex);
        } else {
            System.out.println("getProjectDurationWithSlack: End - B");
            return null; //no project with project slack
        }
    }

    @Override
    public Timestamp addDays(Timestamp stamp, int days) {
        Date date = new Date(stamp.getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days

        Timestamp futureDate = new Timestamp(cal.getTime().getTime());
        return futureDate;
    }

    @Override
    public Integer getDifferenceDays(Timestamp t1, Timestamp t2) {

        Date d1 = new Date(t1.getTime());
        Date d2 = new Date(t2.getTime());
        long diff = d2.getTime() - d1.getTime();
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) + 1;
    }

    @Override
    public List<WeldJob> receivedWeldJobs(String username) {

        Query query = em.createQuery("Select p FROM Project AS p where p.projectCompletion = false AND p.customerKey =:username");
        query.setParameter("username", username);

        List<Project> projects = query.getResultList();
        List<WeldJob> weldJobs = new ArrayList<>();
        if (!projects.isEmpty()) {
            for (int i = 0; i < projects.size(); i++) {
                for (int j = 0; j < projects.get(i).getWeldJobs().size(); j++) {
                    weldJobs.add(projects.get(i).getWeldJobs().get(j));
                }
            }//System.out.println("receivedWeldJobs - size: " + weldJobs.size() + " " + weldJobs.get(0).getProjectNo());
            return weldJobs;
        } else {
            return null;
        }
    }

    @Override
    public List<WeldJob> getWeldJobs(Project p) {
        Query query = em.createQuery("Select w FROM WeldJob AS w where w.projectNo=:projectNo");
        query.setParameter("projectNo", p.getProjectNo());

        return query.getResultList();
    }

    @Override
    public List<Project> getAllProjects() {
        Query query = em.createQuery("Select p FROM Project AS p");
        return query.getResultList();
    }

    @Override
    public List<Project> getProjectByProjectNo(String projectNo) {
        Query query = em.createQuery("Select p FROM Project AS p where p.projectNo=:projectNo");
        query.setParameter("projectNo", projectNo);

        return query.getResultList();
    }

    @Override
    public List<Project> getUncompletedProjects() {
        Query query = em.createQuery("Select p FROM Project AS p where p.projectCompletion=false");
        return query.getResultList();
    }

    @Override
    public List<Project> getUncompletedStartedProjects() {
        Query query = em.createQuery("Select p FROM Project AS p where p.projectCompletion=false AND p.actualStart IS NOT NULL");
        return query.getResultList();
    }

    @Override
    public void conductMerge(Project p) {
        em.merge(p);
    }

    @Override
    public List<Project> getCompletedProjects() {
        Query query = em.createQuery("Select p FROM Project AS p where p.projectCompletion=true");
        return query.getResultList();
    }

}
