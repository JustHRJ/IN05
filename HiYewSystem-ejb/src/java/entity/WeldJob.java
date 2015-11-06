/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Jitcheong
 */
@Entity
public class WeldJob implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weldJobId; 
    private String projectNo; // id from project
    private String empName; // 
    //private Integer machineId; // id from machine
    private String metal1; // id from metal entity
    private String metal2; // id from metal entity
    //private String fillerCode; //id from filler entity
    private Double surfaceArea;
    private Integer totalQuantity;
    private Integer quantityWelded;
    private String weldingType;
    private Integer duration;

    //Design the association after justin and gx have linked up itementity and filler entity
    //change itemEntity to filler and fillerentity to fillerComposition
    //@gx: change your itemCode to fillerCode
    //I am waiting; It's a simple job don't drag; update the report too
    @ManyToOne
    private Project project;
    @OneToOne
    private MachineEntity machine;
    @OneToOne()
    private FillerEntity filler;

    public WeldJob() {
    }

    public WeldJob(Integer weldJobId, String projectNo, String empName, Integer machineId, String metal1, String metal2, String fillerCode, Double SurfaceArea, Project project, FillerEntity filler) {
        this.weldJobId = weldJobId;
        this.projectNo = projectNo;
        this.empName = empName;
        //this.machineId = machineId;
        this.metal1 = metal1;
        this.metal2 = metal2;
        //this.fillerCode = fillerCode;
        this.surfaceArea = SurfaceArea;
        this.project = project;
        this.filler = filler;
    }
    
    public Integer getWeldJobId() {
        return weldJobId;
    }

    public void setWeldJobId(Integer weldJobId) {
        this.weldJobId = weldJobId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (weldJobId != null ? weldJobId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the weldJobId fields are not set
        if (!(object instanceof WeldJob)) {
            return false;
        }
        WeldJob other = (WeldJob) object;
        if ((this.weldJobId == null && other.weldJobId != null) || (this.weldJobId != null && !this.weldJobId.equals(other.weldJobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.WeldJob[ id=" + weldJobId + " ]";
    }

    /**
     * @return the projectNo
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * @param projectNo the projectNo to set
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    /**
     * @return the empName
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * @param empName the empName to set
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }


    /**
     * @return the SurfaceArea
     */
    public Double getSurfaceArea() {
        return surfaceArea;
    }

    /**
     * @param SurfaceArea the SurfaceArea to set
     */
    public void setSurfaceArea(Double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    /**
     * @return the metal1
     */
    public String getMetal1() {
        return metal1;
    }

    /**
     * @param metal1 the metal1 to set
     */
    public void setMetal1(String metal1) {
        this.metal1 = metal1;
    }

    /**
     * @return the metal2
     */
    public String getMetal2() {
        return metal2;
    }

    /**
     * @param metal2 the metal2 to set
     */
    public void setMetal2(String metal2) {
        this.metal2 = metal2;
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the filler
     */
    public FillerEntity getFiller() {
        return filler;
    }

    /**
     * @param filler the filler to set
     */
    public void setFiller(FillerEntity filler) {
        this.filler = filler;
    }

    /**
     * @return the machine
     */
    public MachineEntity getMachine() {
        return machine;
    }

    /**
     * @param machine the machine to set
     */
    public void setMachine(MachineEntity machine) {
        this.machine = machine;
    }

    /**
     * @return the totalQuantity
     */
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * @param totalQuantity the totalQuantity to set
     */
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
     * @return the quantityWeilded
     */
    public Integer getQuantityWeilded() {
        return quantityWelded;
    }

    /**
     * @param quantityWeilded the quantityWeilded to set
     */
    public void setQuantityWeilded(Integer quantityWeilded) {
        this.quantityWelded = quantityWeilded;
    }

    /**
     * @return the weldingType
     */
    public String getWeldingType() {
        return weldingType;
    }

    /**
     * @param weldingType the weldingType to set
     */
    public void setWeldingType(String weldingType) {
        this.weldingType = weldingType;
    }

    /**
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
}
