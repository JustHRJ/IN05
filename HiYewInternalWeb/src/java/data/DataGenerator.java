/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;
import entity.Project;
import entity.EmployeeEntity;
import entity.MachineEntity;
import entity.WeldJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author Jitcheong
 */
public class DataGenerator {
    ProjectSessionBeanLocal projectSessionBean = lookupProjectSessionBeanLocal();
    
    
    public static void main(String [] args){
        //createProjects();
        
        //createWeldJobs();
        //createEmployees();
        //createMachines();
    }
    
    public void createProjects(){
        Project p1 = new Project();
        p1.setProjectNo("PPPP");
        //projectSessionBean.createProject(p1);
        System.out.println("Project created");
    }
    
    public void createWeldJobs(){
        WeldJob w1 = new WeldJob();
    }
    
    public void createEmployees(){
        EmployeeEntity e1 = new EmployeeEntity();
    }
    
    public void createMachines(){
        MachineEntity m1 = new MachineEntity();
    }

    private ProjectSessionBeanLocal lookupProjectSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (ProjectSessionBeanLocal) c.lookup("java:global/HiYewSystem/HiYewSystem-ejb/ProjectSessionBean!session.stateless.ProjectSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
