package managedbean;

import entity.Customer;
import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import manager.EmailManager;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.ProductQuotationSessionBeanLocal;

@Named(value = "adminProductQuotationManagedBean")
@ViewScoped
public class AdminProductQuotationManagedBean implements Serializable {

    @EJB
    private ProductQuotationSessionBeanLocal productQuotationSessionBean;
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private String status = "Pending";
    
    // get current year quotations by default
    private Integer year = Calendar.getInstance().get(Calendar.YEAR);
    private ArrayList<ProductQuotation> receivedNewProductQuotationList;
    private ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList;

    private Map<String, String> statuses;
    private Map<String, String> years;

    private ProductQuotation selectedProductQuotation;

    public AdminProductQuotationManagedBean() {
        receivedNewProductQuotationList = new ArrayList<>();
        displayProductQuotationDescriptionList = new ArrayList<>();

        statuses = new HashMap<>();
        years = new HashMap<>();
        selectedProductQuotation = new ProductQuotation();
    }

    @PostConstruct
    public void init() {
        setReceivedNewProductQuotationList(new ArrayList<>(productQuotationSessionBean.receivedCustomerNewProductQuotationList(getStatus())));
        getStatuses().put("Pending", "Pending");
        getStatuses().put("Processed", "Processed");
        getStatuses().put("Relayed", "Relayed");
        getYears().put("2015", "2015");
    }

    public void filterByStatusNoMsg() {
        receivedNewProductQuotationList = new ArrayList<>(productQuotationSessionBean.receivedCustomerNewProductQuotationList(status));
    }

    public void filterByStatus() {
        System.out.println("status ==== " + status);
        receivedNewProductQuotationList = new ArrayList<>(productQuotationSessionBean.receivedCustomerNewProductQuotationList(status));
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Current list of \"" + status + "\" purchase order(s) are up to date.", ""));
    }

    public String formatDate(Timestamp Ttmestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(Ttmestamp.getTime());
    }

    public void selectQuotation(ProductQuotation productQuotation) {
        System.out.println(productQuotation.getProductQuotationNo());
        setDisplayProductQuotationDescriptionList(new ArrayList<>(productQuotation.getProductQuotationDescriptionList()));
        setSelectedProductQuotation(productQuotation);
        // System.out.println("productQuotation.getProductQuotationDescriptionList().size() ===== " + productQuotation.getProductQuotationDescriptionList().size());
    }

    public void updateQuotationPrices() {
        productQuotationSessionBean.updateProductQuotationPrices(getDisplayProductQuotationDescriptionList());
        FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product quotation has been updated successfully!", ""));
    }

    public void updateQuotationRelayedStatus(String username) {
        productQuotationSessionBean.updateProductQuotationRelayedStatus(selectedProductQuotation);

        Customer customer = customerSessionBean.getCustomerByUsername(username);
        if (customer.getSubscribeEmail()) {
            EmailManager emailManager = new EmailManager();
            // Email Germany supplier for purchasing products
            emailManager.emailGermanySupplierToRFQ(selectedProductQuotation.getProductQuotationNo(), this.retrieveEmailProductQuotationDescriptionList(selectedProductQuotation.getProductQuotationNo()));
        }

        selectedProductQuotation = new ProductQuotation();
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product quotation has been sent to supplier!", ""));
    }

    public ArrayList<ProductQuotationDescription> retrieveEmailProductQuotationDescriptionList(String quotationNo) {
        return new ArrayList<>(productQuotationSessionBean.retrieveProductQuotationDescriptionList(quotationNo));
    }

    public boolean check() {
        boolean option = true;
        for (int i = 0; i < displayProductQuotationDescriptionList.size(); i++) {
            ProductQuotationDescription qd = displayProductQuotationDescriptionList.get(i);
            if (qd.getUnitPrice() == null) {
                option = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Price(s) must be quoted!", ""));
            } else if (qd.getUnitPrice() != null) {
                if (qd.getUnitPrice() < 0) {
                    qd.setUnitPrice(null);
                    option = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Price(s) must be more than zero!", ""));
                }
            }
        }
        System.out.println("option === " + option);
        return option;
    }

    public void updateQuotationStatus(String username) {
        if (check() == true) {
            productQuotationSessionBean.updateProductQuotationStatus(selectedProductQuotation);

            Customer customer = customerSessionBean.getCustomerByUsername(username);
            if (customer.getSubscribeEmail()) {
                EmailManager emailManager = new EmailManager();
                emailManager.emailProductQuotationPriceUpdate(customer.getName(), customer.getEmail(), selectedProductQuotation.getProductQuotationNo());
            }
            selectedProductQuotation = new ProductQuotation();
        } else {

        }
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the receivedNewProductQuotationList
     */
    public ArrayList<ProductQuotation> getReceivedNewProductQuotationList() {
        return receivedNewProductQuotationList;
    }

    /**
     * @param receivedNewProductQuotationList the
     * receivedNewProductQuotationList to set
     */
    public void setReceivedNewProductQuotationList(ArrayList<ProductQuotation> receivedNewProductQuotationList) {
        this.receivedNewProductQuotationList = receivedNewProductQuotationList;
    }

    /**
     * @return the displayProductQuotationDescriptionList
     */
    public ArrayList<ProductQuotationDescription> getDisplayProductQuotationDescriptionList() {
        return displayProductQuotationDescriptionList;
    }

    /**
     * @param displayProductQuotationDescriptionList the
     * displayProductQuotationDescriptionList to set
     */
    public void setDisplayProductQuotationDescriptionList(ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList) {
        this.displayProductQuotationDescriptionList = displayProductQuotationDescriptionList;
    }

    /**
     * @return the statuses
     */
    public Map<String, String> getStatuses() {
        return statuses;
    }

    /**
     * @param statuses the statuses to set
     */
    public void setStatuses(Map<String, String> statuses) {
        this.statuses = statuses;
    }

    /**
     * @return the years
     */
    public Map<String, String> getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(Map<String, String> years) {
        this.years = years;
    }

    /**
     * @return the selectedProductQuotation
     */
    public ProductQuotation getSelectedProductQuotation() {
        return selectedProductQuotation;
    }

    /**
     * @param selectedProductQuotation the selectedProductQuotation to set
     */
    public void setSelectedProductQuotation(ProductQuotation selectedProductQuotation) {
        this.selectedProductQuotation = selectedProductQuotation;
    }
}
