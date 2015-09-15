/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateful.session;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.MachineEntity;
import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.PayrollEntity;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.Query;

/**
 *
 * @author JustHRJ
 */
@Stateful
public class HiYewSystemBean implements HiYewSystemServerRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    //all this is for machines
    public boolean addMachine(String machineName, String machineIdentity, Timestamp machineExpiry, String description, int extension) {
        MachineEntity machine = new MachineEntity();
        try {
            Query q = em.createQuery("Select machine from MachineEntity machine where machine.machine_number = :id");
            q.setParameter("id", machineIdentity);
            machine = (MachineEntity) q.getSingleResult();
            return false;
        } catch (Exception ex) {
            machine.setMachine_name(machineName);
            machine.setMachine_number(machineIdentity);
            machine.setMachine_expiry(machineExpiry);
            machine.setStatus("available");
            machine.setDescription(description);
            machine.setExtension(extension);
            em.persist(machine);
            return true;
        }

    }

    public boolean createPayroll(String employeeName, int late, int sick) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.employee_name =:id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            Collection<PayrollEntity> pay = e.getPayRecords();
            for (Object o : pay) {
                PayrollEntity p = (PayrollEntity) o;
                if (p.getStatus().equals("unset")) {
                    p.setStatus("set");
                    if (late < 3 && sick < 2) {
                        p.setBonus(true);
                    } else {
                        p.setBonus(false);
                    }
                    em.merge(p);
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Vector> payRecords() {
        Query q = em.createQuery("select c from EmployeeEntity c");
        List<Vector> result = new ArrayList();
        for (Object o : q.getResultList()) {
            EmployeeEntity e = (EmployeeEntity) o;
            Collection<PayrollEntity> pays = e.getPayRecords();
            for (Object d : pays) {
                PayrollEntity p = (PayrollEntity) d;
                if (p.getStatus().equals("unset")) {
                    Vector im = new Vector();
                    im.add(e.getEmployee_name());
                    im.add(e.getEmployee_basic());
                    im.add("unconfirm");
                    im.add("unconfirm");
                    result.add(im);
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public List<String> getEmployee() {
        List<String> result = new ArrayList<String>();
        Query q = em.createQuery("select c from EmployeeEntity c");
        for (Object o : q.getResultList()) {
            EmployeeEntity e = (EmployeeEntity) o;
            result.add(e.getEmployee_name());
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public List<String> getEmployeeE(String username) {
        List<String> result = new ArrayList<String>();
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            result.add(e.getEmployee_name());
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<MachineEntity> getAllMachine() {
        Query q = em.createQuery("Select c from MachineEntity c");
        List<MachineEntity> machineRecords = new ArrayList<MachineEntity>();
        for (Object o : q.getResultList()) {
            MachineEntity machine = (MachineEntity) o;
            machineRecords.add(machine);
        }
        if (machineRecords.isEmpty()) {
            return null;
        } else {
            return machineRecords;
        }
    }

    public void deleteMachine(String machineName) {
        MachineEntity machine = new MachineEntity();
        try {
            Query q = em.createQuery("Select machine from MachineEntity machine where machine.machine_name = :id");
            q.setParameter("id", machineName);
            machine = (MachineEntity) q.getSingleResult();
            machine.setStatus("disable");
            em.merge(machine);
        } catch (Exception ex) {

        }
    }

    public void updateMachine(String machineName, MachineEntity machine) {
        try {
            machine.setMachine_name(machineName);
            em.merge(machine);
        } catch (Exception ex) {

        }
    }

// still having trouble comparing due to calendar comparison issues. to try different approach
    public List<MachineEntity> checkMachineExpiry() {

        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.MONTH, 1);
        ts.setTime(cal.getTime().getTime());
        List<MachineEntity> results = new ArrayList<MachineEntity>();
        Query q = em.createQuery("select c from MachineEntity c");

        for (Object o : q.getResultList()) {
            MachineEntity machine = (MachineEntity) o;
            Timestamp time = machine.getMachine_expiry();
            if (ts.after(time)) {
                results.add(machine);
            }
        }
        if (results.isEmpty()) {
            return null;
        }
        return results;
    }

    private boolean checkPass(String pass) {
        try {
            EmployeeEntity e = new EmployeeEntity();
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_passNumber = :id");
            q.setParameter("id", pass);
            e = (EmployeeEntity) q.getSingleResult();
            return false;
        } catch (Exception ex) {
            return true;
        }
    }

    public boolean addEmployee(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, String password, Timestamp expiry, String contact) {
        EmployeeEntity xin = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select xin from EmployeeEntity xin where xin.employee_name = :id");
            q.setParameter("id", employee);
            xin = (EmployeeEntity) q.getSingleResult();
            return false;
        } catch (Exception ex) {
            boolean check = checkUsername(username);
            boolean check2 = checkPass(employee_passNumber);
            if (check && check2) {
                xin.setEmployee_name(employee);
                xin.setEmployee_address(employee_address);
                xin.setEmployee_passNumber(employee_passNumber);
                xin.setNumber_of_leaves(number_of_leave);
                Collection<LeaveEntity> leaveRecords = new ArrayList();
                xin.setLeaveRecords(leaveRecords);
                Collection<PayrollEntity> payRecords = new ArrayList();
                xin.setPayRecords(payRecords);
                xin.setEmployee_account_status(position);
                xin.setPreviousPosition("none");
                xin.setUsername(username);
                String passwordHashed = hashingPassword(password);
                xin.setPassword(passwordHashed);
                xin.setEmployee_passExpiry(expiry);
                xin.setEmployee_contact(contact);
                Calendar c = Calendar.getInstance();
                Timestamp ts = new Timestamp(c.getTime().getTime());
                xin.setEmployee_employedDate(ts);
                em.persist(xin);
                return true;
            } else {
                return false;
            }
        }
    }

    public List<EmployeeEntity> expiredEmployees(String username) {
        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.MONTH, 1);
        ts.setTime(cal.getTime().getTime());
        List<EmployeeEntity> results = new ArrayList<EmployeeEntity>();
        EmployeeEntity c = new EmployeeEntity();
        try {
            Query q = em.createQuery("select c from EmployeeEntity c where c.username =:id");
            q.setParameter("id", username);
            c = (EmployeeEntity) q.getSingleResult();
            if (c.getEmployee_passExpiry() != null) {
                Timestamp time = c.getEmployee_passExpiry();

                if (ts.after(time)) {
                    results.add(c);
                }
            }
            if (results.isEmpty()) {
                return null;
            }
            return results;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<EmployeeEntity> expiredEmployees() {
        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.MONTH, 1);
        ts.setTime(cal.getTime().getTime());
        List<EmployeeEntity> results = new ArrayList<EmployeeEntity>();
        Query q = em.createQuery("select c from EmployeeEntity c");

        for (Object o : q.getResultList()) {
            EmployeeEntity employee = (EmployeeEntity) o;
            if (employee.getEmployee_passExpiry() != null) {
                Timestamp time = employee.getEmployee_passExpiry();

                if (ts.after(time)) {
                    results.add(employee);
                }
            }
        }
        if (results.isEmpty()) {
            return null;
        }
        return results;
    }

    public int getENoAlert() {
        List<EmployeeEntity> employees = expiredEmployees();
        if (employees == null) {
            return 0;
        } else {
            return employees.size();
        }

    }

    public int getENoAlert(String username) {
        List<EmployeeEntity> employees = expiredEmployees(username);
        if (employees == null) {
            return 0;
        } else {
            return employees.size();
        }

    }

    public int getNoAlert() {
        List<MachineEntity> machines = checkMachineExpiry();
        if (machines == null) {
            return 0;
        } else {
            return machines.size();
        }
    }

    public void extendEmployeePass(String employeeName, Timestamp next) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            e.setEmployee_passExpiry(next);
            em.merge(e);

        } catch (Exception ex) {

        }
    }

    public List<EmployeeEntity> expiredEmployee(String username) {
        Timestamp ts = new Timestamp(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.MONTH, 1);
        ts.setTime(cal.getTime().getTime());
        List<EmployeeEntity> results = new ArrayList<EmployeeEntity>();
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.username = :id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            Timestamp time = e.getEmployee_passExpiry();
            if (ts.after(time)) {
                results.add(e);
            }

            if (results.isEmpty()) {
                return null;
            }
            return results;
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean updateEmployee(EmployeeEntity employee, String employeeA, String contact, String pass, String position) {
        boolean check = false;
        if (!(employeeA.isEmpty())) {
            employee.setEmployee_address(employeeA);
        }
        if (!(contact.isEmpty())) {
            employee.setEmployee_contact(contact);
        }
        if (!(pass.isEmpty())) {
            Timestamp time = java.sql.Timestamp.valueOf(pass + " 00:00:00");
            employee.setEmployee_passExpiry(time);
        }
        
        if(!(position.isEmpty())){
            if(!(employee.getEmployee_account_status().equals(position))){
                if(position.equals("Disable")){
                    employee.setPreviousPosition(employee.getEmployee_account_status());
                    employee.setEmployee_account_status(position);
                    check = true;
                }else{
                    employee.setEmployee_account_status(position);
                    check = true;
                }
            }
        }
        
        if ((!(pass.isEmpty())) || (!(contact.isEmpty())) || (!(employeeA.isEmpty())) || check) {
            em.merge(employee);
            return true;
        }
        return false;
    }

    private boolean checkBetween(Collection<LeaveEntity> leaveRecords, Date start, Date end) {
        Timestamp sd = new Timestamp(start.getTime());
        Timestamp ed = new Timestamp(end.getTime());
        for (Object o : leaveRecords) {
            LeaveEntity l = (LeaveEntity) o;
            if (sd.before(l.getStartDate()) && ed.after(l.getEndDate()) || (sd.after(l.getStartDate()) && sd.before(l.getEndDate())) || ed.after(l.getStartDate()) && ed.before(l.getEndDate()) || sd.equals(l.getStartDate()) || sd.equals(l.getEndDate()) || ed.equals(l.getStartDate()) || ed.equals(l.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    public boolean applyLeave(String employee, int days, String remarks, Date start, Date end) {
        EmployeeEntity lao = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select lao from EmployeeEntity lao where lao.employee_name = :id");
            q.setParameter("id", employee);
            lao = (EmployeeEntity) q.getSingleResult();

            Collection<LeaveEntity> leaves = lao.getLeaveRecords();
            int sum = 0;

            for (Object o : leaves) {
                LeaveEntity l = (LeaveEntity) o;
                if (l.getStatus().equals("pending")) {
                    sum += l.getNumber_of_leave();
                }
            }
            if (lao.getNumber_of_leaves() < days + sum) {
                return false;
            } else if (lao.getEmployee_account_status().equals("disabled")) {
                return false;
            } else if (days < 1) {
                return false;
            } else if (checkBetween(leaves, start, end)) {
                return false;
            } else {
                LeaveEntity application = new LeaveEntity();
                application.setRemarks(remarks);
                application.setNumber_of_leave(days);
                java.util.Date date = new java.util.Date();
                Timestamp time_applied = new Timestamp(date.getTime());
                Timestamp sd = new Timestamp(start.getTime());
                Timestamp ed = new Timestamp(end.getTime());
                application.setAppliedTime(time_applied);
                application.setEndDate(ed);
                application.setStartDate(sd);
                application.setStatus("pending");
                em.persist(application);
                lao.getLeaveRecords().add(application);
                em.merge(lao);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Vector> viewAllLeave() {
        List<Vector> allRecords = new ArrayList();
        Query q = em.createQuery("Select c from EmployeeEntity c");

        for (Object o : q.getResultList()) {
            EmployeeEntity employee = (EmployeeEntity) o;
            Collection<LeaveEntity> appliedLeaves = employee.getLeaveRecords();
            for (Object v : appliedLeaves) {
                LeaveEntity leave = (LeaveEntity) v;
                if (leave.getStatus().equals("pending")) {
                    Vector im = new Vector();
                    im.add(employee.getEmployee_name());
                    im.add(leave.getId());
                    im.add(leave.getNumber_of_leave());
                    im.add(leave.getStartDate());
                    im.add(leave.getEndDate());
                    im.add(leave.getRemarks());
                    im.add(leave.getAppliedTime());
                    allRecords.add(im);
                }
            }
        }

        if (allRecords.isEmpty()) {
            return null;
        }
        return allRecords;
    }

    public void approveLeaveID(Long id, String employee1) {
        LeaveEntity leave = new LeaveEntity();
        EmployeeEntity employee = new EmployeeEntity();
        try {
            Query q = em.createQuery("select employee from EmployeeEntity employee where employee.employee_name =:id");
            q.setParameter("id", employee1);
            employee = (EmployeeEntity) q.getSingleResult();

            Collection<LeaveEntity> leaveRecords = employee.getLeaveRecords();
            for (Object o : leaveRecords) {
                leave = (LeaveEntity) o;
                if (leave.getId() == id) {
                    if (employee.getNumber_of_leaves() >= leave.getNumber_of_leave()) {
                        leave.setStatus("approved");
                        java.util.Date date = new java.util.Date();
                        Timestamp time = new Timestamp(date.getTime());
                        leave.setApprovedTime(time);
                        em.merge(leave);
                        employee.setNumber_of_leaves(employee.getNumber_of_leaves() - leave.getNumber_of_leave());
                        em.merge(employee);
                    }
                }
            }

        } catch (Exception ex) {

        }
    }

    public List<LeaveEntity> viewEmployeeLeavePending(String employeeName) {
        EmployeeEntity employee = new EmployeeEntity();
        List<LeaveEntity> allRecords = new ArrayList<LeaveEntity>();
        try {
            Query q = em.createQuery("Select employee from EmployeeEntity employee where employee.employee_name = :id");
            q.setParameter("id", employeeName);
            employee = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> leaveRecords = employee.getLeaveRecords();
            if (leaveRecords.isEmpty()) {
                return null;
            } else {
                for (Object o : leaveRecords) {
                    LeaveEntity leave = (LeaveEntity) o;
                    if (leave.getStatus().equals("pending")) {
                        allRecords.add(leave);
                    }
                }
            }
            return allRecords;
        } catch (Exception ex) {
            return null;
        }

    }

    public List<EmployeeEntity> viewEmployee(String employeeName) {
        EmployeeEntity employee = new EmployeeEntity();
        List<EmployeeEntity> imm = new ArrayList<EmployeeEntity>();
        try {
            Query q = em.createQuery("Select employee from EmployeeEntity employee where employee.username = :id");
            q.setParameter("id", employeeName);
            employee = (EmployeeEntity) q.getSingleResult();
            imm.add(employee);
            return imm;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<LeaveEntity> viewEmployeeLeave(String employeeName) {
        EmployeeEntity employee = new EmployeeEntity();
        List<LeaveEntity> allRecords = new ArrayList<LeaveEntity>();
        try {
            Query q = em.createQuery("Select employee from EmployeeEntity employee where employee.employee_name = :id");
            q.setParameter("id", employeeName);
            employee = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> leaveRecords = employee.getLeaveRecords();
            if (leaveRecords.isEmpty()) {
                return null;
            } else {
                for (Object o : leaveRecords) {
                    LeaveEntity leave = (LeaveEntity) o;
                    allRecords.add(leave);
                }
                return allRecords;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<EmployeeEntity> viewAllEmployee() {
        Query q = em.createQuery("Select c from EmployeeEntity c");
        List<EmployeeEntity> allEmployee = new ArrayList<EmployeeEntity>();
        for (Object o : q.getResultList()) {
            EmployeeEntity employee = (EmployeeEntity) o;
            allEmployee.add(employee);
        }
        if (allEmployee.isEmpty()) {
            return null;
        }
        return allEmployee;

    }

    public void disableEmployee(String employeeName) {
        EmployeeEntity employee = new EmployeeEntity();
        try {
            Query q = em.createQuery("select employee from EmployeeEntity employee where employee.employee_name = :id");
            q.setParameter("id", employeeName);
            employee = (EmployeeEntity) q.getSingleResult();
            if (!(employee.getEmployee_account_status().equals("disabled"))) {
                employee.setPreviousPosition(employee.getEmployee_account_status());
                employee.setEmployee_account_status("disabled");
                Collection<LeaveEntity> leaveRecords = employee.getLeaveRecords();
                for (Object o : leaveRecords) {
                    LeaveEntity record = (LeaveEntity) o;
                    String status = record.getStatus();
                    if (status.equals("pending")) {
                        record.setStatus("disabled");
                        em.merge(record);
                    }
                }
                em.merge(employee);
            } else {
                employee.setEmployee_account_status(employee.getPreviousPosition());
                employee.setPreviousPosition("none");
                Collection<LeaveEntity> leaveRecords = employee.getLeaveRecords();
                for (Object o : leaveRecords) {
                    LeaveEntity record = (LeaveEntity) o;
                    String status = record.getStatus();
                    if (status.equals("disabled")) {
                        record.setStatus("pending");
                        em.merge(record);
                    }
                }
                em.merge(employee);
            }
        } catch (Exception ex) {

        }
    }

    public String EmployeeStatus(String employeeName) {
        EmployeeEntity employee = new EmployeeEntity();
        try {
            Query q = em.createQuery("select employee from EmployeeEntity employee where employee.employee_name = :id");
            q.setParameter("id", employeeName);
            employee = (EmployeeEntity) q.getSingleResult();
            return employee.getEmployee_account_status();
        } catch (Exception ex) {
            return "";
        }
    }

    public void approveByEmployee(String employee) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employee);
            e = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> leaveRecords = e.getLeaveRecords();
            int allRight = validateLeaves(leaveRecords);

            if (allRight <= e.getNumber_of_leaves()) {
                for (Object o : leaveRecords) {
                    LeaveEntity l = (LeaveEntity) o;
                    if (l.getStatus().equals("pending")) {
                        l.setStatus("approved");
                        java.util.Date date = new java.util.Date();
                        Timestamp approveTime = new Timestamp(date.getTime());
                        l.setApprovedTime(approveTime);
                        e.setNumber_of_leaves(e.getNumber_of_leaves() - l.getNumber_of_leave());
                        em.merge(l);
                        em.merge(e);
                    }
                }
            }
        } catch (Exception ex) {
            LeaveEntity leave = new LeaveEntity();
            leave.setRemarks("hihi");
            leave.setNumber_of_leave(2);
            em.persist(leave);
        }
    }

    public void cancelLeaveApplication(String employee, Long id) {
        EmployeeEntity e = new EmployeeEntity();
        LeaveEntity l = new LeaveEntity();

        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employee);
            e = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> leaveRecords = e.getLeaveRecords();
            for (Object o : leaveRecords) {
                l = (LeaveEntity) o;
                if (Objects.equals(l.getId(), id)) {
                    if (l.getStatus().equals("pending")) {
                        em.remove(l);
                        leaveRecords.remove(l);
                        em.merge(e);
                        break;
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    private int validateLeaves(Collection<LeaveEntity> leaveRecords) {
        int sum = 0;
        for (Object o : leaveRecords) {
            LeaveEntity leave = (LeaveEntity) o;
            if (leave.getStatus().equals("pending")) {
                sum += leave.getNumber_of_leave();
            }
        }
        return sum;
    }

    public String login(String username, String password) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            String hashedPassword = hashingPassword(password);
            if (hashedPassword.equals(e.getPassword())) {
                if (e.getEmployee_account_status().equals("disabled")) {
                    return "disabled";
                } else {
                    return e.getEmployee_account_status();
                }
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            return "fail";
        }
    }

    public void extendMachineExpiry(String machineNumber) {
        MachineEntity machine = new MachineEntity();

        try {
            Query q = em.createQuery("select machine from MachineEntity machine where machine.machine_number =:id");
            q.setParameter("id", machineNumber);
            machine = (MachineEntity) q.getSingleResult();
            Calendar cal = Calendar.getInstance();
            cal.setTime(machine.getMachine_expiry());
            cal.add(Calendar.MONTH, machine.getExtension());
            Timestamp ts = new Timestamp(cal.getTime().getTime());
            machine.setMachine_expiry(ts);
            em.merge(machine);
        } catch (Exception ex) {

        }
    }

    private boolean checkUsername(String username) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            return false;
        } catch (Exception ex) {
            return true;
        }
    }

    private String hashingPassword(String password) {
        String passwordToHash = password;
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(generatedPassword);
        return generatedPassword;
    }

    public List<LeaveEntity> viewEmployeeLeaveU(String username) {
        EmployeeEntity e = new EmployeeEntity();
        List<LeaveEntity> leaves = new ArrayList<LeaveEntity>();
        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.username = :id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> leaveRecords = e.getLeaveRecords();
            for (Object o : leaveRecords) {
                LeaveEntity l = (LeaveEntity) o;
                leaves.add(l);
            }

            if (leaves.isEmpty()) {
                return null;
            } else {
                return leaves;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public EmployeeEntity viewEmployeeU(String username) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.username = :id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            return e;

        } catch (Exception ex) {
            return null;
        }
    }
}
