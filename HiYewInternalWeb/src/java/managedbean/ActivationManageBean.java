/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.util.Date;
import java.util.Vector;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import manager.EmailManager;
import session.stateless.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "activationManageBean")
@RequestScoped
public class ActivationManageBean {
    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private String email;
    /**
     * Creates a new instance of ActivationManageBean
     */
  
    
    public ActivationManageBean() {
    }

    
    public void sendActivationCode(){
        String check = hiYewSystemBean.sendActivationCode(email);
        if(!(check.equals(""))){
             EmailManager emailManager = new EmailManager();
            emailManager.emailActivation(check, email);
        }
    }
    
    
    
    


 
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    
}
