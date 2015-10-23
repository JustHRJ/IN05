/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author JustHRJ
 */
@Entity
public class MachineRepairEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String repairSolution;
    private int laserPulse;
    private double repairCost;
    private Timestamp Date;
    private String Remarks;
    private MachineEntity machine = new MachineEntity();

    @ManyToOne
    public MachineEntity getMachine() {
        return machine;
    }

    public void setMachine(MachineEntity machine) {
        this.machine = machine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MachineRepairEntity)) {
            return false;
        }
        MachineRepairEntity other = (MachineRepairEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MachineRepairEntity[ id=" + id + " ]";
    }

    /**
     * @return the repairSolution
     */
    public String getRepairSolution() {
        return repairSolution;
    }

    /**
     * @param repairSolution the repairSolution to set
     */
    public void setRepairSolution(String repairSolution) {
        this.repairSolution = repairSolution;
    }

    /**
     * @return the laserPulse
     */
    public int getLaserPulse() {
        return laserPulse;
    }

    /**
     * @param laserPulse the laserPulse to set
     */
    public void setLaserPulse(int laserPulse) {
        this.laserPulse = laserPulse;
    }

    /**
     * @return the repairCost
     */
    public double getRepairCost() {
        return repairCost;
    }

    /**
     * @param repairCost the repairCost to set
     */
    public void setRepairCost(double repairCost) {
        this.repairCost = repairCost;
    }

    /**
     * @return the Date
     */
    public Timestamp getDate() {
        return Date;
    }

    /**
     * @param Date the Date to set
     */
    public void setDate(Timestamp Date) {
        this.Date = Date;
    }

    /**
     * @return the Remarks
     */
    public String getRemarks() {
        return Remarks;
    }

    /**
     * @param Remarks the Remarks to set
     */
    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

}
