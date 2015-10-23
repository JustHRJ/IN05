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
    private Integer machineId; // id from machine
    private String metal1; // id from metal entity
    private String metal2; // id from metal entity
    private String fillerCode; //id from filler entity
    private Double SurfaceArea;

    //Design the association after justin and gx have linked up itementity and filler entity
    //change itemEntity to filler and fillerentity to fillerComposition
    //@gx: change your itemCode to fillerCode
    //I am waiting; It's a simple job don't drag; update the report too
    
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
     * @return the machineId
     */
    public Integer getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }


    /**
     * @return the SurfaceArea
     */
    public Double getSurfaceArea() {
        return SurfaceArea;
    }

    /**
     * @param SurfaceArea the SurfaceArea to set
     */
    public void setSurfaceArea(Double SurfaceArea) {
        this.SurfaceArea = SurfaceArea;
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
    
}
