/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerComposition;
import entity.Metal;
import java.util.List;
import java.util.Vector;
import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JustHRJ
 */
public class KnowledgeSystemBeanTest {
    
    public KnowledgeSystemBeanTest() {
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
     * Test of addMetal method, of class KnowledgeSystemBean.
     */
    @Test
    public void testAddMetal() throws Exception {
        System.out.println("addMetal");
        List<Vector> metals = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        instance.addMetal(metals);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of metalRecords method, of class KnowledgeSystemBean.
     */
    @Test
    public void testMetalRecords() throws Exception {
        System.out.println("metalRecords");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<Metal> expResult = null;
        List<Metal> result = instance.metalRecords();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editMetal method, of class KnowledgeSystemBean.
     */
    @Test
    public void testEditMetal() throws Exception {
        System.out.println("editMetal");
        Metal metal = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        instance.editMetal(metal);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteMetal method, of class KnowledgeSystemBean.
     */
    @Test
    public void testDeleteMetal() throws Exception {
        System.out.println("deleteMetal");
        Metal metal = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        instance.deleteMetal(metal);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieveFillerNames method, of class KnowledgeSystemBean.
     */
    @Test
    public void testRetrieveFillerNames() throws Exception {
        System.out.println("retrieveFillerNames");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<String> expResult = null;
        List<String> result = instance.retrieveFillerNames();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of metalNames method, of class KnowledgeSystemBean.
     */
    @Test
    public void testMetalNames() throws Exception {
        System.out.println("metalNames");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<String> expResult = null;
        List<String> result = instance.metalNames();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createPairings method, of class KnowledgeSystemBean.
     */
    @Test
    public void testCreatePairings() throws Exception {
        System.out.println("createPairings");
        String metal = "";
        List<String> fillerChosen = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        instance.createPairings(metal, fillerChosen);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of FillersNotAssociated method, of class KnowledgeSystemBean.
     */
    @Test
    public void testFillersNotAssociated() throws Exception {
        System.out.println("FillersNotAssociated");
        String metalName = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<String> expResult = null;
        List<String> result = instance.FillersNotAssociated(metalName);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of FillersAssociated method, of class KnowledgeSystemBean.
     */
    @Test
    public void testFillersAssociated() throws Exception {
        System.out.println("FillersAssociated");
        String metalName = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<String> expResult = null;
        List<String> result = instance.FillersAssociated(metalName);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addFillers method, of class KnowledgeSystemBean.
     */
    @Test
    public void testAddFillers() throws Exception {
        System.out.println("addFillers");
        List<Vector> fillers = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        instance.addFillers(fillers);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of transferFillerInfo method, of class KnowledgeSystemBean.
     */
    @Test
    public void testTransferFillerInfo() throws Exception {
        System.out.println("transferFillerInfo");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<Vector> expResult = null;
        List<Vector> result = instance.transferFillerInfo();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of transferMetalInfo method, of class KnowledgeSystemBean.
     */
    @Test
    public void testTransferMetalInfo() throws Exception {
        System.out.println("transferMetalInfo");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<Vector> expResult = null;
        List<Vector> result = instance.transferMetalInfo();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fillerRecords method, of class KnowledgeSystemBean.
     */
    @Test
    public void testFillerRecords() throws Exception {
        System.out.println("fillerRecords");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        List<FillerComposition> expResult = null;
        List<FillerComposition> result = instance.fillerRecords();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFiller method, of class KnowledgeSystemBean.
     */
    @Test
    public void testDeleteFiller() throws Exception {
        System.out.println("deleteFiller");
        FillerComposition filler = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        instance.deleteFiller(filler);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editFiller method, of class KnowledgeSystemBean.
     */
    @Test
    public void testEditFiller() throws Exception {
        System.out.println("editFiller");
        FillerComposition filler = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        KnowledgeSystemBeanLocal instance = (KnowledgeSystemBeanLocal)container.getContext().lookup("java:global/classes/KnowledgeSystemBean");
        instance.editFiller(filler);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
