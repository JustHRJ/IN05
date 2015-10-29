/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import entity.CustomerPO;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
import entity.Quotation;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class CustomerSessionBeanTest {
    
    public CustomerSessionBeanTest() {
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

    /**
     * Test of createCustomer method, of class CustomerSessionBean.
     */
    @Test
    public void testCreateCustomer() throws Exception {
        System.out.println("createCustomer");
        Customer customer = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        instance.createCustomer(customer);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findCustomer method, of class CustomerSessionBean.
     */
    @Test
    public void testFindCustomer() throws Exception {
        System.out.println("findCustomer");
        String username = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        Customer expResult = null;
        Customer result = instance.findCustomer(username);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encryptPassword method, of class CustomerSessionBean.
     */
    @Test
    public void testEncryptPassword() throws Exception {
        System.out.println("encryptPassword");
        String password = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        String expResult = "";
        String result = instance.encryptPassword(password);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCustomerByUsername method, of class CustomerSessionBean.
     */
    @Test
    public void testGetCustomerByUsername() throws Exception {
        System.out.println("getCustomerByUsername");
        String username = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        Customer expResult = null;
        Customer result = instance.getCustomerByUsername(username);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addQuotation method, of class CustomerSessionBean.
     */
    @Test
    public void testAddQuotation() throws Exception {
        System.out.println("addQuotation");
        String username = "";
        Quotation quotation = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        instance.addQuotation(username, quotation);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPurchaseOrder method, of class CustomerSessionBean.
     */
    @Test
    public void testAddPurchaseOrder() throws Exception {
        System.out.println("addPurchaseOrder");
        String username = "";
        CustomerPO cpo = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        instance.addPurchaseOrder(username, cpo);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCustomer method, of class CustomerSessionBean.
     */
    @Test
    public void testGetAllCustomer() throws Exception {
        System.out.println("getAllCustomer");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        List<Customer> expResult = null;
        List<Customer> result = instance.getAllCustomer();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCustomer method, of class CustomerSessionBean.
     */
    @Test
    public void testUpdateCustomer() throws Exception {
        System.out.println("updateCustomer");
        Customer c1 = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        instance.updateCustomer(c1);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resetCustomerPassword method, of class CustomerSessionBean.
     */
    @Test
    public void testResetCustomerPassword() throws Exception {
        System.out.println("resetCustomerPassword");
        String username = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        String expResult = "";
        String result = instance.resetCustomerPassword(username);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addProductQuotation method, of class CustomerSessionBean.
     */
    @Test
    public void testAddProductQuotation() throws Exception {
        System.out.println("addProductQuotation");
        String username = "";
        ProductQuotation productQuotation = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        instance.addProductQuotation(username, productQuotation);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addProductPurchaseOrder method, of class CustomerSessionBean.
     */
    @Test
    public void testAddProductPurchaseOrder() throws Exception {
        System.out.println("addProductPurchaseOrder");
        String username = "";
        ProductPurchaseOrder productPurchaseOrder = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerSessionBeanLocal instance = (CustomerSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerSessionBean");
        instance.addProductPurchaseOrder(username, productPurchaseOrder);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
