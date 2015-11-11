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
import manager.EmailManager;
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
    private boolean subscribeEmail_qPriceUpdates;
    private boolean subscribeSMS_qPriceUpdates;
    private boolean subscribeEmail_poDeliveryUpdates;
    private boolean subscribeSMS_poDeliveryUpdates;

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
        FacesContext.getCurrentInstance().addMessage("profileMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile has been updated successfully!", ""));
    }

    public void changeSubscription() throws IOException {
        customerSessionBean.updateCustomer(customer);
        FacesContext.getCurrentInstance().addMessage("emailMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Subcriptions have been updated successfully!", ""));
    }

    public void changePassword() throws IOException {

        if (changePasswordInput.equals("") || newPassword.equals("") || rePassword.equals("")) {
            FacesContext.getCurrentInstance().addMessage("pwdMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Changing password requires all password fields to be filled!", ""));
        } else {
            String encryptedPassword = customer.getPw();
            if (encryptedPassword.equals(customerSessionBean.encryptPassword(changePasswordInput))) {
                if (customerSessionBean.encryptPassword(newPassword).equals(encryptedPassword)) {
                    FacesContext.getCurrentInstance().addMessage("pwdMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Password and current password should not be same.", ""));
                } else {
                    if (newPassword.equals(rePassword)) {
                        customer.setPw(customerSessionBean.encryptPassword(newPassword));
                        changePasswordInput = "";
                        newPassword = "";
                        rePassword = "";

                        EmailManager emailManager = new EmailManager();
                        emailManager.emailSupplierPasswordChanged(customer.getName(), customer.getEmail());

                        customerSessionBean.updateCustomer(customer);
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

    /**
     * @return the subscribeEmail_qPriceUpdates
     */
    public boolean isSubscribeEmail_qPriceUpdates() {
        return subscribeEmail_qPriceUpdates;
    }

    /**
     * @param subscribeEmail_qPriceUpdates the subscribeEmail_qPriceUpdates to
     * set
     */
    public void setSubscribeEmail_qPriceUpdates(boolean subscribeEmail_qPriceUpdates) {
        this.subscribeEmail_qPriceUpdates = subscribeEmail_qPriceUpdates;
    }

    /**
     * @return the subscribeSMS_qPriceUpdates
     */
    public boolean isSubscribeSMS_qPriceUpdates() {
        return subscribeSMS_qPriceUpdates;
    }

    /**
     * @param subscribeSMS_qPriceUpdates the subscribeSMS_qPriceUpdates to set
     */
    public void setSubscribeSMS_qPriceUpdates(boolean subscribeSMS_qPriceUpdates) {
        this.subscribeSMS_qPriceUpdates = subscribeSMS_qPriceUpdates;
    }

    /**
     * @return the subscribeEmail_poDeliveryUpdates
     */
    public boolean isSubscribeEmail_poDeliveryUpdates() {
        return subscribeEmail_poDeliveryUpdates;
    }

    /**
     * @param subscribeEmail_poDeliveryUpdates the
     * subscribeEmail_poDeliveryUpdates to set
     */
    public void setSubscribeEmail_poDeliveryUpdates(boolean subscribeEmail_poDeliveryUpdates) {
        this.subscribeEmail_poDeliveryUpdates = subscribeEmail_poDeliveryUpdates;
    }

    /**
     * @return the subscribeSMS_poDeliveryUpdates
     */
    public boolean isSubscribeSMS_poDeliveryUpdates() {
        return subscribeSMS_poDeliveryUpdates;
    }

    /**
     * @param subscribeSMS_poDeliveryUpdates the subscribeSMS_poDeliveryUpdates
     * to set
     */
    public void setSubscribeSMS_poDeliveryUpdates(boolean subscribeSMS_poDeliveryUpdates) {
        this.subscribeSMS_poDeliveryUpdates = subscribeSMS_poDeliveryUpdates;
    }
}
