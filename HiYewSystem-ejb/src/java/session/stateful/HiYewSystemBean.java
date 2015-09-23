/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateful;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.MachineEntity;
import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.MachineMaintainenceEntity;
import entity.PayrollEntity;
import entity.TrainingScheduleEntity;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
public class HiYewSystemBean implements HiYewSystemBeanLocal {

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
            machine.setStatus("Available");
            machine.setDescription(description);
            machine.setExtension(extension);
            Collection<MachineMaintainenceEntity> machineMaint = new ArrayList<MachineMaintainenceEntity>();
            machine.setMachineMaintainence(machineMaint);
            em.persist(machine);
            return true;
        }

    }

    public boolean deleteMachineMaintainence(Long id) {
        MachineMaintainenceEntity mm = em.find(MachineMaintainenceEntity.class, id);
        if (mm == null) {
            return false;
        } else {
            MachineEntity m = mm.getMachine();
            m.getMachineMaintainence().remove(mm);
            em.remove(mm);
            em.merge(m);
            return true;
        }
    }

    public boolean updateMachineSchedule(MachineMaintainenceEntity mSchedule, Date scheduleDate, String mScheduleHour, String mServiceProvider, String mServiceContact) {
        if (!(scheduleDate == null)) {
            Timestamp time = new Timestamp(scheduleDate.getTime());
            mSchedule.setScheduleDate(time);
        }
        if (!mScheduleHour.isEmpty()) {
            mSchedule.setScheduleTime(mScheduleHour);
        }
        if (!mServiceProvider.isEmpty()) {
            mSchedule.setServiceProvider(mServiceProvider);
        }
        if (!mServiceContact.isEmpty()) {
            mSchedule.setServiceContact(mServiceContact);
        }

        if (!mServiceContact.isEmpty() || !mServiceProvider.isEmpty() || !mScheduleHour.isEmpty() || !(scheduleDate == null)) {
            em.merge(mSchedule);
            return true;
        }
        return false;
    }

    public boolean deleteTraining(String trainingCode) {
        TrainingScheduleEntity t = new TrainingScheduleEntity();
        try {
            Query q = em.createQuery("select t from TrainingScheduleEntity t where t.trainingCode =:id");
            q.setParameter("id", trainingCode);
            t = (TrainingScheduleEntity) q.getSingleResult();
            em.remove(t);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean deleteTrainingEmployee(TrainingScheduleEntity training, String employee) {
        if (training == null || employee == null) {
            return false;
        }
        try {
            EmployeeEntity e = new EmployeeEntity();
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name =:id");
            q.setParameter("id", employee);
            e = (EmployeeEntity) q.getSingleResult();
            if (!(training.getEmployeeRecords().contains(e))) {
                return false;
            } else {
                training.getEmployeeRecords().remove(e);
                em.merge(training);
                return true;
            }

        } catch (Exception ex) {
            return false;
        }
    }

    public boolean addTrainingEmployee(TrainingScheduleEntity schedule, String name) {
        EmployeeEntity e = new EmployeeEntity();
        TrainingScheduleEntity t = new TrainingScheduleEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name =:id");
            q.setParameter("id", name);
            e = (EmployeeEntity) q.getSingleResult();
            if (schedule.getEmployeeRecords().contains(e)) {
                return false;
            } else {
                if (schedule.getTrianingSize() - schedule.getEmployeeRecords().size() == 0) {
                    return false;
                }
                q = em.createQuery("select c from TrainingScheduleEntity c");
                for (Object o : q.getResultList()) {
                    t = (TrainingScheduleEntity) o;
                    if (!t.getTrainingCode().equals(schedule.getTrainingCode())) {
                        for (Object w : t.getEmployeeRecords()) {
                            EmployeeEntity ee = (EmployeeEntity) w;
                            if (ee.getEmployee_name().equals(name)) {
                                if (schedule.getTrainingStartDate().after(t.getTrainingStartDate()) && schedule.getTrainingStartDate().before(t.getTrainingEndDate())) {
                                    return false;
                                } else if (schedule.getTrainingEndDate().after(t.getTrainingStartDate()) && schedule.getTrainingEndDate().before(t.getTrainingEndDate())) {
                                    return false;
                                } else if (schedule.getTrainingStartDate().before(t.getTrainingEndDate()) && schedule.getTrainingEndDate().after(t.getTrainingEndDate())) {
                                    return false;
                                } else if (schedule.getTrainingStartDate().equals(t.getTrainingStartDate()) || schedule.getTrainingStartDate().equals(t.getTrainingEndDate()) || schedule.getTrainingEndDate().equals(t.getTrainingStartDate()) || schedule.getTrainingEndDate().equals(t.getTrainingEndDate())) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                schedule.getEmployeeRecords().add(e);
                em.merge(schedule);
                return true;
            }

        } catch (Exception ex) {
            return false;
        }
    }

    public List<EmployeeEntity> employeeTraining(TrainingScheduleEntity schedule) {
        List<EmployeeEntity> result = new ArrayList<EmployeeEntity>();
        if (schedule == null) {
            return null;
        }

        for (Object o : schedule.getEmployeeRecords()) {
            EmployeeEntity e = (EmployeeEntity) o;
            result.add(e);

        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public boolean addTrainingSchedule(String trainingName, Date trainingStart, Date trainingEnd, String trainingDescription, int size, String trainingCode) {
        TrainingScheduleEntity t = new TrainingScheduleEntity();
        try {
            Query q = em.createQuery("select t from TrainingScheduleEntity t where t.trainingCode = :id");
            q.setParameter("id", trainingCode);
            t = (TrainingScheduleEntity) q.getSingleResult();
            return false;
        } catch (Exception ex) {
            t.setTrainingCode(trainingCode);
            t.setTrainingDescription(trainingDescription);
            t.setTrainingName(trainingName);
            t.setTrianingSize(size);
            Timestamp time = new Timestamp(trainingStart.getTime());
            t.setTrainingStartDate(time);
            time = new Timestamp(trainingEnd.getTime());
            Collection<EmployeeEntity> employee = new ArrayList<EmployeeEntity>();
            t.setEmployeeRecords(employee);
            t.setTrainingEndDate(time);
            em.persist(t);
            return true;
        }
    }

    public List<TrainingScheduleEntity> trainingSchedueList() {
        List<TrainingScheduleEntity> result = new ArrayList<TrainingScheduleEntity>();

        Query q = em.createQuery("select c from TrainingScheduleEntity c");
        for (Object o : q.getResultList()) {
            TrainingScheduleEntity t = (TrainingScheduleEntity) o;
            result.add(t);
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public List<MachineMaintainenceEntity> machineMaintainenceListWeek() {
        List<MachineMaintainenceEntity> result = new ArrayList<MachineMaintainenceEntity>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 6);
        Calendar d = Calendar.getInstance();
        Timestamp time = new Timestamp(c.getTime().getTime());
        Timestamp time1 = new Timestamp(d.getTime().getTime());
        Query q = em.createQuery("select c from MachineMaintainenceEntity c");

        for (Object o : q.getResultList()) {
            MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
            if (time.after(m.getScheduleDate())) {
                if (!time1.after(m.getScheduleDate())) {
                    result.add(m);
                } else if (time1.after(m.getScheduleDate())) {
                    if (time1.getDay() == m.getScheduleDate().getDay() && time1.getMonth() == m.getScheduleDate().getMonth() && time1.getYear() == m.getScheduleDate().getYear()) {
                        result.add(m);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public List<String> machineMaintainenceNames() {
        List<String> result = new ArrayList<String>();
        List<MachineMaintainenceEntity> expiredMachine = machineMaintainenceListExpired();
        List<MachineMaintainenceEntity> weekMachine = machineMaintainenceListWeek();
        List<MachineMaintainenceEntity> otherMachine = machineMaintainenceList();

        for (int i = 0; i < expiredMachine.size(); i++) {
            MachineMaintainenceEntity m = expiredMachine.get(i);
            if (!(result.contains(m.getMachine().getMachine_name()))) {
                result.add(m.getMachine().getMachine_name());
            }
        }
        for (int i = 0; i < weekMachine.size(); i++) {
            MachineMaintainenceEntity m = weekMachine.get(i);
            if (!(result.contains(m.getMachine().getMachine_name()))) {
                result.add(m.getMachine().getMachine_name());
            }
        }
        for (int i = 0; i < otherMachine.size(); i++) {
            MachineMaintainenceEntity m = otherMachine.get(i);
            if (!(result.contains(m.getMachine().getMachine_name()))) {
                result.add(m.getMachine().getMachine_name());
            }
        }

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public List<MachineMaintainenceEntity> machineMaintainenceListExpired() {
        List<MachineMaintainenceEntity> result = new ArrayList<MachineMaintainenceEntity>();
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DATE, -1);
        Timestamp time1 = new Timestamp(d.getTime().getTime());
        Query q = em.createQuery("select c from MachineMaintainenceEntity c");

        for (Object o : q.getResultList()) {
            MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
            if (time1.after(m.getScheduleDate())) {

                result.add(m);

            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public List<Long> getMachineMaintID(String machineName) {
        MachineEntity machine = new MachineEntity();
        List<Long> result = new ArrayList<Long>();
        try {
            Query q = em.createQuery("Select machine from MachineEntity machine where machine.machine_name =:id");
            q.setParameter("id", machineName);
            machine = (MachineEntity) q.getSingleResult();
            Collection<MachineMaintainenceEntity> main = machine.getMachineMaintainence();
            for (Object o : main) {
                MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
                result.add(m.getId());
            }

            if (result.isEmpty()) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<MachineMaintainenceEntity> machineMaintainenceList() {
        List<MachineMaintainenceEntity> result = new ArrayList<MachineMaintainenceEntity>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 6);
        Calendar d = Calendar.getInstance();
        Timestamp time = new Timestamp(c.getTime().getTime());
        Timestamp time1 = new Timestamp(d.getTime().getTime());
        Query q = em.createQuery("select c from MachineMaintainenceEntity c");

        for (Object o : q.getResultList()) {
            MachineMaintainenceEntity m = (MachineMaintainenceEntity) o;
            if (!time1.after(m.getScheduleDate()) && !time.after(m.getScheduleDate())) {
                result.add(m);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public boolean addMachineMaintainence(String machineName, Date mScheduleDate, String mScheduleHour, String maintainenceComments, String mServiceProvider, String mServiceContact) {
        MachineEntity machine = new MachineEntity();
        try {
            Query q = em.createQuery("select machine from MachineEntity machine where machine.machine_name =:id");
            q.setParameter("id", machineName);
            machine = (MachineEntity) q.getSingleResult();
            // to check whether is there conflict
            // - is it available the machine during that time?
            // - is it already having an existing schedule?
            MachineMaintainenceEntity maint = new MachineMaintainenceEntity();

            maint.setComments(machineName);
            maint.setScheduleTime(mScheduleHour);
            maint.setServiceProvider(mServiceProvider);
            maint.setServiceContact(mServiceContact);
            Timestamp time = new Timestamp(mScheduleDate.getTime());
            maint.setScheduleDate(time);
            maint.setComments(maintainenceComments);
            maint.setMachine(machine);
            em.persist(maint);
            machine.getMachineMaintainence().add(maint);
            em.merge(machine);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean updatePay(PayrollEntity pay, boolean bonus) {
        if (bonus) {
            if (pay.getBonus() == 0) {
                pay.setBonus(100.00);
                em.merge(pay);
                return true;
            }
        } else {
            if (pay.getBonus() > 0) {
                pay.setBonus(0.00);
                em.merge(pay);
                return true;
            }
        }
        return false;
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
                    p.setStatus("unissued");
                    if (late < 3 && sick < 2) {
                        p.setBonus(100.00);
                    } else {
                        p.setBonus(0.00);
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

    public List<String> machineNames() {
        List<String> names = new ArrayList<String>();

        Query q = em.createQuery("Select c from MachineEntity c");
        for (Object o : q.getResultList()) {
            MachineEntity m = (MachineEntity) o;
            names.add(m.getMachine_name());
        }

        if (names.isEmpty()) {
            return null;
        }
        return names;
    }

    public List<PayrollEntity> payRecords() {
        Query q = em.createQuery("select c from EmployeeEntity c");
        List<PayrollEntity> result = new ArrayList<PayrollEntity>();
        for (Object o : q.getResultList()) {
            EmployeeEntity e = (EmployeeEntity) o;
            Collection<PayrollEntity> pays = e.getPayRecords();
            for (Object d : pays) {
                PayrollEntity p = (PayrollEntity) d;
                if (p.getStatus().equals("unset")) {
                    result.add(p);
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public boolean notExistMachine(String id) {
        List<MachineEntity> machines = checkMachineExpiry();
        for (Object o : machines) {
            MachineEntity m = (MachineEntity) o;
            if (m.getMachine_number().equals(id)) {
                return false;
            }
        }
        return true;
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

    public List<PayrollEntity> getReleasingPayRecords() {
        List<PayrollEntity> results = new ArrayList<PayrollEntity>();
        double total = 0.00;
        Query q = em.createQuery("select c from EmployeeEntity c");
        for (Object o : q.getResultList()) {
            total = 0.00;
            EmployeeEntity e = (EmployeeEntity) o;
            Collection<PayrollEntity> payrolls = e.getPayRecords();
            for (Object d : payrolls) {
                PayrollEntity p = (PayrollEntity) d;
                if (p.getStatus().equals("unissued")) {
                    results.add(p);
                }
            }
        }
        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    public void releaseAllPay() {
        Query q = em.createQuery("select c from PayrollEntity c");
        for (Object o : q.getResultList()) {
            PayrollEntity p = (PayrollEntity) o;
            if (p.getStatus().equals("unissued")) {
                p.setStatus("Issued");
                em.merge(p);
            }
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
            machine.setStatus("disabled");
            em.merge(machine);
        } catch (Exception ex) {

        }
    }

    public List<PayrollEntity> getPayroll(String employeeName) {
        List<PayrollEntity> result = new ArrayList<PayrollEntity>();

        if (!("select".equals(employeeName))) {
            try {
                double total = 0.00;

                EmployeeEntity e = new EmployeeEntity();
                Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name=:id");
                q.setParameter("id", employeeName);
                e = (EmployeeEntity) q.getSingleResult();
                Collection<PayrollEntity> pays = e.getPayRecords();
                for (Object o : pays) {
                    PayrollEntity p = (PayrollEntity) o;
                    if ((!(p.getStatus().equals("unset") || p.getStatus().equals("unissued")))) {
                        result.add(p);
                    }

                }
                if (result.isEmpty()) {
                    return null;
                }
                return result;

            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    //to calculate factor of the pay (in the case that the worker work less than a month when hired)
    public List<PayrollEntity> getPayroll(String employeeName, String month) {
        List<PayrollEntity> result = new ArrayList<PayrollEntity>();

        if (!("select".equals(employeeName))) {
            try {
                double total = 0.00;
                SimpleDateFormat format = new SimpleDateFormat("MMM");
                EmployeeEntity e = new EmployeeEntity();
                Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name=:id");
                q.setParameter("id", employeeName);
                e = (EmployeeEntity) q.getSingleResult();
                Collection<PayrollEntity> pays = e.getPayRecords();
                if (!("select".equals(month))) {
                    for (Object o : pays) {
                        PayrollEntity p = (PayrollEntity) o;
                        if (p.getMonth().substring(0, 3).equals(month) && (!(p.getStatus().equals("unset") || p.getStatus().equals("unissued")))) {
                            result.add(p);
                        }
                    }

                    if (result.isEmpty()) {
                        return null;
                    }
                    return result;
                } else if ("select".equals(month)) {
                    for (Object o : pays) {
                        PayrollEntity p = (PayrollEntity) o;
                        if (!(p.getStatus().equals("unset") || p.getStatus().equals("unissued"))) {

                            result.add(p);
                        }
                    }
                    if (result.isEmpty()) {
                        return null;
                    }
                    return result;
                }

            } catch (Exception ex) {
                return null;
            }
        } else if (!("select".equals(month))) {
            Query q = em.createQuery("Select c from EmployeeEntity c");
            double total = 0.00;
            for (Object o : q.getResultList()) {
                EmployeeEntity e = (EmployeeEntity) o;
                Collection<PayrollEntity> pay = e.getPayRecords();
                for (Object d : pay) {
                    PayrollEntity p = (PayrollEntity) d;
                    if (p.getMonth().substring(0, 3).equals(month) && (!(p.getStatus().equals("unset") || p.getStatus().equals("unissued")))) {
                        result.add(p);
                    }
                }

            }
            if (result.isEmpty()) {
                return null;
            }
            return result;
        }

        return null;
    }

    public boolean updateMachine(String machineName, MachineEntity machine, String status, Date machineMaint) {
        try {
            boolean check = false;
            if (!(machineName.isEmpty())) {
                machine.setMachine_name(machineName);
            }
            if (!(status.isEmpty())) {
                if (!(machine.getStatus().equals(status))) {
                    if (!(machine.getStatus().equals("Used"))) {
                        machine.setStatus(status);
                        check = true;
                    }
                }
            }
            boolean check2 = false;
            if (machineMaint != null) {
                Timestamp time = new Timestamp(machineMaint.getTime());
                if (!(time.equals(machine.getMachine_expiry()))) {
                    machine.setMachine_expiry(time);
                    check2 = true;
                }
            }

            if ((!(machineName.isEmpty())) || check || check2) {
                em.merge(machine);
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
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

    public boolean addEmployee(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, String password, Timestamp expiry, String contact, String addressPostal, String unit, String optional, double employeePay, Date employedDate) {
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
                xin.setAddressPostal(addressPostal);
                xin.setEmployee_account_status(position);
                xin.setPreviousPosition("none");
                xin.setUsername(username);
                xin.setUnit(unit);
                xin.setOptional(optional);
                String passwordHashed = hashingPassword(password);
                xin.setPassword(passwordHashed);
                xin.setEmployee_passExpiry(expiry);
                xin.setEmployee_contact(contact);
                xin.setAccount_status("firstTime");
                xin.setEmployee_basic(employeePay);
                Timestamp time = new Timestamp(employedDate.getTime());
                xin.setEmployee_employedDate(time);
                //xin.setEmployee_employedDate(ts);
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

    public boolean extendEmployeePass(String employeeName, Timestamp next) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            if (e.getEmployee_passExpiry().after(next)) {
                return false;
            }
            e.setEmployee_passExpiry(next);
            em.merge(e);
            return true;
        } catch (Exception ex) {
            return false;
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

    public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact, Date pass, String position, double pay, int leave) {
        boolean check = false;
        if (!(employeeA.isEmpty())) {
            employee.setEmployee_address(employeeA);
        }
        System.out.println(employee.getEmployee_account_status());
        if (!(employeeUnit.isEmpty())) {
            employee.setUnit(employeeUnit);
        }
        if (!(employeeOptional.isEmpty())) {
            employee.setOptional(employeeOptional);
        }
        if (!(address_postal.isEmpty())) {
            employee.setAddressPostal(address_postal);
        }
        if (!(contact.isEmpty())) {
            employee.setEmployee_contact(contact);
        }
        if (!(pass == null)) {
            employee.setEmployee_passExpiry(new Timestamp(pass.getTime()));

        }

        if (!(position.isEmpty())) {
            if (!(employee.getEmployee_account_status().equals(position))) {
                if (position.equals("disabled")) {
                    disableEmployee(employee.getEmployee_name());
                    employee.setPreviousPosition(employee.getEmployee_account_status());
                    employee.setEmployee_account_status(position);
                    check = true;
                } else {
                    reenableEmployee(employee.getEmployee_name());
                    employee.setPreviousPosition("none");
                    employee.setEmployee_account_status(position);
                    check = true;
                }
            }
        }

        if (employee.getEmployee_basic() != pay && pay != 0) {
            employee.setEmployee_basic(pay);
            check = true;
        }
        if (employee.getNumber_of_leaves() != leave) {
            if (leave != 0) {
                employee.setNumber_of_leaves(leave);
                check = true;
            }
        }

        if ((!(pass == null)) || (!(contact.isEmpty())) || (!(employeeA.isEmpty())) || check || !(employeeOptional.isEmpty()) || !(address_postal.isEmpty()) || !(employeeUnit.isEmpty())) {
            em.merge(employee);
            return true;
        }
        return false;
    }

    public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact) {
        boolean check = false;
        if (!(employeeA.isEmpty())) {
            employee.setEmployee_address(employeeA);
        }
        if (!(employeeUnit.isEmpty())) {
            employee.setUnit(employeeUnit);
        }
        if (!(employeeOptional.isEmpty())) {
            employee.setOptional(employeeOptional);
        }
        if (!(address_postal.isEmpty())) {
            employee.setAddressPostal(address_postal);
        }
        if (!(contact.isEmpty())) {
            employee.setEmployee_contact(contact);
        }

        if ((!(contact.isEmpty())) || (!(employeeA.isEmpty())) || !(employeeOptional.isEmpty()) || !(address_postal.isEmpty()) || !(employeeUnit.isEmpty())) {
            em.merge(employee);
            return true;
        }
        return false;
    }

    public boolean existEmployeeName(String employeeName) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select  e from EmployeeEntity e where e.employee_name =:id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean existEmployeeNumber(String employeeNumber) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select  e from EmployeeEntity e where e.employee_passNumber =:id");
            q.setParameter("id", employeeNumber);
            e = (EmployeeEntity) q.getSingleResult();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean notExistExpiredName(String name) {
        List<EmployeeEntity> employees = expiredEmployees();
        if (employees.isEmpty()) {
            return true;
        }
        for (Object o : employees) {
            EmployeeEntity e = (EmployeeEntity) o;
            if (e.getEmployee_name().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public boolean existEmployeeUsername(String username) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select  e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            return true;
        } catch (Exception ex) {
            return false;
        }
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
                application.setEmployee(lao);
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
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
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
                    im.add(format.format(new Date(leave.getStartDate().getTime())));
                    im.add(format.format(new Date(leave.getEndDate().getTime())));
                    im.add(leave.getRemarks());
                    im.add(format.format(new Date(leave.getAppliedTime().getTime())));
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

    public void reenableEmployee(String employeeName) {

        try {
            EmployeeEntity employee = new EmployeeEntity();
            Query q = em.createQuery("select employee from EmployeeEntity employee where employee.employee_name =:id");
            q.setParameter("id", employeeName);
            employee = (EmployeeEntity) q.getSingleResult();
            if (employee.getEmployee_account_status().equals("disabled")) {
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

    public boolean changePassword(String employeeName, String oldPass, String newPass) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            String oldPassE = hashingPassword(oldPass);
            if (e.getPassword().equals(oldPassE)) {
                String newPassE = hashingPassword(newPass);
                if (oldPassE.equals(newPassE)) {
                    return false;
                }
                e.setPassword(newPassE);
                e.setAccount_status("normal");
                em.merge(e);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
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
                } else if (e.getAccount_status().equals("firstTime")) {
                    return "firstTime";
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
