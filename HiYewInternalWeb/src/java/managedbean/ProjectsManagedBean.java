/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Customer;
import entity.FillerEntity;
import entity.Project;
import entity.WeldJob;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.HiYewICSSessionBeanLocal;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author User
 */
@Named(value = "projManagedBean")
@ViewScoped
public class ProjectsManagedBean implements Serializable {
    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private ArrayList<Project> projects;
    private Map<String, String> projectStatuses;
    private ArrayList<Project> receivedProjects;
    private ArrayList<Project> filteredProj;

    private ArrayList<WeldJob> receivedWeldJobs;

    //for project initiation and closure
    private ArrayList<Project> receivedProjectByProjectNo;
    private Map<String, String> ongoingProjectNos;
    private String selectedProjectNo = "";
    private Project selectedProject;
    private Boolean actualStartVisibility;
    private Boolean actualEndVisibility;
    private Boolean updateBtnVisibility;
    private Date today = new Date();
    private Date actualStart;
    private Date actualEnd;
    
    private String fillerCode = "";

    /**
     * Creates a new instance of ProjectsManagedBean
     */
    public ProjectsManagedBean() {
        projects = new ArrayList<>();
        projectStatuses = new HashMap<>();
        ongoingProjectNos = new HashMap<>();
        receivedProjects = new ArrayList<>();
        receivedWeldJobs = new ArrayList<>();
        selectedProject = new Project();
    }

    @PostConstruct
    public void init() {
        projectStatuses.put("Starting", "Starting");
        projectStatuses.put("Ongoing", "Ongoing");
        projectStatuses.put("Completed", "Completed");

        receivedProjects = new ArrayList<>(projectSessionBean.getAllProjects());
        //for project initiation and closure
        for (Project p : projectSessionBean.getUncompletedProjects()) {
            ongoingProjectNos.put(p.getProjectNo(), p.getProjectNo());
        }
        actualStartVisibility = false;
        actualEndVisibility = false;
        updateBtnVisibility = (actualEndVisibility == false) ? actualStartVisibility : actualEndVisibility;

    }

    public void filterByProjectNo() {
        receivedProjectByProjectNo = new ArrayList<>(projectSessionBean.getProjectByProjectNo(selectedProjectNo));
        if (!receivedProjectByProjectNo.isEmpty()) {
            selectedProject = receivedProjectByProjectNo.get(0);
            receivedWeldJobs = new ArrayList <>(selectedProject.getWeldJobs());
            //set visibility
            if (selectedProject.getActualStart() == null) {
                actualStartVisibility = true;
                actualEndVisibility = false;
            } else {
                actualStartVisibility = false;
                actualEndVisibility = true;
            }

        } else {
            actualStartVisibility = false;
            actualEndVisibility = false;
        }
        updateBtnVisibility = (actualEndVisibility == false) ? actualStartVisibility : actualEndVisibility;
    }
 
