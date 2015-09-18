/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateful;

import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.MachineEntity;
import entity.PayrollEntity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.ejb.Local;

/**
 *
 * @author JustHRJ
 */
@Local
public interface HiYewSystemBeanLocal {

    public boolean addMachine(String machineName, String machineIdentity, Timestamp machineExpiry, String description, int extension);

    public void deleteMachine(String machineName);

    public boolean updateMachine(String machineName, MachineEntity machine, String status);

    public List<MachineEntity> checkMachineExpiry();

    public boolean addEmployee(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, String password, Timestamp expiry, String contact, String addressPostal, String unit, String optional);

    public boolean updateEmployee(EmployeeEntity employee, String a, String b, Date c, String d);

    public boolean applyLeave(String employee, int days, String remarks, Date start, Date end);

    //view all pending leaves

    public List<Vector> viewAllLeave();

    // approve by leave id, employeeName

    public void approveLeaveID(Long id, String name);

    public List<EmployeeEntity> viewEmployee(String employeeName);

    // view by employeename

    public List<LeaveEntity> viewEmployeeLeave(String employeeName);

    public List<EmployeeEntity> viewAllEmployee();

    public void disableEmployee(String employeeName);

    public String EmployeeStatus(String employeeName);

    public void approveByEmployee(String employee);

    public void cancelLeaveApplication(String employee, Long id);

    public List<LeaveEntity> viewEmployeeLeavePending(String employeeName);

    public List<MachineEntity> getAllMachine();

    public void extendMachineExpiry(String machineNumber);

    public String login(String username, String password);

    public List<LeaveEntity> viewEmployeeLeaveU(String username);

    public EmployeeEntity viewEmployeeU(String username);

    public List<String> getEmployee();

    public List<String> getEmployeeE(String username);

    public int getNoAlert();

    public List<EmployeeEntity> expiredEmployees();

    public List<EmployeeEntity> expiredEmployee(String username);

    public void extendEmployeePass(String employeeName, Timestamp next);

    public int getENoAlert();

    public List<Vector> payRecords();

    public boolean createPayroll(String employeeName, int late, int sick);

    public List<EmployeeEntity> expiredEmployees(String username);

    public int getENoAlert(String username);

    public List<Vector> getPayroll(String employeeName, String month);

    public boolean existEmployeeName(String employeeName);

    public boolean existEmployeeNumber(String employeeNumber);

    public boolean existEmployeeUsername(String username);

    public boolean notExistExpiredName(String name);

    public boolean notExistMachine(String id);

    public List<PayrollEntity> getReleasingPayRecords();

    public void releaseAllPay();

    public boolean updatePay(PayrollEntity pay, boolean bonus);
}
