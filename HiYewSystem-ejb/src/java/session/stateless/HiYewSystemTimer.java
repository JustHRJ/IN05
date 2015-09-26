/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.EmployeeEntity;
import entity.LeaveEntity;
import entity.PayrollEntity;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger
            .getLogger(HiYewSystemTimer.class.getName());

    @Schedule(minute = "*/1", hour = "*")
    public void runEveryMonth() {
        Query q = em.createQuery("select c from EmployeeEntity c");
        double salary;
        for (Object o : q.getResultList()) {
            EmployeeEntity e = (EmployeeEntity) o;
            if (!(e.getAccount_status().equals("disabled"))) {
                PayrollEntity p = new PayrollEntity();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                SimpleDateFormat format = new SimpleDateFormat("MMM,yyyy");
                p.setStatus("unset");
                p.setMonth(format.format(c.getTime()));
                p.setEmployee(e);

                Calendar cd = Calendar.getInstance();
                cd.add(Calendar.MONTH, -1);
                Timestamp time = new Timestamp(cd.getTime().getTime());
                Timestamp time1 = e.getEmployee_employedDate();
                if (time1.after(time)) {
                    salary = calculateSalary(e, time, time1);
                } else {
                    salary = calculateSalaryFull(e);
                }
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

    private double calculateSalary(EmployeeEntity e, Timestamp currentDate, Timestamp employedDate) {
        double currentSalary = e.getEmployee_basic();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, - 1);
        Timestamp currentTime = new Timestamp(c.getTime().getTime());
        int diffInDays = (int) ((currentTime.getTime() - employedDate.getTime())
                / (1000 * 60 * 60 * 24)) + 1 ;

        int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

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
                if (cal.get(Calendar.MONTH) == c.get(Calendar.MONTH) || cal2.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                    if (cal2.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                        diffInDays -= cal2.get(Calendar.DATE) - cal.get(Calendar.DATE) + 1;
                    } else if (cal.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                        diffInDays -= numberOfDays - cal.get(Calendar.DATE) + 1;
                    } else if (cal2.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                        diffInDays -= numberOfDays - cal2.get(Calendar.DATE) + 1;
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
        c.add(Calendar.DATE, -1);
        
        int numberOfDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int diffInDays = numberOfDays;
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
                if (cal.get(Calendar.MONTH) == c.get(Calendar.MONTH) || cal2.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                    if (cal2.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                        diffInDays -= cal2.get(Calendar.DATE) - cal.get(Calendar.DATE) + 1;
                    } else if (cal.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                        diffInDays -= numberOfDays - cal.get(Calendar.DATE) + 1;
                    } else if (cal2.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                        diffInDays -= numberOfDays - cal2.get(Calendar.DATE) + 1;
                    }
                }
            }
        }
        currentSalary = diffInDays / (double) numberOfDays * currentSalary;
        currentSalary = Math.round(currentSalary * 100.0) / 100.0;
        return currentSalary;
    }

}
