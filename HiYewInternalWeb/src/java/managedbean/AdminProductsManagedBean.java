package managedbean;

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
import session.stateless.ProductQuotationSessionBeanLocal;

@Named(value = "adminProductsManagedBean")
@ViewScoped
public class AdminProductsManagedBean implements Serializable {

    @EJB
    private ProductQuotationSessionBeanLocal productQuotationSessionBean;

    private String status = "Pending";
    // get current year quotations by default
    private Integer year = Calendar.getInstance().get(Calendar.YEAR);
    private ArrayList<ProductQuotation> receivedNewProductQuotationList;
    private ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList;

    private Map<String, String> statuses;
    private Map<String, String> years;

    private ProductQuotation selectedProductQuotation;

    public AdminProductsManagedBean() {
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
        getYears().put("2015", "2015");
    }

    public void filterByYearAndStatus() {
        setReceivedNewProductQuotationList(new ArrayList<>(productQuotationSessionBean.receivedCustomerNewProductQuotationList(getStatus())));
        System.out.println("filterByYearAndStatus() year ====== " + getYear());
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product quotations have been updated successfully!", ""));
    }

    public void updateQuotationStatus() {
        productQuotationSessionBean.updateProductQuotationStatus(getSelectedProductQuotation());
        setSelectedProductQuotation(new ProductQuotation());
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
