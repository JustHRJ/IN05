package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ProductQuotation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @return the statusesArr
     */
    public static String[] getStatusesArr() {
        return statusesArr;
    }
    @Id
    private String productQuotationNo;
    private Timestamp date;
    private String status; // Relayed, Processed, Pending, Accepted, Rejected

    private final static String[] settledArr;
    private final static String[] unsettledArr;
    private final static String[] statusesArr;

    static {
        settledArr = new String[2];
        settledArr[0] = "Accepted";
        settledArr[1] = "Rejected";

        unsettledArr = new String[3];
        unsettledArr[0] = "Pending";
        unsettledArr[1] = "Processed";
        unsettledArr[2] = "Relayed";

        statusesArr = new String[5];
        statusesArr[0] = "Accepted";
        statusesArr[1] = "Pending";
        statusesArr[2] = "Processed";
        statusesArr[3] = "Rejected";
        statusesArr[4] = "Relayed";
    }

    @OneToMany(mappedBy = "productQuotation")
    private List<ProductQuotationDescription> productQuotationDescriptionList = new ArrayList<>();
    @ManyToOne
    private Customer customer = new Customer();

    public ProductQuotation() {
        status = "Pending";
        date = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public ProductQuotation(String productQuotationNo, String status, Customer customer) {
        this.productQuotationNo = productQuotationNo;
        this.date = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.status = status;
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getProductQuotationNo() != null ? getProductQuotationNo().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductQuotation)) {
            return false;
        } else {
            ProductQuotation other = (ProductQuotation) object;
            if ((this.getProductQuotationNo() == null && other.getProductQuotationNo() != null) || (this.getProductQuotationNo() != null && !this.productQuotationNo.equals(other.productQuotationNo))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductQuotation entity [ID =" + getProductQuotationNo() + "]";
    }

    public List<String> getFilterSettledArr() {
        return Arrays.asList(settledArr);
    }

    public List<String> getFilterUnsettledArr() {
        return Arrays.asList(unsettledArr);
    }

    public List<String> getFilterStatusesArr() {
        return Arrays.asList(statusesArr);
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
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Timestamp date) {
        this.date = date;
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
     * @return the productQuotationDescriptionList
     */
    public List<ProductQuotationDescription> getProductQuotationDescriptionList() {
        return productQuotationDescriptionList;
    }

    /**
     * @param productQuotationDescriptionList the
     * productQuotationDescriptionList to set
     */
    public void setProductQuotationDescriptionList(List<ProductQuotationDescription> productQuotationDescriptionList) {
        this.productQuotationDescriptionList = productQuotationDescriptionList;
    }

    public void addProductQuotationDescriptionList(ProductQuotationDescription productQuotationDesc) {
        this.productQuotationDescriptionList.add(productQuotationDesc);
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
}
