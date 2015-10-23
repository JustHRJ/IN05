/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CustomerPO;
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
public class CustomerPOSessionBeanTest {
    
    public CustomerPOSessionBeanTest() {
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
     * Test of createPO method, of class CustomerPOSessionBean.
     */
    @Test
    public void testCreatePO() throws Exception {
        System.out.println("createPO");
        CustomerPO customerPO = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerPOSessionBeanLocal instance = (CustomerPOSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerPOSessionBean");
        instance.createPO(customerPO);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of conductMerge method, of class CustomerPOSessionBean.
     */
    @Test
    public void testConductMerge() throws Exception {
        System.out.println("conductMerge");
        CustomerPO customerPO = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerPOSessionBeanLocal instance = (CustomerPOSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerPOSessionBean");
        instance.conductMerge(customerPO);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of receivedCustomerPO method, of class CustomerPOSessionBean.
     */
    @Test
    public void testReceivedCustomerPO() throws Exception {
        System.out.println("receivedCustomerPO");
        String username = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        CustomerPOSessionBeanLocal instance = (CustomerPOSessionBeanLocal)container.getContext().lookup("java:global/classes/CustomerPOSessionBean");
        List<CustomerPO> expResult = null;
        List<CustomerPO> result = instance.receivedCustomerPO(username);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
