/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.MachineEntity;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;
import stateful.session.HiYewSystemServerRemote;

/**
 *
 * @author JustHRJ
 */
@ManagedBean
@RequestScoped
public class HiYewManagedBean {

    @EJB
    private HiYewSystemServerRemote hiYewSystemBean;
    private String employeeName;
    private String employeeAddress;
    private String employeePassNumber;
    private int employeeLeave;
    private Long employeeId;
    private String employeePosition;
    private String employeePassExpiry;
    private int leaveNumber;
    private String employeeContact;
    private String leaveRemarks;
    private String fireOrDisabled;
    private String username;
    private String password;
    private String loginPosition = "";
    private String machineName;
    private String machineId;
    private String machineDescript;
    private String machineNxtMaint;
    private int machineSubMaint;
    private Date startDate;
    private Date endDate;
    private int lateArrival;
    private int absentee;

    /**
     * Creates a new instance of HiYewManagedBean
     */
    public HiYewManagedBean() {

    }

    /**
     * @return the employee_name
     */
    public void addMachine() {
        Timestamp machineTime = java.sql.Timestamp.valueOf(machineNxtMaint + " 00:00:00");
        boolean check = hiYewSystemBean.addMachine(machineName, machineId, machineTime, machineDescript, machineSubMaint);
        if (check) {
            FacesMessage msg = new FacesMessage("Machine Added", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Failed to Add", "Please check for existing machine details");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String registerFirst() {
        Timestamp expiry = null;
        if (employeePassExpiry.isEmpty()) {
            expiry = null;
        } else {
            expiry = java.sql.Timestamp.valueOf(employeePassExpiry + " 00:00:00");
        }
        hiYewSystemBean.addEmployee(employeeName, employeePassNumber, employeeAddress, employeeLeave, password, username, password, expiry, employeeContact);
        return "login";
    }

    public String getEAlert() {
        int noOfAlert = hiYewSystemBean.getENoAlert();
        return "Pass Expiry Alert (" + String.valueOf(noOfAlert) + ")";
    }

    public String extendEmployeePass() {
        Timestamp next = java.sql.Timestamp.valueOf(employeePassExpiry + " 00:00:00");
        hiYewSystemBean.extendEmployeePass(employeeName, next);
        return "employee_details";
    }

    public String extendMachine() {
        hiYewSystemBean.extendMachineExpiry(machineId);
        return "viewMachine";
    }

    public Date getToday() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public void createPayroll(){
        boolean check = hiYewSystemBean.createPayroll(employeeName, lateArrival, absentee);
        if(check){
            FacesMessage msg = new FacesMessage("Payroll created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
            FacesMessage msg = new FacesMessage("Failed to createPayroll", "Please recheck");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public String getMonth(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        return format.format(c.getTime());
    }
    
    public String addEmployee() {
        Timestamp expiry = null;
        if (employeePassExpiry.isEmpty()) {
            expiry = null;
        } else {
            expiry = java.sql.Timestamp.valueOf(employeePassExpiry + " 00:00:00");
        }
        boolean result = hiYewSystemBean.addEmployee(employeeName, employeePassNumber, employeeAddress, employeeLeave, employeePosition, username, password, expiry, employeeContact);
        if (result) {
            return "employee_details";
        } else {
            FacesMessage msg = new FacesMessage("Failed to Add", "Please check if existing username and other details");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "employeeRegistration";

        }
    }

    public String getMAlert() {

        int noOfAlert = hiYewSystemBean.getNoAlert();

        return "Machine Alert (" + String.valueOf(noOfAlert) + ")";
    }

    public void setFireOrDisabled(String tes) {
        this.fireOrDisabled = tes;
    }

    public void fireEmployee() {
        hiYewSystemBean.disableEmployee(objectId);
    }

    public List<EmployeeEntity> getEmployees() {
        return hiYewSystemBean.viewEmployee(objectId);
    }

    public void approveLeave() {
        hiYewSystemBean.approveLeaveID((Long.valueOf(objectId1).longValue()), objectId);
    }

    public void approveLeaveEs() {
        hiYewSystemBean.approveByEmployee(employeeName);
    }

    public List<LeaveEntity> getLeaveE() {
        return hiYewSystemBean.viewEmployeeLeavePending(employeeName);
    }

    private int computeNumberLeave(Date start, Date end) {
        int diffInDays = (int) ((end.getTime() - start.getTime())
                / (1000 * 60 * 60 * 24));
        return diffInDays + 1;
    }

    public void applyLeave() {
        leaveNumber = computeNumberLeave(startDate, endDate);
        boolean check = hiYewSystemBean.applyLeave(employeeName, leaveNumber, leaveRemarks, startDate, endDate);
        if (check) {
            FacesMessage msg = new FacesMessage("Applied", employeeName);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Failed To Apply", employeeName);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void updateMachinery(RowEditEvent event) {
        hiYewSystemBean.updateMachine(machineName, (MachineEntity) event.getObject());
        FacesMessage msg = new FacesMessage("Edited", ((MachineEntity) event.getObject()).getMachine_name());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void updateEmployee(RowEditEvent event) {

        boolean check = hiYewSystemBean.updateEmployee((EmployeeEntity) event.getObject(), employeeAddress, employeeContact, employeePassExpiry);

        if (check) {
            FacesMessage msg = new FacesMessage("Edited", ((EmployeeEntity) event.getObject()).getEmployee_name());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Not Edited", ((EmployeeEntity) event.getObject()).getEmployee_name());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the employeeAddress
     */
    public String getEmployeeAddress() {
        return employeeAddress;
    }

    /**
     * @param employeeAddress the employeeAddress to set
     */
    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    /**
     * @return the employeePassNumber
     */
    public String getEmployeePassNumber() {
        return employeePassNumber;
    }

    /**
     * @param employeePassNumber the employeePassNumber to set
     */
    public void setEmployeePassNumber(String employeePassNumber) {
        this.employeePassNumber = employeePassNumber;
    }

    /**
     * @return the employeeLeave
     */
    public int getEmployeeLeave() {
        return employeeLeave;
    }

    /**
     * @param employeeLeave the employeeLeave to set
     */
    public void setEmployeeLeave(int employeeLeave) {
        this.employeeLeave = employeeLeave;
    }

    /**
     * @return the employeeId
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the employeePosition
     */
    public String getEmployeePosition() {
        return employeePosition;
    }

    /**
     * @param employeePosition the employeePosition to set
     */
    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    FacesContext context = FacesContext.getCurrentInstance();
    private String objectId = context.getExternalContext()
            .getRequestParameterMap().get("objectId");

    FacesContext context1 = FacesContext.getCurrentInstance();
    private String objectId1 = context.getExternalContext()
            .getRequestParameterMap().get("objectId1");

    /**
     * @return the objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return the leaveNumber
     */
    public int getLeaveNumber() {
        return leaveNumber;
    }

    /**
     * @param leaveNumber the leaveNumber to set
     */
    public void setLeaveNumber(int leaveNumber) {
        this.leaveNumber = leaveNumber;
    }

    /**
     * @return the remarks
     */
    public String getLeaveRemarks() {
        return leaveRemarks;
    }

    /**
     * @return the objectId1
     */
    public String getObjectId1() {
        return objectId1;
    }

    /**
     * @param objectId1 the objectId1 to set
     */
    public void setObjectId1(String objectId1) {
        this.objectId1 = objectId1;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setLeaveRemarks(String remarks) {
        this.leaveRemarks = remarks;
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
    public String getMachineNxtMaint() {
        return machineNxtMaint;
    }

    /**
     * @param machineNxtMaint the machineNxtMaint to set
     */
    public void setMachineNxtMaint(String machineNxtMaint) {
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

    public String getFireOrDisabled() {
        String status = hiYewSystemBean.EmployeeStatus(objectId);
        if (status.equals("disabled")) {
            return "enabled";
        } else {
            return "fire/enable";
        }
    }

    public void deleteMachine() {
        hiYewSystemBean.deleteMachine(objectId);
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the loginPosition
     */
    public String getLoginPosition() {
        return loginPosition;
    }

    /**
     * @param loginPosition the loginPosition to set
     */
    public void setLoginPosition(String loginPosition) {
        this.loginPosition = loginPosition;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the employeePassExpiry
     */
    public String getEmployeePassExpiry() {
        return employeePassExpiry;
    }

    /**
     * @param employeePassExpiry the employeePassExpiry to set
     */
    public void setEmployeePassExpiry(String employeePassExpiry) {
        this.employeePassExpiry = employeePassExpiry;
    }

    /**
     * @return the employeeContact
     */
    public String getEmployeeContact() {
        return employeeContact;
    }

    /**
     * @param employeeContact the employeeContact to set
     */
    public void setEmployeeContact(String employeeContact) {
        this.employeeContact = employeeContact;
    }

    /**
     * @return the lateArrival
     */
    public int getLateArrival() {
        return lateArrival;
    }

    /**
     * @param lateArrival the lateArrival to set
     */
    public void setLateArrival(int lateArrival) {
        this.lateArrival = lateArrival;
    }

    /**
     * @return the absentee
     */
    public int getAbsentee() {
        return absentee;
    }

    /**
     * @param absentee the absentee to set
     */
    public void setAbsentee(int absentee) {
        this.absentee = absentee;
    }
}
