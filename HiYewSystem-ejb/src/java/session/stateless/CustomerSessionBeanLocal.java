package session.stateless;

import entity.Customer;
import entity.CustomerPO;
import entity.Quotation;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CustomerSessionBeanLocal {

    public void createCustomer(Customer cust);
    
    public Customer findCustomer(String username);

    public List<Customer> getAllCustomer();

    public Customer getCustomerByUsername(String username);

    public void updateCustomer(Customer c1);

    public String resetCustomerPassword(String username);

    public void addQuotation(String username, Quotation quotation);

    public void addPurchaseOrder(String username, CustomerPO cpo);

    public void addProductQuotation(String username, ProductQuotation productQuotation);

    public void addProductPurchaseOrder(String username, ProductPurchaseOrder productPurchaseOrder);

    public String encryptPassword(String password);

}
