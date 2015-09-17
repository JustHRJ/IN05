/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.Customer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.CustomerSessionBeanLocal;

/**
 *
 * @author: Jit Cheong
 */
@Named(value = "csLoginManagedBean")
@ViewScoped
public class csLoginManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    private List<String> users;
    private String user = "";
    private Customer customer;

    private String username = "";
    private String password = "";

    /**
     * Creates a new instance of csLoginManagedBean
     */
    public csLoginManagedBean() {
        customer = new Customer();
        users = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        users.add("Customer");
        users.add("Suppliers");
    }

    public String login() {
        String path = "";
        if (this.user.equals("Customer")) {
            setCustomer(customerSessionBean.getCustomerByUsername(this.username));

            try {
                if (this.password.equals(getCustomer().getPw())) {

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", this.username);

                    System.out.println("Login Success");
                    path = "CustomerHome?faces-redirect=true"; //navigation

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or password!"));
                    System.out.println("Login Fail");

                }
            } catch (NullPointerException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or password!"));
                System.out.println("Login Fail");

            }
        } else {//if user is supplier

        }
        
        return path;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        System.out.println("logout");
        return "csLoginPage?faces-redirect=true"; //navigation
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the users
     */
    public List<String> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<String> users) {
        this.users = users;
    }

    /**
     * @return the customer
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
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

}
