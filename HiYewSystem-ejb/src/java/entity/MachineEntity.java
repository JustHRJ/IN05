/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author JustHRJ
 */
@Entity
public class MachineEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String machine_name;
    private String machine_number; // unique key
    private String machine_type;
    private Timestamp machine_expiry;
    private String status; //Available, Disable, Maintenance, Out of order, In use
    private int extension;
    private String description;
    private Collection<MachineMaintainenceEntity> machineMaintainence = new ArrayList<MachineMaintainenceEntity>();
    private Collection<MachineRepairEntity> machineRepair = new ArrayList<MachineRepairEntity>();
  
    
    public MachineEntity() {

    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "machine")
    public Collection<MachineRepairEntity> getMachineRepair(){
        return machineRepair;
    }
    
    public void setMachineRepair(Collection<MachineRepairEntity> machineRepair){
        this.machineRepair = machineRepair;
    }
    
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "machine")
    public Collection<MachineMaintainenceEntity> getMachineMaintainence() {
        return machineMaintainence;
    }

    public void setMachineMaintainence(Collection<MachineMaintainenceEntity> machineMaintainence) {
        this.machineMaintainence = machineMaintainence;
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
        if (!(object instanceof MachineEntity)) {
            return false;
        }
        MachineEntity other = (MachineEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MachineEntity[ id=" + id + " ]";
    }

    /**
     * @return the machine_name
     */
    public String getMachine_name() {
        return machine_name;
    }

    /**
     * @param machine_name the machine_name to set
     */
    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    /**
     * @return the machine_number
     */
    public String getMachine_number() {
        return machine_number;
    }

    /**
     * @param machine_number the machine_number to set
     */
    public void setMachine_number(String machine_number) {
        this.machine_number = machine_number;
    }

    /**
     * @return the machine_expiry
     */
    public Timestamp getMachine_expiry() {
        return machine_expiry;
    }

    /**
     * @param machine_expiry the machine_expiry to set
     */
    public void setMachine_expiry(Timestamp machine_expiry) {
        this.machine_expiry = machine_expiry;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the extension
     */
    public int getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(int extension) {
        this.extension = extension;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the machine_type
     */
    public String getMachine_type() {
        return machine_type;
    }

    /**
     * @param machine_type the machine_type to set
     */
    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
    }

}
