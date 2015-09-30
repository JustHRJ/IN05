package session.stateless;

import entity.Customer;
import entity.CustomerPO;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
import entity.Quotation;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.SecureRandom;

@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createCustomer(Customer customer) {
        em.persist(customer);
    }
    
    @Override
    public Customer findCustomer(String username) {
        Customer c = em.find(Customer.class, username);
        if (c == null) {
            System.out.println("Customer is null");
            return null;
        } else {
            return c;
        }
    }

    @Override
    public String encryptPassword(String password) {
        String encrypted = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes(), 0, password.length());
            encrypted = new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encrypted;
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        Customer c = em.find(Customer.class, username);
        if (c != null) {
        }
        return c;
    }

    @Override
    public void addQuotation(String username, Quotation quotation) {
        Customer c = em.find(Customer.class, username);
        if (c == null) {
            System.out.println("Customer is null");
        } else {
            c.addQuotations(quotation);
        }
    }

    @Override
    public void addPurchaseOrder(String username, CustomerPO cpo) {
        Customer c = em.find(Customer.class, username);
        if (c == null) {
            System.out.println("Customer is null");
        } else {
            c.addCustomerPO(cpo);
        }
    }

    @Override
    public List<Customer> getAllCustomer() {
        Query query = em.createQuery("SELECT c FROM Customer c");
        return query.getResultList();
    }

    @Override
    public void updateCustomer(Customer c1) {
        Customer c = em.find(Customer.class, c1.getUserName());
        c.setName(c1.getName());
        c.setAddress1(c1.getAddress1());
        c.setPhone(c1.getPhone());
        c.setPw(c1.getPw());
        c.setAddress2(c1.getAddress2());
        c.setEmail(c1.getEmail());
        c.setPostalCode(c1.getPostalCode());
        c.setSubscribeEmail(c1.getSubscribeEmail());
    }

    @Override
    public String resetCustomerPassword(String username) {
        Customer c = em.find(Customer.class, username);
        SecureRandom random = new SecureRandom();
        String newPassword = new BigInteger(50, random).toString(32);
        c.setPw(encryptPassword(newPassword));
        em.persist(c);
        em.flush();
        return c.getName() + ":" + newPassword + ":" + c.getEmail();
    }

    public void addProductQuotation(String username, ProductQuotation productQuotation) {
        Customer customer = em.find(Customer.class, username);
        if (customer == null) {
            System.out.println("Customer is null.");
        } else {
            customer.addProductQuotationList(productQuotation);
        }
    }

    public void addProductPurchaseOrder(String username, ProductPurchaseOrder productPurchaseOrder) {
        Customer customer = em.find(Customer.class, username);
        if (customer == null) {
            System.out.println("Customer is null.");
        } else {
            customer.addProductPurchaseOrderList(productPurchaseOrder);
        }
    }
}
