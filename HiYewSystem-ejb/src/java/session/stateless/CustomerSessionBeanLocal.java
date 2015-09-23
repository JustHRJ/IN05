package session.stateless;

import entity.Customer;
import entity.Quotation;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CustomerSessionBeanLocal {

    public void createCustomer(Customer cust);

    public List<Customer> getAllCustomer();

    public Customer getCustomerByUsername(String username);

    public void updateCustomer(Customer c1);

    public void addQuotation(String username, Quotation quotation);

    public String encryptPassword(String password);

}
