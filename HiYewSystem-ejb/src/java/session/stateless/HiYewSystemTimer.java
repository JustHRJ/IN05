/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.EmployeeEntity;
import entity.PayrollEntity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        for (Object o : q.getResultList()) {
            EmployeeEntity e = (EmployeeEntity) o;
            PayrollEntity p = new PayrollEntity();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("MMM,yyyy");
            p.setStatus("unset");
            p.setMonth(format.format(c.getTime()));
            em.persist(p);
            e.getPayRecords().add(p);
            em.merge(e);
        }
    }

    @Schedule(minute = "*/1", hour = "*")
    public void runEveryMinute() {
        log.log(Level.INFO,
                "running every minute .. now it's: " + new Date().toString());
    }
}
