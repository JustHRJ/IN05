package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String quotationNo;
    private Timestamp date;
    private String termsOfPayment = "30"; //30, 60, 90 days
    private String status; //Processed, Pending, Accepted, Rejected
    
    
    

    @OneToMany(mappedBy = "quotation")
    private List<QuotationDescription> quotationDescriptions = new ArrayList<>();
    
    @ManyToOne
    private Customer customer = new Customer();

    public Quotation() {
        //customer = new Customer();
        //quotationDescription = new QuotationDescription();
        status = "Pending";
        date = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public Quotation(String termsOfPayment, String quotationNo, Customer customer, QuotationDescription qd, String status) {

        this.date = new Timestamp(Calendar.getInstance().getTime().getTime());
        this.termsOfPayment = termsOfPayment;
        this.quotationNo = quotationNo;
        this.customer = customer;
        this.status = status;
        //this.quotationDescription = qd;
    }

    /**
     * @return the quotationNo
     */
    public String getQuotationNo() {
        return quotationNo;
    }

    /**
     * @param quotationNo the quotationNo to set
     */
    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
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
     * @return the termsOfPayment
     */
    public String getTermsOfPayment() {
        return termsOfPayment;
    }

    /**
     * @param termsOfPayment the termsOfPayment to set
     */
    public void setTermsOfPayment(String termsOfPayment) {
        this.termsOfPayment = termsOfPayment;
    }

    /**
     * @return the quotationDescriptions
     */
    public List<QuotationDescription> getQuotationDescriptions() {
        return quotationDescriptions;
    }

    /**
     * @param quotationDescriptions the quotationDescriptions to set
     */
    public void setQuotationDescriptions(List<QuotationDescription> quotationDescriptions) {
        this.quotationDescriptions = quotationDescriptions;
    }

    public void addQuotationDescriptions(QuotationDescription qd) {
        this.quotationDescriptions.add(qd);
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getQuotationNo() != null ? getQuotationNo().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quotation)) {
            return false;
        }
        Quotation other = (Quotation) object;
        if ((this.getQuotationNo() == null && other.getQuotationNo() != null) || (this.getQuotationNo() != null && !this.quotationNo.equals(other.quotationNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.quotation[ id=" + getQuotationNo() + " ]";
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

}
