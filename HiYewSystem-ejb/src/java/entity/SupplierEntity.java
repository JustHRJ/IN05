/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

//import util.enumeration.SupplierStatusEnum;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private boolean subscribeEmail_qPriceUpdates;
    private boolean subscribeSMS_qPriceUpdates;
    private boolean subscribeEmail_poDeliveryUpdates;
    private boolean subscribeSMS_poDeliveryUpdates;
    private String secretQuestion;
    private String secretAnswer;

    //private Integer active; // set 1 for existing supplier;
    //@Enumerated(EnumType.STRING)
    private SupplierStatusEnum supplierStatusEnum;
    
    //@OneToMany(mappedBy = "supplier")
    //private List<SupplierPurchaseOrder> purcOrder = new ArrayList<>();
    
    @OneToMany(mappedBy = "supplier")
    private Set<SuppliedFillerEntity> suppliedItems = new HashSet<SuppliedFillerEntity>();
    
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
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.userName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SupplierEntity other = (SupplierEntity) obj;
        if (!Objects.equals(this.userName, other.userName)) {
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
     * @return the subscribeEmail_qPriceUpdates
     */
    public boolean isSubscribeEmail_qPriceUpdates() {
        return subscribeEmail_qPriceUpdates;
    }

    /**
     * @param subscribeEmail_qPriceUpdates the subscribeEmail_qPriceUpdates to set
     */
    public void setSubscribeEmail_qPriceUpdates(boolean subscribeEmail_qPriceUpdates) {
        this.subscribeEmail_qPriceUpdates = subscribeEmail_qPriceUpdates;
    }

    /**
     * @return the subscribeSMS_qPriceUpdates
     */
    public boolean isSubscribeSMS_qPriceUpdates() {
        return subscribeSMS_qPriceUpdates;
    }

    /**
     * @param subscribeSMS_qPriceUpdates the subscribeSMS_qPriceUpdates to set
     */
    public void setSubscribeSMS_qPriceUpdates(boolean subscribeSMS_qPriceUpdates) {
        this.subscribeSMS_qPriceUpdates = subscribeSMS_qPriceUpdates;
    }

    /**
     * @return the subscribeEmail_poDeliveryUpdates
     */
    public boolean isSubscribeEmail_poDeliveryUpdates() {
        return subscribeEmail_poDeliveryUpdates;
    }

    /**
     * @param subscribeEmail_poDeliveryUpdates the subscribeEmail_poDeliveryUpdates to set
     */
    public void setSubscribeEmail_poDeliveryUpdates(boolean subscribeEmail_poDeliveryUpdates) {
        this.subscribeEmail_poDeliveryUpdates = subscribeEmail_poDeliveryUpdates;
    }

    /**
     * @return the subscribeSMS_poDeliveryUpdates
     */
    public boolean isSubscribeSMS_poDeliveryUpdates() {
        return subscribeSMS_poDeliveryUpdates;
    }

    /**
     * @param subscribeSMS_poDeliveryUpdates the subscribeSMS_poDeliveryUpdates to set
     */
    public void setSubscribeSMS_poDeliveryUpdates(boolean subscribeSMS_poDeliveryUpdates) {
        this.subscribeSMS_poDeliveryUpdates = subscribeSMS_poDeliveryUpdates;
    }

    /**
     * @return the suppliedItems
     */
    public Set<SuppliedFillerEntity> getSuppliedItems() {
        return suppliedItems;
    }

    /**
     * @param suppliedItems the suppliedItems to set
     */
    public void setSuppliedItems(Set<SuppliedFillerEntity> suppliedItems) {
        this.suppliedItems = suppliedItems;
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
