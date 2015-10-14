package managedBean;

import entity.Customer;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import session.stateless.CustomerSessionBeanLocal;

@Named(value = "customerManagedBean")
@ViewScoped
public class CustomerManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    private Customer customer;
    private String username = ""; // when log on taken from session
    private String rePassword = "";
    private String changePasswordInput = "";
    private String newPassword = "";
    private String subscribeEmail = "";

    public CustomerManagedBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("popupMessage");
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            this.username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();

            customer = customerSessionBean.getCustomerByUsername(this.username);
            System.out.println("CustomerManagedBean.java init() ===== Username is " + username);
        }
    }
    
    

    // update customer
    public void handleSave() throws IOException {
        customerSessionBean.updateCustomer(customer);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("popupMessage", "Profile has been updated successfully!");
        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-user-profile.xhtml");
    }

    public void changeSubscribeEmail() throws IOException {
        System.out.println("this.subscribeEmail = " + customer.getSubscribeEmail());
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("popupMessage", "Subcription has been updated successfully!");
        customerSessionBean.updateCustomer(customer);
        FacesContext.getCurrentInstance().addMessage("emailMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Subcription has been updated successfully!", ""));
        // FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-user-profile.xhtml");
    }

    public void changePassword() throws IOException {

        if (changePasswordInput.equals("") || newPassword.equals("") || rePassword.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Changing password requires all password fields to be filled!", ""));
        } else {
            String encryptedPassword = customer.getPw();
            if (encryptedPassword.equals(customerSessionBean.encryptPassword(changePasswordInput))) {
                if (customerSessionBean.encryptPassword(newPassword).equals(encryptedPassword)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Password and current password should not be same.", ""));
                } else {
                    if (newPassword.equals(rePassword)) {
                        customer.setPw(customerSessionBean.encryptPassword(newPassword));
                        handleSave();
                        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password has been changed successfully!", ""));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("popupMessage", "Password has been changed successfully!");
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-user-profile.xhtml");
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password mismatch!", ""));
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect current password!", ""));
            }
        }
    }

    /**
     * @return the subscribeEmail
     */
    public String getSubscribeEmail() {
        return subscribeEmail;
    }

    /**
     * @param subscribeEmail the subscribeEmail to set
     */
    public void setSubscribeEmail(String subscribeEmail) {
        this.subscribeEmail = subscribeEmail;
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
     * @return the customers
     */
    public Customer getCustomer() {

        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
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
