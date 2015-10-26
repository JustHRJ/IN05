/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.MachineEntity;
import entity.MachineMaintainenceEntity;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;
import session.stateless.HiYewSystemBeanLocal;
import session.stateless.MachineSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "machineManageBean")
@RequestScoped
public class MachineManageBean {

    /**
     * Creates a new instance of MachineManageBean
     */
  
    @EJB
    private MachineSystemBeanLocal machineSystemBean;
    private String machineType;
    private String machine_status = "";
    private List<Long> machineMaintainenceIDList;
    private String machineName = "";
    private String machineId = "";
    private String machineDescript = "";
    private Date machineNxtMaint = null;
    private int machineSubMaint = 0;
    private String machineMaintainenceID = "";
    private Date mScheduleDate = null;
    private String mScheduleHour = "";
    private String maintainenceComments = "";
    private String mServiceProvider = "";
    private String mServiceContact = "";

    public MachineManageBean() {
    }

    public String addMachine() {
        Timestamp machineTime = new Timestamp(machineNxtMaint.getTime());
        if (machineType.equals("A")) {
            machineSubMaint = 2;
        } else if (machineType.equals("B")) {
            machineSubMaint = 4;
        } else {
            machineSubMaint = 6;
        }
        boolean check = machineSystemBean.addMachine(machineName, machineId, machineTime, machineDescript, machineSubMaint);
        if (check) {
            return "viewMachine";
        } else {
            FacesMessage msg = new FacesMessage("Failed to Add", "Please check for existing machine number or machine Name");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
    }

    public String retrieveMachineName() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("machineName");
    }

