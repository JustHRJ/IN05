package managedbean;

import com.hoiio.sdk.services.SmsService;
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
import javax.ejb.EJBException;
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

    private Boolean correctPrice = true;

    private ArrayList<ProductQuotation> filteredProductList;

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
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "The list of status \"" + status + "\" are up to date.", ""));
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

    public void updateQuotationPrices(String productQuotationNo) {
        correctPrice = true;
        productQuotationSessionBean.updateProductQuotationPrices(getDisplayProductQuotationDescriptionList());
        FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product quotation (#" + productQuotationNo + ") has been updated successfully!", ""));
    }

    public void updateQuotationRelayedStatus(String productQuotationNo, String username) {
        correctPrice = true;
        productQuotationSessionBean.updateProductQuotationRelayedStatus(selectedProductQuotation);

        Customer customer = customerSessionBean.getCustomerByUsername(username);

        // Email Germany supplier for purchasing products
        EmailManager emailManager = new EmailManager();
        emailManager.emailGermanySupplierToRFQ(selectedProductQuotation.getProductQuotationNo(), this.retrieveEmailProductQuotationDescriptionList(selectedProductQuotation.getProductQuotationNo()));

        selectedProductQuotation = new ProductQuotation();
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product quotation (#" + productQuotationNo + ") has been sent to supplier!", ""));
    }

    public ArrayList<ProductQuotationDescription> retrieveEmailProductQuotationDescriptionList(String quotationNo) {
        return new ArrayList<>(productQuotationSessionBean.retrieveProductQuotationDescriptionList(quotationNo));
    }

    public boolean check() {
        boolean option = true;
        for (int i = 0; i < displayProductQuotationDescriptionList.size(); i++) {
            ProductQuotationDescription qd = displayProductQuotationDescriptionList.get(i);
            if (qd.getQuotedPrice() == null) {
                option = false;
                correctPrice = false;
                FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_WARN, "Quoted price must be quoted for item #" + qd.getProductQuotationDescNo() + "!", ""));
            } else if (qd.getQuotedPrice() != null) {
                if (qd.getQuotedPrice() < 0) {
                    qd.setQuotedPrice(null);
                    option = false;
                    correctPrice = false;
                    FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_WARN, "Quoted price must be more than zero for item #" + qd.getProductQuotationDescNo() + "!", ""));
                }
            }

            if (qd.getCostPrice() == null) {
                option = false;
                correctPrice = false;
                FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_WARN, "Cost price must be quoted for item #" + qd.getProductQuotationDescNo() + "!", ""));
            } else if (qd.getCostPrice() != null) {
                if (qd.getCostPrice() < 0) {
                    qd.setCostPrice(null);
                    option = false;
                    correctPrice = false;
                    FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_WARN, "Cost price must be more than zero for item #" + qd.getProductQuotationDescNo() + "!", ""));
                }
            }
        }
        System.out.println("option === " + option);
        return option;
    }

    public void updateQuotationStatus(String productQuotationNo, String username) {
        correctPrice = true;
        if (check() == true) {
            productQuotationSessionBean.updateProductQuotationStatus(selectedProductQuotation);

            Customer customer = customerSessionBean.getCustomerByUsername(username);
            if (customer.isSubscribeEmail_qPriceUpdates()) {
                EmailManager emailManager = new EmailManager();
                emailManager.emailProductQuotationPriceUpdate(customer.getName(), customer.getEmail(), selectedProductQuotation.getProductQuotationNo());
            }
            if (customer.isSubscribeSMS_qPriceUpdates()) {
                // SMS custmer for delivery date update
//                try {
//                    SmsService smsService1 = new SmsService("2USsHuRw6nYOlkh4", "Yie0bbxSDekwTT1d");
//                    smsService1.send("+6592706232", "HiYew: Your quotation # details have been updated! You may go to HiYew Customer Portal to view the updates.", "", "", "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new EJBException(e.getMessage());
//                }
            }
            FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Update notification for product quotation (#" + productQuotationNo + ") has been sent to customer!", ""));
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

    /**
     * @return the correctPrice
     */
    public Boolean getCorrectPrice() {
        return correctPrice;
    }

    /**
     * @param correctPrice the correctPrice to set
     */
    public void setCorrectPrice(Boolean correctPrice) {
        this.correctPrice = correctPrice;
    }

    /**
     * @return the filteredProductList
     */
    public ArrayList<ProductQuotation> getFilteredProductList() {
        return filteredProductList;
    }

    /**
     * @param filteredProductList the filteredProductList to set
     */
    public void setFilteredProductList(ArrayList<ProductQuotation> filteredProductList) {
        this.filteredProductList = filteredProductList;
    }
}
