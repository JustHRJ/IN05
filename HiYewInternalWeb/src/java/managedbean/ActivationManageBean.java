/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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

    public void sendActivationCode() throws IOException {
        String check = hiYewSystemBean.sendActivationCode(email);
        if (!(check.equals(""))) {
            FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Supplier Code has been sent.", ""));
            EmailManager emailManager = new EmailManager();
            emailManager.emailActivation(check, email);
//            FacesContext facesCtx = FacesContext.getCurrentInstance();
//            ExternalContext externalContext = facesCtx.getExternalContext();
//            externalContext.redirect("/HiYewInternalWeb/ps-activation.xhtml");
        } else {
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesCtx.getExternalContext();
            externalContext.redirect("/HiYewInternalWeb/ps-activation.xhtml");
            // FacesContext.getCurrentInstance().addMessage("null", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email exists.", ""));
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
