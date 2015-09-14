/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateful.session;

import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.MachineEntity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.ejb.Remote;

/**
 *
 * @author JustHRJ
 */
@Remote
public interface HiYewSystemServerRemote {

    public boolean addMachine(String machineName, String machineIdentity, Timestamp machineExpiry, String description, int extension);

    public void deleteMachine(String machineName);

    public void updateMachine(String machineName, MachineEntity machine);

    public List<MachineEntity> checkMachineExpiry();
    
    public boolean addEmployee(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, String password, Timestamp expiry,String contact);

    public boolean updateEmployee(EmployeeEntity employee, String a, String b, String c );
    
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
    
}
