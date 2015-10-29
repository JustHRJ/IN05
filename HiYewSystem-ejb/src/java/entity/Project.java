/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author: Jitcheong
 */
@Entity
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String projectNo;
    private String customerKey;
    private Timestamp actualStart;
    private Timestamp actualEnd;
    private Timestamp plannedStart;
    private Timestamp plannedEnd;
    private Timestamp latestStart;
    private Timestamp latestEnd;
    private Boolean projectCompletion;
    
    //private Boolean projectOverrun;
    //private Integer projectDaysExceed;
    //private String causeOfDelay;
    //private String takeAway;
    //private Integer daysDiffFromActualAndPlannedStart;
    //private Integer daysDiffFromActualAndPlannedEnd;
            
    private String projectManager; //if applicable
    
    @OneToMany(mappedBy = "project")
    private List <WeldJob> weldJobs = new ArrayList<>();
    

    public Project() {
    }

    public Project(String projectNo, Timestamp actualStart, Timestamp actualEnd, Timestamp plannedStart, Timestamp plannedEnd, Timestamp latestStart, Timestamp latestEnd, String projectManager) {
        this.projectNo = projectNo;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.plannedStart = plannedStart;
        this.plannedEnd = plannedEnd;
        this.latestStart = latestStart;
        this.latestEnd = latestEnd;
        this.projectManager = projectManager;

    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectNo != null ? projectNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the projectNo fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.projectNo == null && other.projectNo != null) || (this.projectNo != null && !this.projectNo.equals(other.projectNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Project[ id=" + projectNo + " ]";
    }

    /**
     * @return the actualStart
     */
    public Timestamp getActualStart() {
        return actualStart;
    }

    /**
     * @param actualStart the actualStart to set
     */
    public void setActualStart(Timestamp actualStart) {
        this.actualStart = actualStart;
    }

    /**
     * @return the actualEnd
     */
    public Timestamp getActualEnd() {
        return actualEnd;
    }

    /**
     * @param actualEnd the actualEnd to set
     */
    public void setActualEnd(Timestamp actualEnd) {
        this.actualEnd = actualEnd;
    }

    /**
     * @return the plannedStart
     */
    public Timestamp getPlannedStart() {
        return plannedStart;
    }

    /**
     * @param plannedStart the plannedStart to set
     */
    public void setPlannedStart(Timestamp plannedStart) {
        this.plannedStart = plannedStart;
    }

    /**
     * @return the plannedEnd
     */
    public Timestamp getPlannedEnd() {
        return plannedEnd;
    }

    /**
     * @param plannedEnd the plannedEnd to set
     */
    public void setPlannedEnd(Timestamp plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    /**
     * @return the latestStart
     */
    public Timestamp getLatestStart() {
        return latestStart;
    }

    /**
     * @param latestStart the latestStart to set
     */
    public void setLatestStart(Timestamp latestStart) {
        this.latestStart = latestStart;
    }

    /**
     * @return the latestEnd
     */
    public Timestamp getLatestEnd() {
        return latestEnd;
    }

    /**
     * @param latestEnd the latestEnd to set
     */
    public void setLatestEnd(Timestamp latestEnd) {
        this.latestEnd = latestEnd;
    }

    /**
     * @return the projectManager
     */
    public String getProjectManager() {
        return projectManager;
    }

    /**
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * @return the projectCompletion
     */
    public Boolean getProjectCompletion() {
        return projectCompletion;
    }

    /**
     * @param projectCompletion the projectCompletion to set
     */
    public void setProjectCompletion(Boolean projectCompletion) {
        this.projectCompletion = projectCompletion;
    }

    /**
     * @return the weldJobs
     */
    public List <WeldJob> getWeldJobs() {
        return weldJobs;
    }

    /**
     * @param weldJobs the weldJobs to set
     */
    public void setWeldJobs(List <WeldJob> weldJobs) {
        this.weldJobs = weldJobs;
    }

    public void addWeldJobs(WeldJob weldJob) {
        this.weldJobs.add(weldJob);
    }

    /**
     * @return the customerKey
     */
    public String getCustomerKey() {
        return customerKey;
    }

    /**
     * @param customerKey the customerKey to set
     */
    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }
    
}
