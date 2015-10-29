package session.stateless;

import entity.Customer;
import entity.CustomerPO;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CustomerPOSessionBean implements CustomerPOSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createPO(CustomerPO customerPO) {
        em.persist(customerPO);
    }

    @Override
    public void conductMerge(CustomerPO customerPO) {
        em.merge(customerPO);
    }

    @Override
    public List<CustomerPO> receivedCustomerPO(String username) {

        Customer c = em.find(Customer.class, username);

        return c.getCustomerPOs();
    }

    public Date getFirstDayOfYear(int year) {
        Calendar cld = Calendar.getInstance();
        cld.set(year, 0, 0);
        return cld.getTime();
    }

    public Date getLastDayOfYear(int year) {
        Calendar cld = Calendar.getInstance();
        cld.set(year + 1, 0, -1);
        return cld.getTime();
    }

    @Override
    public List<CustomerPO> getCustomerPOsByYear(Integer year) {
        
        Query query = em.createQuery("Select p FROM CustomerPO AS p where p.poDate >= :firstDayOfYear and p.poDate <= :lastDayOfYear");

        Date first = getFirstDayOfYear(year);
        Timestamp firstDayOfYear = new Timestamp(first.getTime());

        Date last = getLastDayOfYear(year);
        Timestamp lastDayOfYear = new Timestamp(last.getTime());

        query.setParameter("firstDayOfYear", firstDayOfYear);
        query.setParameter("lastDayOfYear", lastDayOfYear);

        List<CustomerPO> customerPOs = query.getResultList();
        //System.out.println("quotation size is " + quotations.size());
        return customerPOs;
    }

}
