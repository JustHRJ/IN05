/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.Quotation;
import entity.QuotationDescription;
import java.util.ArrayList;
import java.util.List;
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

    public void createQuotationDesciption(QuotationDescription quotationDescription);

    public List<Quotation> receivedQuotations(String username);

    public List<Quotation> receivedCustomerNewQuotations(String status, Integer year);

    public void conductMerge(Quotation quotation);

    public void updateQuotationPrices(ArrayList<QuotationDescription> list);

    public void updateQuotationStatus(Quotation q);




  
    
}
