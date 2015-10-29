/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.EmployeeEntity;
import entity.MachineEntity;
import entity.Project;
import entity.WeldJob;
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

    @Override
    public void createProject(Project project) {
        em.persist(project);
    }

    @Override
    public void createWeldJob(WeldJob weldJob) {
        em.persist(weldJob);
    }

    @Override
    public void conductMerge(WeldJob weldJob) {
        em.merge(weldJob);
    }
    

    //return null if there is no available employee
    @Override
    public EmployeeEntity getAvailableEmployee() {
        Query query = em.createQuery("Select e FROM EmployeeEntity AS e where e.availability=true");
        List<EmployeeEntity> employees = query.getResultList();
        if (employees.isEmpty()) {
            return null;
        } else {
            return employees.get(0);
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

    //get duration (days) from past projects of same nature
    //return -1 if there is no similar projects
    @Override
    public Integer getSimilarPastProjectDuration(String metal1, String metal2) {
        System.out.println("getSimilarPastProjectDuration: Start");
        Integer days = 0;
        //find projects with two similar metals for welding 
        Query query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND "
                + "( (w.metal1=:metal1 OR w.metal1=:metal2) AND (w.metal2=:metal1 OR w.metal2=:metal2) ) ");
        query.setParameter("metal1", metal1);
        query.setParameter("metal2", metal2);

        List<Project> projects = query.getResultList();
        //if not, find projects with one similar metal for welding
        if (projects.isEmpty()) {
            query = em.createQuery("Select w FROM WeldJob AS w where w.project.projectCompletion = true AND "
                + "( (w.metal1=:metal1 OR w.metal1=:metal2) AND (w.metal2=:metal1 OR w.metal2=:metal2) ) ");

            query.setParameter("metal1", metal1);
            query.setParameter("metal2", metal2);

            projects = query.getResultList();
        }
        int avgDays = 0;
        //if there are references to such projects, we get the longest duration among these projects
        if (!projects.isEmpty()) {
            //get the average duration from these projects (ActualEnd - ActualStart)
            for (Project p : projects) {
                //if (days == -1) {
                //    days = getDifferenceDays(p.getActualStart(), p.getActualEnd());
                //}
                //if (days < getDifferenceDays(p.getActualStart(), p.getActualEnd())) {
                //    days = getDifferenceDays(p.getActualStart(), p.getActualEnd());
                //}
                days += getDifferenceDays(p.getActualStart(), p.getActualEnd());
            }
        }
        avgDays = days / projects.size();
        return avgDays;
    }

    //get project with earliest completion date
    @Override
    public Project getProjectWithEarliestCompletionDate() {
        System.out.println("getProjectWithEarliestCompletionDate: Start");
        Query query = em.createQuery("Select p FROM Project AS p where p.plannedEnd = "
                + "(Select min(p.plannedEnd) FROM Project AS p where p.projectCompletion = false )");

        try {
            Project p = (Project) query.getSingleResult(); System.out.println("getProjectWithEarliestCompletionDate: End; - A");
            return p; 
        } catch (NoResultException e) { System.out.println("getProjectWithEarliestCompletionDate: End - B");
            return null; 
        }
    }

    //get project with project slacks which can accomodate the durations of new projects
    @Override
    public Project getProjectDurationWithSlack(Integer days) {
        System.out.println("getProjectDurationWithSlack: Start");
        Query query = em.createQuery("Select p FROM Project AS p where p.projectCompletion = false AND p.plannedEnd > p.actualEnd");
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
            } System.out.println("getProjectDurationWithSlack: End - A");
            return projects.get(chosenProjectIndex);
        } else { System.out.println("getProjectDurationWithSlack: End - B");
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
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
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
    public List <Project> getUncompletedProjects(){
        Query query = em.createQuery("Select p FROM Project AS p where p.projectCompletion = false");
        return query.getResultList();
    }
    
    @Override
    public void conductMerge(Project p) {
        em.merge(p);
    }

}
