package managedBean;

import com.itextpdf.text.DocumentException;
import entity.Customer;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import session.stateless.CustomerSessionBeanLocal;
import session.stateless.ProductPurchaseOrderSessionBeanLocal;
import session.stateless.ProductQuotationSessionBeanLocal;

@Named(value = "productPurchaseOrderManagedBean")
@ViewScoped
public class ProductPurchaseOrderManagedBean implements Serializable {

    @EJB
    private ProductPurchaseOrderSessionBeanLocal productPurchaseOrderSessionBean;
    @EJB
    private ProductQuotationSessionBeanLocal productQuotationSessionBean;
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private ProductPurchaseOrder newPurchaseOrder;
    private ProductQuotation productQuotation;

    private String username = "";

    // Attributes binded to view
    private String purchaseOrderNo = "";
    private String purchaseOrderCustomerNo = "";
    private String attention = "";
    private String orderDate = "";
    private String deliveryDate = "";
    private String totalPrice = "";
    private String mailingAddr1 = "";
    private String mailingAddr2 = "";

    private Double totalQuotedPrice = 0.0;
    private Integer totalQuantity = 0;

    private String description;
    private Double total = 0.0;
    private Timestamp deliveryDateTimestamp;

    private ArrayList<ProductPurchaseOrder> receivedProductPurchaseOrderList;
    private ArrayList<ProductQuotationDescription> displayProductQuotationDescriptionList;
    private ProductQuotation selectedProductionQuotation;

    public ProductPurchaseOrderManagedBean() {
        newPurchaseOrder = new ProductPurchaseOrder();
        selectedProductionQuotation = new ProductQuotation();
        receivedProductPurchaseOrderList = new ArrayList<>();
        displayProductQuotationDescriptionList = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            setUsername(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString());
            //System.out.println("ProductPurchaseOrderManagedBean.java: Username is " + getUsername());
        }
        setOrderDate(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        setDescription("");
        setReceivedProductPurchaseOrderList(new ArrayList<>(productPurchaseOrderSessionBean.receivedProductPurchaseOrder(getUsername())));
        //System.out.println("receivedProductPurchaseOrderList.size() ==== " + getReceivedProductPurchaseOrderList().size());
    }

    public List<String> getFilterStatusesArr() {
        return newPurchaseOrder.getFilterStatusesArr();
    }

    public String formatPrice(Double input) {
        DecimalFormat df = new DecimalFormat("0.00");
        //System.out.println("formatPrice() ===== " + df.format(input));
        return df.format(input);
    }

