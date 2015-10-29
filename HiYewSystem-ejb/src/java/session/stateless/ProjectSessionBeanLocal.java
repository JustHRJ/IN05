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
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface ProjectSessionBeanLocal {

    public EmployeeEntity getAvailableEmployee();

    public MachineEntity getAvailableMachine(String machineType);

    public Integer getSimilarPastProjectDuration(String metal1, String metal2);

    public Project getProjectWithEarliestCompletionDate();

    public Project getProjectDurationWithSlack(Integer days);

    public Timestamp addDays(Timestamp stamp, int days);

    public void createProject(Project project);

    public void createWeldJob(WeldJob weldJob);

    public void conductMerge(WeldJob weldJob);

    public List<WeldJob> receivedWeldJobs(String username);

    public void conductMerge(Project p);

    public List<Project> getAllProjects();

    public Integer getDifferenceDays(Timestamp t1, Timestamp t2);

    public List<Project> getUncompletedProjects();

    public List<WeldJob> getWeldJobs(Project p);
    
}
