/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CompositeQuotationDescKey;
import entity.Customer;
import entity.Quotation;
import entity.QuotationDescription;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
            newQuotationNo = custName.substring(0, 4);
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
        //CompositeQuotationDescKey cqd = new CompositeQuotationDescKey();
        //cqd.setQuotationNo(quotationDescription.getQuotationNo());
        //cqd.setQuotationDescNo(quotationDescription.getQuotationDescNo());
        
        em.persist(quotationDescription);
    }
    
    //@Override
    //public String createQuotationByUsername(String username){
      //  String quotationNo = "";
        
        //Quotation q;
        //if(q == null){
          //  Customer c = em.find(Customer.class, username);
            
            //q = new Quotation();
        //    quotationNo = q.getQuotationNo();
          //  em.persist(q);
            //set quotation into customer
            //c.addQuotations(q);
            //System.out.println("Created new quotation");
        //}
    //    return quotationNo;
    //}
    
    //@Override
   // public Quotation getQuotationById(String quotationNo){
   //     Quotation q = em.find(Quotation.class, quotationNo);
   //     return q;
   // }
}
    
    
    
    

    

    

