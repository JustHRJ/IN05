package session.stateless;

import entity.Customer;
import entity.ProductPurchaseOrder;
import entity.Quotation;
import entity.QuotationDescription;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/**
 *
 * @author Randy Ng
 */
public class CustomerSessionBeanRemoteInterfaceTest {
    //QuotationSessionBeanLocal quotationSessionBean = lookupQuotationSessionBeanLocal();

    CustomerSessionBeanRemoteInterface customerSessionBeanRemoteInterface = lookupCustomerSessionBeanRemote();

//    @PersistenceContext
//    private EntityManager em;
    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    private Customer customer;
    private Quotation quotation;
    private QuotationDescription qd;
    private ArrayList<QuotationDescription> qdList;
    private ProductPurchaseOrder ppo;
    Exception ex;

    public CustomerSessionBeanRemoteInterfaceTest() {
        customer = new Customer();
        quotation = new Quotation();
        qd = new QuotationDescription();
        qdList = new ArrayList<>();
        ppo = new ProductPurchaseOrder();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    /*
     @Test
     NORMAL CASE test01CreateCustomer case
     public void test01CreateCustomer(){
     System.out.println("NORMAL CASE: test01CreateCustomer");
        
     customer.setAddress1("Bobo Road");
     customer.setAddress2("Blk 905, #10-345");
     customer.setName("CustomerA");
     customer.setEmail("njxrandy91@gmail.com");
     customer.setPhone("91221212");
     customer.setPostalCode("653232");
     customer.setPw(customerSessionBeanRemoteInterface.encryptPassword("11111111"));
     customer.setSubscribeEmail_qPriceUpdates(true);
     customer.setSubscribeSMS_qPriceUpdates(true);
     customer.setSubscribeEmail_poDeliveryUpdates(true);
     customer.setSubscribeSMS_poDeliveryUpdates(true);
     customer.setUserName("jared");
     customer.setSecretQuestion("What is the name of your favourite teacher?");
     customer.setSecretAnswer("tanweekek");

     customerSessionBeanRemoteInterface.createCustomer(customer);
     customer = new Customer();
        
     Customer cust = customerSessionBeanRemoteInterface.getCustomerByUsername("jared");
     assertEquals(cust.getEmail(), "njxrandy91@gmail.com");
     }
     */

    @Test
    // NORMAL CASE test02EncryptPassword case
    public void test02EncryptPassword() throws NoSuchAlgorithmException {
        System.out.println("NORMAL CASE: test02EncryptPassword");

        String encrypted = null;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("11111111".getBytes(), 0, 8);
        encrypted = new BigInteger(1, messageDigest.digest()).toString(16);

        assertEquals(encrypted, customerSessionBeanRemoteInterface.encryptPassword("11111111"));
    }

    @Test
    // CONFLICT CASE test03EncryptPassword case
    public void test03EncryptPassword() throws NoSuchAlgorithmException {
        System.out.println("CONFLICT CASE: test03EncryptPassword");

        String encrypted = null;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("11111111".getBytes(), 0, 8);
        encrypted = new BigInteger(1, messageDigest.digest()).toString(16);

        assertNotEquals(encrypted, customerSessionBeanRemoteInterface.encryptPassword(null));
    }

    @Test
    // NORMAL CASE test04FindCustomer case
    public void test04FindCustomer() {
        System.out.println("NORMAL CASE: test04FindCustomer");
        assertNotNull(customerSessionBeanRemoteInterface.findCustomer("jared"));
    }

    @Test
    // CONFLICT CASE test05FindCustomer case
    public void test05FindCustomer() {
        System.out.println("CONFLICT CASE: test05FindCustomer");
        assertNull(customerSessionBeanRemoteInterface.findCustomer("alonso"));
    }

    @Test
    // NORMAL CASE test06GetCustomerByUsername case
    public void test06GetCustomerByUsername() {
        System.out.println("NORMAL CASE: test06GetCustomerByUsername");
        assertNotNull(customerSessionBeanRemoteInterface.getCustomerByUsername("jared"));
    }

    @Test
    // CONFLICT CASE test07GetCustomerByUsername case
    public void test07GetCustomerByUsername() {
        System.out.println("CONFLICT CASE: test07GetCustomerByUsername");
        assertNull(customerSessionBeanRemoteInterface.getCustomerByUsername("haha"));
    }

    @Test
    // NORMAL CASE test08GetAllCustomer case
    public void test08GetAllCustomer() {
        System.out.println("NORMAL CASE: test08GetAllCustomer");
        assertEquals(customerSessionBeanRemoteInterface.getAllCustomer().size(), 23);
    }

    @Test
    // NORMAL CASE test09ResetCustomerPassword case
    public void test09ResetCustomerPassword() {
        System.out.println("NORMAL CASE: test09ResetCustomerPassword");
        String result = customerSessionBeanRemoteInterface.resetCustomerPassword("jared");
        assertNotNull(result);
    }

    @Test
    // NORMAL CASE test10UpdateCustomer case
    public void test10UpdateCustomer() {
        System.out.println("NORMAL CASE: test10UpdateCustomer");
        try {
            customer = customerSessionBeanRemoteInterface.getCustomerByUsername("jared");
            customerSessionBeanRemoteInterface.updateCustomer(customer);
        } catch (Exception e) {
            ex = e;
        }
        assertEquals(null, ex);
    }

    private CustomerSessionBeanRemoteInterface lookupCustomerSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CustomerSessionBeanRemoteInterface) c.lookup("java:global/HiYewSystem/HiYewSystem-ejb/CustomerSessionBean!session.stateless.CustomerSessionBeanRemoteInterface");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
