/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Project;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.ProjectSessionBeanLocal;

/**
 *
 * @author User
 */
@Named(value = "projectsManagedBean")
@ViewScoped
public class ProjectsManagedBean implements Serializable{
    @EJB
    private ProjectSessionBeanLocal projectSessionBean;

    private ArrayList <Project> projects;
    private Map<String, String> projectStatuses;
    /**
     * Creates a new instance of ProjectsManagedBean
     */
    public ProjectsManagedBean() {
        projects = new ArrayList <>();
        projectStatuses = new HashMap<>();
    }
    
    @PostConstruct
    public void init(){
        projectStatuses.put("Starting", "Starting");
        projectStatuses.put("Ongoing", "Ongoing");
        projectStatuses.put("Completed", "Completed");
    }
    
    
}
