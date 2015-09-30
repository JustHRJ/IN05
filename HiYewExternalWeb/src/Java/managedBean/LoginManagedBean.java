package managedBean;

import entity.Customer;
import entity.SupplierEntity;
import java.io.IOException;
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
import session.stateless.SupplierSessionBeanLocal;
import manager.EmailManager;

@Named(value = "loginManagedBean")
@ViewScoped
public class LoginManagedBean implements Serializable {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    @EJB
    private SupplierSessionBeanLocal supplierSessionBean;
    private List<String> users;
    private String user = "";
    private Customer customer;
    private Customer newCustomer;
    private SupplierEntity supplier;
    private SupplierEntity newSupplier;
    private String username = "";
    private String password = "";
    private String rePassword = "";

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("loginMessage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("forgotMessage");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("registerMessage");
        customer = new Customer();
        supplier = new SupplierEntity();
        newCustomer = new Customer();
        newSupplier = new SupplierEntity();
        users = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        users.add("Customer");
        users.add("Supplier");
    }

    public String login() {
        String path = "";
        if (this.user.equals("Customer")) {
            setCustomer(customerSessionBean.getCustomerByUsername(this.username));
            try {
                if (customerSessionBean.encryptPassword(this.password).equals(getCustomer().getPw())) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", this.username);
                    System.out.println("Login Success");
                    path = "c-user-profile?faces-redirect=true"; //navigation
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid Username or password! Please try again.");
                    //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or password!"));
                    System.out.println("Login Fail");
                    path = ""; //navigation
                }
            } catch (NullPointerException e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid Username or password! Please try again.");
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or password!"));
                System.out.println("Login Fail");
                path = ""; //navigation
            }
        } else { //if user is supplier
            System.out.println(this.username);
            
            // NULLPOINTEREXCEPTION 
            setSupplier(supplierSessionBean.getSupplierByUsername(this.username));
            
            try {
                if (supplierSessionBean.encryptPassword(this.password).equals(getSupplier().getPw())) {

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", this.username);

                    System.out.println("Login Success");
                    path = "SupplierHome?faces-redirect=true"; //navigation

                } else {
                    FacesContext.getCurrentInstance().addMessage("loginMessage", new FacesMessage("Invalid Username or password!"));
                    System.out.println("Login Fail");

                }
            } catch (NullPointerException e) {
                FacesContext.getCurrentInstance().addMessage("loginMessage", new FacesMessage("Invalid Username or password!"));
                System.out.println("Login Fail");

            }
        }
        return path;
    }

    public void checkLoginRedirect() throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/login.xhtml");
        }
    }

    public void checkAfterLoginRedirect() throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            System.out.println(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username"));
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            username = "";
            password = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/login.xhtml");
            System.out.println("");
        }
    }

    public void logout() throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            username = "";
            password = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/login.xhtml");

            System.out.println("Logout Success");
        }
    }

    public String sendPassword() {
        if (this.user.equals("Customer")) {
            customer = customerSessionBean.getCustomerByUsername(username);
            System.out.println("username ==== " + username);
            if (this.customer != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your password has been reset successfully! Please check your email.");
                System.out.println("User's email: " + customer.getEmail());
                String output[] = customerSessionBean.resetCustomerPassword(customer.getUserName()).split(":");
                System.out.println("User's new password: " + output[0]);
                EmailManager emailManager = new EmailManager();
                emailManager.emailPassword(output[0], output[1], output[2]);
                return "login?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forgotMessage", "Username does not exist!");
                return "";
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
                newCustomer.setSubscribeEmail(true);
                customerSessionBean.createCustomer(newCustomer);
                EmailManager emailManager = new EmailManager();
                emailManager.emailSuccessfulRegistration(newCustomer.getName(), newCustomer.getUserName(), rePassword, newCustomer.getEmail());
                newCustomer = new Customer(); // To reinitialise and create new customer
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your account registration has been successful.");
                return "login?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "Your password and confirmation password do not match.");
                
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "This username has been taken by someone else. Please choose a different username.");
            
        }
        return "";
    }
    
    
    public String createSupplier() {
        supplier = supplierSessionBean.getSupplierByUsername(newSupplier.getUserName());
        if (supplier == null) {
            if (newSupplier.getPw().equals(rePassword)) {
                // encrypt password
                newSupplier.setPw(customerSessionBean.encryptPassword(newSupplier.getPw()));
                newSupplier.setSubscribeEmail("true");
                supplierSessionBean.createSupplier(newSupplier);
                EmailManager emailManager = new EmailManager();
                emailManager.emailSuccessfulRegistration(newSupplier.getName(), newSupplier.getUserName(), rePassword, newSupplier.getEmail());
                newSupplier = new SupplierEntity(); // To reinitialise and create new customer
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your account registration has been successful.");
                return "SupplierHome?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "Your password and confirmation password do not match.");
                
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "This username has been taken by someone else. Please choose a different username.");
            
        }
        return "";
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

    public SupplierEntity getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
    }
    
     public SupplierEntity getNewSupplier() {
        return newSupplier;
    }

    /**
     * @param newSupplier the newCustomer to set
     */
    public void setNewSupplier(SupplierEntity newSupplier) {
        this.newSupplier = newSupplier;
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