    public void receivedProductPurchaseOrderList() {
        //System.out.println("ProductPurchaseOrderManagedBean.java receivedProductPurchaseOrderList() ===== " + username);

        receivedProductPurchaseOrderList = new ArrayList<>(productPurchaseOrderSessionBean.receivedProductPurchaseOrder(getUsername()));

        FacesContext.getCurrentInstance().addMessage("poMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Current list of purchase order(s) are up to date.", ""));
    }

    public void retrieveProductQuotationDescriptionList(String purchaseOrderNo) {
        //System.out.println("ProductPurchaseOrderManagedBean.java retrieveProductQuotationDescriptionList() purchaseOrderNo ===== " + purchaseOrderNo);

        setDisplayProductQuotationDescriptionList(new ArrayList<>(productPurchaseOrderSessionBean.retrieveProductQuotationDescriptionList(purchaseOrderNo)));
    }

    public void createPurchaseOrder() throws IOException, DocumentException {
        selectedProductionQuotation.setStatus("Accepted");
        productQuotationSessionBean.conductMerge(selectedProductionQuotation);

        newPurchaseOrder.setProductPurchaseOrderCustomerID(this.purchaseOrderCustomerNo);
        newPurchaseOrder.setProductPurchaseOrderID(selectedProductionQuotation.getProductQuotationNo());
        newPurchaseOrder.setMailingAddr1(this.mailingAddr1);
        newPurchaseOrder.setMailingAddr2(this.mailingAddr2);
        newPurchaseOrder.setDeliveryDate(getDeliveryDateTimestamp());
        newPurchaseOrder.setTotalPrice(getTotal());
        newPurchaseOrder.setCustomer(selectedProductionQuotation.getCustomer());
        newPurchaseOrder.setProductQuotation(selectedProductionQuotation);
        newPurchaseOrder.setStatus("Pending");

        //System.out.println("before this.purchaseOrderCustomerNo ===== " + this.purchaseOrderCustomerNo);
        //System.out.println("before this.mailingAddr1 ===== " + this.mailingAddr1);
        //System.out.println("before this.mailingAddr2 ===== " + this.mailingAddr2);
        //System.out.println("before newPurchaseOrder.getMailingAddr1() ===== " + newPurchaseOrder.getMailingAddr1());
        //System.out.println("before newPurchaseOrder.getMailingAddr2() ===== " + newPurchaseOrder.getMailingAddr2());
        //System.out.println("before username ===== " + username);
        if ((newPurchaseOrder.getMailingAddr1() == null && newPurchaseOrder.getMailingAddr2() == null) || (newPurchaseOrder.getMailingAddr1().equals("") && newPurchaseOrder.getMailingAddr2().equals(""))) {
            Customer c = customerSessionBean.findCustomer(username);
            if (c != null) {
                newPurchaseOrder.setMailingAddr1(c.getAddress1());
                newPurchaseOrder.setMailingAddr2(c.getAddress2());

                //System.out.println("insde get exiting address  ===== " + c.getAddress1());
                //System.out.println("inside get exiting address ===== " + c.getAddress2());
            }
        }

        //System.out.println("after newPurchaseOrder.getMailingAddr1() ===== " + newPurchaseOrder.getMailingAddr1());
        //System.out.println("after newPurchaseOrder.getMailingAddr2() ===== " + newPurchaseOrder.getMailingAddr2());
        // persist
        productPurchaseOrderSessionBean.createProductPurchaseOrder(newPurchaseOrder);

        // add into product purchase order collection in persistence context
        customerSessionBean.addProductPurchaseOrder(selectedProductionQuotation.getCustomer().getUserName(), newPurchaseOrder);
        this.purchaseOrderCustomerNo = "";
        FacesContext.getCurrentInstance().addMessage("qMsg", new FacesMessage(FacesMessage.SEVERITY_INFO, "You have sent a Purchase Order No. " + selectedProductionQuotation.getProductQuotationNo(), ""));

        // generate PDF
        //FileDownloadView fdv = new FileDownloadView();
        //fdv.getFile(newPurchaseOrder, selectedProductionQuotation);
        // reinitialise
        setNewPurchaseOrder(new ProductPurchaseOrder());
    }

    public void generatePurchaseOrder(ProductQuotation productQuotation) {
        //System.out.println("Customer sent product purchase order!" + productQuotation.getProductQuotationDescriptionList().size());
        setPurchaseOrderNo(productQuotation.getProductQuotationNo());
        setAttention(productQuotation.getCustomer().getName());
        selectedProductionQuotation = productQuotation;
        description = "";
        total = 0.0;
        for (ProductQuotationDescription pqd : productQuotation.getProductQuotationDescriptionList()) {
            System.out.println("description === " + description);
            description = description + pqd.getProductQuotationDescNo().toString() + ". " + pqd.getItemName() + " - SGD " + String.format("%.2f", pqd.getQuotedPrice()) + " x " + pqd.getQuantity() + "" + "\r\n";
            // compute total price
            total = (Double) (getTotal() + (pqd.getQuantity() * pqd.getQuotedPrice()));
        }
        description = description + "\n* All price(s) stated are price per unit.\n* Final price is including delivery charge.\n* Paid orders are processed and dispatched from\n&nbsp;&nbsp;&nbsp;our warehouse within 1 to 3 working days.";

        setTotalPrice(String.format("%.2f", getTotal()));
        showDialog();
    }

    public String formatDate(Timestamp t) {
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        return sd.format(t.getTime());
    }

    public void showDialog() {
        //System.out.println("Show Dialog - AFTER Customer sent product purchase order!");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar').show();");
    }

    /**
     * @return the newPurchaseOrder
     */
    public ProductPurchaseOrder getNewPurchaseOrder() {
        return newPurchaseOrder;
    }

    /**
     * @param newPurchaseOrder the newPurchaseOrder to set
     */
    public void setNewPurchaseOrder(ProductPurchaseOrder newPurchaseOrder) {
        this.newPurchaseOrder = newPurchaseOrder;
    }

    /**
     * @return the productQuotation
     */
    public ProductQuotation getProductQuotation() {
        return productQuotation;
    }

    /**
     * @param productQuotation the productQuotation to set
     */
    public void setProductQuotation(ProductQuotation productQuotation) {
        this.productQuotation = productQuotation;
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
     * @return the purchaseOrderNo
     */
    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    /**
     * @param purchaseOrderNo the purchaseOrderNo to set
     */
    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    /**
     * @return the attention
     */
    public String getAttention() {
        return attention;
    }

    /**
     * @param attention the attention to set
     */
    public void setAttention(String attention) {
        this.attention = attention;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the deliveryDate
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return the totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * @return the deliveryDateTimestamp
     */
    public Timestamp getDeliveryDateTimestamp() {
        return deliveryDateTimestamp;
    }

    /**
     * @param deliveryDateTimestamp the deliveryDateTimestamp to set
     */
    public void setDeliveryDateTimestamp(Timestamp deliveryDateTimestamp) {
        this.deliveryDateTimestamp = deliveryDateTimestamp;
    }

    /**
     * @return the receivedProductPurchaseOrderList
     */
    public ArrayList<ProductPurchaseOrder> getReceivedProductPurchaseOrderList() {
        return receivedProductPurchaseOrderList;
    }

    /**
     * @param receivedProductPurchaseOrderList the
     * receivedProductPurchaseOrderList to set
     */
    public void setReceivedProductPurchaseOrderList(ArrayList<ProductPurchaseOrder> receivedProductPurchaseOrderList) {
        this.receivedProductPurchaseOrderList = receivedProductPurchaseOrderList;
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
     * @return the mailingAddr1
     */
    public String getMailingAddr1() {
        return mailingAddr1;
    }

    /**
     * @param mailingAddr1 the mailingAddr1 to set
     */
    public void setMailingAddr1(String mailingAddr1) {
        this.mailingAddr1 = mailingAddr1;
    }

    /**
     * @return the mailingAddr2
     */
    public String getMailingAddr2() {
        return mailingAddr2;
    }

    /**
     * @param mailingAddr2 the mailingAddr2 to set
     */
    public void setMailingAddr2(String mailingAddr2) {
        this.mailingAddr2 = mailingAddr2;
    }

    /**
     * @return the purchaseOrderCustomerNo
     */
    public String getPurchaseOrderCustomerNo() {
        return purchaseOrderCustomerNo;
    }

    /**
     * @param purchaseOrderCustomerNo the purchaseOrderCustomerNo to set
     */
    public void setPurchaseOrderCustomerNo(String purchaseOrderCustomerNo) {
        this.purchaseOrderCustomerNo = purchaseOrderCustomerNo;
    }

    /**
     * @return the selectedProductionQuotation
     */
    public ProductQuotation getSelectedProductionQuotation() {
        return selectedProductionQuotation;
    }

    /**
     * @param selectedProductionQuotation the selectedProductionQuotation to set
     */
    public void setSelectedProductionQuotation(ProductQuotation selectedProductionQuotation) {
        this.selectedProductionQuotation = selectedProductionQuotation;
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
