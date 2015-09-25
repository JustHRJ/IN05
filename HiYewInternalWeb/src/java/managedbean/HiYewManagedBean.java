/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.MachineEntity;
import entity.MachineMaintainenceEntity;
import entity.PayrollEntity;
import entity.TrainingScheduleEntity;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import manager.EmailManager;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import session.stateful.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@ManagedBean
@RequestScoped
public class HiYewManagedBean {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private boolean bonus;
    private String employeeName = "";
    private String address_postal = "";
    private String employeeAddress = "";
    private String employeeAddressUnit = "";
    private String employeeAdressOptional = "";
    private String employeeEmail = "";
    private String employeePassNumber = "";
    private int employeeLeave = 0;
    private Long employeeId;
    private String employeePosition = "";
    private Date employeePassExpiry = null;
    private int leaveNumber = 0;
    private String employeeContact = "";
    private String leaveRemarks = "";
    private String fireOrDisabled;
    private String machine_status = "";
    private String username = "";
    private String password = "";
    private String oldPassword = "";
    private List<Long> machineMaintainenceIDList;
    private String machineName = "";
    private String machineId = "";
    private String machineDescript = "";
    private Date machineNxtMaint = null;
    private int machineSubMaint = 0;
    private Date startDate = null;
    private Date employedDate = null;
    private Date endDate = null;
    private int lateArrival = 0;
    private int absentee = 0;
    private String months = "";
    private String machineMaintainenceID = "";
    private double employeePay = 0.00;
    private Date mScheduleDate = null;
    private String mScheduleHour = "";
    private String maintainenceComments = "";
    private String mServiceProvider = "";
    private String mServiceContact = "";
    private String trainingDescription = "";
    private Date trainingStartDate = null;
    private Date trainingEndDate = null;
    private int trainingSize;
    private String trainingName = "";
    private String trainingCode = "";
    private String leaveType = "";

    /**
     * Creates a new instance of HiYewManagedBean
     */
    public HiYewManagedBean() {

    }

