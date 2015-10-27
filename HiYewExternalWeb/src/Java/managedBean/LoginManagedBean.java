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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.SupplierSessionBeanLocal;
import manager.EmailManager;
import session.stateless.HiYewSystemBeanLocal;

@Named(value = "loginManagedBean")
@ViewScoped
public class LoginManagedBean implements Serializable {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;

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
    private Boolean visibility = false;
    private String supplierCodeWord = "";
    private String secretQn = "";
    private String secretAns = "";

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
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
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid username or password! Please try again.");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
                    System.out.println("Login Fail");
                    path = ""; //navigation
                }
            } catch (NullPointerException e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid username or password! Please try again.");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
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
                    path = "s-user-profile?faces-redirect=true"; //navigation

                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid username or password! Please try again.");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
                    System.out.println("Login Fail");

                }
            } catch (NullPointerException ex) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Invalid username or password! Please try again.");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
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

        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            username = "";
            password = "";

            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/login.xhtml");

            System.out.println("Logout Success");
        }
    }

    public String sendPassword() throws IOException {
        if (this.user.equals("Customer")) {
            customer = customerSessionBean.getCustomerByUsername(username);
            //System.out.println("username ==== " + username);
            if (this.customer != null) {
                if (customer.getSecretQuestion().equals(secretQn) && customer.getSecretAnswer().equals(secretAns)) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your password has been reset successfully! Please check your email.");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "green");
                    String output[] = customerSessionBean.resetCustomerPassword(customer.getUserName()).split(":");
                    //System.out.println("User's new password: " + output[0]);
                    EmailManager emailManager = new EmailManager();
                    emailManager.emailPassword(output[0], output[1], output[2]);
                    FacesContext facesCtx = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = facesCtx.getExternalContext();
                    externalContext.redirect("/HiYewExternalWeb/login.xhtml");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forgotMessage", "Invalid secret question or answer!");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("forgotMessage", "Username does not exist!");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
            }
        } else { // if user is supplier
            return "login?faces-redirect=true";
        }
        return "";
    }

    public void createSupplierFromExistingCustomerAcct(String user) {
        if (hiYewSystemBean.checkActivationCode(supplierCodeWord) == true) {
            Customer c = customerSessionBean.getCustomerByUsername(user);
            //map values from customer acct to supplier acct for same user
            newSupplier.setAddress1(c.getAddress1());
            newSupplier.setAddress2(c.getAddress2());
            newSupplier.setCompanyName(c.getName());
            newSupplier.setEmail(c.getEmail());
            newSupplier.setPhone(c.getPhone());
            newSupplier.setPostalCode(c.getPostalCode());
            newSupplier.setPw(c.getPw());
            newSupplier.setSubscribeEmail_qPriceUpdates(c.isSubscribeEmail_qPriceUpdates());
            newSupplier.setSubscribeSMS_qPriceUpdates(c.isSubscribeSMS_qPriceUpdates());
            newSupplier.setSubscribeEmail_poDeliveryUpdates(c.isSubscribeEmail_poDeliveryUpdates());
            newSupplier.setSubscribeSMS_poDeliveryUpdates(c.isSubscribeSMS_poDeliveryUpdates());
            newSupplier.setUserName(c.getUserName());
            newSupplier.setSecretQuestion(c.getSecretQuestion());
            newSupplier.setSecretAnswer(c.getSecretAnswer());

            supplierSessionBean.createSupplier(newSupplier);
            newSupplier = new SupplierEntity();

            EmailManager emailManager = new EmailManager();
            emailManager.emailSuccessfulRegistration(newSupplier.getCompanyName(), newSupplier.getUserName(), newSupplier.getPw(), newSupplier.getEmail());

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Supplier account has been created succesfully!");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "green");
            hiYewSystemBean.deleteActivationCode(supplierCodeWord);
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "The supplier's code provided is invalid!");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
        }
    }

    public String createUser() throws IOException {
        if (user.equals("Customer")) {
            customer = customerSessionBean.getCustomerByUsername(newCustomer.getUserName());
            if (customer == null) {
                if (newCustomer.getPw().equals(rePassword)) {
                    // encrypt password
                    newCustomer.setPw(customerSessionBean.encryptPassword(newCustomer.getPw()));
                    newCustomer.setSubscribeEmail_qPriceUpdates(true);
                    newCustomer.setSubscribeSMS_qPriceUpdates(true);
                    newCustomer.setSubscribeEmail_poDeliveryUpdates(true);
                    newCustomer.setSubscribeSMS_poDeliveryUpdates(true);

                    customerSessionBean.createCustomer(newCustomer);
                    EmailManager emailManager = new EmailManager();
                    emailManager.emailSuccessfulRegistration(newCustomer.getName(), newCustomer.getUserName(), rePassword, newCustomer.getEmail());

                    newCustomer = new Customer();

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your customer account registration has been successful.");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "green");
                    FacesContext facesCtx = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = facesCtx.getExternalContext();
                    externalContext.redirect("/HiYewExternalWeb/login.xhtml");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your password and confirmation password do not match.");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "This username has been taken by someone else. Please choose a different username.");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
            }
        } else { //create supplier
            //validate if passcode is similar
            if (hiYewSystemBean.checkActivationCode(supplierCodeWord) == true) {

                supplier = supplierSessionBean.getSupplierByUsername(newCustomer.getUserName());
                if (supplier == null) {
                    if (newCustomer.getPw().equals(rePassword)) {

                        // encrypt password
                        newCustomer.setPw(customerSessionBean.encryptPassword(newCustomer.getPw()));
                        newCustomer.setSubscribeEmail_qPriceUpdates(true);
                        newCustomer.setSubscribeSMS_qPriceUpdates(true);
                        newCustomer.setSubscribeEmail_poDeliveryUpdates(true);
                        newCustomer.setSubscribeSMS_poDeliveryUpdates(true);

                        //map values from customer acct to supplier acct for same user
                        newSupplier.setAddress1(newCustomer.getAddress1());
                        newSupplier.setAddress2(newCustomer.getAddress2());
                        newSupplier.setCompanyName(newCustomer.getName());
                        newSupplier.setEmail(newCustomer.getEmail());
                        newSupplier.setPhone(newCustomer.getPhone());
                        newSupplier.setPostalCode(newCustomer.getPostalCode());
                        newSupplier.setPw(newCustomer.getPw());
                        newSupplier.setSubscribeEmail_qPriceUpdates(newCustomer.isSubscribeEmail_qPriceUpdates());
                        newSupplier.setSubscribeSMS_qPriceUpdates(newCustomer.isSubscribeSMS_qPriceUpdates());
                        newSupplier.setSubscribeEmail_poDeliveryUpdates(newCustomer.isSubscribeEmail_poDeliveryUpdates());
                        newSupplier.setSubscribeSMS_poDeliveryUpdates(newCustomer.isSubscribeSMS_poDeliveryUpdates());
                        newSupplier.setUserName(newCustomer.getUserName());
                        newSupplier.setSecretQuestion(newCustomer.getSecretQuestion());
                        newSupplier.setSecretAnswer(newCustomer.getSecretAnswer());

                        supplierSessionBean.createSupplier(newSupplier);

                        EmailManager emailManager = new EmailManager();
                        emailManager.emailSuccessfulRegistration(newSupplier.getCompanyName(), newSupplier.getUserName(), rePassword, newSupplier.getEmail());

                        newSupplier = new SupplierEntity();
                        newCustomer = new Customer();

                        hiYewSystemBean.deleteActivationCode(supplierCodeWord);

                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loginMessage", "Your supplier account registration has been successful.");
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "green");
                        FacesContext facesCtx = FacesContext.getCurrentInstance();
                        ExternalContext externalContext = facesCtx.getExternalContext();
                        externalContext.redirect("/HiYewExternalWeb/login.xhtml");
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "Your password and confirmation password do not match.");
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
                    }
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "This username has been taken by someone else. Please choose a different username.");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("registerMessage", "The supplier's code provided is invalid!");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("msgColor", "red");
            }
        }
        return "";
    }

    public void display() {
        visibility = user.equals("Supplier");
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

    /**
     * @return the visibility
     */
    public Boolean getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the supplierCodeWord
     */
    public String getSupplierCodeWord() {
        return supplierCodeWord;
    }

    /**
     * @param supplierCodeWord the supplierCodeWord to set
     */
    public void setSupplierCodeWord(String supplierCodeWord) {
        this.supplierCodeWord = supplierCodeWord;
    }

    /**
     * @return the secretQn
     */
    public String getSecretQn() {
        return secretQn;
    }

    /**
     * @param secretQn the secretQn to set
     */
    public void setSecretQn(String secretQn) {
        this.secretQn = secretQn;
    }

    /**
     * @return the secretAns
     */
    public String getSecretAns() {
        return secretAns;
    }

    /**
     * @param secretAns the secretAns to set
     */
    public void setSecretAns(String secretAns) {
        this.secretAns = secretAns;
    }
}
