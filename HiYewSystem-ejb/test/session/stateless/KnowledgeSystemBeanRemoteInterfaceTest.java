/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerComposition;
import entity.FillerEntity;
import entity.Metal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
public class KnowledgeSystemBeanRemoteInterfaceTest {

    KnowledgeSystemBeanRemoteInterface KnowledgeSystemBeanInterface = lookupSystemUserSessionRemote();

    public KnowledgeSystemBeanRemoteInterfaceTest() {
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
     * Test of addMetal method, of class KnowledgeSystemBeanRemoteInterface. //
     */
//    @Test
//    public void testAddMetal() {
//        System.out.println("addMetal Test 1");
//        KnowledgeSystemBeanInterface.addMetal(null);
//    }
//
//    /**
//     * Test of metalRecords method, of class KnowledgeSystemBeanRemoteInterface.
//     */
    @Test
    public void testMetalRecords() {
        System.out.println("metalRecords  Test case - Size of Metal Records");
        List<Metal> metals = KnowledgeSystemBeanInterface.metalRecords();
        assertEquals(metals.size(), 15);
        System.out.println("testMetalRecords ---------------------------------------------------- Done");
    }
    //how to test for empty cases

//    /**
//     * Test of editMetal method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testEditMetal() {
//        System.out.println("editMetal");
//        Metal metal = null;
//        KnowledgeSystemBeanInterface.editMetal(metal);    
//    }
//
//    /**
//     * Test of deleteMetal method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testDeleteMetal() {
//        System.out.println("deleteMetal");
//        Metal metal = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.deleteMetal(metal);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveFillerNames method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */
    @Test
    public void testRetrieveFillerNames() {
        System.out.println("retrieveFillerNames - Size of Filler Records ");
        List<String> result = KnowledgeSystemBeanInterface.retrieveFillerNames();
        assertEquals(result.size(), 5);
        System.out.println("testRetrieveFillerNames ---------------------------------------------------- Done");
    }
//
//    /**
//     * Test of metalNames method, of class KnowledgeSystemBeanRemoteInterface.
//     */

    @Test
    public void testMetalNames() {
        System.out.println("metalNames - Metal has records");
        List<String> result = KnowledgeSystemBeanInterface.metalNames();
        assertEquals(result.size(), 15);
        System.out.println("testMetalNames ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.
    }
//
//    /**
//     * Test of createPairings method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testCreatePairings() {
//        System.out.println("createPairings");
//        String metal = "";
//        List<String> fillerChosen = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.createPairings(metal, fillerChosen);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of FillersNotAssociated method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */

    @Test
    public void testFillersNotAssociated() {
        System.out.println("FillersNotAssociated - metalName = ''");
        String metalName = "";
        List<String> result = KnowledgeSystemBeanInterface.FillersNotAssociated(metalName);
        ArrayList<String> results = new ArrayList<String>();
        assertEquals(result, results);
        System.out.println("testFillersNotAssociated ---------------------------------------------------- Done");
    }

    @Test
    public void testFillersNotAssociated2() {
        System.out.println("FillersNotAssociated - metalName = null");
        String metalName = null;
        List<String> result = KnowledgeSystemBeanInterface.FillersNotAssociated(metalName);
        ArrayList<String> results = new ArrayList<String>();
        assertEquals(result, results);
        System.out.println("testFillersNotAssociated2 ---------------------------------------------------- Done");
    }

    @Test
    public void testFillersNotAssociated3() {
        System.out.println("FillersNotAssociated - Fillers have records (not associated)");
        String metalName = "Metal A";
        List<String> result = KnowledgeSystemBeanInterface.FillersNotAssociated(metalName);

        assertEquals(result.size(), 3);
        System.out.println("testFillersNotAssociated3 ---------------------------------------------------- Done");
    }

    @Test
    public void testFillersNotAssociated4() {
        System.out.println("FillersNotAssociated - Metal not exist (not associated)");
        String metalName = "Metal AB";
        List<String> result = KnowledgeSystemBeanInterface.FillersNotAssociated(metalName);
        ArrayList<String> results = new ArrayList<String>();
        assertEquals(result, results);
        System.out.println("testFillersNotAssociated4 ---------------------------------------------------- Done");
    }

//
//    /**
//     * Test of FillersAssociated method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */
    @Test
    public void testFillersAssociated() {
        System.out.println("FillersAssociated - metalName = ''");
        String metalName = "";
        List<String> result = KnowledgeSystemBeanInterface.FillersAssociated(metalName);
        ArrayList<String> results = new ArrayList<String>();
        assertEquals(result, results);
        System.out.println("testFillersAssociated ---------------------------------------------------- Done");
    }

    @Test
    public void testFillersAssociated2() {
        System.out.println("FillersAssociated - metalName = null");
        String metalName = null;
        List<String> result = KnowledgeSystemBeanInterface.FillersAssociated(metalName);
        ArrayList<String> results = new ArrayList<String>();
        assertEquals(result, results);
        System.out.println("testFillersAssociated2 ---------------------------------------------------- Done");
    }

    @Test
    public void testFillersAssociated3() {
        System.out.println("FillersAssociated - metalName have records (Associated)");
        String metalName = "Metal A";
        List<String> result = KnowledgeSystemBeanInterface.FillersAssociated(metalName);

        assertEquals(result.size(), 2);
        System.out.println("testFillersAssociated3 ---------------------------------------------------- Done");
    }

    @Test
    public void testFillersAssociated4() {
        System.out.println("FillersAssociated - metal does not exist (Associated)");
        String metalName = "Metal AB";
        List<String> result = KnowledgeSystemBeanInterface.FillersAssociated(metalName);
        ArrayList<String> results = new ArrayList<String>();
        assertEquals(result, results);
        System.out.println("testFillersAssociated4 ---------------------------------------------------- Done");
    }
//
//    /**
//     * Test of addFillers method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testAddFillers() {
//        System.out.println("addFillers");
//        List<Vector> fillers = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.addFillers(fillers);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of transferFillerInfo method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */

    @Test
    public void testTransferFillerInfo() {
        System.out.println("transferFillerInfo - Filler has information");
        List<Vector> result = KnowledgeSystemBeanInterface.transferFillerInfo();
        assertEquals(result.size(), 5);
        System.out.println("testTransferFillerInfo ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of transferMetalInfo method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */

    @Test
    public void testTransferMetalInfo() {
        System.out.println("transferMetalInfo - Table have records");
        List<Vector> result = KnowledgeSystemBeanInterface.transferMetalInfo();
        assertEquals(result.size(), 15);
        System.out.println("testTransferMetalInfo ---------------------------------------------------- Done");
    }

    @Test
    public void testFillerRecords() {
        System.out.println("fillerRecords - FillerComposition have records");
        List<FillerComposition> result = KnowledgeSystemBeanInterface.fillerRecords();
        assertEquals(result.size(), 5);
        System.out.println("testFillerRecords ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.
    }


//
//    /**
//     * Test of deleteFiller method, of class KnowledgeSystemBeanRemoteInterface.
//     */

//
//    /**
//     * Test of editFiller method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testEditFiller() {
//        System.out.println("editFiller");
//        FillerComposition filler = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.editFiller(filler);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addMatch method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testAddMatch() {
//        System.out.println("addMatch");
//        List<Vector> results = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.addMatch(results);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of transferMatchingInfo method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testTransferMatchingInfo() {
//        System.out.println("transferMatchingInfo");
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        List<Vector> expResult = null;
//        List<Vector> result = instance.transferMatchingInfo();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addNewFiller method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testAddNewFiller_FillerComposition() {
//        System.out.println("addNewFiller");
//        FillerComposition filler = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.addNewFiller(filler);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addNewMetal method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testAddNewMetal() {
//        System.out.println("addNewMetal");
//        Metal metal = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.addNewMetal(metal);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addNewFiller method, of class KnowledgeSystemBeanRemoteInterface.
//     */
//    @Test
//    public void testAddNewFiller_FillerComposition_FillerEntity() {
//        System.out.println("addNewFiller");
//        FillerComposition fillerC = null;
//        FillerEntity filler = null;
//        KnowledgeSystemBeanRemoteInterface instance = new KnowledgeSystemBeanRemoteInterfaceImpl();
//        instance.addNewFiller(fillerC, filler);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveFiller method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */
    @Test
    public void testRetrieveFiller() {
        System.out.println("retrieveFiller Test 1 - null case");
        FillerComposition filler = new FillerComposition();
        FillerComposition filler1 = KnowledgeSystemBeanInterface.retrieveFiller(null);
        assertEquals(filler1, filler);
        System.out.println("testRetrieveFiller ---------------------------------------------------- Done");
    }

    @Test
    public void testRetrieveFiller2() {
        System.out.println("retrieveFiller Test 2 - Filler A");
        FillerComposition filler = new FillerComposition();
        FillerEntity filler2 = new FillerEntity();
        filler2.setFillerCode("Filler A");
        filler.setName("Filler A");
        FillerComposition filler1 = KnowledgeSystemBeanInterface.retrieveFiller(filler2);
        assertEquals(filler.getName(), filler1.getName());
        System.out.println("testRetrieveFiller2 ---------------------------------------------------- Done");
    }

    @Test
    public void testRetrieveFiller3() {
        System.out.println("retrieveFiller Test 3 - Filler Z (no such filler)");
        FillerComposition filler = new FillerComposition();
        FillerEntity filler2 = new FillerEntity();
        filler2.setFillerCode("Filler Z");
        FillerComposition filler1 = KnowledgeSystemBeanInterface.retrieveFiller(filler2);
        assertEquals(filler1, filler);
        System.out.println("testRetrieveFiller3 ---------------------------------------------------- Done");
    }
//
//    /**
//     * Test of checkFillerID method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */

    @Test
    public void testCheckFillerID() {
        System.out.println("checkFillerID - Have existing record");
        String id = "Filler A";
        boolean result = KnowledgeSystemBeanInterface.checkFillerID(id);
        assertTrue(result);
        System.out.println("testCheckFillerID ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCheckFillerID2() {
        System.out.println("checkFillerID - id is ''");
        String id = "";
        boolean result = KnowledgeSystemBeanInterface.checkFillerID(id);
        assertFalse(result);
        System.out.println("testCheckFillerID2 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCheckFillerID3() {
        System.out.println("checkFillerID - id is null");
        String id = null;
        boolean result = KnowledgeSystemBeanInterface.checkFillerID(id);
        assertFalse(result);
        System.out.println("testCheckFillerID3 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCheckFillerID4() {
        System.out.println("checkFillerID - Have no record");
        String id = "Filler AB";
        boolean result = KnowledgeSystemBeanInterface.checkFillerID(id);
        assertFalse(result);
        System.out.println("testCheckFillerID4 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }
//
//    /**
//     * Test of checkFillerName method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */

    @Test
    public void testCheckFillerName() {
        System.out.println("checkFillerName - have record");
        String id = "Filler A";
        boolean result = KnowledgeSystemBeanInterface.checkFillerName(id);
        assertTrue(result);
        System.out.println("testCheckFillerName ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testCheckFillerName2() {
        System.out.println("checkFillerName - ''");
        String id = "";
        boolean result = KnowledgeSystemBeanInterface.checkFillerName(id);
        assertFalse(result);
        System.out.println("testCheckFillerName2 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testCheckFillerName3() {
        System.out.println("checkFillerName - null");
        String id = null;
        boolean result = KnowledgeSystemBeanInterface.checkFillerName(id);
        assertFalse(result);
        System.out.println("testCheckFillerName3 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testCheckFillerName4() {
        System.out.println("checkFillerName - no record");
        String id = "Filler AB";
        boolean result = KnowledgeSystemBeanInterface.checkFillerName(id);
        assertFalse(result);
        System.out.println("testCheckFillerName4 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.
    }
//
//    /**
//     * Test of checkMetalName method, of class
//     * KnowledgeSystemBeanRemoteInterface.
//     */

    @Test
    public void testCheckMetalName() {
        System.out.println("checkMetalName - MetalName do not Exist");
        String id = "Metal AB";
        boolean result = KnowledgeSystemBeanInterface.checkMetalName(id);
        assertFalse(result);
        System.out.println("testCheckMetalName ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCheckMetalName2() {
        System.out.println("checkMetalName - MetalName Exist");
        String id = "Metal A";
        boolean result = KnowledgeSystemBeanInterface.checkMetalName(id);
        assertTrue(result);
        System.out.println("testCheckMetalName2 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testCheckMetalName3() {
        System.out.println("checkMetalName - MetalName is ''");
        String id = "";
        boolean result = KnowledgeSystemBeanInterface.checkMetalName(id);
        assertFalse(result);
        System.out.println("testCheckMetalName3 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void testCheckMetalName4() {
        System.out.println("checkMetalName - MetalName is null");
        String id = null;
        boolean result = KnowledgeSystemBeanInterface.checkMetalName(id);
        assertFalse(result);
        System.out.println("testCheckMetalName4 ---------------------------------------------------- Done");
        // TODO review the generated test code and remove the default call to fail.

    }

    public class KnowledgeSystemBeanRemoteInterfaceImpl implements KnowledgeSystemBeanRemoteInterface {

        public void addMetal(List<Vector> metals) {
        }

        public List<Metal> metalRecords() {
            return null;
        }

        public void editMetal(Metal metal) {
        }

        public void deleteMetal(Metal metal) {
        }

        public List<String> retrieveFillerNames() {
            return null;
        }

        public List<String> metalNames() {
            return null;
        }

        public boolean createPairings(String metal, List<String> fillerChosen) {
            return true;
        }

        public List<String> FillersNotAssociated(String metalName) {
            return null;
        }

        public List<String> FillersAssociated(String metalName) {
            return null;
        }

        public void addFillers(List<Vector> fillers) {
        }

        public List<Vector> transferFillerInfo() {
            return null;
        }

        public List<Vector> transferMetalInfo() {
            return null;
        }

        public List<FillerComposition> fillerRecords() {
            return null;
        }

        public void deleteFiller(FillerComposition filler) {
        }

        public void editFiller(FillerComposition filler) {
        }

        public void addMatch(List<Vector> results) {
        }

        public List<Vector> transferMatchingInfo() {
            return null;
        }

        public void addNewFiller(FillerComposition filler) {
        }

        public void addNewMetal(Metal metal) {
        }

        public void addNewFiller(FillerComposition fillerC, FillerEntity filler) {
        }

        public FillerComposition retrieveFiller(FillerEntity filler) {
            return null;
        }

        public boolean checkFillerID(String id) {
            return false;
        }

        public boolean checkFillerName(String id) {
            return false;
        }

        public boolean checkMetalName(String id) {
            return false;
        }
    }

    private KnowledgeSystemBeanRemoteInterface lookupSystemUserSessionRemote() {
        try {
            Context c = new InitialContext();
            return (KnowledgeSystemBeanRemoteInterface) c.lookup("java:global/HiYewSystem/HiYewSystem-ejb/KnowledgeSystemBean!session.stateless.KnowledgeSystemBeanRemoteInterface");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
