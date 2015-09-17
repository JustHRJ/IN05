/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import util.enumeration.CustomerStatusEnum;

/**
 *
 * @author: Jit Cheong
 */
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private String userName;
    private String pw;
    private String name;
    private String address;
    private String phone;
    //private Integer active; // set 1 for existing customer;
    //@Enumerated(EnumType.STRING)
    private CustomerStatusEnum customerStatusEnum;
    
    @OneToMany(mappedBy="customer")
    private List <Quotation> quotations;

    public Customer() {
        customerStatusEnum = CustomerStatusEnum.ACTIVE;
    }

    public Customer(String userName, String pw, String name, String address, String phone, CustomerStatusEnum customerStatusEnum, List <Quotation> quotations) {
        this.userName = userName;
        this.pw = pw;
        this.name = name;
        this.address = address;
        this.phone = phone;
        //this.active = active;
        this.customerStatusEnum = customerStatusEnum;
        this.quotations = quotations;
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
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @return the rfqs
     */
    public List <Quotation> getQuotations() {
        return quotations;
    }

    /**
     * @param quotations the rfqs to set
     */
    public void setQuotations(List <Quotation> quotations) {
        this.quotations = quotations;
    }
    
    public void addQuotations(Quotation q){
        this.quotations.add(q);
    }

    
    
}
