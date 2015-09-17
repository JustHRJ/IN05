/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.Customer;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
            //customers = customerSessionBean.getAllCustomer();
        }
    }

    public String createCustomer() {

        customer = customerSessionBean.getCustomerByUsername(newCustomer.getUserName());

        if (customer == null) {
            //System.out.println("Customer is null");
            customerSessionBean.createCustomer(newCustomer);
            newCustomer = new Customer(); //To reinitialise and create new customer
            return "csLoginPage?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username is taken. Try again with a different user name!"));
            System.out.println("Duplicate primary key for username");
            return "";
        }

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
    public void handleSave() {
        customerSessionBean.updateCustomer(customer);
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

}
