package session.stateless;

import entity.SupplierEntity;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import util.enumeration.SupplierStatusEnum;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplierSessionBeanRemoteInterfaceTest
{

    SupplierSessionBeanRemoteInterface supplierSessionBeanRemoteInterface = lookupSupplierSessionBeanRemote();

    @PersistenceContext
    private EntityManager em;
    private SupplierEntity supplier;
    
    public SupplierSessionBeanRemoteInterfaceTest() {
        supplier = new SupplierEntity();
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
    
    @Test
    // NORMAL CASE test01CreateSupplier case
    public void test01CreateSupplier(){
        System.out.println("NORMAL CASE: test01CreateSupplier");

        supplier.setAddress1("Beach Road");
        supplier.setAddress2("Blk 235, #09-322");
        supplier.setCompanyName("SupplierA");
        supplier.setEmail("njxrandy91@gmail.com");
        supplier.setPhone("91234568");
        supplier.setPostalCode("653232");
        supplier.setPw(supplierSessionBeanRemoteInterface.encryptPassword("11111111"));
        supplier.setSubscribeEmail_qPriceUpdates(true);
        supplier.setSubscribeSMS_qPriceUpdates(true);
        supplier.setSubscribeEmail_poDeliveryUpdates(true);
        supplier.setSubscribeSMS_poDeliveryUpdates(true);
        supplier.setUserName("aj");
        supplier.setSecretQuestion("What is the name of your favourite teacher?");
        supplier.setSecretAnswer("tanweekek");
        
        supplierSessionBeanRemoteInterface.createSupplier(supplier);
        supplier = new SupplierEntity();

        SupplierEntity supplierr = supplierSessionBeanRemoteInterface.getSupplierByUsername("aj");
        assertEquals(supplierr.getEmail(), "njxrandy91@gmail.com");
    }
    
    @Test
    // NORMAL CASE test02EncryptPassword case
    public void test02EncryptPassword() throws NoSuchAlgorithmException{
        System.out.println("NORMAL CASE: test02EncryptPassword");
        
        String encrypted = null;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("11111111".getBytes(), 0, 8);
        encrypted = new BigInteger(1, messageDigest.digest()).toString(16);
        
        assertEquals(encrypted, supplierSessionBeanRemoteInterface.encryptPassword("11111111"));
    }
    
    @Test
    // CONFLICT CASE test03EncryptPassword case
    public void test03EncryptPassword() throws NoSuchAlgorithmException{
        System.out.println("CONFLICT CASE: test03EncryptPassword");
        
        String encrypted = null;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("11111111".getBytes(), 0, 8);
        encrypted = new BigInteger(1, messageDigest.digest()).toString(16);
        
        assertNotEquals(encrypted, supplierSessionBeanRemoteInterface.encryptPassword(null));
    }
    
    @Test
    // NORMAL CASE test04GetSupplierByUsername case
    public void test04GetSupplierByUsername() {
        System.out.println("NORMAL CASE: test04GetSupplierByUsername");
        assertNotNull(supplierSessionBeanRemoteInterface.getSupplierByUsername("aj"));
    }
    
    @Test
    // CONFLICT CASE test05GetSupplierByUsername case
    public void test05GetSupplierByUsername() {
        System.out.println("CONFLICT CASE: test05GetSupplierByUsername");
        assertNull(supplierSessionBeanRemoteInterface.getSupplierByUsername("haha"));
    }
    
    @Test
    // NORMAL CASE test06GetAllSupplier case
    public void test06GetAllSupplier() {
        System.out.println("NORMAL CASE: test06GetAllSupplier");
        List<SupplierEntity> result = supplierSessionBeanRemoteInterface.getAllSupplier();
        assertEquals(result.size(), 3);
    }
    
    @Test
    // NORMAL CASE test07UpdateSupplier case
    public void test07UpdateSupplier() {
        System.out.println("NORMAL CASE: test07UpdateSupplier");
        SupplierEntity supplier = supplierSessionBeanRemoteInterface.getSupplierByUsername("aj");
        supplier.setAddress1("Sunny");
        supplierSessionBeanRemoteInterface.updateSupplier(supplier);
    }
    
    @Test
    // NORMAL CASE test08ResetSupplierPassword case
    public void test08ResetSupplierPassword() {
        System.out.println("NORMAL CASE: test08ResetSupplierPassword");
        String result = supplierSessionBeanRemoteInterface.resetSupplierPassword("aj");
        assertNotNull(result);
    }
    
    private SupplierSessionBeanRemoteInterface lookupSupplierSessionBeanRemote() 
    {
        try 
        {
            Context c = new InitialContext();
            //  java:global[/<app-name>]/<module-name>/<bean-name>!<fully-qualified-interface-name>
            return (SupplierSessionBeanRemoteInterface) c.lookup("java:global/HiYewSystem/HiYewSystem-ejb/SupplierSessionBean!session.stateless.SupplierSessionBeanRemoteInterface");
        }   
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
