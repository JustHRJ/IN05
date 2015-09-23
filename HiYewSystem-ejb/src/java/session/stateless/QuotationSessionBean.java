/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.Quotation;
import entity.QuotationDescription;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author: Jit Cheong
 */
@Stateless
public class QuotationSessionBean implements QuotationSessionBeanLocal {
    
    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public String getQuotationNo(String username){
        Customer c = em.find(Customer.class, username);
        return generateQuotationNo(c);
    }
    
    public String generateQuotationNo(Customer customer){
        String custName = customer.getName();
        String newQuotationNo = "";
        String [] splitArray = null;
        if(custName.contains("-")){
            splitArray = custName.split("-");
            for(String s: splitArray){
                newQuotationNo += s.substring(0, 1);
            }
        }else if(custName.contains(" ")){
            splitArray = custName.split(" ");
            for(String s: splitArray){
                newQuotationNo += s.substring(0, 1);
            }
        }else{
            newQuotationNo = custName;
        }
            newQuotationNo += new SimpleDateFormat("yyyyMMddss").format(Calendar.getInstance().getTime());
            
            return newQuotationNo; 
    }
    
    @Override
    public void createQuotation(Quotation quotation) {
        em.persist(quotation);
    }
    
    @Override
    public void createQuotationDesciption(QuotationDescription quotationDescription){
        
        em.persist(quotationDescription);
    }
    
    @Override
    public List<Quotation> receivedQuotations(String username){
        
        
        Query query = em.createQuery("Select q FROM Quotation AS q where q.status='Processed' and q.date >= :thirtyDaysAgo AND q.customer.userName=:username ");
        
        Date now = addDays(new Date(), -30);
        Timestamp thirtyDaysAgo = new Timestamp(now.getTime());
        
        query.setParameter("thirtyDaysAgo", thirtyDaysAgo);
        query.setParameter("username", username);
        
        List <Quotation> quotations = query.getResultList();
        return quotations;
    }
    
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
    
    
    
    

    

    

