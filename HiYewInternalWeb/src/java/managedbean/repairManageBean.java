/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.MachineEntity;
import entity.MachineRepairEntity;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import session.stateless.HiYewSystemBeanLocal;
import session.stateless.MachineSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "repairManageBean")
@RequestScoped
public class repairManageBean {
    
    @EJB
    private MachineSystemBeanLocal machineSystemBean;


    private MachineRepairEntity machine = new MachineRepairEntity();
    private Date repairDate;
    private MachineEntity selectedMachine = new MachineEntity();
    
    private String machineName;

    /**
     * Creates a new instance of repairManageBean
     */
    public repairManageBean() {
    }

    
    public List<MachineRepairEntity> getRetrieveRepairs(){
        return machineSystemBean.repairList(selectedMachine);
    }
    
    public void createRepair() throws IOException{
        machineSystemBean.createRepair(machine, repairDate, machineName);
         FacesContext facesCtx = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesCtx.getExternalContext();
        externalContext.redirect("/HiYewInternalWeb/mms-view-machine.xhtml");
    }
    
    
    /**
     * @return the machine
     */
    public MachineRepairEntity getMachine() {
        return machine;
    }

    /**
     * @param machine the machine to set
     */
    public void setMachine(MachineRepairEntity machine) {
        this.machine = machine;
    }

    /**
     * @return the machineName
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * @param machineName the machineName to set
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /**
     * @return the repairDate
     */
    public Date getRepairDate() {
        return repairDate;
    }

    /**
     * @param repairDate the repairDate to set
     */
    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    /**
     * @return the selectedRepair
     */
    public MachineEntity getSelectedMachine() {
        return selectedMachine;
    }

    /**
     * @param selectedRepair the selectedRepair to set
     */
    public void setSelectedMachine(MachineEntity selectedRepair) {
        this.selectedMachine = selectedRepair;
    }

}
