/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ActivationCode;
import entity.EmployeeClaimEntity;
import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.PayrollEntity;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Tracykkh
 */
@Stateless
@LocalBean
public class HiYewSystemTimer {

    //   Add business logic below. (Right-click in editor and choose
    //   "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger
            .getLogger(HiYewSystemTimer.class.getName());

    
    // this method activates every beginning of the year, and checks for all "approved" leave, so that all the leaves that has been accumulated has been replaced in the new year
    @Schedule(dayOfMonth = "1", month = "1", year = "*")
    public void resetLeaves() {
        EmployeeEntity e = new EmployeeEntity();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Query q = em.createQuery("Select c from EmployeeEntity c");
        Calendar c = Calendar.getInstance();
        // to set the test for 2015
        // c.add(Calendar.YEAR, -1); <-- by right
        c.set(Calendar.YEAR, 2015);
        Date cd = c.getTime();
        String pastYear = format.format(cd);
        System.out.println(pastYear);

        for (Object o : q.getResultList()) {
            e = (EmployeeEntity) o;
            Collection<LeaveEntity> leaves = e.getLeaveRecords();
            int leaveSum = 0;
            for (Object w : leaves) {
                LeaveEntity l = (LeaveEntity) w;
                if (l.getApprovedTime() != null && l.getType().equals("paid") && l.getStatus().equals("approved")) {
                    Timestamp leaveTime = l.getApprovedTime();
                    Date leaveTimeD = new Date(leaveTime.getTime());
                    String approveYear = format.format(leaveTimeD);
                    System.out.println(approveYear);
                    if (approveYear.equals(pastYear)) {
                        leaveSum += l.getNumber_of_leave();
                        l.setStatus("approved (Archived)");
                    }
                    em.merge(l);
                }
                if(l.getType().equals("paid") && l.getStatus().equals("pending")){
                    String appliedYear = format.format(new Date(l.getAppliedTime().getTime()));
                    if(appliedYear.equals(pastYear)){
                        l.setStatus("rejected");
                        l.setRemarks("Notice - past overdue leave application");
                        em.merge(l);
                    }
                }
            }
            e.setNumber_of_leaves(leaveSum + e.getNumber_of_leaves());
            
            em.merge(e);
        }
    }

    @Schedule(dayOfMonth = "1")
    public void runEveryMonth() {
        Query q = em.createQuery("select c from EmployeeEntity c");
        double salary;
        for (Object o : q.getResultList()) {
            EmployeeEntity e = (EmployeeEntity) o;
            if (!(e.getAccount_status().equals("disabled"))) {
                PayrollEntity p = new PayrollEntity();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                c.set(Calendar.DATE, numberOfDays);
                SimpleDateFormat format = new SimpleDateFormat("MMM,yyyy");
                p.setStatus("unset");
                p.setMonth(format.format(c.getTime()));
                p.setEmployee(e);

                Calendar cd = Calendar.getInstance();
                cd.add(Calendar.MONTH, -1);
                cd.set(Calendar.DATE, 1);
                Timestamp time = new Timestamp(cd.getTime().getTime());
                Timestamp time1 = e.getEmployee_employedDate();
                if (time1.after(time)) {
                    salary = calculateSalary(e, time, time1);
                } else {
                    salary = calculateSalaryFull(e);
                }
                double taxiClaim = calculateTaxiClaim(e, c);
                p.setTaxiClaim(taxiClaim);
                p.setSalary(salary);
                em.persist(p);
                e.getPayRecords().add(p);
                em.merge(e);
            }
        }
    }

    @Schedule(minute = "*/1", hour = "*")
    public void runEveryMinute() {
        log.log(Level.INFO,
                "running every minute .. now it's: " + new Date().toString());
    }

    @Schedule(hour = "*")
    public void runEveryHour() {
        Query q = em.createQuery("Select c FROM ActivationCode c");
        Calendar c = Calendar.getInstance();
        Timestamp time = new Timestamp(c.getTime().getTime());
        for (Object o : q.getResultList()) {
            ActivationCode code = (ActivationCode) o;
            if (code.getExpiry().before(time)) {
                em.remove(code);
            }
        }
        log.log(Level.INFO,
                "running every hour.. now it's: " + new Date().toString());
    }

    private double calculateSalary(EmployeeEntity e, Timestamp currentDate, Timestamp employedDate) {
        double currentSalary = e.getEmployee_basic();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DATE, numberOfDays);
        Calendar d = Calendar.getInstance();
        d.setTime(c.getTime());
        d.add(Calendar.MONTH, -1);
        d.set(Calendar.MILLISECOND, 0);
        d.set(Calendar.SECOND, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.DATE, 1);

        System.out.println(c.getTime().getTime());
        System.out.println(employedDate.getTime());
        int diffInDays = (int) ((c.getTime().getTime() - employedDate.getTime())
                / (1000 * 60 * 60 * 24));

        System.out.println(diffInDays + "here");

        if (diffInDays > 0) {
            diffInDays += 1;
        } else {
            if (employedDate.after(c.getTime())) {
                diffInDays = 0;
            }
            if (employedDate.before(c.getTime())) {
                diffInDays = 1;
            }
            if (new Timestamp(c.getTime().getTime()).equals(employedDate)) {
                diffInDays = 1;
            }
        }
        System.out.println("diff in Days " + diffInDays);

        Collection<LeaveEntity> leaveRecords = e.getLeaveRecords();

