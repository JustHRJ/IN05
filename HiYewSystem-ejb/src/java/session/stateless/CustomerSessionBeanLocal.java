/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.Quotation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author: Jit Cheong
 */
@Local
public interface CustomerSessionBeanLocal {

    public void createCustomer(Customer cust);

    public List<Customer> getAllCustomer();

    public Customer getCustomerByUsername(String username);

    public void updateCustomer(Customer c1);

    public void addQuotation(String username, Quotation quotation);

    
    
}