    /**
     * @return the employee_name
     */
    public String addMachine() {
        Timestamp machineTime = new Timestamp(machineNxtMaint.getTime());
        boolean check = hiYewSystemBean.addMachine(machineName, machineId, machineTime, machineDescript, machineSubMaint);
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

    public void updateTraining(RowEditEvent event) {
        boolean check = hiYewSystemBean.updateTraining((TrainingScheduleEntity) event.getObject(), trainingStartDate, trainingEndDate, trainingSize);
        if (check) {
            FacesMessage msg = new FacesMessage("Trainng edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Trainng not edited. Error, or nothing to edit");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void deleteTrainingSchedule() {
        boolean check = hiYewSystemBean.deleteTraining(trainingCode);
        if (!check) {
            FacesMessage msg = new FacesMessage("Failed to Delete", "Please check for correct scheduleCode");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }

    public void extendMachineMaintenance() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("machineName", machineName);

        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/MMS/addMaintainenceSchedule.xhtml");
    }

    public void redirectP() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/changePassword.xhtml");
    }

    public void deleteMachineMaintainence() throws IOException {

        boolean check = false;
        if (machineMaintainenceID.isEmpty()) {
            FacesMessage msg = new FacesMessage("Nothing to delete", machineMaintainenceID);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } else {
            check = hiYewSystemBean.deleteMachineMaintainence(machineMaintainenceID);
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
        boolean check = hiYewSystemBean.updateMachineSchedule((MachineMaintainenceEntity) event.getObject(), mScheduleDate, mScheduleHour, mServiceProvider, mServiceContact);
        if (check) {

            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("viewMaintainenceSchedule.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Not Edited", ((MachineMaintainenceEntity) event.getObject()).getMachine().getMachine_name());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void addTrainingSchedule() throws IOException {
        boolean check = false;
        if (trainingStartDate.after(trainingEndDate)) {
            check = false;
        } else {
            check = hiYewSystemBean.addTrainingSchedule(trainingName, trainingStartDate, trainingEndDate, trainingDescription, trainingSize, trainingCode);
        }
        if (check) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/HRMS/viewTraining.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Not Added", "Either existing schedule, else end date should not be before start date");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void addMachineSchedule() throws IOException {
        boolean check = hiYewSystemBean.addMachineMaintainence(machineName, getmScheduleDate(), getmScheduleHour(), getMaintainenceComments(), getmServiceProvider(), getmServiceContact());
        if (check) {
            FacesMessage msg = new FacesMessage("Schedule Added", machineName + " has a maintainence schedule");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/MMS/viewMaintainenceSchedule.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Failed to Add", "Please check for exisiting schedule");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public double calculateTotal(double basic, double bonus) {
        return basic + bonus;
    }

    public void updatePay(RowEditEvent event) {
        hiYewSystemBean.updatePay((PayrollEntity) event.getObject(), bonus);
    }

    public List<PayrollEntity> getPayrolls() {
        if (months == null) {
            return hiYewSystemBean.getPayroll(employeeName);
        }
        return hiYewSystemBean.getPayroll(employeeName, months);
    }

    public void registerFirst() throws IOException {
        Timestamp expiry = null;
        if (employeePassExpiry == null) {
            expiry = null;
        } else {
            expiry = new Timestamp(employeePassExpiry.getTime());
        }
        Vector result = hiYewSystemBean.addEmployee(employeeName, employeePassNumber, employeeAddress, employeeLeave, employeePosition, username, expiry, employeeContact, address_postal, employeeAddressUnit, employeeAdressOptional, employeePay, employedDate, employeeEmail);
        if (result != null) {
            EmailManager emailManager = new EmailManager();
            emailManager.emailPassword(result.get(0).toString(), result.get(1).toString(), result.get(2).toString(), "hurulez@gmail.com");
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("/HiYewInternalWeb/login.xhtml");

        }

    }

    public String extendEmployeePass() {
        Timestamp next = new Timestamp(employeePassExpiry.getTime());
        boolean check = hiYewSystemBean.extendEmployeePass(employeeName, next);
        if (check) {
            return "employee_details";
        } else {
            return "alertEmployee.xhtml";
        }
    }

    public String extendMachine() {
        hiYewSystemBean.extendMachineExpiry(machineId);
        return "viewMachine";
    }

    public Date getToday() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public void createPayroll() {
        boolean check = hiYewSystemBean.createPayroll(employeeName, lateArrival, absentee);
        if (check) {
            FacesMessage msg = new FacesMessage("Payroll created", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Failed to createPayroll", "Please recheck");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String getMonth2() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("MMM,yyyy");
        return format.format(c.getTime());
    }

    public String getMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMM,yyyy");
        return format.format(c.getTime());
    }

    public void changePassword() throws IOException {
        System.out.println(employeeName);
        boolean check = hiYewSystemBean.changePassword(employeeName, oldPassword, password);
        if (check) {
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("/HiYewInternalWeb/login.xhtml");
        } else {
            FacesMessage msg = new FacesMessage("Failed to change", "Please check old password / same previous password");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void addEmployee() throws IOException {
        Timestamp expiry = null;
        if (employeePassExpiry == null) {
            expiry = null;
        } else {
            expiry = new Timestamp(employeePassExpiry.getTime());
        }
        Vector result = hiYewSystemBean.addEmployee(employeeName, employeePassNumber, employeeAddress, employeeLeave, employeePosition, username, expiry, employeeContact, address_postal, employeeAddressUnit, employeeAdressOptional, employeePay, employedDate, employeeEmail);
        if (result != null) {
            EmailManager emailManager = new EmailManager();
            emailManager.emailPassword(result.get(0).toString(), result.get(1).toString(), result.get(2).toString(), result.get(3).toString());

            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("/HiYewInternalWeb/HRMS/employee_details.xhtml");

        } else {
            FacesMessage msg = new FacesMessage("Failed to Add", "Please check if existing username and other details");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }

    public void resetPassword() throws IOException {
        Vector result = hiYewSystemBean.resetPassword(username);
        if (result == null || result.size() == 0) {
            FacesMessage msg = new FacesMessage("Failed to Reset Password", "Account is disabled");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            EmailManager emailManager = new EmailManager();
            emailManager.emailPassword(result.get(0).toString(), result.get(1).toString(), result.get(2).toString(), result.get(3).toString());
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("/HiYewInternalWeb/HRMS/login.xhtml");

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

    public List<Long> getMaintainenceIDList() {
        return machineMaintainenceIDList;
    }

    public void approveLeave() {
        hiYewSystemBean.approveLeaveID((Long.valueOf(objectId1).longValue()), objectId);
    }

    public void rejectLeave() {
        hiYewSystemBean.rejectLeaveID((Long.valueOf(objectId1).longValue()), objectId);
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
        boolean check;

        if (leaveType.equals("paid")) {
            if (leaveNumber > 10) {
                check = false;
            } else {
                check = hiYewSystemBean.applyLeave(employeeName, leaveNumber, leaveRemarks, startDate, endDate, leaveType);
            }
        } else {
            check = hiYewSystemBean.applyLeave(employeeName, leaveNumber, leaveRemarks, startDate, endDate, leaveType);
        }
        if (check) {
            FacesMessage msg = new FacesMessage("Applied", employeeName);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Failed To Apply", employeeName);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void onMachineChange() {
        if (machineName != null && !machineName.equals("")) {
            machineMaintainenceIDList = hiYewSystemBean.getMachineMaintID(machineName);
        } else {
            machineMaintainenceIDList = new ArrayList<Long>();
        }
    }

    public void releaseAllPay() {
        hiYewSystemBean.releaseAllPay();
    }

    public void updateMachinery(RowEditEvent event) {
        boolean check = hiYewSystemBean.updateMachine(machineName, (MachineEntity) event.getObject(), machine_status, machineNxtMaint);
        if (check) {
            FacesMessage msg = new FacesMessage("Edited", ((MachineEntity) event.getObject()).getMachine_number());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Not Edited", ((MachineEntity) event.getObject()).getMachine_number());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void indexRedirect() throws IOException {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesCtx.getExternalContext();
        externalContext.redirect("/HiYewExternalWeb/csLoginPage.xhtml");
    }

    public void updateEmployee(RowEditEvent event) {

        boolean check = false;
        System.out.println("here");
        System.out.println(employeePay);
        System.out.println(employeeLeave);
        if (employeePassExpiry == null && employeePosition.isEmpty() && employeePay == 0 && employeeLeave == 0) {
            check = hiYewSystemBean.updateEmployee((EmployeeEntity) event.getObject(), employeeAddress, employeeAddressUnit, employeeAdressOptional, address_postal, employeeContact, employeeEmail);
        } else {
            System.out.println("here");
            check = hiYewSystemBean.updateEmployee((EmployeeEntity) event.getObject(), employeeAddress, employeeAddressUnit, employeeAdressOptional, address_postal, employeeContact, employeePassExpiry, employeePosition, employeePay, employeeLeave, employeeEmail);
            System.out.println(check);
        }
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
    public Date getEmployeePassExpiry() {
        return employeePassExpiry;
    }

    /**
     * @param employeePassExpiry the employeePassExpiry to set
     */
    public void setEmployeePassExpiry(Date employeePassExpiry) {
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
     * @return the months
     */
    public String getMonths() {
        return months;
    }

    /**
     * @param months the months to set
     */
    public void setMonths(String months) {
        this.months = months;
    }

    /**
     * @return the address_postal
     */
    public String getAddress_postal() {
        return address_postal;
    }

    /**
     * @param address_postal the address_postal to set
     */
    public void setAddress_postal(String address_postal) {
        this.address_postal = address_postal;
    }

    /**
     * @return the employeeAddressUnit
     */
    public String getEmployeeAddressUnit() {
        return employeeAddressUnit;
    }

    /**
     * @param employeeAddressUnit the employeeAddressUnit to set
     */
    public void setEmployeeAddressUnit(String employeeAddressUnit) {
        this.employeeAddressUnit = employeeAddressUnit;
    }

    /**
     * @return the employeeAdressOptional
     */
    public String getEmployeeAdressOptional() {
        return employeeAdressOptional;
    }

    /**
     * @param employeeAdressOptional the employeeAdressOptional to set
     */
    public void setEmployeeAdressOptional(String employeeAdressOptional) {
        this.employeeAdressOptional = employeeAdressOptional;
    }

    /**
     * @return the bonus
     */
    public boolean isBonus() {
        return bonus;
    }

    /**
     * @param bonus the bonus to set
     */
    public void setBonus(boolean bonus) {
        this.bonus = bonus;
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
     * @return the trainingDescription
     */
    public String getTrainingDescription() {
        return trainingDescription;
    }

    /**
     * @param trainingDescription the trainingDescription to set
     */
    public void setTrainingDescription(String trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    /**
     * @return the trainingStartDate
     */
    public Date getTrainingStartDate() {
        return trainingStartDate;
    }

    /**
     * @param trainingStartDate the trainingStartDate to set
     */
    public void setTrainingStartDate(Date trainingStartDate) {
        this.trainingStartDate = trainingStartDate;
    }

    /**
     * @return the trainingEndDate
     */
    public Date getTrainingEndDate() {
        return trainingEndDate;
    }

    /**
     * @param trainingEndDate the trainingEndDate to set
     */
    public void setTrainingEndDate(Date trainingEndDate) {
        this.trainingEndDate = trainingEndDate;
    }

    /**
     * @return the trainingSize
     */
    public int getTrainingSize() {
        return trainingSize;
    }

    /**
     * @param trainingSize the trainingSize to set
     */
    public void setTrainingSize(int trainingSize) {
        this.trainingSize = trainingSize;
    }

    /**
     * @return the trainingName
     */
    public String getTrainingName() {
        return trainingName;
    }

    /**
     * @param trainingName the trainingName to set
     */
    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    /**
     * @return the trainingCode
     */
    public String getTrainingCode() {
        return trainingCode;
    }

    /**
     * @param trainingCode the trainingCode to set
     */
    public void setTrainingCode(String trainingCode) {
        this.trainingCode = trainingCode;
    }

    /**
     * @return the employeePay
     */
    public double getEmployeePay() {
        return employeePay;
    }

    /**
     * @param employeePay the employeePay to set
     */
    public void setEmployeePay(double employeePay) {
        this.employeePay = employeePay;
    }

    /**
     * @return the employedDate
     */
    public Date getEmployedDate() {
        return employedDate;
    }

    /**
     * @param employedDate the employedDate to set
     */
    public void setEmployedDate(Date employedDate) {
        this.employedDate = employedDate;
    }

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the employeeEmail
     */
    public String getEmployeeEmail() {
        return employeeEmail;
    }

    /**
     * @param employeeEmail the employeeEmail to set
     */
    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    /**
     * @return the leaveType
     */
    public String getLeaveType() {
        return leaveType;
    }

    /**
     * @param leaveType the leaveType to set
     */
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    /**
     * @return the selectedEmployee
     */
}
