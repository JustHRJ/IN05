/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Project;
import entity.WeldJob;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author JitCheong
 */
@Named(value = "weldJobProgressManagedBean")
@ViewScoped
public class WeldJobProgressManagedBean implements Serializable {

    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private ArrayList<Project> receivedStartedProjects;
    private Map<String, String> ongoingProjectNos;
    private String selectedProjectNo = "";
    private ArrayList<Project> receivedProjectByProjectNo;
    private Project selectedProject;

    private int quantityWelded;

    /**
     * Creates a new instance of WeldJobProgressManagedBean
     */
    public WeldJobProgressManagedBean() {
        receivedStartedProjects = new ArrayList<>();
        ongoingProjectNos = new HashMap<>();
        receivedProjectByProjectNo = new ArrayList<>();
        selectedProject = new Project();
    }

    @PostConstruct
    public void init() {

        receivedStartedProjects = new ArrayList<>(projectSessionBean.getUncompletedStartedProjects());

        if (projectSessionBean.getUncompletedStartedProjects() != null) {
            for (Project p : projectSessionBean.getUncompletedProjects()) {
                ongoingProjectNos.put(p.getProjectNo(), p.getProjectNo());
            }
        }

    }

    public void filterByProjectNo() {
        receivedProjectByProjectNo = new ArrayList<>(projectSessionBean.getProjectByProjectNo(selectedProjectNo));
        if (!receivedProjectByProjectNo.isEmpty()) {
            selectedProject = receivedProjectByProjectNo.get(0);

        }

    }

    public Integer getProgress(Integer totalQty, Integer qtyWelded) {
        if (qtyWelded == null) {
            qtyWelded = 0;
        }
        double p = (1.0 * qtyWelded / totalQty) * 100;
        return (int) p;
    }

    public void onEditRow(RowEditEvent event) {
        WeldJob w = (WeldJob) event.getObject();//gives me unedited value
        w.setQuantityWelded(quantityWelded);
        projectSessionBean.conductWeldJobMerge(w);
        
        Project p = projectSessionBean.getProjectByProjectNo(w.getProjectNo()).get(0);
        double partialWeldJob = 0, totalWeldJob = 0;
        for(int i=0; i<p.getWeldJobs().size(); i++){
            partialWeldJob += w.getSurfaceArea() * w.getQuantityWelded();
            totalWeldJob += w.getSurfaceArea() * w.getTotalQuantity();
        }
        int percent = (int)((partialWeldJob/totalWeldJob) * 100);
        p.setProjectProgress(percent);
        projectSessionBean.conductProjectMerge(p);
    }

    /**
     * @return the receivedStartedProjects
     */
    public ArrayList<Project> getReceivedStartedProjects() {
        return receivedStartedProjects;
    }

    /**
     * @param receivedStartedProjects the receivedStartedProjects to set
     */
    public void setReceivedStartedProjects(ArrayList<Project> receivedStartedProjects) {
        this.receivedStartedProjects = receivedStartedProjects;
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
     * @return the quantityWelded
     */
    public int getQuantityWelded() {
        return quantityWelded;
    }

    /**
     * @param quantityWelded the quantityWelded to set
     */
    public void setQuantityWelded(int quantityWelded) {
        this.quantityWelded = quantityWelded;
    }

}
