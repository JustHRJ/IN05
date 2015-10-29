/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Customer;
import entity.Project;
import entity.WeldJob;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author User
 */
@Named(value = "projManagedBean")
@ViewScoped
public class ProjectsManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private ArrayList<Project> projects;
    private Map<String, String> projectStatuses;
    private ArrayList<Project> receivedProjects;
    private ArrayList<Project> filteredProj;
    
    private ArrayList <WeldJob> receivedWeldJobs;

    /**
     * Creates a new instance of ProjectsManagedBean
     */
    public ProjectsManagedBean() {
        projects = new ArrayList<>();
        projectStatuses = new HashMap<>();
        receivedProjects = new ArrayList<>();
        receivedWeldJobs = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        projectStatuses.put("Starting", "Starting");
        projectStatuses.put("Ongoing", "Ongoing");
        projectStatuses.put("Completed", "Completed");
        receivedProjects = new ArrayList<>(projectSessionBean.getAllProjects());
    }

    public void getAllProjects() {
        receivedProjects = new ArrayList<>(projectSessionBean.getAllProjects());
    }

    /**
     * @return the receivedProjects
     */
    public ArrayList<Project> getReceivedProjects() {
        return receivedProjects;
    }

    /**
     * @param receivedProjects the receivedProjects to set
     */
    public void setReceivedProjects(ArrayList<Project> receivedProjects) {
        this.receivedProjects = receivedProjects;
    }

    public String formatDate(Timestamp t) {

        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        if (t != null) {
            return sd.format(t.getTime());
        }
        return "";
    }

    public String getDurationForCompletedProjects(Boolean completion, Timestamp start, Timestamp end) {
        String dur = "";
        if (completion == true) {
            dur = projectSessionBean.getDifferenceDays(start, end).toString();
        }
        return dur;
    }

    public String getProjectStatus(Boolean completion) {
        String status = "Y";
        if (completion == false) {
            status = "N";
        }
        return status;
    }

    public String getCustomerName(String username) {
        Customer c = customerSessionBean.getCustomerByUsername(username);
        if (c != null) {
            return c.getName();
        }
        return "";
    }

    /**
     * @return the filteredProj
     */
    public ArrayList<Project> getFilteredProj() {
        return filteredProj;
    }

    /**
     * @param filteredProj the filteredProj to set
     */
    public void setFilteredProj(ArrayList<Project> filteredProj) {
        this.filteredProj = filteredProj;
    }

    public Integer compareActualEndAndPlannedEnd(Boolean completion, Timestamp actualEnd, Timestamp plannedEnd) {
        if (completion == true) {
            Integer value = projectSessionBean.getDifferenceDays(actualEnd, plannedEnd);
            return value;
        }
        return 1;
    }
    
    public void selectProject(Project p){
        receivedWeldJobs = new ArrayList <> (projectSessionBean.getWeldJobs(p));
    }

    /**
     * @return the receivedWeldJobs
     */
    public ArrayList <WeldJob> getReceivedWeldJobs() {
        return receivedWeldJobs;
    }

    /**
     * @param receivedWeldJobs the receivedWeldJobs to set
     */
    public void setReceivedWeldJobs(ArrayList <WeldJob> receivedWeldJobs) {
        this.receivedWeldJobs = receivedWeldJobs;
    }


}
