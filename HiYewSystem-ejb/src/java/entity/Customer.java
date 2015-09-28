package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import util.enumeration.CustomerStatusEnum;

@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String userName;
    private String pw;
    private String name;
    private String address1;
    private String address2;
    private String phone;
    private String email;
    private String postalCode;
    private String subscribeEmail;

    // private Integer active; // set 1 for existing customer;
    // @Enumerated(EnumType.STRING)
    private CustomerStatusEnum customerStatusEnum;

    @OneToMany(mappedBy = "customer")
    private List<Quotation> quotations = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<CustomerPO> customerPOs = new ArrayList<>();

    ///////////////////////////////////// PRODUCTS /////////////////////////////////////
    @OneToMany(mappedBy = "customer")
    private List<ProductQuotation> productQuotationList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<ProductPurchaseOrder> productPurchaseOrderList = new ArrayList<>();
    ///////////////////////////////////// PRODUCTS /////////////////////////////////////

    public Customer() {
        customerStatusEnum = CustomerStatusEnum.ACTIVE;
    }

    public Customer(String userName, String pw, String name, String address1, String address2, String phone, CustomerStatusEnum customerStatusEnum, List<Quotation> quotations, List<CustomerPO> customerPOs) {
        this.userName = userName;
        this.pw = pw;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.phone = phone;
        this.customerStatusEnum = customerStatusEnum;
        this.quotations = quotations;
        this.customerPOs = customerPOs;
    }

    /**
     * @return the subscribeEmail
     */
    public String getSubscribeEmail() {
        return subscribeEmail;
    }

    /**
     * @param subscribeEmail the subscribeEmail to set
     */
    public void setSubscribeEmail(String subscribeEmail) {
        this.subscribeEmail = subscribeEmail;
    }

    /**
     * @param subscribeEmail the subscribeEmail to set
     */
    public void setSubscribeEmail(String subscribeEmail) {
        this.subscribeEmail = subscribeEmail;
    }
    
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the pw
     */
    public String getPw() {
        return pw;
    }

    /**
     * @param pw the pw to set
     */
    public void setPw(String pw) {

        this.pw = pw;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ userName=" + userName + " ]";
    }

    /**
     * @return the customerStatusEnum
     */
    public CustomerStatusEnum getCustomerStatusEnum() {
        return customerStatusEnum;
    }

    /**
     * @param customerStatusEnum the customerStatusEnum to set
     */
    public void setCustomerStatusEnum(CustomerStatusEnum customerStatusEnum) {
        this.customerStatusEnum = customerStatusEnum;
    }

    /**
     * @return the quotations
     */
    public List<Quotation> getQuotations() {
        return quotations;
    }

    /**
     * @param quotations the quotations to set
     */
    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public void addQuotations(Quotation q) {
        this.quotations.add(q);
    }

    public void addCustomerPO(CustomerPO po) {
        this.customerPOs.add(po);
    }

    /**
     * @return the customerPOs
     */
    public List<CustomerPO> getCustomerPOs() {
        return customerPOs;
    }

    /**
     * @param customerPOs the customerPOs to set
     */
    public void setCustomerPOs(List<CustomerPO> customerPOs) {
        this.customerPOs = customerPOs;
    }

    /**
     * @return the productQuotationList
     */
    public List<ProductQuotation> getProductQuotationList() {
        return productQuotationList;
    }

    /**
     * @param productQuotationList the productQuotationList to set
     */
    public void setProductQuotationList(List<ProductQuotation> productQuotationList) {
        this.productQuotationList = productQuotationList;
    }

    /**
     * @return the productPurchaseOrderList
     */
    public List<ProductPurchaseOrder> getProductPurchaseOrderList() {
        return productPurchaseOrderList;
    }

    /**
     * @param productPurchaseOrderList the productPurchaseOrderList to set
     */
    public void setProductPurchaseOrderList(List<ProductPurchaseOrder> productPurchaseOrderList) {
        this.productPurchaseOrderList = productPurchaseOrderList;
    }

    public void addProductQuotationList(ProductQuotation productQuotation) {
        this.productQuotationList.add(productQuotation);
    }

    public void addProductPurchaseOrderList(ProductPurchaseOrder productPurchaseOrder) {
        this.productPurchaseOrderList.add(productPurchaseOrder);
    }

}
