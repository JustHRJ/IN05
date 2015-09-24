package managedBean;

import entity.Customer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.CustomerSessionBeanLocal;
import manager.EmailManager;

@Named(value = "LoginManagedBean")
@ViewScoped
public class LoginManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    private List<String> users;
    private String user = "";
    private Customer customer;
    private Customer newCustomer;
    private String username = "";
    private String password = "";
    private String rePassword = "";

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("username");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("userRole");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("loginMessage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("forgotMessage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("registerMessage");
        customer = new Customer();
        newCustomer = new Customer();
        users = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        users.add("Customer");
        users.add("Supplier");
    }

    public void register() {

    }

    public String login() {
        String path = "";
        if (this.user.equals("Customer")) {
            setCustomer(customerSessionBean.getCustomerByUsername(this.username));
            try {
                if (customerSessionBean.encryptPassword(this.password).equals(getCustomer().getPw())) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userRole", "Customer");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", this.username);
                    System.out.println("Login Success");
                    path = "customer-home?faces-redirect=true"; //navigation
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid Username or password! Please try again.");
                    //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or password!"));
                    System.out.println("Login Fail");
                    path = "login?faces-redirect=true"; //navigation
                }
            } catch (NullPointerException e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid Username or password! Please try again.");
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or password!"));
                System.out.println("Login Fail");
                path = "login?faces-redirect=true"; //navigation
            }
        } else { //if user is supplier
        }
        return path;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        System.out.println("Logout Success");
        return "login?faces-redirect=true"; // navigation
    }

    public String sendPassword() {
        if (this.user.equals("Customer")) {
            customer = customerSessionBean.getCustomerByUsername(username);
            if (this.customer != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your password has been reset successfully! Please check your email.");
                System.out.println("User's email: " + customer.getEmail());
                String output[] = customerSessionBean.resetCustomerPassword(customer.getUserName()).split(":");
                System.out.println("User's new password: " + output[0]);
                EmailManager emailManager = new EmailManager();
                emailManager.emailPassword(output[0], output[1]);
                return "login?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forgotMessage", "Username does not exist!");
                return "forgot-password?faces-redirect=true";
            }
        } else { // if user is supplier
            return "login?faces-redirect=true";
        }
    }

    public String createCustomer() {
        customer = customerSessionBean.getCustomerByUsername(newCustomer.getUserName());
        if (customer == null) {
            if (newCustomer.getPw().equals(rePassword)) {
                // encrypt password
                newCustomer.setPw(customerSessionBean.encryptPassword(newCustomer.getPw()));
                customerSessionBean.createCustomer(newCustomer);
                newCustomer = new Customer(); // To reinitialise and create new customer
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your account registration has been successful.");
                return "login?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "Your password and confirmation password do not match.");
                return "register-customer?faces-redirect=true";
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "This username has been taken by someone else. Please choose a different username.");
            return "register-customer?faces-redirect=true";
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
