package managedBean;

import entity.Customer;
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

    public CustomerManagedBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("loginMessage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("forgotMessage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("registerMessage");
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            this.username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();

            customer = customerSessionBean.getCustomerByUsername(this.username);
            System.out.println("@Customer: Username is " + username);
        }
    }

    //update customer
    public void handleSave() {
        customerSessionBean.updateCustomer(customer);
    }

    public void changePassword() {

        if (changePasswordInput.equals("") || newPassword.equals("") || rePassword.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changing Password requires all password fields to be filled!", ""));
        } else {
            String encryptedPassword = customer.getPw();
            // System.out.println(encryptedPassword);
            // System.out.println(customerSessionBean.encryptPassword(changePasswordInput));
            if (encryptedPassword.equals(customerSessionBean.encryptPassword(changePasswordInput))) {
                if (customerSessionBean.encryptPassword(newPassword).equals(encryptedPassword)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New Password is same as current password!", ""));
                } else {
                    if (newPassword.equals(rePassword)) {
                        customer.setPw(customerSessionBean.encryptPassword(newPassword));
                        handleSave();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password changed successfully!", ""));

                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password Mismatch!", ""));
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid password!", ""));
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
}
