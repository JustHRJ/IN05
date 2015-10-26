/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.util.ArrayList;
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
 * @author QiWen
 */
public class ProductQuotationSessionBeanTest {
    
    public ProductQuotationSessionBeanTest() {
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
     * Test of receivedProductQuotationList method, of class ProductQuotationSessionBean.
     */
    @Test
    public void testReceivedProductQuotationList() throws Exception {
        System.out.println("receivedProductQuotationList");
        String username = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductQuotationSessionBeanLocal instance = (ProductQuotationSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductQuotationSessionBean");
        List<ProductQuotation> expResult = null;
        List<ProductQuotation> result = instance.receivedProductQuotationList(username);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of receivedCustomerNewProductQuotationList method, of class ProductQuotationSessionBean.
     */
    @Test
    public void testReceivedCustomerNewProductQuotationList() throws Exception {
        System.out.println("receivedCustomerNewProductQuotationList");
        String status = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductQuotationSessionBeanLocal instance = (ProductQuotationSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductQuotationSessionBean");
        List<ProductQuotation> expResult = null;
        List<ProductQuotation> result = instance.receivedCustomerNewProductQuotationList(status);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieveProductQuotationDescriptionList method, of class ProductQuotationSessionBean.
     */
    @Test
    public void testRetrieveProductQuotationDescriptionList() throws Exception {
        System.out.println("retrieveProductQuotationDescriptionList");
        String quotationNo = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductQuotationSessionBeanLocal instance = (ProductQuotationSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductQuotationSessionBean");
        List<ProductQuotationDescription> expResult = null;
        List<ProductQuotationDescription> result = instance.retrieveProductQuotationDescriptionList(quotationNo);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProductQuotationPrices method, of class ProductQuotationSessionBean.
     */
    @Test
    public void testUpdateProductQuotationPrices() throws Exception {
        System.out.println("updateProductQuotationPrices");
        ArrayList<ProductQuotationDescription> list = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductQuotationSessionBeanLocal instance = (ProductQuotationSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductQuotationSessionBean");
        instance.updateProductQuotationPrices(list);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProductQuotationRelayedStatus method, of class ProductQuotationSessionBean.
     */
    @Test
    public void testUpdateProductQuotationRelayedStatus() throws Exception {
        System.out.println("updateProductQuotationRelayedStatus");
        ProductQuotation inProductQuotation = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductQuotationSessionBeanLocal instance = (ProductQuotationSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductQuotationSessionBean");
        instance.updateProductQuotationRelayedStatus(inProductQuotation);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProductQuotationStatus method, of class ProductQuotationSessionBean.
     */
    @Test
    public void testUpdateProductQuotationStatus() throws Exception {
        System.out.println("updateProductQuotationStatus");
        ProductQuotation inProductQuotation = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductQuotationSessionBeanLocal instance = (ProductQuotationSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductQuotationSessionBean");
        instance.updateProductQuotationStatus(inProductQuotation);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
