package managedBean;

import entity.SupplierEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import manager.EmailManager;
import session.stateless.SupplierSessionBeanLocal;

@Named(value = "supplierManagedBean")
@ViewScoped
public class SupplierManagedBean implements Serializable {

    @EJB
    private SupplierSessionBeanLocal supplierSessionBean;
    private SupplierEntity supplier;
    private String username = ""; // when log on taken from session
    private String rePassword = "";
    private String changePasswordInput = "";
    private String newPassword = "";

    public SupplierManagedBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("popupMessage");
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            this.username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();

            supplier = supplierSessionBean.getSupplierByUsername(this.username);
            System.out.println("@Supplier: Username is " + username);
        }
    }

    // update supplier
    public void handleSave() throws IOException {
        supplierSessionBean.updateSupplier(supplier);
        FacesContext.getCurrentInstance().addMessage("profileMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile has been updated successfully!", ""));
    }

    public void changeSubscribeEmail() throws IOException {

    }

    public void changePassword() throws IOException {

        if (changePasswordInput.equals("") || newPassword.equals("") || rePassword.equals("")) {
            FacesContext.getCurrentInstance().addMessage("pwdMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Changing password requires all password fields to be filled!", ""));
        } else {
            String encryptedPassword = supplier.getPw();
            if (encryptedPassword.equals(supplierSessionBean.encryptPassword(changePasswordInput))) {
                if (supplierSessionBean.encryptPassword(newPassword).equals(encryptedPassword)) {
                    FacesContext.getCurrentInstance().addMessage("pwdMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Password and current password should not be same.", ""));
                } else {
                    if (newPassword.equals(rePassword)) {
                        supplier.setPw(supplierSessionBean.encryptPassword(newPassword));
                        rePassword = "";
                        changePasswordInput = "";
                        newPassword = "";
                        EmailManager emailManager = new EmailManager();
                        emailManager.emailSupplierPasswordChanged(supplier.getCompanyName(), supplier.getEmail());

                        supplierSessionBean.updateSupplier(supplier);
                        FacesContext.getCurrentInstance().addMessage("pwdMsg", new FacesMessage("Password has been changed successfully!", ""));
                    } else {
                        FacesContext.getCurrentInstance().addMessage("pwdMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password mismatch!", ""));
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("pwdMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect current password!", ""));
            }
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the supplier
     */
    public SupplierEntity getSupplier() {

        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
    }

    /**
     * @return the rePassword
     */
    public String getRePassword() {
        return rePassword;
    }

    /**
     * @param rePassword the rePassword to set
     */
    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    /**
     * @return the changePasswordInput
     */
    public String getChangePasswordInput() {
        return changePasswordInput;
    }

    /**
     * @param changePasswordInput the changePasswordInput to set
     */
    public void setChangePasswordInput(String changePasswordInput) {
        this.changePasswordInput = changePasswordInput;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
