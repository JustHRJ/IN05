package session.stateless;

import entity.MachineEntity;
import entity.MachineMaintainenceEntity;
import entity.MachineRepairEntity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MachineSystemBeanRemoteInterfaceTest
{
    MachineSystemBeanRemoteInterface machineSystemBeanRemoteInterface = lookupMachineSystemBeanRemote();
    
    @PersistenceContext
    private EntityManager em;
    
    public MachineSystemBeanRemoteInterfaceTest() {
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
    // NORMAL CASE test01AddMachine6 case
    public void test01AddMachine6() {
        System.out.println("NORMAL CASE: test01AddMachine6 - M6"); 

        String machine_name = "M6";
        String machine_number = "6";
        String machine_type = "Laser";
        Timestamp machine_expiry = Timestamp.valueOf("2016-09-02 00:00:00");
        int extension = 2;
        String description = "machine6";
        
        boolean result = machineSystemBeanRemoteInterface.addMachine(machine_name, machine_number, machine_expiry, description, extension, machine_type);
        assertTrue(result);
      
    }
    
    @Test
    // CONFLICT CASE test02AddMachine6 case
    public void test02AddMachine6() {
        System.out.println("CONFLICT CASE: test02AddMachine6 - M6"); 

        String machine_name = "M6";
        String machine_number = "6";
        String machine_type = "Laser";
        Timestamp machine_expiry = Timestamp.valueOf("2016-09-02 00:00:00");
        int extension = 2;
        String description = "machine6";
        
        boolean result = machineSystemBeanRemoteInterface.addMachine(machine_name, machine_number, machine_expiry, description, extension, machine_type);
        assertFalse(result);
    } 
    
    @Test
    // NORMAL CASE test03DeleteMachine7 case
    public void test03DeleteMachine7() {
        System.out.println("NORMAL CASE: test03DeleteMachine7 - M7");
        boolean result = false;
        String machine_name = "M7";
        machineSystemBeanRemoteInterface.deleteMachine(machine_name);
        List<MachineEntity> machineRecords = new ArrayList<MachineEntity>();
        machineRecords = machineSystemBeanRemoteInterface.getAllMachine();
        
        for(int i = 0; i < machineRecords.size(); i++){
            if(machineRecords.get(i).getMachine_name().equals("M7") && (machineRecords.get(i).getStatus().equals("disabled"))){
                result = true;
            }  
        }
        assertTrue(result);
    } 
    
    @Test
    // CONFLICT CASE test04DeleteMachine7 case
    public void test04DeleteMachine7() {
        System.out.println("CONFLICT CASE: test04DeleteMachine7 - M7");
        boolean result = false;
        String machine_name = "M7";
        machineSystemBeanRemoteInterface.deleteMachine(machine_name);
        List<MachineEntity> machineRecords = new ArrayList<MachineEntity>();
        machineRecords = machineSystemBeanRemoteInterface.getAllMachine();

        for(int i = 0; i < machineRecords.size(); i++){
            if(machineRecords.get(i).getMachine_name().equals("M7") && (machineRecords.get(i).getStatus().equals("disabled"))){
                result = true;
            }  
        }
        assertTrue(result);
    }
    
    @Test
    // NORMAL CASE test05UpdateMachine8 case
    public void test05UpdateMachine8(){
        System.out.println("NORMAL CASE: test05UpdateMachine8 - M8");
        MachineEntity machine = machineSystemBeanRemoteInterface.getMachine("M8");
  
        Timestamp machine_expiry = Timestamp.valueOf("2016-09-02 00:00:00");
        machine.setMachine_expiry(machine_expiry);
        
        // Update machine name
        boolean result = machineSystemBeanRemoteInterface.updateMachine("Edited", machine, "Available", machine_expiry);
        assertTrue(result);
    }
    
    @Test
    // CONFLICT CASE test06UpdateMachine9 case
    public void test06UpdateMachine9(){
        System.out.println("CONFLICT CASE: test06UpdateMachine9 - M9");
        MachineEntity machine = machineSystemBeanRemoteInterface.getMachine("M9");

        Timestamp machine_expiry = Timestamp.valueOf("2016-09-02 00:00:00");
        machine.setMachine_expiry(machine_expiry);
        
        // Update machine name
        boolean result = machineSystemBeanRemoteInterface.updateMachine(null, machine, null, null);
        assertFalse(result);
    }
    
    @Test
    // NORMAL CASE test07ExistMachineName5 case
    public void test07ExistMachineName5(){
        System.out.println("NORMAL CASE: test07ExistMachineName5 - M5");
        String name = "M5";
        boolean result = machineSystemBeanRemoteInterface.existMachineName(name);
        assertTrue(result);
    }
    
    @Test
    // CONFLICT CASE test08ExistMachineName100 case
    public void test08ExistMachineName100(){
        System.out.println("CONFLICT CASE: test08ExistMachineName100 - M100");
        String name = "M100";
        boolean result = machineSystemBeanRemoteInterface.existMachineName(name);
        assertFalse(result);
    }
    
    @Test
    // NORMAL CASE test09GetNoAlert case
    public void test09GetNoAlert(){
        System.out.println("NORMAL CASE: test09GetNoAlert");
        int result = machineSystemBeanRemoteInterface.getNoAlert();
        int expResult = 1;
        assertEquals(expResult, result); 
    }
    
    @Test
    // NORMAL CASE test10NotExistMachine7 case
    public void test10NotExistMachine7(){
        System.out.println("NORMAL CASE: test10NotExistMachine7 - M7");
        String id = "M7";
        
        boolean result = machineSystemBeanRemoteInterface.notExistMachine(id);
        assertFalse(result);
    }
    
    @Test
    // CONFLICT CASE test11NotExistMachine100 case
    public void test11NotExistMachine100(){
        System.out.println("CONFLICT CASE: test11NotExistMachine100 - M100");
        String id = "M100";
        
        boolean result = machineSystemBeanRemoteInterface.notExistMachine(id);
        assertTrue(result);
    }
    
    @Test
    // NORMAL CASE test12MachineNames case
    public void test12MachineNames(){
        System.out.println("NORMAL CASE: test12MachineNames");
        List<String> machineNames = machineSystemBeanRemoteInterface.machineNames();
        int result = machineNames.size();
        
        int expResult = 6;
        assertEquals(expResult, result);
    }
    
    @Test
    // NORMAL CASE test13GetAllMachine case
    public void test13GetAllMachine(){
        System.out.println("NORMAL CASE: test13GetAllMachine");
        List<MachineEntity> machineRecords = machineSystemBeanRemoteInterface.getAllMachine();
        int result = machineRecords.size();
        
        int expResult = 6;
        assertEquals(expResult, result);
    }
    
    @Test
    // NORMAL CASE test14CreateRepair15 case
    public void test14CreateRepair15(){
        System.out.println("NORMAL CASE: test14CreateRepair - M15");
       
        MachineRepairEntity machine = new MachineRepairEntity();
        String machineName = "M15";
        
        String repairSolution = "repair";
        int laserPulse = 2500;
        double repairCost = 500;
        Timestamp repairDate = Timestamp.valueOf("2016-09-02 00:00:00");
        String remarks = "nil";
        
        machine.setLaserPulse(laserPulse);
        machine.setRepairCost(repairCost);
        machine.setRemarks(remarks);
        machine.setRepairSolution(repairSolution);

        machineSystemBeanRemoteInterface.createRepair(machine, repairDate, machineName);
        int result = machine.getLaserPulse();
        assertEquals(laserPulse, result);
    }
    
    @Test
    // NORMAL CASE test15RepairList case
    public void test15RepairList(){
        System.out.println("NORMAL CASE: test15RepairList");
        MachineEntity machine = machineSystemBeanRemoteInterface.getMachine("M15");
        List<MachineRepairEntity> repairList = machineSystemBeanRemoteInterface.repairList(machine);
        assertEquals(1 ,repairList.size());
    }
    
    @Test
    // NORMAL CASE test16AddMachineMaintainence15 case
    public void test16AddMachineMaintainence15(){
        System.out.println("NORMAL CASE: test16AddMachineMaintainence15 - M15");
        String machineName = "M15";
        Date mScheduleDate = new Date();
        String mScheduleHour = "1234";
        String maintainenceComments = "nil" ;
        String mServiceProvider = "service";
        String mServiceContact = "90808082";
        
        boolean result = machineSystemBeanRemoteInterface.addMachineMaintainence(machineName, mScheduleDate, mScheduleHour, maintainenceComments, mServiceProvider, mServiceContact);
        assertTrue(result);
    }
    
    @Test
    // CONFLICT CASE test17AddMachineMaintainence15 case
    public void test17AddMachineMaintainence15(){
        System.out.println("CONFLICT CASE: test17AddMachineMaintainence15 - M15");
        String machineName = "M15";
        Date mScheduleDate = new Date();
        String mScheduleHour = "1234";
        String maintainenceComments = "nil" ;
        String mServiceProvider = "service";
        String mServiceContact = "90808082";
        
        boolean result = machineSystemBeanRemoteInterface.addMachineMaintainence(machineName, mScheduleDate, mScheduleHour, maintainenceComments, mServiceProvider, mServiceContact);
        assertFalse(result);
    }
    
    @Test
    // NORMAL CASE test18MachineMaintainenceList case
    public void test18MachineMaintainenceList(){
        System.out.println("NORMAL CASE: test18MachineMaintainenceList");
        List<MachineMaintainenceEntity> list = machineSystemBeanRemoteInterface.machineMaintainenceList();
        assertEquals(list.size(), 1);
    }
    
    @Test
    // NORMAL CASE test19GetMachineMaintID15 case
    public void test19GetMachineMaintID15(){
        System.out.println("NORMAL CASE: test19GetMachineMaintID15 - M15");
        String machineName = "M15";
        List<Long> maintID = machineSystemBeanRemoteInterface.getMachineMaintID(machineName);
        assertEquals(maintID.size(), 1);
    }
    
    @Test
    // CONFLICT CASE test20GetMachineMaintID100 case
    public void test20GetMachineMaintID100(){
        System.out.println("CONFLICT CASE: test20GetMachineMaintID100");
        String machineName = "M100";
        assertNull(machineSystemBeanRemoteInterface.getMachineMaintID(machineName));
    }
    
    @Test
    // NORMAL CASE test21MachineMaintainenceListExpired case
    public void test21MachineMaintainenceListExpired(){
        System.out.println("NORMAL CASE: test21MachineMaintainenceListExpired");
        List<MachineMaintainenceEntity> result = machineSystemBeanRemoteInterface.machineMaintainenceListExpired();
        assertEquals(result.size(), 1);
    }
    
    @Test
    // NORMAL CASE test22MachineMaintainenceListWeek case
    public void test22MachineMaintainenceListWeek(){
        System.out.println("NORMAL CASE: test22MachineMaintainenceListWeek");
        List<MachineMaintainenceEntity> result = machineSystemBeanRemoteInterface.machineMaintainenceListWeek();
        assertEquals(result.size(), 2);
    }
    
    
    @Test
    // NORMAL CASE test23MachineMaintainenceNames case
    public void test23MachineMaintainenceNames(){
        System.out.println("NORMAL CASE: test23MachineMaintainenceNames");
        List<String> result = machineSystemBeanRemoteInterface.machineMaintainenceNames();
        assertEquals(result.size(), 4);
    }
    
    @Test
    // NORMAL CASE test24UpdateMachineSchedule case
    public void test24UpdateMachineSchedule(){
        System.out.println("NORMAL CASE: test24UpdateMachineSchedule");
        MachineMaintainenceEntity mSchedule1 = new MachineMaintainenceEntity();
        //Collection<MachineMaintainenceEntity> mSchedule = new ArrayList<MachineMaintainenceEntity>();
        MachineEntity machine = new MachineEntity();
        machine = machineSystemBeanRemoteInterface.getMachine("M15");
        
        Date scheduleDate = new Date();
        String mScheduleHour = "1345";
        String mServiceProvider = "Provider";
        String mServiceContact = "89012424";
                
        //boolean result = machineSystemBeanRemoteInterface.updateMachineSchedule(mSchedule, scheduleDate, mScheduleHour, mServiceProvider, mServiceContact);
        //assertTrue(result);
    }
    
    @Test
    // NORMAL CASE test25DeleteMachineMaintainence case
    public void test25DeleteMachineMaintainence(){
        System.out.println("NORMAL CASE: test25DeleteMachineMaintainence");
        boolean result = machineSystemBeanRemoteInterface.deleteMachineMaintainence("1058");
        
        assertTrue(result);
    }
    
    @Test
    // CONFLICT CASE test26DeleteMachineMaintainence1000 case
    public void test26DeleteMachineMaintainence1000(){
        System.out.println("CONFLICT CASE: test26DeleteMachineMaintainence1000");
        boolean result = machineSystemBeanRemoteInterface.deleteMachineMaintainence("1000");
        
        assertFalse(result);
    }

    @Test
    // NORMAL CASE test27ExtendMachineExpiry15 case
    public void test27ExtendMachineExpiry15(){
        System.out.println("NORMAL CASE: test27ExtendMachineExpiry15");
        String machineNum = "15";
        boolean result = machineSystemBeanRemoteInterface.extendMachineExpiry(machineNum);
        
        assertTrue(result);
    }
    
    @Test
    // CONFLICT CASE test28ExtendMachineExpiry100 case
    public void test28ExtendMachineExpiry100(){
        System.out.println("CONFLICT CASE: test28ExtendMachineExpiry100");
        String machineNum = "100";
        boolean result = machineSystemBeanRemoteInterface.extendMachineExpiry(machineNum);
        
        assertFalse(result);
    }
    
    @Test
    // NORMAL CASE test29CheckMachineExpiry case
    public void test29CheckMachineExpiry(){
        System.out.println("NORMAL CASE: test29CheckMachineExpiry");
        List<MachineEntity> result = machineSystemBeanRemoteInterface.checkMachineExpiry();
        
        assertEquals(result.size(), 1);
    }
    
    
    @Test
    // NORMAL CASE test31GetMachine15 case
    public void test31GetMachine15(){
        System.out.println("NORMAL CASE: test31GetMachine15");
        assertNotNull(machineSystemBeanRemoteInterface.getMachine("M15"));
    }
    
    @Test
    // CONFLICT CASE test32GetMachine100 case
    public void test32GetMachine100(){
        System.out.println("CONFLICT CASE: test32GetMachine100");
        assertNull(machineSystemBeanRemoteInterface.getMachine("M100"));
    }
    
    private MachineSystemBeanRemoteInterface lookupMachineSystemBeanRemote() 
    {
        try 
        {
            Context c = new InitialContext();
            //  java:global[/<app-name>]/<module-name>/<bean-name>!<fully-qualified-interface-name>
            return (MachineSystemBeanRemoteInterface) c.lookup("java:global/HiYewSystem/HiYewSystem-ejb/MachineSystemBean!session.stateless.MachineSystemBeanRemoteInterface");
        }   
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}