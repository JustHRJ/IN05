/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author JustHRJ
 */
@Named(value = "redirector")
@RequestScoped
public class redirector {

    /**
     * Creates a new instance of redirector
     */
    public redirector() {
    }

    public void forgetPassword() throws IOException{
          FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/forgetPassword.xhtml");
    }
    
    public void employeeTraining() throws IOException{
          FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/HRMS/addTrainingSchedule.xhtml");
    }
}
