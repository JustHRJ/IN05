package managedBean;

import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.ProductQuotationSessionBeanLocal;

@Named(value = "productQuotationManagedBean")
@SessionScoped
public class ProductQuotationManagedBean implements Serializable {

    @EJB
    private ProductQuotationSessionBeanLocal productQuotationSessionBean;
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private String username = "";
    private String date = "";
    private String productQuotationNo = "";
    private Integer count;

    private ArrayList<ProductQuotationDescription> cacheList = new ArrayList<>();
    ;
    private ProductQuotation newProductQuotation;
    private ProductQuotationDescription newProductQuotationDescription;

    private ArrayList<ProductQuotation> receivedProductQuotationList;
    private ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList;

    public ProductQuotationManagedBean() {
        System.out.println("ProductQuotationManagedBean.java ProductQuotationManagedBean()");
        newProductQuotation = new ProductQuotation();
        newProductQuotationDescription = new ProductQuotationDescription();
        receivedProductQuotationList = new ArrayList<>();
        displayProductQuotationDescriptionList = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        count = 1;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            System.out.println("ProductQuotationManagedBean.java: Username is " + getUsername());
        }
        date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

        receivedProductQuotationList = new ArrayList<>(productQuotationSessionBean.receivedProductQuotationList(username));
    }

    public String formatPrice(Double input) {
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("formatPrice() ===== " + df.format(input));
        return df.format(input);
    }

    public void checkToReset() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString() == null) {

        } else if (!username.equals(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString())) {
            System.out.println("ProductQuotationManagedBean.java receivedProductQuotations() ===== call method reset()");
            reset();
        }
    }

    public void reset() {
        System.out.println("ProductQuotationManagedBean.java ProductQuotationManagedBean()");
        newProductQuotation = new ProductQuotation();
        newProductQuotationDescription = new ProductQuotationDescription();
        receivedProductQuotationList = new ArrayList<>();
        displayProductQuotationDescriptionList = new ArrayList<>();

        count = 1;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            username = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString();
            System.out.println("ProductQuotationManagedBean.java: Username is " + getUsername());
        }
        date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    public void receivedProductQuotations() {
        System.out.println("ProductQuotationManagedBean.java receivedProductQuotations() ===== " + username);

        if (!username.equals(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString())) {
            System.out.println("ProductQuotationManagedBean.java receivedProductQuotations() ===== call method reset()");
            reset();
        }

        receivedProductQuotationList = new ArrayList<>(productQuotationSessionBean.receivedProductQuotationList(username));

        FacesContext.getCurrentInstance().addMessage("qMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Current list of quotations are up to date.", ""));
    }

    public String formatDate(Timestamp t) {
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        return sd.format(t.getTime());
    }

    public void selectProductQuotation(ProductQuotation productQuotation) {
        System.out.println("productQuotation.getProductQuotationNo() ===== " + productQuotation.getProductQuotationNo());
        if (productQuotation != null) {
            displayProductQuotationDescriptionList = new ArrayList<>(productQuotation.getProductQuotationDescriptionList());
        }

        // System.out.println("ProductQuotationDescriptionList size is " + productQuotation.getProductQuotationDescriptionList().size());
    }

    public void addToCacheList(String productType, String itemName, Integer quantity) {

        System.out.println("cacheList.size() === " + cacheList.size());
        System.out.println("count === " + count);
        if (cacheList.size() > 0) {
            System.out.println("inside for loop");
            for (ProductQuotationDescription pqd : cacheList) {
                if (pqd.getItemName().equals(itemName)) {
                    System.out.println("pqd.getItemName() === " + pqd.getItemName());
                    System.out.println("itemName === " + itemName);
                    FacesContext.getCurrentInstance().addMessage("warnMsg", new FacesMessage(FacesMessage.SEVERITY_WARN, "Item has been added to cart!", ""));
                } else {
                    newProductQuotationDescription.setProductQuotationDescNo(count);
                    newProductQuotationDescription.setProductType(productType);
                    newProductQuotationDescription.setItemName(itemName);
                    newProductQuotationDescription.setQuantity(quantity);

                    cacheList.add(newProductQuotationDescription);

                    count += 1;
                    newProductQuotationDescription = new ProductQuotationDescription();

                    FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Item is added to RFQ list!", ""));
                }
            }
        } else {
            newProductQuotationDescription.setProductQuotationDescNo(count);
            newProductQuotationDescription.setProductType(productType);
            newProductQuotationDescription.setItemName(itemName);
            newProductQuotationDescription.setQuantity(quantity);

            cacheList.add(newProductQuotationDescription);

            count += 1;
            newProductQuotationDescription = new ProductQuotationDescription();

            FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Item is added to RFQ list!", ""));
        }
    }

    public void deleteProductQuotationDescription(ProductQuotationDescription productQuotationDescription) {
        if (productQuotationDescription != null) {
            getCacheList().remove(productQuotationDescription.getProductQuotationDescNo() - 1);
            reassignQuotationNo();
        }
    }

    public void reassignQuotationNo() {
        for (int i = 0; i < getCacheList().size(); i++) {
            getCacheList().get(i).setProductQuotationDescNo(i + 1);
        }
        setCount((Integer) getCacheList().size() + 1);
    }

    public void createProductQuotation(ActionEvent event) throws IOException {
        System.out.println("getUsername() ==== " + getUsername());
        // generate product quotation number
        setProductQuotationNo(getProductQuotationSessionBean().getProductQuotationNo(getUsername()));
        // assign product quotation number
        getNewProductQuotation().setProductQuotationNo(getProductQuotationNo());
        getNewProductQuotation().setCustomer(getCustomerSessionBean().getCustomerByUsername(getUsername()));
        // persist product quotation
        getProductQuotationSessionBean().createProductQuotation(getNewProductQuotation());
        // add quotation description into its respective quotation
        for (ProductQuotationDescription pqd : getCacheList()) {
            pqd.setProductQuotationNo(getProductQuotationNo());
            pqd.setProductQuotation(getNewProductQuotation());
            getNewProductQuotation().addProductQuotationDescriptionList(pqd);
            getProductQuotationSessionBean().createProductQuotationDesciption(pqd);//persist qd
        }
        getProductQuotationSessionBean().conductMerge(getNewProductQuotation());
        // add quotation to customer
        getCustomerSessionBean().addProductQuotation(getUsername(), getNewProductQuotation());

        // clear cachelist
        getCacheList().clear();
        // set count back to 1
        setCount((Integer) 1);
        // reinitialise quotation
        setNewProductQuotation(new ProductQuotation());
        setNewProductQuotationDescription(new ProductQuotationDescription());
        // set quotation tab to be selected
        System.out.println("Your request for product price quotation has been sent successfully!");
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewExternalWeb/c-products.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("An RFQ has been sent successfully!", ""));
    }

    public void setRejectionStatus(ProductQuotation productQuotation) {
        System.out.println("Customer rejected projuct quotation!");
        productQuotation.setStatus("Rejected");
        getProductQuotationSessionBean().conductMerge(productQuotation);
        FacesContext.getCurrentInstance().addMessage("qMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "You have rejected Product Quotation No. " + productQuotation.getProductQuotationNo(), ""));
    }

    public boolean viewVisibility(ProductQuotation productQuotation) {
        if (productQuotation == null) {
            return false;
        }

        if (productQuotation.getStatus().equals("Pending") || productQuotation.getStatus().equals("Accepted") || productQuotation.getStatus().equals("Rejected")) {
            return false;
        }
        return true;
    }

    /**
     * @return the productQuotationSessionBean
     */
    public ProductQuotationSessionBeanLocal getProductQuotationSessionBean() {
        return productQuotationSessionBean;
    }

    /**
     * @param productQuotationSessionBean the productQuotationSessionBean to set
     */
    public void setProductQuotationSessionBean(ProductQuotationSessionBeanLocal productQuotationSessionBean) {
        this.productQuotationSessionBean = productQuotationSessionBean;
    }

    /**
     * @return the customerSessionBean
     */
    public CustomerSessionBeanLocal getCustomerSessionBean() {
        return customerSessionBean;
    }

    /**
     * @param customerSessionBean the customerSessionBean to set
     */
    public void setCustomerSessionBean(CustomerSessionBeanLocal customerSessionBean) {
        this.customerSessionBean = customerSessionBean;
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
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the productQuotationNo
     */
    public String getProductQuotationNo() {
        return productQuotationNo;
    }

    /**
     * @param productQuotationNo the productQuotationNo to set
     */
    public void setProductQuotationNo(String productQuotationNo) {
        this.productQuotationNo = productQuotationNo;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the cacheList
     */
    public ArrayList<ProductQuotationDescription> getCacheList() {
        return cacheList;
    }

    /**
     * @param cacheList the cacheList to set
     */
    public void setCacheList(ArrayList<ProductQuotationDescription> cacheList) {
        this.cacheList = cacheList;
    }

    /**
     * @return the newProductQuotation
     */
    public ProductQuotation getNewProductQuotation() {
        return newProductQuotation;
    }

    /**
     * @param newProductQuotation the newProductQuotation to set
     */
    public void setNewProductQuotation(ProductQuotation newProductQuotation) {
        this.newProductQuotation = newProductQuotation;
    }

    /**
     * @return the newProductQuotationDescription
     */
    public ProductQuotationDescription getNewProductQuotationDescription() {
        return newProductQuotationDescription;
    }

    /**
     * @param newProductQuotationDescription the newProductQuotationDescription
     * to set
     */
    public void setNewProductQuotationDescription(ProductQuotationDescription newProductQuotationDescription) {
        this.newProductQuotationDescription = newProductQuotationDescription;
    }

    /**
     * @return the receivedProductQuotationList
     */
    public ArrayList<ProductQuotation> getReceivedProductQuotationList() {
        return receivedProductQuotationList;
    }

    /**
     * @param receivedProductQuotationList the receivedProductQuotationList to
     * set
     */
    public void setReceivedProductQuotationList(ArrayList<ProductQuotation> receivedProductQuotationList) {
        this.receivedProductQuotationList = receivedProductQuotationList;
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
}
