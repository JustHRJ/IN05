/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.Quotation;
import entity.QuotationDescription;
import java.text.DateFormat;
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
    
    //public List<Quotation> receiveQuotations(){
      //  Query query = em.createQuery("Select q FROM Quotation AS q, where q.status='Processed' and (:curDate - :quoteDate)<= 30 ");
       // Date curDate = new Date();
        
       // return null;
    //}
}
    
    
    
    

    

    