        for (Object o : leaveRecords) {
            LeaveEntity l = (LeaveEntity) o;
            if (l.getStatus().equals("approved") && l.getType().equals("unpaid")) {
                Timestamp month = l.getStartDate();
                Timestamp month2 = l.getEndDate();
                Date date2 = new Date(month2.getTime());
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);
                Date date1 = new Date(month.getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                if (cal.after(d) && cal2.before(c)) {
                    diffInDays -= calculateDays(cal, cal2);
                    System.out.println("here");
                } else if (cal.after(d) && cal2.after(c) && cal.before(c)) {
                    diffInDays -= calculateDays(cal, c);
                    System.out.println("here1");
                } else if (d.after(cal) && c.after(cal2) && cal2.after(d)) {
                    diffInDays -= calculateDays(d, cal2);
                    System.out.println("here2");
                } else if (d.after(cal) && cal2.after(c)) {
                    diffInDays = 0;
                    System.out.println("here3");
                } else if (cal.equals(d)) {
                    if (c.after(cal2)) {
                        diffInDays -= calculateDays(cal, cal2);
                        System.out.println("here4");
                    } else {
                        diffInDays = 0;
                        System.out.println("here5");
                    }
                } else if (cal2.equals(d)) {
                    diffInDays -= 1;
                    System.out.println("here6");
                } else if (cal.equals(c)) {
                    diffInDays -= 1;
                    System.out.println("here7");
                } else if (cal2.equals(c)) {
                    if (cal.after(d)) {
                        diffInDays -= calculateDays(cal, cal2);
                        System.out.println("here8");
                    } else {
                        diffInDays = 0;
                        System.out.println("here9");
                    }
                }
            }
        }

        currentSalary = diffInDays / (double) numberOfDays * currentSalary;
        currentSalary = Math.round(currentSalary * 100.0) / 100.0;
        return currentSalary;
    }

    private double calculateSalaryFull(EmployeeEntity e) {
        double currentSalary = e.getEmployee_basic();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);

        Calendar d = Calendar.getInstance();
        d.setTime(c.getTime());
        d.add(Calendar.MONTH, -1);
        d.set(Calendar.MILLISECOND, 0);
        d.set(Calendar.SECOND, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.HOUR_OF_DAY, 0);

        System.out.println(c.getTime());
        System.out.println(d.getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int diffInDays = numberOfDays;
        Collection<LeaveEntity> leaveRecords = e.getLeaveRecords();
        c.set(Calendar.DATE, numberOfDays);
        d.set(Calendar.DATE, 1);
        for (Object o : leaveRecords) {
            LeaveEntity l = (LeaveEntity) o;
            if (l.getStatus().equals("approved") && l.getType().equals("unpaid")) {
                Timestamp month = l.getStartDate();
                Timestamp month2 = l.getEndDate();
                Date date2 = new Date(month2.getTime());
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);
                Date date1 = new Date(month.getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                if (cal.after(d) && cal2.before(c)) {
                    diffInDays -= calculateDays(cal, cal2);
                    System.out.println("here");
                } else if (cal.after(d) && cal2.after(c) && cal.before(c)) {
                    diffInDays -= calculateDays(cal, c);
                    System.out.println("here1");
                } else if (d.after(cal) && c.after(cal2) && cal2.after(d)) {
                    diffInDays -= calculateDays(d, cal2);
                    System.out.println("here2");
                } else if (d.after(cal) && cal2.after(c)) {
                    diffInDays = 0;
                    System.out.println("here3");
                } else if (cal.equals(d)) {
                    if (c.after(cal2)) {
                        diffInDays -= calculateDays(cal, cal2);
                        System.out.println("here4");
                    } else {
                        diffInDays = 0;
                        System.out.println("here5");
                    }
                } else if (cal2.equals(d)) {
                    diffInDays -= 1;
                    System.out.println("here6");
                } else if (cal.equals(c)) {
                    diffInDays -= 1;
                    System.out.println("here7");
                } else if (cal2.equals(c)) {
                    if (cal.after(d)) {
                        diffInDays -= calculateDays(cal, cal2);
                        System.out.println("here8");
                    } else {
                        diffInDays = 0;
                        System.out.println("here9");
                    }
                }

            }
        }
        currentSalary = diffInDays / (double) numberOfDays * currentSalary;
        currentSalary = Math.round(currentSalary * 100.0) / 100.0;
        return currentSalary;
    }

    private int calculateDays(Calendar c, Calendar d) {
        Timestamp td = new Timestamp(d.getTime().getTime());
        Timestamp tc = new Timestamp(c.getTime().getTime());

        int diffInDays = (int) ((td.getTime() - tc.getTime())
                / (1000 * 60 * 60 * 24));
        System.out.println(diffInDays);
        return diffInDays + 1;
    }

    private double calculateTaxiClaim(EmployeeEntity e, Calendar c) {
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        c.add(Calendar.MONTH, 1);
        String payMonth = format.format(c.getTime());
        double total = 0.00;

        for (Object o : e.getEmployeeClaims()) {
            EmployeeClaimEntity ec = (EmployeeClaimEntity) o;
            if (ec.getApprovedDate() == null) {

            } else {
                String claimMonth = format.format(new Date(ec.getApprovedDate().getTime()));
                if (claimMonth.equals(payMonth) && ec.getStatus().equals("approved")) {
                    total += ec.getClaimAmt();
                }
            }
        }
        return total;
    }

}
