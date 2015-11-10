package managedbean;

import com.hoiio.sdk.services.SmsService;
import entity.Customer;
import entity.ProductPurchaseOrder;
import entity.ProductQuotationDescription;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import session.stateless.ProductPurchaseOrderSessionBeanLocal;

@Named(value = "adminProductPOManagedBean")
@ViewScoped
public class AdminProductPOManagedBean implements Serializable {

    @EJB
    private ProductPurchaseOrderSessionBeanLocal productPurchaseOrderSessionBean;
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private String status = "Unsettled";

    private Date deliveryDate;

    private Double totalQuotedPrice = 0.0;
    private Integer totalQuantity = 0;

    private ArrayList<ProductPurchaseOrder> receivedNewProductPOList;
    private ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList;

    private Map<String, String> statuses;

    private ProductPurchaseOrder selectedProductPO;

    public AdminProductPOManagedBean() {
        receivedNewProductPOList = new ArrayList<>();
        displayProductQuotationDescriptionList = new ArrayList<>();

        statuses = new HashMap<>();
        selectedProductPO = new ProductPurchaseOrder();
    }

    @PostConstruct
    public void init() {
        receivedNewProductPOList = new ArrayList<>(productPurchaseOrderSessionBean.receivedCustomerNewProductPOList(status));
        getStatuses().put("Settled", "Settled");
        getStatuses().put("Unsettled", "Unsettled");
    }

    public String formatPrice(Double input) {
        DecimalFormat df = new DecimalFormat("0.00");
        //System.out.println("formatPrice() ===== " + df.format(input));
        return df.format(input);
    }

    public List<String> getFilterSettledArr() {
        return selectedProductPO.getFilterSettledArr();
    }

    public List<String> getFilterUnsettledArr() {
        return selectedProductPO.getFilterUnsettledArr();
    }

