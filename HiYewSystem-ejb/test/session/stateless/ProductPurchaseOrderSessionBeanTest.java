/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ProductPurchaseOrder;
import entity.ProductQuotationDescription;
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
public class ProductPurchaseOrderSessionBeanTest {
    
    public ProductPurchaseOrderSessionBeanTest() {
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
     * Test of createProductPurchaseOrder method, of class ProductPurchaseOrderSessionBean.
     */
    @Test
    public void testCreateProductPurchaseOrder() throws Exception {
        System.out.println("createProductPurchaseOrder");
        ProductPurchaseOrder productPurchaseOrder = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductPurchaseOrderSessionBeanLocal instance = (ProductPurchaseOrderSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductPurchaseOrderSessionBean");
        instance.createProductPurchaseOrder(productPurchaseOrder);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of receivedProductPurchaseOrder method, of class ProductPurchaseOrderSessionBean.
     */
    @Test
    public void testReceivedProductPurchaseOrder() throws Exception {
        System.out.println("receivedProductPurchaseOrder");
        String username = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductPurchaseOrderSessionBeanLocal instance = (ProductPurchaseOrderSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductPurchaseOrderSessionBean");
        List<ProductPurchaseOrder> expResult = null;
        List<ProductPurchaseOrder> result = instance.receivedProductPurchaseOrder(username);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of receivedCustomerNewProductPOList method, of class ProductPurchaseOrderSessionBean.
     */
    @Test
    public void testReceivedCustomerNewProductPOList() throws Exception {
        System.out.println("receivedCustomerNewProductPOList");
        String status = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductPurchaseOrderSessionBeanLocal instance = (ProductPurchaseOrderSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductPurchaseOrderSessionBean");
        List<ProductPurchaseOrder> expResult = null;
        List<ProductPurchaseOrder> result = instance.receivedCustomerNewProductPOList(status);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieveProductQuotationDescriptionList method, of class ProductPurchaseOrderSessionBean.
     */
    @Test
    public void testRetrieveProductQuotationDescriptionList() throws Exception {
        System.out.println("retrieveProductQuotationDescriptionList");
        String purchaseOrderNo = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductPurchaseOrderSessionBeanLocal instance = (ProductPurchaseOrderSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductPurchaseOrderSessionBean");
        List<ProductQuotationDescription> expResult = null;
        List<ProductQuotationDescription> result = instance.retrieveProductQuotationDescriptionList(purchaseOrderNo);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updatePODeliveryDate method, of class ProductPurchaseOrderSessionBean.
     */
    @Test
    public void testUpdatePODeliveryDate() throws Exception {
        System.out.println("updatePODeliveryDate");
        ProductPurchaseOrder inProductPurchaseOrder = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductPurchaseOrderSessionBeanLocal instance = (ProductPurchaseOrderSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductPurchaseOrderSessionBean");
        instance.updatePODeliveryDate(inProductPurchaseOrder);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProductPORelayedStatus method, of class ProductPurchaseOrderSessionBean.
     */
    @Test
    public void testUpdateProductPORelayedStatus() throws Exception {
        System.out.println("updateProductPORelayedStatus");
        ProductPurchaseOrder inProductPurchaseOrder = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductPurchaseOrderSessionBeanLocal instance = (ProductPurchaseOrderSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductPurchaseOrderSessionBean");
        instance.updateProductPORelayedStatus(inProductPurchaseOrder);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProductPOStatus method, of class ProductPurchaseOrderSessionBean.
     */
    @Test
    public void testUpdateProductPOStatus() throws Exception {
        System.out.println("updateProductPOStatus");
        ProductPurchaseOrder inProductPurchaseOrder = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        ProductPurchaseOrderSessionBeanLocal instance = (ProductPurchaseOrderSessionBeanLocal)container.getContext().lookup("java:global/classes/ProductPurchaseOrderSessionBean");
        instance.updateProductPOStatus(inProductPurchaseOrder);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