    public void updateProjectActualDate() {
        if(actualStart != null){
            selectedProject.setActualStart(new Timestamp(actualStart.getTime()));
            selectedProject.setProjectProgress(100);
            actualStart = null;
            actualStartVisibility = false;
        }
        if(actualEnd != null){
            selectedProject.setActualEnd(new Timestamp(actualEnd.getTime()));
            selectedProject.setProjectCompletion(true);
            actualEnd = null;
            actualEndVisibility = false;
        }
        updateBtnVisibility = false;
        projectSessionBean.conductProjectMerge(selectedProject);
        
        for(int i=0; i<receivedWeldJobs.size(); i++){
            projectSessionBean.conductWeldJobMerge(receivedWeldJobs.get(i));
        }

        String str = "";
        if (selectedProject.getActualEnd() == null) { //after setting start date
            for (int i = 0; i < selectedProject.getWeldJobs().size(); i++) {
                str += selectedProject.getWeldJobs().get(i).getEmpName() + " has been attached to a welding job using Machine ";
                str += selectedProject.getWeldJobs().get(i).getMachine().getMachine_number() + "\r\n";
                
                
                projectSessionBean.setEmployeeAvailability(selectedProject.getWeldJobs().get(i).getEmpName(), false);
                selectedProject.getWeldJobs().get(i).getMachine().setStatus("In use");
            }
            
        } else { // after setting ending date
            for (int i = 0; i < selectedProject.getWeldJobs().size(); i++) {
                str += selectedProject.getWeldJobs().get(i).getEmpName() + " are currently ready for new welding job assignment.\r\n";
                str += "Machine " + selectedProject.getWeldJobs().get(i).getMachine().getMachine_number() + " are currently available for use.\r\n";
                
                projectSessionBean.setEmployeeAvailability(selectedProject.getWeldJobs().get(i).getEmpName(), true);
                selectedProject.getWeldJobs().get(i).getMachine().setStatus("Available");
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("resourceAvailabilityMessage", str);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "orange");
    }
    
    public void onEditRow(RowEditEvent event) {
        WeldJob w = (WeldJob) event.getObject();//gives me unedited value
        FillerEntity f = hiYewICSSessionBean.getExistingItem(fillerCode);
        
        w.setFiller(f);
        projectSessionBean.conductWeldJobMerge(w);
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

    public void selectProject(Project p) {
        receivedWeldJobs = new ArrayList<>(projectSessionBean.getWeldJobs(p));
    }

    /**
     * @return the receivedWeldJobs
     */
    public ArrayList<WeldJob> getReceivedWeldJobs() {
        return receivedWeldJobs;
    }

    /**
     * @param receivedWeldJobs the receivedWeldJobs to set
     */
    public void setReceivedWeldJobs(ArrayList<WeldJob> receivedWeldJobs) {
        this.receivedWeldJobs = receivedWeldJobs;
    }

    /**
     * @return the ongoingProjectNos
     */
    public Map<String, String> getOngoingProjectNos() {
        return ongoingProjectNos;
    }

    /**
     * @param ongoingProjectNos the ongoingProjectNos to set
     */
    public void setOngoingProjectNos(Map<String, String> ongoingProjectNos) {
        this.ongoingProjectNos = ongoingProjectNos;
    }

    /**
     * @return the selectedProjectNo
     */
    public String getSelectedProjectNo() {
        return selectedProjectNo;
    }

    /**
     * @param selectedProjectNo the selectedProjectNo to set
     */
    public void setSelectedProjectNo(String selectedProjectNo) {
        this.selectedProjectNo = selectedProjectNo;
    }

    /**
     * @return the receivedProjectByProjectNo
     */
    public ArrayList<Project> getReceivedProjectByProjectNo() {
        return receivedProjectByProjectNo;
    }

    /**
     * @param receivedProjectByProjectNo the receivedProjectByProjectNo to set
     */
    public void setReceivedProjectByProjectNo(ArrayList<Project> receivedProjectByProjectNo) {
        this.receivedProjectByProjectNo = receivedProjectByProjectNo;
    }

    /**
     * @return the actualStartVisibility
     */
    public Boolean getActualStartVisibility() {
        return actualStartVisibility;
    }

    /**
     * @param actualStartVisibility the actualStartVisibility to set
     */
    public void setActualStartVisibility(Boolean actualStartVisibility) {
        this.actualStartVisibility = actualStartVisibility;
    }

    /**
     * @return the actualEndVisibility
     */
    public Boolean getActualEndVisibility() {
        return actualEndVisibility;
    }

    /**
     * @param actualEndVisibility the actualEndVisibility to set
     */
    public void setActualEndVisibility(Boolean actualEndVisibility) {
        this.actualEndVisibility = actualEndVisibility;
    }

    /**
     * @return the selectedProject
     */
    public Project getSelectedProject() {
        return selectedProject;
    }

    /**
     * @param selectedProject the selectedProject to set
     */
    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    /**
     * @return the today
     */
    public Date getToday() {
        return today;
    }

    /**
     * @param today the today to set
     */
    public void setToday(Date today) {
        this.today = today;
    }

    /**
     * @return the updateBtnVisibility
     */
    public Boolean getUpdateBtnVisibility() {
        return updateBtnVisibility;
    }

    /**
     * @param updateBtnVisibility the updateBtnVisibility to set
     */
    public void setUpdateBtnVisibility(Boolean updateBtnVisibility) {
        this.updateBtnVisibility = updateBtnVisibility;
    }

    /**
     * @return the actualStart
     */
    public Date getActualStart() {
        return actualStart;
    }

    /**
     * @param actualStart the actualStart to set
     */
    public void setActualStart(Date actualStart) {
        this.actualStart = actualStart;
    }

    /**
     * @return the actualEnd
     */
    public Date getActualEnd() {
        return actualEnd;
    }

    /**
     * @param actualEnd the actualEnd to set
     */
    public void setActualEnd(Date actualEnd) {
        this.actualEnd = actualEnd;
    }

    /**
     * @return the fillerCode
     */
    public String getFillerCode() {
        return fillerCode;
    }

    /**
     * @param fillerCode the fillerCode to set
     */
    public void setFillerCode(String fillerCode) {
        this.fillerCode = fillerCode;
    }

}