    public Date getToday() {
        //System.out.println("getToday() ===== ");
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public void retrieveProductQuotationDescriptionList(String purchaseOrderNo) {
        displayProductQuotationDescriptionList = new ArrayList<>(productPurchaseOrderSessionBean.retrieveProductQuotationDescriptionList(purchaseOrderNo));
    }

    public void filterByStatusNoMsg() {
        receivedNewProductPOList = new ArrayList<>(productPurchaseOrderSessionBean.receivedCustomerNewProductPOList(status));
    }

    public void filterByStatus() {
        //System.out.println("status ==== " + status);
        receivedNewProductPOList = new ArrayList<>(productPurchaseOrderSessionBean.receivedCustomerNewProductPOList(status));
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Current list of \"" + status + "\" purchase order(s) are up to date.", ""));
    }

    public String formatDate(Timestamp timestamp) {
        //System.out.println("Ttmestamp ===== " + timestamp);
        if (timestamp != null) {
            //System.out.println("inside if ===== ");
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(date);
        } else {
            //System.out.println("else ===== ");
            return "";
        }

    }

    public Date formatDate1(Timestamp timestamp) {
        //System.out.println("Ttmestamp ===== " + timestamp);
        if (timestamp != null) {
            //System.out.println("inside if ===== ");
            Date date = new Date(timestamp.getTime());

            return date;
        } else {
            //System.out.println("else ===== ");
            return null;
        }

    }

    public void selectPurchaseOrder(ProductPurchaseOrder productPurchaseOrder) {
        displayProductQuotationDescriptionList = new ArrayList<>(productPurchaseOrderSessionBean.retrieveProductQuotationDescriptionList(productPurchaseOrder.getProductPurchaseOrderID()));
        selectedProductPO = productPurchaseOrder;
    }

    public void updatePODeliveryDate(ProductPurchaseOrder productPurchaseOrder) {
        if (deliveryDate != null) {
            selectedProductPO.setDeliveryDate(new Timestamp(deliveryDate.getTime()));
            productPurchaseOrderSessionBean.updatePODeliveryDate(selectedProductPO);
            FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product PO (#" + selectedProductPO.getProductPurchaseOrderID() + ") delivery date has been updated successfully!", ""));
            selectedProductPO = new ProductPurchaseOrder();
            deliveryDate = null;
        } else if (productPurchaseOrder.getDeliveryDate() != null) {
            selectedProductPO.setDeliveryDate(new Timestamp(productPurchaseOrder.getDeliveryDate().getTime()));
            productPurchaseOrderSessionBean.updatePODeliveryDate(selectedProductPO);
            FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product PO (#" + selectedProductPO.getProductPurchaseOrderID() + ") delivery date has been updated successfully!", ""));
            selectedProductPO = new ProductPurchaseOrder();
            deliveryDate = null;
        } else {
            FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please input delivery date!", ""));
        }

    }

    public void updatePODeliveredStatus() {
        productPurchaseOrderSessionBean.updateProductPODeliveredStatus(selectedProductPO);

        FacesContext.getCurrentInstance().addMessage("inner-msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product PO (#" + selectedProductPO.getProductPurchaseOrderID() + ") has been delivered to customer successfully!", ""));
        selectedProductPO = new ProductPurchaseOrder();
    }

    public void updatePORelayedStatus(String username) {
        productPurchaseOrderSessionBean.updateProductPORelayedStatus(selectedProductPO);

        //Customer customer = customerSessionBean.getCustomerByUsername(username);
        // Email Germany supplier for purchasing products
        EmailManager emailManager = new EmailManager();
        emailManager.emailGermanySupplierToPurchase(selectedProductPO.getProductPurchaseOrderID(), this.retrieveEmailProductQuotationDescriptionList(selectedProductPO.getProductPurchaseOrderID()));

        selectedProductPO = new ProductPurchaseOrder();
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product PO (#" + selectedProductPO.getProductPurchaseOrderID() + ") has been sent to supplier!", ""));
    }

    public ArrayList<ProductQuotationDescription> retrieveEmailProductQuotationDescriptionList(String purchaseOrderNo) {
        return new ArrayList<>(productPurchaseOrderSessionBean.retrieveProductQuotationDescriptionList(purchaseOrderNo));
    }

    public void updateProductPOStatus(String username) {
        productPurchaseOrderSessionBean.updateProductPOStatus(selectedProductPO);

        Customer customer = customerSessionBean.getCustomerByUsername(username);
        if (customer.isSubscribeEmail_poDeliveryUpdates()) {
            EmailManager emailManager = new EmailManager();
            // Email customer for delivery date update
            emailManager.emailProductPODeliveryDateUpdate(customer.getName(), customer.getEmail(), selectedProductPO.getProductPurchaseOrderCustomerID());
        }

        if (customer.isSubscribeSMS_poDeliveryUpdates()) {
            // SMS custmer for delivery date update
            try {
                SmsService smsService1 = new SmsService("2USsHuRw6nYOlkh4", "Yie0bbxSDekwTT1d");
                smsService1.send("+65" + customer.getPhone(), "HiYew: We have updated the delivery date for your item(s) for Purchase Order #" + selectedProductPO.getProductPurchaseOrderCustomerID() + ". You may go to HiYew Customer Portal to view the updates.", "", "", "");
            } catch (Exception e) {
                e.printStackTrace();
                throw new EJBException(e.getMessage());
            }
        }

        selectedProductPO = new ProductPurchaseOrder();
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Update notification for Product Purchase Order #" + selectedProductPO.getProductPurchaseOrderID() + " has been sent to customer successfully!", ""));
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
     * @return the displayProductQuotationDescriptionList
     */
    public ArrayList<ProductQuotationDescription> getDisplayProductQuotationDescriptionList() {
        totalQuotedPrice = 0.0;
        totalQuantity = 0;
        for (ProductQuotationDescription pqd : displayProductQuotationDescriptionList) {
            if (pqd.getQuotedPrice() != null) {
                totalQuotedPrice = totalQuotedPrice + pqd.getQuotedPrice();
            }
            totalQuantity = totalQuantity + pqd.getQuantity();
        }
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
     * @return the receivedNewProductPOList
     */
    public ArrayList<ProductPurchaseOrder> getReceivedNewProductPOList() {
        return receivedNewProductPOList;
    }

    /**
     * @param receivedNewProductPOList the receivedNewProductPOList to set
     */
    public void setReceivedNewProductPOList(ArrayList<ProductPurchaseOrder> receivedNewProductPOList) {
        this.receivedNewProductPOList = receivedNewProductPOList;
    }

    /**
     * @return the selectedProductPO
     */
    public ProductPurchaseOrder getSelectedProductPO() {
        return selectedProductPO;
    }

    /**
     * @param selectedProductPO the selectedProductPO to set
     */
    public void setSelectedProductPO(ProductPurchaseOrder selectedProductPO) {
        this.selectedProductPO = selectedProductPO;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return the totalQuotedPrice
     */
    public Double getTotalQuotedPrice() {
        return totalQuotedPrice;
    }

    /**
     * @param totalQuotedPrice the totalQuotedPrice to set
     */
    public void setTotalQuotedPrice(Double totalQuotedPrice) {
        this.totalQuotedPrice = totalQuotedPrice;
    }

    /**
     * @return the totalQuantity
     */
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * @param totalQuantity the totalQuantity to set
     */
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
