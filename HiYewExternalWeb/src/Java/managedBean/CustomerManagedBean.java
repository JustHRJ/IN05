/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.Customer;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import session.stateless.CustomerSessionBeanLocal;

/**
 *
 * @author: Jit Cheong
 */
@Named(value = "customerManagedBean")
@ViewScoped
public class CustomerManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private Customer customer;
    private Customer newCustomer;

    private String username = ""; //when log on taken from session
    private String rePassword = "";

    //for changing of password
    private String changePasswordInput = "";
    private String newPassword = "";

    /**
     * Creates a new instance of NewJSFManagedBean
     */
    public CustomerManagedBean() {
        newCustomer = new Customer();
        //customers = new ArrayList<>();
    }

    @PostConstruct
    public void init() {

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user") != null) {
            this.username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user").toString();

            customer = customerSessionBean.getCustomerByUsername(this.username);
            System.out.println("@C: Username is " + username);
            //customers = customerSessionBean.getAllCustomer();
        }
    }

    public String createCustomer() {

        customer = customerSessionBean.getCustomerByUsername(newCustomer.getUserName());

        if (customer == null) {
            if (newCustomer.getPw().equals(rePassword)) {
                //encrypt password
                newCustomer.setPw(customerSessionBean.encryptPassword(newCustomer.getPw()));
                customerSessionBean.createCustomer(newCustomer);
                newCustomer = new Customer(); //To reinitialise and create new customer
                return "csLoginPage?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password mismatch!"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username is taken. Try again with a different user name!"));
            System.out.println("Duplicate primary key for username");
        }
        return "";
    }

    //public void checkUsername() {
    //  setCustomer(customerSessionBean.getCustomerByUsername(this.username));
    // if (customer != null) {
    //    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username is in used."));
    //}else{
    //   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username is not in used."));
    //}
    //System.out.println("Check username availability");
    //}
    //update customer
    public void handleSave() {
        customerSessionBean.updateCustomer(customer);
    }

    public void changePassword() {
        
        if (changePasswordInput.equals("") || newPassword.equals("") || rePassword.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Changing Password requires all password fields to be filled!"));
        } else {
            String decryptedPassword = customerSessionBean.decryptPassword(customer.getPw());
            if (decryptedPassword.equals(changePasswordInput)) {
                if (newPassword.equals(decryptedPassword)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New Password is same as current password!"));
                } else {
                    if (newPassword.equals(rePassword)) {
                        customer.setPw(customerSessionBean.encryptPassword(newPassword));
                        handleSave();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password changed successfully!"));
                         
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password Mismatch!"));
                    }
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid password!"));
            }
        }
        
    }
    
    


    /**
     * @return the newCustomer
     */
    public Customer getNewCustomer() {
        return newCustomer;
    }

    /**
     * @param newCustomer the newCustomer to set
     */
    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
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
