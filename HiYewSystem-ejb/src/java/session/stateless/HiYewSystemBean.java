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
public class HiYewSystemBean implements HiYewSystemBeanLocal, HiYewSystemBeanRemoteInterface {

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
            if (format.format(c.getAppliedDate()).equals(months)) {
                claimRecords.add(c);
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
        if(claim == null){
            return false;
        } 
        if(employeeName == null || employeeName.isEmpty()){
            return false;
        }
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
        if (training == null || employee == null || employee.equals("")) {
            System.out.println("here");
            return false;
        }
        try {
            EmployeeEntity e = new EmployeeEntity();
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name =:id");
            q.setParameter("id", employee);
            e = (EmployeeEntity) q.getSingleResult();
            if (!(training.getEmployeeRecords().contains(e))) { // this part throws exception for testing
                System.out.println("here1");
                return false;
            } else {
                training.getEmployeeRecords().remove(e);
                em.merge(training);
                return true;
            }

        } catch (Exception ex) {
            System.out.println("here2");
            return false;
        }
    }

    public boolean addTrainingEmployee(TrainingScheduleEntity schedule, String name) {
        if (schedule == null) {
            System.out.println("here");
            return false;
        }
        if (name == null || name.equals("")) {
            System.out.println("here2");
            return false;
        }
        EmployeeEntity e = new EmployeeEntity();
        TrainingScheduleEntity t = new TrainingScheduleEntity();
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name =:id");
            q.setParameter("id", name);
            System.out.println("here7");
            e = (EmployeeEntity) q.getSingleResult();
            System.out.println("here8");
            System.out.println(e);

            if (!schedule.getEmployeeRecords().isEmpty()) {
                if (schedule.getEmployeeRecords().contains(e)) {
                    System.out.println("here3");
                    return false;
                } else {
                    if (schedule.getTrianingSize() - schedule.getEmployeeRecords().size() == 0) {
                        System.out.println("here4");
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
                    System.out.println("here5");
                    return true;
                }
            } else {

                schedule.getEmployeeRecords().add(e);
                em.merge(schedule);
                System.out.println("here9");
                return true;
            }

        } catch (Exception ex) {
            System.out.println("here6");
            ex.printStackTrace();
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
        if (trainingCode == null || trainingCode.equals("")) {
            return false;
        }
        if (trainingStart == null || (trainingEnd == null)) {
            return false;
        }

        if (trainingName == null || trainingName.equals("")) {
            return false;
        }

        if (size < 1) {
            return false;
        }

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
                t.getEmployeeRecords().size();
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
            t.getEmployeeRecords().size();
            result.add(t);
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public boolean updatePay(PayrollEntity pay, boolean bonus, double others) {

        if (pay == null) {
            return false;
        }

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
        if (employeeName == null || employeeName.equals("")) {
            return false;
        }
        if (late < 0 || sick < 0 || overtime < 0) {
            return false;
        }

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

    public List<PayrollEntity> getPayroll(String employeeName) {
        List<PayrollEntity> result = new ArrayList<PayrollEntity>();

        if (employeeName == null || employeeName.equals("")) {
            return null;
        }

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
        if (employeeName == null || employeeName.equals("")) {
            return null;
        }
        if (month == null || month.equals("")) {
            return null;
        }
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

// still having trouble comparing due to calendar comparison issues. to try different approach
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
            try {
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
                    xin.setAvailability(true);
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
            } catch (Exception xx) {
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

    public boolean extendEmployeePass(String employeeName, Timestamp next) {
        if (employeeName == null || employeeName.equals("")) {
            return false;
        }
        if (next == null) {
            return false;
        }
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
        if (employee == null) {
            return false;
        }
        if ((!(employeeA.isEmpty())) && (!(employeeA.equals(employee.getEmployee_address())))) {
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
                    }
                    check = true;
                } else {
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

                    }
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
        if (name == null || name.equals("")) {
            return true;
        }
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

            if (new Timestamp(start.getTime()).before(lao.getEmployee_employedDate()) || new Timestamp(end.getTime()).before(lao.getEmployee_employedDate())) {
                return "Cannot apply leave before employed Date";
            }
            if (lao.getNumber_of_leaves() < days + sum) {
                return "not enought leave";
            } else if (lao.getEmployee_account_status().equals("disabled")) {
                return "Employee is disabled";
            } else if (days < 1) {
                return "End date start before start date";
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
            throw ex;
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
                if (Objects.equals(leave.getId(), id)) {
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
            throw ex;
        }
    }

    public List<LeaveEntity> viewEmployeeLeavePending(String employeeName) {
        if (employeeName == null || employeeName.equals("")) {
            return null;
        }
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

    public String EmployeeStatus(String employeeName) {
        if (employeeName == null || employeeName.equals("")) {
            return "";
        }
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
            int count = 0;
            if (allRight <= e.getNumber_of_leaves()) {
                for (Object o : leaveRecords) {
                    LeaveEntity l = (LeaveEntity) o;
                    System.out.println(l.getStatus());
                    if (l.getStatus().equals("pending")) {
                        l.setStatus("approved");
                        java.util.Date date = new java.util.Date();
                        Timestamp approveTime = new Timestamp(date.getTime());
                        l.setApprovedTime(approveTime);
                        e.setNumber_of_leaves(e.getNumber_of_leaves() - l.getNumber_of_leave());
                        count += 1;
                        em.merge(l);
                        em.merge(e);
                    }
                }
                System.out.println(count);
                if (count > 0) {
                    return true;
                } else {
                    return false;
                }
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
            throw ex;
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
        if (employeeName == null || employeeName.equals("")) {
            return "no such user";
        }
        if (oldPass == null || oldPass.equals("")) {
            return "no old password input";
        }
        if (newPass == null || newPass.equals("")) {
            return "no newPass input";
        }
        try {
            Query q = em.createQuery("select e from EmployeeEntity e where e.employee_name = :id");
            q.setParameter("id", employeeName);
            e = (EmployeeEntity) q.getSingleResult();
            String oldPassE = hashingPassword(oldPass);
            System.out.println("old password in xhtml" + oldPassE);
            System.out.println("password from database" + e.getPassword());
            if (e.getPassword().equals(oldPassE)) {
                String newPassE = hashingPassword(newPass);
                if (oldPassE.equals(newPassE)) {
                    return "Password same as old password";
                }
                e.setPassword(newPassE);
                e.setAccount_status("normal");
                em.merge(e);
                System.out.println("here - changed");
                return "changed";
            } else {
                System.out.println("here - unchanged");
                return "Old password is incorrect";
            }
        } catch (Exception ex) {

            System.out.println("here - screwed");
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

    private boolean checkUsername(String username) {
        if (username == null || username.equals("")) {
            return false;
        }
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
        if (username == null || username.equals("")) {
            return null;
        }
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
        if (username == null || username.equals("")) {
            return null;
        }
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
        if (email == null || email.equals("")) {
            return null;
        }
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
        if (code == null || code.equals("")) {
            return false;
        }
        Query q = em.createQuery("Select a from ActivationCode a where a.code = :code");
        q.setParameter("code", code);
        if (q.getResultList().size() >= 1) {
            available = true;
        }
        return available;
    }

    @Override
    public void deleteActivationCode(String code) {
        if (code == null || code.equals("")) {

        }
        try {
            Query q = em.createQuery("Delete from ActivationCode a where a.code = :code");
            q.setParameter("code", code);
            q.executeUpdate();
        } catch (Exception ex) {
            System.out.println("code does not exist");
        }
    }

}
