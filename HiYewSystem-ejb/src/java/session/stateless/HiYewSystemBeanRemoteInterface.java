/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.EmployeeClaimEntity;
import entity.SupplierPurchaseOrder;
import entity.EmployeeEntity;
import entity.FillerComposition;
import entity.LeaveEntity;
import entity.MachineEntity;
import entity.MachineMaintainenceEntity;
import entity.MachineRepairEntity;
import entity.Metal;
import entity.PayrollEntity;
import entity.TrainingScheduleEntity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author JustHRJ
 */
@Remote
public interface HiYewSystemBeanRemoteInterface {

    public Vector addEmployee(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, Timestamp expiry, String contact, String addressPostal, String unit, String optional, double pay, Date employedDate, String employeeEmail);

    public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact, Date pass, String position, double pay, int leave, String email);

    public String applyLeave(String employee, int days, String remarks, Date start, Date end, String type);

    //view all pending leaves
    public List<Vector> viewAllLeave();

    // approve by leave id, employeeName
    public void approveLeaveID(Long id, String name);

    public List<EmployeeEntity> viewEmployee(String employeeName);

    // view by employeename
    public List<LeaveEntity> viewEmployeeLeave(String employeeName);

    public List<EmployeeEntity> viewAllEmployee();



    public String EmployeeStatus(String employeeName);

    public boolean approveByEmployee(String employee);

    public void cancelLeaveApplication(String employee, Long id);

    public List<LeaveEntity> viewEmployeeLeavePending(String employeeName);

    public String login(String username, String password);

    public List<LeaveEntity> viewEmployeeLeaveU(String username);

    public EmployeeEntity viewEmployeeU(String username);

    public List<String> getEmployee();

    public List<String> getEmployeeE(String username);

    public List<EmployeeEntity> expiredEmployees();

    public List<EmployeeEntity> expiredEmployee(String username);

    public boolean extendEmployeePass(String employeeName, Timestamp next);

    public int getENoAlert();

    public List<PayrollEntity> payRecords();

    public boolean createPayroll(String employeeName, int late, int sick, double overtime);

    public List<EmployeeEntity> expiredEmployees(String username);

    public int getENoAlert(String username);

    public List<PayrollEntity> getPayroll(String employeeName, String month);

    public boolean existEmployeeName(String employeeName);

    public boolean existEmployeeNumber(String employeeNumber);

    public boolean existEmployeeUsername(String username);

    public boolean notExistExpiredName(String name);

    public List<PayrollEntity> getReleasingPayRecords();

    public void releaseAllPay();

    public boolean updatePay(PayrollEntity pay, boolean bonus, double others);

    public boolean addTrainingSchedule(String trainingName, Date trainingStart, Date trainingEnd, String trainingDescription, int size, String trainingCode);

    public List<TrainingScheduleEntity> trainingSchedueList();

    public boolean addTrainingEmployee(TrainingScheduleEntity schedule, String name);

    public List<EmployeeEntity> employeeTraining(TrainingScheduleEntity schedule);

    public boolean deleteTrainingEmployee(TrainingScheduleEntity training, String employee);

    public List<PayrollEntity> getPayroll(String employeeName);

    public boolean deleteTraining(String trainingCode);

    public String changePassword(String employeeName, String oldPass, String newPass);

    public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact, String email);

  

    public void rejectLeaveID(Long id, String employee1);

    public List<String> employeeTrainingName(TrainingScheduleEntity schedule);

    public boolean updateTraining(TrainingScheduleEntity training, Date start, Date end, int size);

    public Vector resetPassword(String username, String secretQuestion, String secretAnswer);

    public List<LeaveEntity> employeeLeaveToday();

    public List<LeaveEntity> employeeLeave7days();

    public List<Vector> employeeTrainingToday();

    public List<Vector> employeeTraining7Days();

    public List<LeaveEntity> employeeLeaveMonth();

    public List<Vector> employeeTrainingMonth();

    public List<LeaveEntity> employeeLeaveTodayUser(String username);

    public List<Vector> employeeTrainingTodayUser(String username);

    public List<LeaveEntity> employeeLeave7daysUser(String username);

    public List<Vector> employeeTraining7DaysUser(String username);

    public List<LeaveEntity> employeeLeaveMonthUser(String username);

    public List<Vector> employeeTrainingMonthUser(String username);

    public void addNewAdmin(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, Timestamp expiry, String contact, String addressPostal, String unit, String optional, double employeePay, Date employedDate, String email, String password);

    public boolean applyClaim(String employeeName, EmployeeClaimEntity claim, String destination);

    public void attachDocument(EmployeeClaimEntity claim, String destination);

    public List<EmployeeClaimEntity> pendingClaimRecords();

    public void approveClaim(EmployeeClaimEntity claim);

    public List<EmployeeClaimEntity> approvedClaimRecords(String employeeName);

    public List<EmployeeClaimEntity> approvedClaimRecordsA(String employeeName, String months);

    public List<EmployeeClaimEntity> approvedClaimRecordsM(String months);

    public void rejectClaim(EmployeeClaimEntity c);

    public String getEmployeeEs(String username);

    public List<TrainingScheduleEntity> trainingScheduleListAvailable();

    public List<TrainingScheduleEntity> pastEmployeeTraining(EmployeeEntity employee);

    public void removeClaim(EmployeeClaimEntity claim);

    public String changePasswordF(String employeeName, String oldPass, String newPass, String secretQuestion, String secretAnswer);

    public boolean updateClaim(EmployeeClaimEntity claim, double amount, Date date);

    public String sendActivationCode(String email);

    public boolean checkActivationCode(String code);

    public void deleteActivationCode(String code);

}
