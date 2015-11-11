package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ProductPurchaseOrder implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the statusesArr
     */
    public static String[] getStatusesArr() {
        return statusesArr;
    }

    @Id
    private String productPurchaseOrderID;

    private String productPurchaseOrderCustomerID;
    private Timestamp productPurchaseOrderDate;
    private Timestamp deliveryDate;
    private String mailingAddr1;
    private String mailingAddr2;
    private Double totalPrice;
    private String status; // Relayed, Processed, Pending, Accepted, Rejected

    private final static String[] settledArr;
    private final static String[] unsettledArr;
    private final static String[] statusesArr;

    static {
        settledArr = new String[1];
        settledArr[0] = "Delivered";

        unsettledArr = new String[3];
        unsettledArr[0] = "Pending";
        unsettledArr[1] = "Processed";
        unsettledArr[2] = "Relayed";

        statusesArr = new String[4];
        statusesArr[0] = "Delivered";
        statusesArr[1] = "Pending";
        statusesArr[2] = "Processed";
        statusesArr[3] = "Relayed";
    }

    @OneToOne
    private ProductQuotation productQuotation;
    @ManyToOne
    private Customer customer;

    public ProductPurchaseOrder() {
        productPurchaseOrderDate = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public ProductPurchaseOrder(String productPurchaseOrderID, String productPurchaseOrderCustomerID, Timestamp deliveryDate, String mailingAddr1, String mailingAddr2, Double totalPrice, ProductQuotation productQuotation, Customer customer) {
        this.productPurchaseOrderID = productPurchaseOrderID;
        this.productPurchaseOrderCustomerID = productPurchaseOrderCustomerID;
        this.productPurchaseOrderDate = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.deliveryDate = deliveryDate;
        this.mailingAddr1 = mailingAddr1;
        this.mailingAddr2 = mailingAddr2;
        this.totalPrice = totalPrice;
        this.productQuotation = productQuotation;
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getProductPurchaseOrderID() != null ? getProductPurchaseOrderID().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the poId fields are not set
        if (!(object instanceof ProductPurchaseOrder)) {
            return false;
        }
        ProductPurchaseOrder other = (ProductPurchaseOrder) object;
        if ((this.getProductPurchaseOrderID() == null && other.getProductPurchaseOrderID() != null) || (this.getProductPurchaseOrderID() != null && !this.productPurchaseOrderID.equals(other.productPurchaseOrderID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductPurchaseOrder entity [ID=" + getProductPurchaseOrderID() + "]";
    }
    
    public List<String> getFilterSettledArr() {
        return Arrays.asList(settledArr);
    }
     
    public List<String> getFilterUnsettledArr() {
        return Arrays.asList(unsettledArr);
    }
     
    public List<String> getFilterStatusesArr() {
        return Arrays.asList(getStatusesArr());
    }

    /**
     * @return the productPurchaseOrderID
     */
    public String getProductPurchaseOrderID() {
        return productPurchaseOrderID;
    }

    /**
     * @param productPurchaseOrderID the productPurchaseOrderID to set
     */
    public void setProductPurchaseOrderID(String productPurchaseOrderID) {
        this.productPurchaseOrderID = productPurchaseOrderID;
    }

    /**
     * @return the productPurchaseOrderDate
     */
    public Timestamp getProductPurchaseOrderDate() {
        return productPurchaseOrderDate;
    }

    /**
     * @param productPurchaseOrderDate the productPurchaseOrderDate to set
     */
    public void setProductPurchaseOrderDate(Timestamp productPurchaseOrderDate) {
        this.productPurchaseOrderDate = productPurchaseOrderDate;
    }

    /**
     * @return the deliveryDate
     */
    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
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
     * @return the totalPrice
     */
    public Double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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
     * @return the productPurchaseOrderCustomerID
     */
    public String getProductPurchaseOrderCustomerID() {
        return productPurchaseOrderCustomerID;
    }

    /**
     * @param productPurchaseOrderCustomerID the productPurchaseOrderCustomerID
     * to set
     */
    public void setProductPurchaseOrderCustomerID(String productPurchaseOrderCustomerID) {
        this.productPurchaseOrderCustomerID = productPurchaseOrderCustomerID;
    }

    /**
     * @return the settledArr
     */
    public static String[] getSettledArr() {
        return settledArr;
    }

    /**
     * @return the unsettledArr
     */
    public static String[] getUnsettledArr() {
        return unsettledArr;
    }
}
