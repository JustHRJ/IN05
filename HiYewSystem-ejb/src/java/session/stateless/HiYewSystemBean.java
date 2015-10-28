/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ActivationCode;
import entity.EmployeeClaimEntity;
import entity.SupplierPurchaseOrder;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.MachineEntity;
import entity.EmployeeEntity;
import entity.FillerComposition;
import entity.LeaveEntity;
import entity.MachineMaintainenceEntity;
import entity.MachineRepairEntity;
import entity.Metal;
import entity.PayrollEntity;
import entity.TrainingScheduleEntity;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JustHRJ
 */
@Stateless
public class HiYewSystemBean implements HiYewSystemBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    //all this is for machines
    public List<EmployeeClaimEntity> pendingClaimRecords() {
        Query q = em.createQuery("select c from EmployeeClaimEntity c");
        List<EmployeeClaimEntity> claimRecords = new ArrayList<EmployeeClaimEntity>();
        for (Object o : q.getResultList()) {
            EmployeeClaimEntity c = (EmployeeClaimEntity) o;
            if (c.getStatus().equals("pending")) {
                claimRecords.add(c);
            }
        }

        if (claimRecords.isEmpty()) {
            return null;
        } else {
            return claimRecords;
        }
    }

    public List<TrainingScheduleEntity> pastEmployeeTraining(EmployeeEntity employee) {
        Query q = em.createQuery("select c from TrainingScheduleEntity c");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Timestamp time = new Timestamp(c.getTime().getTime());
        List<TrainingScheduleEntity> results = new ArrayList<TrainingScheduleEntity>();

        for (Object o : q.getResultList()) {
            TrainingScheduleEntity t = (TrainingScheduleEntity) o;
            if (t.getEmployeeRecords().contains(employee)) {
                Timestamp trainingtime = t.getTrainingEndDate();
                if (time.after(trainingtime)) {
                    results.add(t);
                }
            }
        }
        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

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
            Collection<MachineRepairEntity> repairs = new ArrayList<MachineRepairEntity>();
            machine.setMachineRepair(repairs);
            em.persist(machine);
            return true;
        }

    }

    public boolean updateClaim(EmployeeClaimEntity claim, double amount, Date date) {
        boolean check = false;
        EmployeeEntity employee = claim.getEmployee();
        if (amount > 0 && amount != claim.getAmount()) {
            double check1 = checkExceedLimit(employee, amount);
            if (check1 == 0) {
                return false;
            }
            check = true;
            claim.setAmount(amount);
            claim.setClaimAmt(check1);
        }
        if (date != null) {
            Timestamp time = new Timestamp(date.getTime());
            if (time.equals(claim.getClaimDate())) {

            } else {
                claim.setClaimDate(time);
                check = true;
            }
        }
        if (check) {
            em.merge(claim);
            return true;
        } else {
            return false;
        }
    }

    public boolean createPO(String supPONo, Timestamp date, String termsOfPayment, String description, String supCompanyName, int quantity) {
        SupplierPurchaseOrder supPO = new SupplierPurchaseOrder();
        try {
            Query q = em.createQuery("Select supPO from SupplierPurchaseOrder supPO where supPO.supPONo = :supPONo");
            q.setParameter("supPONo", supPONo);
            supPO = (SupplierPurchaseOrder) q.getSingleResult();
            return false;
        } catch (Exception ex) {
            supPO.setSupPONo(supPONo);
            supPO.setTermsOfPayment(termsOfPayment);
            supPO.setDescription(description);
            supPO.setDate(date);
            supPO.setSupCompanyName(supCompanyName);
            supPO.setSupPoStatus("Pending");
            supPO.setQuantity(quantity);
            em.persist(supPO);
            return true;
        }

    }

    private double checkExceedLimit(EmployeeEntity e, double cd) {
        double sum = 0.0;
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        Calendar c = Calendar.getInstance();
        String month = format.format(c.getTime());
        for (Object o : e.getEmployeeClaims()) {
            EmployeeClaimEntity claim = (EmployeeClaimEntity) o;
            Date date = new Date(claim.getClaimDate().getTime());
            String claimMonth = format.format(date);
            if (month.equals(claimMonth) && (claim.getStatus().equals("approved") || claim.getStatus().equals(("pending")))) {
                sum += claim.getAmount();
            }
        }
        double remainder = 100 - sum;
        if (remainder <= 0) {
            return 0;
        } else {
            if (remainder < cd) {
                return remainder;
            } else {
                return cd;
            }
        }
    }

    private double checkExceedLimit(EmployeeEntity e, EmployeeClaimEntity cd) {
        double sum = 0.0;
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        Calendar c = Calendar.getInstance();
        String month = format.format(c.getTime());
        for (Object o : e.getEmployeeClaims()) {
            EmployeeClaimEntity claim = (EmployeeClaimEntity) o;
            Date date = new Date(claim.getClaimDate().getTime());
            String claimMonth = format.format(date);
            if (month.equals(claimMonth) && (claim.getStatus().equals("approved") || claim.getStatus().equals(("pending")))) {
                sum += claim.getAmount();
            }
        }
        double remainder = 100 - sum;
        if (remainder <= 0) {
            return 0;
        } else {
            if (remainder < cd.getAmount()) {
                return remainder;
            } else {
                return cd.getAmount();
            }
        }
    }

    // for all approved Claim
    public List<EmployeeClaimEntity> approvedClaimRecords(String employeeName) {
        Query q = em.createQuery("select c from EmployeeClaimEntity c");
        System.out.println("here");
        List<EmployeeClaimEntity> claimRecords = new ArrayList<EmployeeClaimEntity>();
        for (Object o : q.getResultList()) {
            EmployeeClaimEntity c = (EmployeeClaimEntity) o;
            if (c.getEmployee().getEmployee_name().equals(employeeName)) {
                claimRecords.add(c);
            }
        }

        if (claimRecords.isEmpty()) {
            return null;
        } else {
            return claimRecords;
        }
    }

    public List<EmployeeClaimEntity> approvedClaimRecordsA(String employeeName, String months) {
        Query q = em.createQuery("select c from EmployeeClaimEntity c");
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        List<EmployeeClaimEntity> claimRecords = new ArrayList<EmployeeClaimEntity>();
        for (Object o : q.getResultList()) {
            EmployeeClaimEntity c = (EmployeeClaimEntity) o;
            if (c.getApprovedDate() != null) {
                if (c.getEmployee().getEmployee_name().equals(employeeName) && format.format(c.getApprovedDate()).equals(months)) {
                    claimRecords.add(c);
                }
            }
        }
        if (claimRecords.isEmpty()) {
            return null;
        } else {
            return claimRecords;
        }
    }

    public List<EmployeeClaimEntity> approvedClaimRecordsM(String months) {
        Query q = em.createQuery("select c from EmployeeClaimEntity c");
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        List<EmployeeClaimEntity> claimRecords = new ArrayList<EmployeeClaimEntity>();
        for (Object o : q.getResultList()) {
            EmployeeClaimEntity c = (EmployeeClaimEntity) o;
            if (c.getApprovedDate() != null) {
                if (format.format(c.getApprovedDate()).equals(months)) {
                    claimRecords.add(c);
                }
            }
        }

        if (claimRecords.isEmpty()) {
            return null;
        } else {
            return claimRecords;
        }
    }

    public void removeClaim(EmployeeClaimEntity claim) {
        System.out.println("hello1");
        String employeeName = claim.getEmployee().getEmployee_name();
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name = :id ");
            q.setParameter("id", employeeName);
            System.out.println("hello3");
            e = (EmployeeEntity) q.getSingleResult();
            System.out.println("hello5");
            e.getEmployeeClaims().remove(claim);
            em.merge(e);
            Long id = claim.getId();
            EmployeeClaimEntity c = em.find(EmployeeClaimEntity.class, id);
            if (c == null) {

            } else {
                em.remove(c);
            }

        } catch (Exception ex) {
            System.out.println("hello2");
        }
    }

    public boolean applyClaim(String employeeName, EmployeeClaimEntity claim, String destination) {
        EmployeeEntity employee = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select employee from EmployeeEntity employee where employee.employee_name = :id");
            q.setParameter("id", employeeName);
            employee = (EmployeeEntity) q.getSingleResult();
            double check = checkExceedLimit(employee, claim);
            if (check == 0) {
                return false;
            }
            claim.setClaimAmt(check);
            claim.setEmployee(employee);
            claim.setStatus("pending");
            claim.setFileDestination(destination);
            em.persist(claim);
            employee.getEmployeeClaims().add(claim);
            em.merge(employee);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<SupplierPurchaseOrder> getAllPO() {
        Query q = em.createQuery("Select c from SupplierPurchaseOrder c");
        List<SupplierPurchaseOrder> poRecords = new ArrayList<SupplierPurchaseOrder>();
        for (Object o : q.getResultList()) {
            SupplierPurchaseOrder PO = (SupplierPurchaseOrder) o;
            poRecords.add(PO);
        }
        if (poRecords.isEmpty()) {
            return null;
        } else {
            return poRecords;
        }
    }

    public boolean existMachineName(String name) {
        MachineEntity m = new MachineEntity();
        try {
            Query q = em.createQuery("select m from MachineEntity m where m.machine_name = :id");
            q.setParameter("id", name);
            m = (MachineEntity) q.getSingleResult();
            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    public boolean deleteMachineMaintainence(String id) {
        try {
            MachineMaintainenceEntity mm = em.find(MachineMaintainenceEntity.class, Long.parseLong(id));
            if (mm == null) {
                return false;
            } else {
                MachineEntity m = mm.getMachine();
                m.getMachineMaintainence().remove(mm);
                em.remove(mm);
                em.merge(m);
                return true;
            }
        } catch (Exception ex) {
            return false;
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

    public List<String> employeeTrainingName(TrainingScheduleEntity schedule) {
        List<String> result = new ArrayList<String>();
        if (schedule == null) {
            return null;
        }

        for (Object o : schedule.getEmployeeRecords()) {
            EmployeeEntity e = (EmployeeEntity) o;
            result.add(e.getEmployee_name());

        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
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

    public boolean updateTraining(TrainingScheduleEntity training, Date start, Date end, int size) {
        boolean check = false;
        if (start != null) {
            Timestamp time = new Timestamp(start.getTime());

            if (end != null) {
                if (start.after(end)) {
                    return false;
                }
            } else {
                if (time.after(training.getTrainingEndDate())) {
                    return false;
                }
            }

            training.setTrainingStartDate(time);
            check = true;
        }
        if (end != null) {
            Timestamp time1 = new Timestamp(end.getTime());

            if (start != null) {
                if (start.after(end)) {
                    return false;
                }
            } else {
                if (training.getTrainingStartDate().after(end)) {
                    return false;
                }
            }

            training.setTrainingEndDate(time1);
            check = true;
        }
        if (size != 0 && size != training.getTrianingSize()) {
            training.setTrianingSize(size);
            check = true;
        }

        if (check) {
            em.merge(training);
            return true;
        }
        return false;
    }

    public List<TrainingScheduleEntity> trainingScheduleListAvailable() {
        List<TrainingScheduleEntity> result = new ArrayList<TrainingScheduleEntity>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        System.out.println(c.getTime());
        Timestamp time = new Timestamp(c.getTime().getTime());
        Query q = em.createQuery("select c from TrainingScheduleEntity c");
        for (Object o : q.getResultList()) {
            TrainingScheduleEntity t = (TrainingScheduleEntity) o;
            Timestamp trainingtime = t.getTrainingStartDate();
            if (trainingtime.after(time) || trainingtime.equals(time)) {
                result.add(t);
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
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
            if (m.getStatus().equals("incomplete")) {
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
            if (m.getStatus().equals("incomplete")) {
                if (time1.after(m.getScheduleDate())) {
                    result.add(m);
                }
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
            if (m.getStatus().equals("incomplete")) {
                if (!time1.after(m.getScheduleDate()) && !time.after(m.getScheduleDate())) {
                    result.add(m);
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    private boolean checkClashMaintainence(MachineEntity machine) {
        Collection<MachineMaintainenceEntity> maint = machine.getMachineMaintainence();
        for (Object o : maint) {
            MachineMaintainenceEntity mm = (MachineMaintainenceEntity) o;
            if (mm.getStatus().equals("incomplete")) {
                return false;
            }
        }
        return true;
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
            boolean check = checkClashMaintainence(machine);
            if (check) {
                maint.setComments(machineName);
                maint.setScheduleTime(mScheduleHour);
                maint.setServiceProvider(mServiceProvider);
                maint.setServiceContact(mServiceContact);
                Timestamp time = new Timestamp(mScheduleDate.getTime());
                maint.setScheduleDate(time);
                maint.setComments(maintainenceComments);
                maint.setMachine(machine);
                maint.setStatus("incomplete");
                em.persist(maint);
                machine.getMachineMaintainence().add(maint);
                em.merge(machine);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean updatePay(PayrollEntity pay, boolean bonus, double others) {

        boolean check = false;
        if (bonus) {
            if (pay.getBonus() == 0) {
                pay.setBonus(100);
                check = true;
            }
        } else {
            if (pay.getBonus() > 0) {
                pay.setBonus(0);
                check = true;
            }
        }
        if (others != 0 && pay.getOtherAmount() != others) {
            pay.setOtherAmount(others);
            check = true;
        }
        if (check) {
            em.merge(pay);
            return true;
        } else {
            return false;
        }

    }

    public void rejectClaim(EmployeeClaimEntity c) {
        c.setStatus("rejected");
        Calendar cd = Calendar.getInstance();
        c.setApprovedDate(new Timestamp(cd.getTime().getTime()));
        em.merge(c);
    }

    public boolean createPayroll(String employeeName, int late, int sick, double overtime) {
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
                    if (late < 3 && sick < 2 && p.getSalary() > (e.getEmployee_basic() / (double) 4 * (double) 3)) {
                        p.setBonus(100.00);
                    } else {
                        p.setBonus(0.00);
                    }
                    p.setOvertime(overtime * 10);
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
        if (machines == null) {
            return true;
        }

        for (Object o : machines) {
            MachineEntity m = (MachineEntity) o;
            if (m.getMachine_name().equals(id)) {
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

    public String getEmployeeEs(String username) {

        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            return e.getEmployee_name();

        } catch (Exception ex) {
            return "";
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

    public boolean updateSupPoStatus(String supPoStatus, List<SupplierPurchaseOrder> selectedList) {
        try {
            for (Object o : selectedList) {
                SupplierPurchaseOrder e = (SupplierPurchaseOrder) o;
                e.setSupPoStatus(supPoStatus);
                em.merge(e);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
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

    public boolean updatePO(String termsOfPayment, SupplierPurchaseOrder supplierPurchaseOrder, String description, int quantity) {
        boolean check = false;
        boolean check2 = false;
        System.out.println(termsOfPayment);
        if (!(termsOfPayment.isEmpty()) && !(termsOfPayment.equals(supplierPurchaseOrder.getTermsOfPayment()))) {
            supplierPurchaseOrder.setTermsOfPayment(termsOfPayment);
            check = true;
        }

        if (!(description.isEmpty())) {
            supplierPurchaseOrder.setDescription(description);
            //check = true;
            //System.out.println(description);
        }
        //if (!(supCompanyName.isEmpty()) && !(supCompanyName.equals(supplierPurchaseOrder.getSupCompanyName()))) {
        //     supplierPurchaseOrder.setSupCompanyName(supCompanyName);
        //     check = true;
        //}

        if ((quantity != 0) && (quantity != supplierPurchaseOrder.getQuantity())) {
            supplierPurchaseOrder.setQuantity(quantity);
            check2 = true;
        }

        if (check || !(description.isEmpty()) || check2) {
            em.merge(supplierPurchaseOrder);
            return true;
        }
        //if(check == true){
        //   em.merge(supplierPurchaseOrder);
        //    return true;   
        //}
        return false;

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

    public void addNewAdmin(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, Timestamp expiry, String contact, String addressPostal, String unit, String optional, double employeePay, Date employedDate, String email, String password) {
        EmployeeEntity xin = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select xin from EmployeeEntity xin where xin.employee_name = :id");
            q.setParameter("id", employee);
            xin = (EmployeeEntity) q.getSingleResult();
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
                xin.setEmailAddress(email);
                xin.setAccount_status("firstTime");
                xin.setEmployee_basic(employeePay);
                Collection<EmployeeClaimEntity> claims = new ArrayList<EmployeeClaimEntity>();
                xin.setEmployeeClaims(claims);
                Calendar c = Calendar.getInstance();
                c.set(Calendar.MILLISECOND, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.HOUR_OF_DAY, 0);
                xin.setEmployee_employedDate(new Timestamp(c.getTime().getTime()));
                //xin.setEmployee_employedDate(ts);
                em.persist(xin);

            } else {

            }
        }
    }

    public void approveClaim(EmployeeClaimEntity claim) {
        Calendar cal = Calendar.getInstance();
        Timestamp time = new Timestamp(cal.getTime().getTime());
        claim.setApprovedDate(time);
        claim.setStatus("approved");
        em.merge(claim);
    }

    public void attachDocument(EmployeeClaimEntity claim, String destination) {
        claim.setFileDestination(destination);
        em.merge(claim);
    }

    public Vector addEmployee(String employee, String employee_passNumber, String employee_address, int number_of_leave, String position, String username, Timestamp expiry, String contact, String addressPostal, String unit, String optional, double employeePay, Date employedDate, String email) {

        EmployeeEntity xin = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select xin from EmployeeEntity xin where xin.employee_name = :id");
            q.setParameter("id", employee);
            xin = (EmployeeEntity) q.getSingleResult();
            return null;
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
                String password1 = createRandomPass();
                System.out.println("admin createRandomPass() ============ " + password1);
                String passwordHashed = hashingPassword(password1);
                xin.setPassword(passwordHashed);
                xin.setEmployee_passExpiry(expiry);
                xin.setEmployee_contact(contact);
                xin.setEmailAddress(email);
                xin.setAccount_status("firstTime");
                xin.setEmployee_basic(employeePay);
                Timestamp time = new Timestamp(employedDate.getTime());
                xin.setEmployee_employedDate(time);
                Collection<EmployeeClaimEntity> claims = new ArrayList<EmployeeClaimEntity>();
                xin.setEmployeeClaims(claims);                //xin.setEmployee_employedDate(ts);
                em.persist(xin);
                Vector im = new Vector();
                im.add(xin.getEmployee_name());
                im.add(xin.getUsername());
                im.add(password1);
                im.add(xin.getEmailAddress());
                return im;
            } else {
                return null;
            }
        }
    }

    public Vector resetPassword(String username, String secretQuestion, String secretAnswer) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            if (e.getEmployee_account_status().equals("disabled")) {
                return null;
            }
            if (e.getSecretQuestion().equals(secretQuestion)) {
                if (e.getSecretAnswer().equals(secretAnswer)) {
                    String password = createRandomPass();
                    e.setPassword(hashingPassword(password));
                    e.setAccount_status("reset");
                    em.merge(e);
                    Vector im = new Vector();
                    im.add(e.getEmployee_name());
                    im.add(username);
                    im.add(password);
                    im.add(e.getEmailAddress());

                    return im;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
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

    public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact, Date pass, String position, double pay, int leave, String email) {
        boolean check = false;
        if (!(employeeA.isEmpty()) && !(employeeA.equals(employee.getEmployee_address()))) {
            employee.setEmployee_address(employeeA);
            check = true;
        }
        if (!(employeeUnit.isEmpty()) && !(employeeUnit.equals(employee.getUnit()))) {
            employee.setUnit(employeeUnit);
            check = true;
        }
        if (!(employeeOptional.isEmpty()) && !(employeeOptional.equals(employee.getOptional()))) {
            employee.setOptional(employeeOptional);
            check = true;
        }
        if (!(address_postal.isEmpty()) && !(address_postal.equals(employee.getAddressPostal()))) {
            employee.setAddressPostal(address_postal);
            check = true;
        }
        if (!(email.isEmpty()) && !(email.equals(employee.getEmailAddress()))) {
            employee.setEmailAddress(email);
            check = true;
        }

        if (!(contact.isEmpty()) && !(contact.equals(employee.getEmployee_contact()))) {
            employee.setEmployee_contact(contact);
            check = true;
        }
        if (!(pass == null) && !(new Timestamp(pass.getTime()).equals(employee.getEmployee_passExpiry()))) {
            employee.setEmployee_passExpiry(new Timestamp(pass.getTime()));
            check = true;
        }

        if (!(position.isEmpty()) && !(position.equals("none")) && !(position.equals(employee.getEmployee_account_status()))) {
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

        if (check) {
            em.merge(employee);
            return true;
        }
        return false;
    }

    public boolean updateEmployee(EmployeeEntity employee, String employeeA, String employeeUnit, String employeeOptional, String address_postal, String contact, String email) {
        boolean check = false;
        if (!(employeeA.isEmpty())) {
            employee.setEmployee_address(employeeA);
            check = true;
        }
        if (!(employeeUnit.isEmpty())) {
            employee.setUnit(employeeUnit);
            check = true;
        }
        if (!(employeeOptional.isEmpty())) {
            employee.setOptional(employeeOptional);
            check = true;
        }
        if (!(address_postal.isEmpty())) {
            employee.setAddressPostal(address_postal);
            check = true;
        }

        if (!(email.isEmpty())) {
            employee.setEmailAddress(email);
            check = true;
        }

        if (!(contact.isEmpty())) {
            employee.setEmployee_contact(contact);
            check = true;
        }

        if (check) {
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

    public List<Vector> employeeTrainingTodayUser(String username) {

        try {
            EmployeeEntity employee = new EmployeeEntity();
            Query s = em.createQuery("select employee from EmployeeEntity employee where employee.username =:id");
            s.setParameter("id", username);
            employee = (EmployeeEntity) s.getSingleResult();
            List<Vector> result = new ArrayList<Vector>();
            Calendar c = Calendar.getInstance();
            Timestamp currentTime = new Timestamp(c.getTime().getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Query q = em.createQuery("select c from TrainingScheduleEntity c");
            for (Object o : q.getResultList()) {
                TrainingScheduleEntity t = (TrainingScheduleEntity) o;
                if ((currentTime.after(t.getTrainingStartDate()) && currentTime.before(t.getTrainingEndDate())) || (format.format(currentTime).equals(format.format(t.getTrainingStartDate()))) || (format.format(currentTime).equals(format.format(t.getTrainingEndDate())))) {
                    Collection<EmployeeEntity> employees = t.getEmployeeRecords();
                    if (employees.contains(employee)) {
                        Vector im = new Vector();
                        im.add(employee.getEmployee_name());
                        im.add(employee.getEmployee_passNumber());
                        im.add(t.getTrainingName());
                        im.add(t.getTrainingCode());
                        im.add(t.getTrainingStartDate());
                        im.add(t.getTrainingEndDate());
                        result.add(im);

                    }
                }
            }
            if (result.isEmpty() || result.size() == 0) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Vector> employeeTrainingToday() {
        List<Vector> result = new ArrayList<Vector>();
        Calendar c = Calendar.getInstance();
        Timestamp currentTime = new Timestamp(c.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Query q = em.createQuery("select c from TrainingScheduleEntity c");
        for (Object o : q.getResultList()) {
            TrainingScheduleEntity t = (TrainingScheduleEntity) o;
            if ((currentTime.after(t.getTrainingStartDate()) && currentTime.before(t.getTrainingEndDate())) || (format.format(currentTime).equals(format.format(t.getTrainingStartDate()))) || (format.format(currentTime).equals(format.format(t.getTrainingEndDate())))) {
                Collection<EmployeeEntity> employees = t.getEmployeeRecords();
                for (Object d : employees) {
                    EmployeeEntity e = (EmployeeEntity) d;
                    Vector im = new Vector();
                    im.add(e.getEmployee_name());
                    im.add(e.getEmployee_passNumber());
                    im.add(t.getTrainingName());
                    im.add(t.getTrainingCode());
                    im.add(t.getTrainingStartDate());
                    im.add(t.getTrainingEndDate());
                    result.add(im);
                }
            }
        }
        if (result.isEmpty() || result.size() == 0) {
            return null;
        } else {
            return result;
        }

    }

    public List<Vector> employeeTraining7DaysUser(String username) {
        try {
            EmployeeEntity employee = new EmployeeEntity();
            Query s = em.createQuery("select employee from EmployeeEntity employee where employee.username =:id");
            s.setParameter("id", username);
            employee = (EmployeeEntity) s.getSingleResult();
            List<Vector> result = new ArrayList<Vector>();
            Calendar c = Calendar.getInstance();
            Calendar d = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            d.add(Calendar.DATE, 8);

            Timestamp time1 = new Timestamp(c.getTime().getTime());
            Timestamp time8 = new Timestamp(d.getTime().getTime());

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            String time1s = format.format(time1);
            String time8s = format.format(time8);

            Query q = em.createQuery("select c from TrainingScheduleEntity c");

            for (Object o : q.getResultList()) {
                TrainingScheduleEntity t = (TrainingScheduleEntity) o;
                if ((t.getTrainingStartDate().after(time1) && t.getTrainingStartDate().before(time8)) || (t.getTrainingEndDate().after(time1) && t.getTrainingEndDate().before(time8)) || format.format(t.getTrainingStartDate()).equals(time1s) || format.format(t.getTrainingStartDate()).equals(time8s) || format.format(t.getTrainingEndDate()).equals(time1s) || format.format(t.getTrainingEndDate()).equals(time8s)) {
                    Collection<EmployeeEntity> employees = t.getEmployeeRecords();
                    if (employees.contains(employee)) {
                        Vector im = new Vector();
                        im.add(employee.getEmployee_name());
                        im.add(employee.getEmployee_passNumber());
                        im.add(t.getTrainingName());
                        im.add(t.getTrainingCode());
                        im.add(t.getTrainingStartDate());
                        im.add(t.getTrainingEndDate());
                        result.add(im);
                    }
                }
            }
            if (result.isEmpty() || result.size() == 0) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Vector> employeeTraining7Days() {
        List<Vector> result = new ArrayList<Vector>();
        Calendar c = Calendar.getInstance();
        Calendar d = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        d.add(Calendar.DATE, 8);

        Timestamp time1 = new Timestamp(c.getTime().getTime());
        Timestamp time8 = new Timestamp(d.getTime().getTime());

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        String time1s = format.format(time1);
        String time8s = format.format(time8);

        Query q = em.createQuery("select c from TrainingScheduleEntity c");

        for (Object o : q.getResultList()) {
            TrainingScheduleEntity t = (TrainingScheduleEntity) o;
            if ((t.getTrainingStartDate().after(time1) && t.getTrainingStartDate().before(time8)) || (t.getTrainingEndDate().after(time1) && t.getTrainingEndDate().before(time8)) || format.format(t.getTrainingStartDate()).equals(time1s) || format.format(t.getTrainingStartDate()).equals(time8s) || format.format(t.getTrainingEndDate()).equals(time1s) || format.format(t.getTrainingEndDate()).equals(time8s)) {
                Collection<EmployeeEntity> employees = t.getEmployeeRecords();
                for (Object p : employees) {
                    EmployeeEntity e = (EmployeeEntity) p;
                    Vector im = new Vector();
                    im.add(e.getEmployee_name());
                    im.add(e.getEmployee_passNumber());
                    im.add(t.getTrainingName());
                    im.add(t.getTrainingCode());
                    im.add(t.getTrainingStartDate());
                    im.add(t.getTrainingEndDate());
                    result.add(im);
                }
            }
        }
        if (result.isEmpty() || result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    public List<Vector> employeeTrainingMonth() {
        List<Vector> result = new Vector();
        Calendar c = Calendar.getInstance();
        Timestamp currentMonth = new Timestamp(c.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("MM");
        String currentMonths = format.format(currentMonth);

        Query q = em.createQuery("select c from TrainingScheduleEntity c");
        for (Object o : q.getResultList()) {
            TrainingScheduleEntity t = (TrainingScheduleEntity) o;
            if (format.format(t.getTrainingStartDate()).equals(currentMonths) || format.format(t.getTrainingEndDate()).equals(currentMonths)) {
                Collection<EmployeeEntity> employees = t.getEmployeeRecords();
                for (Object p : employees) {
                    EmployeeEntity e = (EmployeeEntity) p;
                    Vector im = new Vector();
                    im.add(e.getEmployee_name());
                    im.add(e.getEmployee_passNumber());
                    im.add(t.getTrainingName());
                    im.add(t.getTrainingCode());
                    im.add(t.getTrainingStartDate());
                    im.add(t.getTrainingEndDate());
                    result.add(im);
                }
            }
        }
        if (result.isEmpty() || result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    public List<Vector> employeeTrainingMonthUser(String username) {
        try {
            EmployeeEntity employee = new EmployeeEntity();
            Query s = em.createQuery("select employee from EmployeeEntity employee where employee.username =:id");
            s.setParameter("id", username);
            employee = (EmployeeEntity) s.getSingleResult();
            List<Vector> result = new Vector();
            Calendar c = Calendar.getInstance();
            Timestamp currentMonth = new Timestamp(c.getTime().getTime());
            SimpleDateFormat format = new SimpleDateFormat("MM");
            String currentMonths = format.format(currentMonth);

            Query q = em.createQuery("select c from TrainingScheduleEntity c");
            for (Object o : q.getResultList()) {
                TrainingScheduleEntity t = (TrainingScheduleEntity) o;
                if (format.format(t.getTrainingStartDate()).equals(currentMonths) || format.format(t.getTrainingEndDate()).equals(currentMonths)) {
                    Collection<EmployeeEntity> employees = t.getEmployeeRecords();
                    if (employees.contains(employee)) {
                        Vector im = new Vector();
                        im.add(employee.getEmployee_name());
                        im.add(employee.getEmployee_passNumber());
                        im.add(t.getTrainingName());
                        im.add(t.getTrainingCode());
                        im.add(t.getTrainingStartDate());
                        im.add(t.getTrainingEndDate());
                        result.add(im);
                    }
                }
            }
            if (result.isEmpty() || result.size() == 0) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<LeaveEntity> employeeLeaveMonthUser(String username) {
        List<LeaveEntity> result = new ArrayList<LeaveEntity>();
        Calendar c = Calendar.getInstance();
        Timestamp currentMonth = new Timestamp(c.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        String currentMonths = format.format(currentMonth);

        try {
            EmployeeEntity e = new EmployeeEntity();
            Query q = em.createQuery("select e from EmployeeEntity e where e.username = :id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> records = e.getLeaveRecords();
            for (Object o : records) {
                LeaveEntity l = (LeaveEntity) o;
                if (format.format(l.getStartDate()).equals(currentMonths) || format.format(l.getEndDate()).equals(currentMonths)) {
                    result.add(l);
                }
            }
            if (result.isEmpty() || result.size() == 0) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<LeaveEntity> employeeLeaveMonth() {
        List<LeaveEntity> result = new ArrayList<LeaveEntity>();
        Calendar c = Calendar.getInstance();
        Timestamp currentMonth = new Timestamp(c.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        String currentMonths = format.format(currentMonth);

        Query q = em.createQuery("select c from LeaveEntity c");
        for (Object o : q.getResultList()) {
            LeaveEntity l = (LeaveEntity) o;
            if (format.format(l.getStartDate()).equals(currentMonths) || format.format(l.getEndDate()).equals(currentMonths)) {
                result.add(l);
            }
        }
        if (result.isEmpty() || result.size() == 0) {
            return null;
        } else {
            return result;
        }
    }

    public List<LeaveEntity> employeeLeave7daysUser(String username) {
        EmployeeEntity e = new EmployeeEntity();
        List<LeaveEntity> result = new ArrayList<LeaveEntity>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        Timestamp Time1 = new Timestamp(c.getTime().getTime());
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DATE, 8);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp Time8 = new Timestamp(d.getTime().getTime());
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> records = e.getLeaveRecords();

            for (Object o : records) {
                LeaveEntity l = (LeaveEntity) o;
                if (l.getStartDate().after(Time1) && l.getStartDate().before(Time8) && l.getStatus().equals("approved")) {
                    result.add(l);
                } else if (l.getEndDate().after(Time1) && l.getEndDate().before(Time8) && l.getStatus().equals("approved")) {
                    result.add(l);
                } else if (format.format(l.getStartDate()).equals(format.format(Time1.getTime())) && l.getStatus().equals("approved")) {
                    result.add(l);
                } else if (format.format(l.getStartDate()).equals(format.format(Time8.getTime())) && l.getStatus().equals("approved")) {
                    result.add(l);
                } else if (format.format(l.getEndDate()).equals(format.format(Time1.getTime())) && l.getStatus().equals("approved")) {
                    result.add(l);
                } else if (format.format(l.getEndDate()).equals(format.format(Time8.getTime())) && l.getStatus().equals("approved")) {
                    result.add(l);
                }
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

    public List<LeaveEntity> employeeLeave7days() {
        List<LeaveEntity> result = new ArrayList<LeaveEntity>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        Timestamp Time1 = new Timestamp(c.getTime().getTime());
        Calendar d = Calendar.getInstance();
        d.add(Calendar.DATE, 8);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp Time8 = new Timestamp(d.getTime().getTime());
        Query q = em.createQuery("select c from LeaveEntity c");
        for (Object o : q.getResultList()) {
            LeaveEntity l = (LeaveEntity) o;
            if (l.getStartDate().after(Time1) && l.getStartDate().before(Time8) && l.getStatus().equals("approved")) {
                result.add(l);
            } else if (l.getEndDate().after(Time1) && l.getEndDate().before(Time8) && l.getStatus().equals("approved")) {
                result.add(l);
            } else if (format.format(l.getStartDate()).equals(format.format(Time1.getTime())) && l.getStatus().equals("approved")) {
                result.add(l);
            } else if (format.format(l.getStartDate()).equals(format.format(Time8.getTime())) && l.getStatus().equals("approved")) {
                result.add(l);
            } else if (format.format(l.getEndDate()).equals(format.format(Time1.getTime())) && l.getStatus().equals("approved")) {
                result.add(l);
            } else if (format.format(l.getEndDate()).equals(format.format(Time8.getTime())) && l.getStatus().equals("approved")) {
                result.add(l);
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public List<LeaveEntity> employeeLeaveTodayUser(String username) {
        EmployeeEntity e = new EmployeeEntity();
        List<LeaveEntity> result = new ArrayList<LeaveEntity>();
        Calendar c = Calendar.getInstance();
        Timestamp currentTime = new Timestamp(c.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.username =:id");
            q.setParameter("id", username);
            e = (EmployeeEntity) q.getSingleResult();
            Collection<LeaveEntity> records = e.getLeaveRecords();
            if (records == null || records.isEmpty()) {
                return null;
            }
            for (Object o : records) {
                LeaveEntity l = (LeaveEntity) o;
                if (currentTime.after(l.getStartDate()) && currentTime.before(l.getEndDate()) && l.getStatus().equals("approved")) {
                    result.add(l);
                } else if (format.format(currentTime).equals(format.format(l.getStartDate())) && l.getStatus().equals("approved")) {
                    result.add(l);
                } else if (format.format(currentTime).equals(format.format(l.getEndDate())) && l.getStatus().equals("approved")) {
                    result.add(l);
                }

            }

            if (result.isEmpty() || result.size() == 0) {
                return null;
            } else {
                return result;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public List<LeaveEntity> employeeLeaveToday() {
        List<LeaveEntity> result = new ArrayList<LeaveEntity>();
        Calendar c = Calendar.getInstance();
        Timestamp currentTime = new Timestamp(c.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Query q = em.createQuery("select c from LeaveEntity c");
        for (Object o : q.getResultList()) {
            LeaveEntity l = (LeaveEntity) o;
            if (currentTime.after(l.getStartDate()) && currentTime.before(l.getEndDate()) && l.getStatus().equals("approved")) {
                result.add(l);
            } else if (format.format(currentTime).equals(format.format(l.getStartDate())) && l.getStatus().equals("approved")) {
                result.add(l);
            } else if (format.format(currentTime).equals(format.format(l.getEndDate())) && l.getStatus().equals("approved")) {
                result.add(l);
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
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
            if (!(l.getStatus().equals("rejected"))) {
                if (sd.before(l.getStartDate()) && ed.after(l.getEndDate()) || (sd.after(l.getStartDate()) && sd.before(l.getEndDate())) || ed.after(l.getStartDate()) && ed.before(l.getEndDate()) || sd.equals(l.getStartDate()) || sd.equals(l.getEndDate()) || ed.equals(l.getStartDate()) || ed.equals(l.getEndDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String applyLeave(String employee, int days, String remarks, Date start, Date end, String type) {
        EmployeeEntity lao = new EmployeeEntity();
        try {
            Query q = em.createQuery("Select lao from EmployeeEntity lao where lao.employee_name = :id");
            q.setParameter("id", employee);
            lao = (EmployeeEntity) q.getSingleResult();
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            Date date1 = c.getTime();
            Collection<LeaveEntity> leaves = lao.getLeaveRecords();
            int sum = 0;

            for (Object o : leaves) {
                LeaveEntity l = (LeaveEntity) o;
                if (l.getStatus().equals("pending")) {
                    sum += l.getNumber_of_leave();
                }
            }
            if (type.equals("unpaid")) {
                if (checkBetween(leaves, start, end)) {
                    return "Exisiting leave during applying period";
                }
                if (days < 1) {
                    return "End date starts before Start date";
                }
                if (lao.getEmployee_account_status().equals("disabled")) {
                    return "Employee account is disabled";
                }
                if (new Timestamp(start.getTime()).before(lao.getEmployee_employedDate()) || new Timestamp(end.getTime()).before(lao.getEmployee_employedDate())) {
                    return "Cannot apply leave before employed Date";
                }
                System.out.println("here");
                LeaveEntity application = new LeaveEntity();
                System.out.println("here1");
                application.setRemarks(remarks);
                System.out.println("here2");
                application.setNumber_of_leave(days);
                System.out.println("here3");
                java.util.Date date = new java.util.Date();
                System.out.println("here4");
                Timestamp time_applied = new Timestamp(date.getTime());
                System.out.println("here5");
                Timestamp sd = new Timestamp(start.getTime());
                System.out.println("here6");
                Timestamp ed = new Timestamp(end.getTime());
                System.out.println("here7");
                application.setAppliedTime(time_applied);
                System.out.println("here8");
                application.setEndDate(ed);
                System.out.println("here9");
                application.setStartDate(sd);
                System.out.println("here10");
                application.setStatus("pending");
                System.out.println("here11");
                application.setType(type);
                System.out.println("here12");
                application.setEmployee(lao);
                System.out.println("here13");
                em.persist(application);
                System.out.println("here14");
                lao.getLeaveRecords().add(application);
                System.out.println("here15");
                em.merge(lao);
                System.out.println("here16");
                return "applied";
            }

            if (lao.getNumber_of_leaves() < days + sum) {
                return "not enought leave";
            } else if (lao.getEmployee_account_status().equals("disabled")) {
                return "Employee is disabled";
            } else if (days < 1) {
                return "End date start before early date";
            } else if (checkBetween(leaves, start, end)) {
                return "Earlier leave has been applied";
            } else if (date1.after(start) || date1.after(end)) {
                return "Applied date is before today!";
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
                application.setType(type);
                em.persist(application);
                lao.getLeaveRecords().add(application);
                em.merge(lao);
                return "applied";
            }
        } catch (Exception ex) {
            return "No such Employee";
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
                    im.add(leave.getType());
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

    public void rejectLeaveID(Long id, String employee1) {
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
                    leave.setStatus("rejected");
                    Calendar c = Calendar.getInstance();
                    leave.setApprovedTime(new Timestamp(c.getTime().getTime()));
                    em.merge(leave);
                }
            }
        } catch (Exception ex) {

        }
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
                    if (employee.getNumber_of_leaves() >= leave.getNumber_of_leave() && leave.getType().equals("paid")) {
                        leave.setStatus("approved");
                        java.util.Date date = new java.util.Date();
                        Timestamp time = new Timestamp(date.getTime());
                        leave.setApprovedTime(time);
                        em.merge(leave);
                        employee.setNumber_of_leaves(employee.getNumber_of_leaves() - leave.getNumber_of_leave());
                        em.merge(employee);
                    } else {
                        leave.setStatus("approved");
                        java.util.Date date = new java.util.Date();
                        Timestamp time = new Timestamp(date.getTime());
                        leave.setApprovedTime(time);
                        em.merge(leave);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("here");
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

    public boolean approveByEmployee(String employee) {
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
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
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
            if (leave.getStatus().equals("pending") && leave.getType().equals("paid")) {
                sum += leave.getNumber_of_leave();
            }
        }
        return sum;
    }

    public String changePassword(String employeeName, String oldPass, String newPass) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            String oldPassE = hashingPassword(oldPass);
            if (e.getPassword().equals(oldPassE)) {
                String newPassE = hashingPassword(newPass);
                if (oldPassE.equals(newPassE)) {
                    return "Password same as old password";
                }
                e.setPassword(newPassE);
                e.setAccount_status("normal");
                em.merge(e);
                return "changed";
            } else {
                return "Old password is incorrect";
            }
        } catch (Exception ex) {
            return "no such user";
        }
    }

    public String changePasswordF(String employeeName, String oldPass, String newPass, String secretQuestion, String secretAnswer) {
        EmployeeEntity e = new EmployeeEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            String oldPassE = hashingPassword(oldPass);
            if (e.getPassword().equals(oldPassE)) {
                String newPassE = hashingPassword(newPass);
                if (oldPassE.equals(newPassE)) {
                    return "Password same as old password";
                }
                e.setPassword(newPassE);
                e.setAccount_status("normal");
                e.setSecretAnswer(secretAnswer);
                e.setSecretQuestion(secretQuestion);
                em.merge(e);
                return "changed";
            } else {
                return "Old password is incorrect";
            }
        } catch (Exception ex) {
            return "no such user";
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
                } else if (e.getAccount_status().equals("reset")) {
                    return "reset";
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

    public boolean extendMachineExpiry(String machineNumber) {
        MachineEntity machine = new MachineEntity();

        try {
            Query q = em.createQuery("select machine from MachineEntity machine where machine.machine_number =:id");
            q.setParameter("id", machineNumber);
            machine = (MachineEntity) q.getSingleResult();
            Collection<MachineMaintainenceEntity> machineR = machine.getMachineMaintainence();
            for (Object o : machineR) {
                MachineMaintainenceEntity mm = (MachineMaintainenceEntity) o;
                if (mm.getStatus().equals("incomplete")) {
                    mm.setStatus("complete");
                    em.merge(mm);
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(machine.getMachine_expiry());
            cal.add(Calendar.MONTH, machine.getExtension());
            Timestamp ts = new Timestamp(cal.getTime().getTime());
            machine.setMachine_expiry(ts);
            machine.setStatus("available");
            em.merge(machine);
            return true;
        } catch (Exception ex) {
            return false;
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

    private String createRandomPass() {
        SecureRandom random = new SecureRandom();
        String newPassword = new BigInteger(50, random).toString(32);
        System.out.println(newPassword);
        return newPassword;

    }

    public String sendActivationCode(String email) {
        ActivationCode code = new ActivationCode();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);

        Timestamp time = new Timestamp(c.getTime().getTime());
        String pass = createRandomPass();
        code.setCode(pass);
        code.setExpiry(time);
        em.persist(code);
        return pass;

    }

    @Override
    public boolean checkActivationCode(String code) {
        boolean available = false;

        Query q = em.createQuery("Select a from ActivationCode a where a.code = :code");
        q.setParameter("code", code);
        if (q.getResultList().size() >= 1) {
            available = true;
        }
        return available;
    }

    @Override
    public void deleteActivationCode(String code) {
        Query q = em.createQuery("Delete from ActivationCode a where a.code = :code");
        q.setParameter("code", code);
        q.executeUpdate();

    }

    public void addFillers(List<Vector> fillers) {
        Query z = em.createQuery("select c from Metal c");
        for (Object o : z.getResultList()) {
            Metal m = (Metal) o;
            m.setFillers(null);
            em.merge(m);
        }

        Query q = em.createQuery("select c from FillerEntity c");
        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;
            em.remove(f);
        }
        if (fillers != null) {
            for (Object o : fillers) {
                Vector im = (Vector) o;
                FillerComposition f = new FillerComposition();
                f.setName((String) im.get(1));
                f.setCopper(Integer.parseInt(im.get(2).toString()));
                f.setZinc(Integer.parseInt(im.get(3).toString()));
                f.setIron(Integer.parseInt(im.get(4).toString()));
                f.setLead(Integer.parseInt(im.get(5).toString()));
                f.setAluminium(Integer.parseInt(im.get(6).toString()));
                f.setCarbon(Integer.parseInt(im.get(7).toString()));
                f.setNickel(Integer.parseInt(im.get(8).toString()));
                f.setManganese(Integer.parseInt(im.get(9).toString()));
                f.setSilicon(Integer.parseInt(im.get(10).toString()));
                f.setChromium(Integer.parseInt(im.get(11).toString()));
                em.persist(f);
            }
            System.out.println("ompleted");
        }
    }

    public List<Vector> transferFillerInfo() {
        List<Vector> results = new ArrayList<Vector>();
        Query q = em.createQuery("select c from FillerEntity c");
        System.out.println("shit");
        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;
            Vector im = new Vector();
            im.add(f.getId());
            im.add(f.getName());
            im.add(f.getCopper());
            im.add(f.getZinc());
            im.add(f.getIron());
            im.add(f.getLead());
            im.add(f.getAluminium());
            im.add(f.getCarbon());
            im.add(f.getNickel());
            im.add(f.getManganese());
            im.add(f.getSilicon());
            im.add(f.getChromium());

            results.add(im);
        }
        if (results.isEmpty()) {
            System.out.println("here");
            return null;
        }
        System.out.println("collected");
        System.out.println(results.size());
        return results;
    }

    public List<Vector> transferMetalInfo() {
        List<Vector> results = new ArrayList<Vector>();
        Query q = em.createQuery("select c from Metal c");
        System.out.println("shit");
        for (Object o : q.getResultList()) {
            Metal f = (Metal) o;
            Vector im = new Vector();

            im.add(f.getMetalName());
            im.add(f.getAluminium());
            im.add(f.getCarbon());
            im.add(f.getCopper());
            im.add(f.getZinc());
            im.add(f.getIron());
            im.add(f.getManganese());
            im.add(f.getNickel());
            im.add(f.getLead());
            im.add(f.getSilicon());
            im.add(f.getChromium());

            results.add(im);
        }
        if (results.isEmpty()) {
            System.out.println("here");
            return null;
        }
        System.out.println("collected");
        System.out.println(results.size());
        return results;
    }

    public List<FillerComposition> fillerRecords() {

        List<FillerComposition> results = new ArrayList<FillerComposition>();
        Query q = em.createQuery("select c from FillerEntity c");

        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;

            results.add(f);
        }
        if (results.isEmpty()) {
            System.out.println("here");
            return null;
        }

        return results;

    }

    public void editFiller(FillerComposition filler) {
        if (filler == null) {
            System.out.println("filler info is missing");
        } else {
            em.merge(filler);
        }
    }

    public void deleteFiller(FillerComposition filler) {
        if (filler != null) {
            Long id = filler.getId();
            FillerComposition f = em.find(FillerComposition.class, id);
            if (f == null) {
                System.out.println("no filler to delete");
            } else {
                em.remove(f);
            }
        }
    }

    public void createRepair(MachineRepairEntity machine, Date date, String machineName) {
        Timestamp time = new Timestamp(date.getTime());
        machine.setDate(time);

        MachineEntity m = new MachineEntity();
        try {
            Query q = em.createQuery("select m from MachineEntity m where m.machine_name =:id");
            q.setParameter("id", machineName);
            m = (MachineEntity) q.getSingleResult();
            machine.setMachine(m);
            em.persist(machine);

            m.getMachineRepair().add(machine);

            em.merge(m);

        } catch (Exception ex) {

        }
    }

    public List<MachineRepairEntity> repairList(MachineEntity machine) {
        Collection<MachineRepairEntity> records = machine.getMachineRepair();

        List<MachineRepairEntity> results = new ArrayList<MachineRepairEntity>();

        for (Object o : records) {
            MachineRepairEntity m = (MachineRepairEntity) o;
            results.add(m);
        }
        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    public void addMetal(List<Vector> metals) {
        Query q = em.createQuery("Select c from Metal c");
        for (Object o : q.getResultList()) {
            Metal m = (Metal) o;
            m.setFillers(null);
            em.merge(m);
            em.remove(m);
            em.flush();
        }

        if (metals != null) {
            for (Object o : metals) {
                Vector im = (Vector) o;
                Metal f = new Metal();

                f.setMetalName(im.get(0).toString());
                f.setAluminium(Integer.parseInt(im.get(1).toString()));
                f.setCarbon(Integer.parseInt(im.get(2).toString()));
                f.setCopper(Integer.parseInt(im.get(3).toString()));
                f.setChromium(Integer.parseInt(im.get(4).toString()));
                f.setZinc(Integer.parseInt(im.get(5).toString()));
                f.setIron(Integer.parseInt(im.get(6).toString()));
                f.setLead(Integer.parseInt(im.get(7).toString()));
                f.setManganese(Integer.parseInt(im.get(8).toString()));
                f.setNickel(Integer.parseInt(im.get(9).toString()));
                f.setSilicon(Integer.parseInt(im.get(10).toString()));
                em.persist(f);
            }
        }
        System.out.println("metal added");
    }

    public List<Metal> metalRecords() {
        List<Metal> results = new ArrayList<Metal>();
        Query q = em.createQuery("select c from Metal c");

        for (Object o : q.getResultList()) {
            Metal f = (Metal) o;

            results.add(f);
        }
        if (results.isEmpty()) {
            System.out.println("no metal records found");
            return null;
        }

        return results;
    }

    public void editMetal(Metal metal) {
        if (metal == null) {
            System.out.println("no metal info present");
        } else {
            em.merge(metal);
        }
    }

    public void deleteMetal(Metal metal) {
        if (metal == null) {
            System.out.println("no metal to delete");
        } else {
            String id = metal.getMetalName();
            Metal m = em.find(Metal.class, id);
            if (m == null) {
                System.out.println("entity failed to find id of metal");
            } else {
                em.remove(m);
                em.flush();
            }
        }
    }

    public List<String> retrieveFillerNames() {
        Query q = em.createQuery("select c from FillerEntity c");
        List<String> results = new ArrayList<String>();
        for (Object o : q.getResultList()) {
            FillerComposition f = (FillerComposition) o;
            results.add(f.getName());
        }
        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    public List<String> metalNames() {
        List<String> result = new ArrayList<String>();
        Query q = em.createQuery("select c from Metal c");
        for (Object o : q.getResultList()) {
            Metal m = (Metal) o;
            result.add(m.getMetalName());
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public void createPairings(String metal, List<String> fillerChosen) {
        Metal m = new Metal();
        Collection<FillerComposition> fillers = new ArrayList<FillerComposition>();
        try {
            Query q = em.createQuery("select m from Metal m where m.metalName = :id");
            q.setParameter("id", metal);
            m = (Metal) q.getSingleResult();

            if (fillerChosen.isEmpty()) {
                System.out.println("no fillerchosen");
            }
            System.out.println(fillerChosen.size());
            for (Object o : fillerChosen) {
                String fn = (String) o;
                System.out.println(fn);
                FillerComposition f = new FillerComposition();
                q = em.createQuery("select f from FillerEntity f where f.name =:id");
                q.setParameter("id", fn);
                f = (FillerComposition) q.getSingleResult();
                fillers.add(f);
            }
            System.out.println("error here3");
            m.setFillers(fillers);
            System.out.println("error here4");
            em.merge(m);
            System.out.println("Pairing created");
        } catch (Exception ex) {
            System.out.println("error occured");
        }
    }

    public List<String> FillersNotAssociated(String metalName) {
        Metal m = new Metal();
        List<String> result = new ArrayList<String>();
        try {
            Query q = em.createQuery("select c from FillerEntity c");
            Query z = em.createQuery("select m from Metal m where m.metalName = :id");
            z.setParameter("id", metalName);
            m = (Metal) z.getSingleResult();
            for (Object o : q.getResultList()) {
                FillerComposition f = (FillerComposition) o;
                if (m.getFillers().contains(f)) {
                    System.out.println("should not add: not associated");
                } else {
                    result.add(f.getName());
                }
            }
            if (result.isEmpty()) {
                return new ArrayList<String>();
            } else {
                return result;
            }

        } catch (Exception ex) {
            return new ArrayList<String>();
        }

    }

    public List<String> FillersAssociated(String metalName) {
        Metal m = new Metal();
        List<String> result = new ArrayList<String>();
        try {
            Query q = em.createQuery("select c from FillerEntity c");
            Query z = em.createQuery("select m from Metal m where m.metalName = :id");
            z.setParameter("id", metalName);
            m = (Metal) z.getSingleResult();
            for (Object o : q.getResultList()) {
                FillerComposition f = (FillerComposition) o;
                if (m.getFillers().contains(f)) {
                    result.add(f.getName());
                } else {
                    System.out.println("should not add: associated");

                }
            }
            if (result.isEmpty()) {
                return new ArrayList<String>();
            } else {
                return result;
            }

        } catch (Exception ex) {
            return new ArrayList<String>();
        }
    }

   
}
