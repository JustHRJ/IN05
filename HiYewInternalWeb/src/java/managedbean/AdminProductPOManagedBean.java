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

    private String status = "Pending";

    private Date deliveryDate;

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
        getStatuses().put("Pending", "Pending");
        getStatuses().put("Processed", "Processed");
        getStatuses().put("Relayed", "Relayed");
    }

    public String formatPrice(Double input) {
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("formatPrice() ===== " + df.format(input));
        return df.format(input);
    }

    public Date getToday() {
        System.out.println("getToday() ===== ");
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
        System.out.println("status ==== " + status);
        receivedNewProductPOList = new ArrayList<>(productPurchaseOrderSessionBean.receivedCustomerNewProductPOList(status));
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Current list of \"" + status + "\" purchase order(s) are up to date.", ""));
    }

    public String formatDate(Timestamp timestamp) {
        System.out.println("Ttmestamp ===== " + timestamp);
        if (timestamp != null) {
            System.out.println("inside if ===== ");
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(date);
        } else {
            System.out.println("else ===== ");
            return "";
        }

    }

    public Date formatDate1(Timestamp timestamp) {
        System.out.println("Ttmestamp ===== " + timestamp);
        if (timestamp != null) {
            System.out.println("inside if ===== ");
            Date date = new Date(timestamp.getTime());

            return date;
        } else {
            System.out.println("else ===== ");
            return null;
        }

    }

    public void selectPurchaseOrder(ProductPurchaseOrder productPurchaseOrder) {
        displayProductQuotationDescriptionList = new ArrayList<>(productPurchaseOrderSessionBean.retrieveProductQuotationDescriptionList(productPurchaseOrder.getProductPurchaseOrderID()));
        selectedProductPO = productPurchaseOrder;
    }

    public void updatePODeliveryDate() {
        selectedProductPO.setDeliveryDate(new Timestamp(deliveryDate.getTime()));
        productPurchaseOrderSessionBean.updatePODeliveryDate(selectedProductPO);
        selectedProductPO = new ProductPurchaseOrder();
        deliveryDate = null;
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product PO delivery date has been updated successfully!", ""));
    }

    public void updatePORelayedStatus(String username) {
        productPurchaseOrderSessionBean.updateProductPORelayedStatus(selectedProductPO);

        Customer customer = customerSessionBean.getCustomerByUsername(username);

        // Email Germany supplier for purchasing products
        EmailManager emailManager = new EmailManager();
        emailManager.emailGermanySupplierToPurchase(selectedProductPO.getProductPurchaseOrderID(), this.retrieveEmailProductQuotationDescriptionList(selectedProductPO.getProductPurchaseOrderID()));

        selectedProductPO = new ProductPurchaseOrder();
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product PO has been sent to supplier!", ""));
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
//            // SMS custmer for delivery date update
//            try {
//                SmsService smsService1 = new SmsService("2USsHuRw6nYOlkh4", "Yie0bbxSDekwTT1d");
//                smsService1.send("+6592706232", "HiYew: Your delivery details for purchase order #" + selectedProductPO.getProductPurchaseOrderCustomerID() + " have been updated! You may go to HiYew Customer Portal to view the updates.", "", "", "");
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new EJBException(e.getMessage());
//            }
        }

        selectedProductPO = new ProductPurchaseOrder();
        FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product PO has been updated successfully!", ""));
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
}
