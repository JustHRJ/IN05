/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ItemEntity;
import entity.RackEntity;
import entity.ShelveEntity;
import entity.StorageInfoEntity;
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
 * @author K.guoxiang
 */
public class HiYewICSSessionBeanTest {
    
    public HiYewICSSessionBeanTest() {
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
     * Test of createItem method, of class HiYewICSSessionBean.
     */
    @Test
    public void testCreateItem() throws Exception {
        System.out.println("createItem");
        ItemEntity item = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.createItem(item);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllItems method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetAllItems() throws Exception {
        System.out.println("getAllItems");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        List<ItemEntity> expResult = null;
        List<ItemEntity> result = instance.getAllItems();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllLowStockItems method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetAllLowStockItems() throws Exception {
        System.out.println("getAllLowStockItems");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        List<ItemEntity> expResult = null;
        List<ItemEntity> result = instance.getAllLowStockItems();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExistingItem method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetExistingItem() throws Exception {
        System.out.println("getExistingItem");
        String itemCode = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        ItemEntity expResult = null;
        ItemEntity result = instance.getExistingItem(itemCode);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getItemCodeAutoComplete method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetItemCodeAutoComplete() throws Exception {
        System.out.println("getItemCodeAutoComplete");
        String input = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        List<String> expResult = null;
        List<String> result = instance.getItemCodeAutoComplete(input);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExistingItemByName method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetExistingItemByName() throws Exception {
        System.out.println("getExistingItemByName");
        String itemName = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        ItemEntity expResult = null;
        ItemEntity result = instance.getExistingItemByName(itemName);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateItemDetails method, of class HiYewICSSessionBean.
     */
    @Test
    public void testUpdateItemDetails() throws Exception {
        System.out.println("updateItemDetails");
        ItemEntity item = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.updateItemDetails(item);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stockUp method, of class HiYewICSSessionBean.
     */
    @Test
    public void testStockUp() throws Exception {
        System.out.println("stockUp");
        ItemEntity item = null;
        int inQty = 0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.stockUp(item, inQty);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stockDown method, of class HiYewICSSessionBean.
     */
    @Test
    public void testStockDown() throws Exception {
        System.out.println("stockDown");
        ItemEntity item = null;
        int outQty = 0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.stockDown(item, outQty);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteItem method, of class HiYewICSSessionBean.
     */
    @Test
    public void testDeleteItem() throws Exception {
        System.out.println("deleteItem");
        ItemEntity item = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.deleteItem(item);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCost method, of class HiYewICSSessionBean.
     */
    @Test
    public void testUpdateCost() throws Exception {
        System.out.println("updateCost");
        ItemEntity item = null;
        double newCost = 0.0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.updateCost(item, newCost);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testCreateRack() throws Exception {
        System.out.println("createRack");
        RackEntity rack = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.createRack(rack);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createShelve method, of class HiYewICSSessionBean.
     */
    @Test
    public void testCreateShelve() throws Exception {
        System.out.println("createShelve");
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.createShelve(shelve);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createBin method, of class HiYewICSSessionBean.
     */
    @Test
    public void testCreateBin() throws Exception {
        System.out.println("createBin");
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.createBin(shelve);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addShelveToRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testAddShelveToRack() throws Exception {
        System.out.println("addShelveToRack");
        RackEntity rack = null;
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.addShelveToRack(rack, shelve);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExistingRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetExistingRack() throws Exception {
        System.out.println("getExistingRack");
        String rackID = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        RackEntity expResult = null;
        RackEntity result = instance.getExistingRack(rackID);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExistingShelve method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetExistingShelve() throws Exception {
        System.out.println("getExistingShelve");
        String shelveID = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        ShelveEntity expResult = null;
        ShelveEntity result = instance.getExistingShelve(shelveID);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getShelvesInRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetShelvesInRack() throws Exception {
        System.out.println("getShelvesInRack");
        String rackID = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        ArrayList<ShelveEntity> expResult = null;
        ArrayList<ShelveEntity> result = instance.getShelvesInRack(rackID);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNextIDForRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetNextIDForRack() throws Exception {
        System.out.println("getNextIDForRack");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        String expResult = "";
        String result = instance.getNextIDForRack();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNextIDForShelve method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetNextIDForShelve() throws Exception {
        System.out.println("getNextIDForShelve");
        String rackID = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        String expResult = "";
        String result = instance.getNextIDForShelve(rackID);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllRacks method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetAllRacks() throws Exception {
        System.out.println("getAllRacks");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        List<RackEntity> expResult = null;
        List<RackEntity> result = instance.getAllRacks();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addStorageInfo method, of class HiYewICSSessionBean.
     */
    @Test
    public void testAddStorageInfo() throws Exception {
        System.out.println("addStorageInfo");
        ItemEntity item = null;
        ShelveEntity shelve = null;
        int storedQty = 0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.addStorageInfo(item, shelve, storedQty);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addShelveFillCapac method, of class HiYewICSSessionBean.
     */
    @Test
    public void testAddShelveFillCapac() throws Exception {
        System.out.println("addShelveFillCapac");
        ShelveEntity shelve = null;
        ItemEntity item = null;
        int storedQty = 0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.addShelveFillCapac(shelve, item, storedQty);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getShelveFreeSpace method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetShelveFreeSpace() throws Exception {
        System.out.println("getShelveFreeSpace");
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        double expResult = 0.0;
        double result = instance.getShelveFreeSpace(shelve);
        assertEquals(expResult, result, 0.0);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkIfItemInShelve method, of class HiYewICSSessionBean.
     */
    @Test
    public void testCheckIfItemInShelve() throws Exception {
        System.out.println("checkIfItemInShelve");
        ItemEntity item = null;
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        boolean expResult = false;
        boolean result = instance.checkIfItemInShelve(item, shelve);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUnallocatedQty method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetUnallocatedQty() throws Exception {
        System.out.println("getUnallocatedQty");
        ItemEntity item = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        Long expResult = null;
        Long result = instance.getUnallocatedQty(item);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllStorageInfoOfShelve method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetAllStorageInfoOfShelve() throws Exception {
        System.out.println("getAllStorageInfoOfShelve");
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        List<StorageInfoEntity> expResult = null;
        List<StorageInfoEntity> result = instance.getAllStorageInfoOfShelve(shelve);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllStorageInfoOfItem method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetAllStorageInfoOfItem() throws Exception {
        System.out.println("getAllStorageInfoOfItem");
        ItemEntity item = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        List<StorageInfoEntity> expResult = null;
        List<StorageInfoEntity> result = instance.getAllStorageInfoOfItem(item);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStorageInfoOfRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetStorageInfoOfRack() throws Exception {
        System.out.println("getStorageInfoOfRack");
        RackEntity rack = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        List<StorageInfoEntity> expResult = null;
        List<StorageInfoEntity> result = instance.getStorageInfoOfRack(rack);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStorageInfo method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetStorageInfo() throws Exception {
        System.out.println("getStorageInfo");
        ItemEntity item = null;
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        StorageInfoEntity expResult = null;
        StorageInfoEntity result = instance.getStorageInfo(item, shelve);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reduceStorageQty method, of class HiYewICSSessionBean.
     */
    @Test
    public void testReduceStorageQty() throws Exception {
        System.out.println("reduceStorageQty");
        ItemEntity item = null;
        ShelveEntity shelve = null;
        int reduceAmt = 0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.reduceStorageQty(item, shelve, reduceAmt);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reduceShelveFillCapac method, of class HiYewICSSessionBean.
     */
    @Test
    public void testReduceShelveFillCapac() throws Exception {
        System.out.println("reduceShelveFillCapac");
        ShelveEntity shelve = null;
        ItemEntity item = null;
        int reduceQty = 0;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.reduceShelveFillCapac(shelve, item, reduceQty);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteStorageInfo method, of class HiYewICSSessionBean.
     */
    @Test
    public void testDeleteStorageInfo() throws Exception {
        System.out.println("deleteStorageInfo");
        ItemEntity item = null;
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.deleteStorageInfo(item, shelve);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRackStatus method, of class HiYewICSSessionBean.
     */
    @Test
    public void testUpdateRackStatus() throws Exception {
        System.out.println("updateRackStatus");
        RackEntity rack = null;
        String status = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.updateRackStatus(rack, status);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateShelveStatus method, of class HiYewICSSessionBean.
     */
    @Test
    public void testUpdateShelveStatus() throws Exception {
        System.out.println("updateShelveStatus");
        ShelveEntity shelve = null;
        String status = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.updateShelveStatus(shelve, status);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkAllShelvesInRackStatus method, of class HiYewICSSessionBean.
     */
    @Test
    public void testCheckAllShelvesInRackStatus() throws Exception {
        System.out.println("checkAllShelvesInRackStatus");
        RackEntity rack = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.checkAllShelvesInRackStatus(rack);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkIfGotItemsInRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testCheckIfGotItemsInRack() throws Exception {
        System.out.println("checkIfGotItemsInRack");
        RackEntity rack = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        boolean expResult = false;
        boolean result = instance.checkIfGotItemsInRack(rack);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteRack method, of class HiYewICSSessionBean.
     */
    @Test
    public void testDeleteRack() throws Exception {
        System.out.println("deleteRack");
        RackEntity rack = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.deleteRack(rack);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteShelve method, of class HiYewICSSessionBean.
     */
    @Test
    public void testDeleteShelve() throws Exception {
        System.out.println("deleteShelve");
        ShelveEntity shelve = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        instance.deleteShelve(shelve);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWireVolume method, of class HiYewICSSessionBean.
     */
    @Test
    public void testGetWireVolume() throws Exception {
        System.out.println("getWireVolume");
        ItemEntity item = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        HiYewICSSessionBeanLocal instance = (HiYewICSSessionBeanLocal)container.getContext().lookup("java:global/classes/HiYewICSSessionBean");
        double expResult = 0.0;
        double result = instance.getWireVolume(item);
        assertEquals(expResult, result, 0.0);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
