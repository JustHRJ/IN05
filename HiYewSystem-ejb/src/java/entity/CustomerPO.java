package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class CustomerPO implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    private String poId; //follows quotation no
    private String customerRefPoNumber; //provided by customer in case they choose to have their own po number instead

    private Timestamp poDate;
    //private Timestamp expectedStartDate;
    //private Timestamp expectedEndDate;
    private Timestamp deliveryDate;
    //Assumptions: Not possible for customer to self-collect
    private String mailingAddr1; //street name
    private String mailingAddr2; //unit no
    private Double totalPrice;

    @OneToOne
    private Quotation quotation; //One-to-One Uni-Directional Relationship (page 201)
    @ManyToOne
    private Customer customer;

    public CustomerPO() {
        poDate = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public CustomerPO(String poId, String customerRefPoNumber , String mailingAddr1, String mailingAddr2, Double totalPrice, Quotation quotation, Customer customer) {
        this.poId = poId;
        this.poDate = new Timestamp(Calendar.getInstance().getTime().getTime());
        //this.expectedStartDate = expectedStartDate;
        //this.expectedEndDate = expectedEndDate;
        this.mailingAddr1 = mailingAddr1;
        this.mailingAddr2 = mailingAddr2;
        this.totalPrice = totalPrice;
        this.quotation = quotation;
        this.customer = customer;
        this.customerRefPoNumber = customerRefPoNumber;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }
    
    public String formatDate(Timestamp t) {

        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        if(t != null){
            return sd.format(t.getTime());
        }
        return "";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (poId != null ? poId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the poId fields are not set
        if (!(object instanceof CustomerPO)) {
            return false;
        }
        CustomerPO other = (CustomerPO) object;
        if ((this.poId == null && other.poId != null) || (this.poId != null && !this.poId.equals(other.poId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.customerPO[ id=" + poId + " ]";
    }

    /**
     * @return the poDate
     */
    public Timestamp getPoDate() {
        return poDate;
    }

    /**
     * @param poDate the poDate to set
     */
    public void setPoDate(Timestamp poDate) {
        this.poDate = poDate;
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
     * @return the quotation
     */
    public Quotation getQuotation() {
        return quotation;
    }

    /**
     * @param quotation the quotation to set
     */
    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
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
     * @return the customerRefPoNumber
     */
    public String getCustomerRefPoNumber() {
        return customerRefPoNumber;
    }

    /**
     * @param customerRefPoNumber the customerRefPoNumber to set
     */
    public void setCustomerRefPoNumber(String customerRefPoNumber) {
        this.customerRefPoNumber = customerRefPoNumber;
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

}
