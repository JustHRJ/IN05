/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.Quotation;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface QuotationSessionBeanLocal {

    //public String createQuotationByUsername(String username);

    //public Quotation getQuotationById(String quotationNo);

    public String getQuotationNo(String username);

    public void createQuotation(Quotation quotation);




  
    
}