    public void extendMachineMaintenance() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("machineName", machineName);

        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/MMS/addMaintainenceSchedule.xhtml");
    }

    public void deleteMachineMaintainence() throws IOException {

        boolean check = false;
        if (machineMaintainenceID.isEmpty()) {
            FacesMessage msg = new FacesMessage("Nothing to delete", machineMaintainenceID);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } else {
            check = machineSystemBean.deleteMachineMaintainence(machineMaintainenceID);
        }
        if (check) {
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("viewMaintainenceSchedule.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Maintenance id does not exist", machineMaintainenceID);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void updateMachineSchedule(RowEditEvent event) throws IOException {
        boolean check = machineSystemBean.updateMachineSchedule((MachineMaintainenceEntity) event.getObject(), mScheduleDate, mScheduleHour, mServiceProvider, mServiceContact);
        if (check) {

            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("viewMaintainenceSchedule.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Not Edited", ((MachineMaintainenceEntity) event.getObject()).getMachine().getMachine_name());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void addMachineSchedule() throws IOException {
        System.out.println(machineName + "here");
        boolean check = machineSystemBean.addMachineMaintainence(machineName, getmScheduleDate(), getmScheduleHour(), getMaintainenceComments(), getmServiceProvider(), getmServiceContact());
        if (check) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("machineName");
            FacesMessage msg = new FacesMessage("Schedule Added", machineName + " has a maintainence schedule");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/MMS/viewMaintainenceSchedule.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Failed to Add", "Please check for exisiting schedule");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String extendMachine() {
        boolean check = machineSystemBean.extendMachineExpiry(machineId);
        if (check) {
            return "viewMachine";
        } else {
            FacesMessage msg = new FacesMessage("Machine not updated.", "Please enter Machine ID");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
    }

    public String formatDate(Timestamp date) {
        if (date != null) {
            Date date1 = new Date(date.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(date1);
        } else {
            return "";
        }
    }

    public String getMAlert() {

        int noOfAlert = machineSystemBean.getNoAlert();

        return "Machine Alert (" + String.valueOf(noOfAlert) + ")";
    }

    public void onMachineChange() {
        if (machineName != null && !machineName.equals("")) {
            machineMaintainenceIDList = machineSystemBean.getMachineMaintID(machineName);
        } else {
            machineMaintainenceIDList = new ArrayList<Long>();
        }
    }

    public void updateMachinery(RowEditEvent event) {
        boolean check = machineSystemBean.updateMachine(machineName, (MachineEntity) event.getObject(), machine_status, machineNxtMaint);
        if (check) {
            FacesMessage msg = new FacesMessage("Edited", ((MachineEntity) event.getObject()).getMachine_number());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Not Edited", ((MachineEntity) event.getObject()).getMachine_number());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * @return the machineType
     */
    public String getMachineType() {
        return machineType;
    }

    /**
     * @param machineType the machineType to set
     */
    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    /**
     * @return the machine_status
     */
    public String getMachine_status() {
        return machine_status;
    }

    /**
     * @param machine_status the machine_status to set
     */
    public void setMachine_status(String machine_status) {
        this.machine_status = machine_status;
    }

    /**
     * @return the machineMaintainenceIDList
     */
    public List<Long> getMachineMaintainenceIDList() {
        return machineMaintainenceIDList;
    }

    /**
     * @param machineMaintainenceIDList the machineMaintainenceIDList to set
     */
    public void setMachineMaintainenceIDList(List<Long> machineMaintainenceIDList) {
        this.machineMaintainenceIDList = machineMaintainenceIDList;
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
     * @return the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machineDescript
     */
    public String getMachineDescript() {
        return machineDescript;
    }

    /**
     * @param machineDescript the machineDescript to set
     */
    public void setMachineDescript(String machineDescript) {
        this.machineDescript = machineDescript;
    }

    /**
     * @return the machineNxtMaint
     */
    public Date getMachineNxtMaint() {
        return machineNxtMaint;
    }

    /**
     * @param machineNxtMaint the machineNxtMaint to set
     */
    public void setMachineNxtMaint(Date machineNxtMaint) {
        this.machineNxtMaint = machineNxtMaint;
    }

    /**
     * @return the machineSubMaint
     */
    public int getMachineSubMaint() {
        return machineSubMaint;
    }

    /**
     * @param machineSubMaint the machineSubMaint to set
     */
    public void setMachineSubMaint(int machineSubMaint) {
        this.machineSubMaint = machineSubMaint;
    }

    /**
     * @return the machineMaintainenceID
     */
    public String getMachineMaintainenceID() {
        return machineMaintainenceID;
    }

    /**
     * @param machineMaintainenceID the machineMaintainenceID to set
     */
    public void setMachineMaintainenceID(String machineMaintainenceID) {
        this.machineMaintainenceID = machineMaintainenceID;
    }

    /**
     * @return the mScheduleDate
     */
    public Date getmScheduleDate() {
        return mScheduleDate;
    }

    /**
     * @param mScheduleDate the mScheduleDate to set
     */
    public void setmScheduleDate(Date mScheduleDate) {
        this.mScheduleDate = mScheduleDate;
    }

    /**
     * @return the mScheduleHour
     */
    public String getmScheduleHour() {
        return mScheduleHour;
    }

    /**
     * @param mScheduleHour the mScheduleHour to set
     */
    public void setmScheduleHour(String mScheduleHour) {
        this.mScheduleHour = mScheduleHour;
    }

    /**
     * @return the maintainenceComments
     */
    public String getMaintainenceComments() {
        return maintainenceComments;
    }

    /**
     * @param maintainenceComments the maintainenceComments to set
     */
    public void setMaintainenceComments(String maintainenceComments) {
        this.maintainenceComments = maintainenceComments;
    }

    /**
     * @return the mServiceProvider
     */
    public String getmServiceProvider() {
        return mServiceProvider;
    }

    /**
     * @param mServiceProvider the mServiceProvider to set
     */
    public void setmServiceProvider(String mServiceProvider) {
        this.mServiceProvider = mServiceProvider;
    }

    /**
     * @return the mServiceContact
     */
    public String getmServiceContact() {
        return mServiceContact;
    }

    /**
     * @param mServiceContact the mServiceContact to set
     */
    public void setmServiceContact(String mServiceContact) {
        this.mServiceContact = mServiceContact;
    }

    public List<MachineMaintainenceEntity> getMaintainenceMachineWeek() {
        return machineSystemBean.machineMaintainenceListWeek();
    }

    public List<MachineMaintainenceEntity> getMaintainenceMachineExpired() {
        return machineSystemBean.machineMaintainenceListExpired();
    }

    public List<MachineMaintainenceEntity> getMaintainenceMachine() {
        return machineSystemBean.machineMaintainenceList();
    }

    public List<MachineEntity> getMachines() {
        return machineSystemBean.getAllMachine();
    }

    public List<MachineEntity> getExpiredMachines() {
        return machineSystemBean.checkMachineExpiry();
    }

    public List<String> getMachineNames() {
        return machineSystemBean.machineNames();
    }

    public List<String> getMachineMaint() {
        return machineSystemBean.machineMaintainenceNames();
    }

}
