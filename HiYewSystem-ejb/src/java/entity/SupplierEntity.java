/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

//import util.enumeration.SupplierStatusEnum;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import util.enumeration.SupplierStatusEnum;
//import javax.persistence.OneToMany;

/**
 *
 * @author Randy Ng
 */
@Entity
public class SupplierEntity implements Serializable {
    
    private static long serialVersionUID = 1L;
 
    @Id
    private String userName;
    private String pw;
    //private String name;
    private String address1;
    private String address2;
    private String phone;
    private String email;
    private String postalCode;
    private String companyName;
    private boolean subscribeEmail;
    private String secretQuestion;
    private String secretAnswer;

    //private Integer active; // set 1 for existing supplier;
    //@Enumerated(EnumType.STRING)
    private SupplierStatusEnum supplierStatusEnum;
    
    //@OneToMany(mappedBy = "supplier")
    //private List<SupplierPurchaseOrder> purcOrder = new ArrayList<>();
    
    public SupplierEntity() {
        supplierStatusEnum = SupplierStatusEnum.ACTIVE;
    }
    
    public SupplierEntity(String userName, String pw, String address1, String address2, String phone, String email, String postalCode, SupplierStatusEnum supplierStatusEnum, String companyName) /*, List<SupplierPurchaseOrder> purcOrder)*/ {
        this.userName = userName;
        this.pw = pw;
        //this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.phone = phone;
        this.email = email;
        this.postalCode = postalCode;
        this.companyName = companyName;
        this.supplierStatusEnum = supplierStatusEnum;
        //this.purcOrder = purcOrder;
    }
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getUserName() != null ? getUserName().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SupplierEntity)) {
            return false;
        }
        SupplierEntity other = (SupplierEntity) object;
        if ((this.getUserName() == null && other.getUserName() != null) || (this.getUserName() != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SupplierEntitiy[ userName=" + getUserName() + " ]";
    }

    /**
     * @return the customerStatusEnum
     */
    //public SupplierStatusEnum getSupplierStatusEnum() {
    //    return supplierStatusEnum;
    //}

    /**
     * @param supplierStatusEnum the supplierStatusEnum to set
     */
    //public void setSupplierStatusEnum(SupplierStatusEnum supplierStatusEnum) {
    //    this.supplierStatusEnum = supplierStatusEnum;
    //}

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

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean getSubscribeEmail() {
        return subscribeEmail;
    }

    /**
     * @param subscribeEmail the subscribeEmail to set
     */
    public void setSubscribeEmail(boolean subscribeEmail) {
        this.subscribeEmail = subscribeEmail;
    }

    /**
     * @return the supplierStatusEnum
     */
    public SupplierStatusEnum getSupplierStatusEnum() {
        return supplierStatusEnum;
    }

    /**
     * @param supplierStatusEnum the supplierStatusEnum to set
     */
    public void setSupplierStatusEnum(SupplierStatusEnum supplierStatusEnum) {
        this.supplierStatusEnum = supplierStatusEnum;
    }

    /**
     * @return the secretQuestion
     */
    public String getSecretQuestion() {
        return secretQuestion;
    }

    /**
     * @param secretQuestion the secretQuestion to set
     */
    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    /**
     * @return the secretAnswer
     */
    public String getSecretAnswer() {
        return secretAnswer;
    }

    /**
     * @param secretAnswer the secretAnswer to set
     */
    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }
    /**
     * @return the purcOrder
     */
    //public List<SupplierPurchaseOrder> getPurcOrder() {
    //    return purcOrder;
    //}

    /**
     * @param purcOrder the purcOrder to set
     */
    //public void setPurcOrder(List<SupplierPurchaseOrder> purcOrder) {
    //    this.purcOrder = purcOrder;
    //}

    
}
