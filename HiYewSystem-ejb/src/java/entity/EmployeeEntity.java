/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author JustHRJ
 */
@Entity
public class EmployeeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String employee_name;
    private String employee_passNumber;
    private String employee_address;
    private int number_of_leaves;
    private String employee_account_status; //admin, staff
    private Collection<LeaveEntity> leaveRecords = new ArrayList<LeaveEntity>();
    private Collection<PayrollEntity> payRecords = new ArrayList<PayrollEntity>();
    private String previousPosition;
    private String username;
    private String password;
    private Timestamp employee_passExpiry;
    private String employee_contact;
    private double employee_basic;
    private Timestamp employee_employedDate;
    private String addressPostal;
    private String unit;
    private String secretQuestion;
    private String secretAnswer;
    private String optional;
    private String account_status;
    private String emailAddress;
    private Boolean availability;
    private Collection<EmployeeClaimEntity> claimRecords = new ArrayList<EmployeeClaimEntity>();
    
    public EmployeeEntity() {
    }

    public EmployeeEntity(String employee_name, String employee_passNumber, String employee_address, int number_of_leaves, String employee_account_status, String previousPosition, String username, String password, Timestamp employee_passExpiry, String employee_contact, double employee_basic, Timestamp employee_employedDate, String addressPostal, String unit, String secretQuestion, String secretAnswer, String optional, String account_status, String emailAddress) {
        this.employee_name = employee_name;
        this.employee_passNumber = employee_passNumber;
        this.employee_address = employee_address;
        this.number_of_leaves = number_of_leaves;
        this.employee_account_status = employee_account_status;
        this.previousPosition = previousPosition;
        this.username = username;
        this.password = password;
        this.employee_passExpiry = employee_passExpiry;
        this.employee_contact = employee_contact;
        this.employee_basic = employee_basic;
        this.employee_employedDate = employee_employedDate;
        this.addressPostal = addressPostal;
        this.unit = unit;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
        this.optional = optional;
        this.account_status = account_status;
        this.emailAddress = emailAddress;
    }
    
    @OneToMany(cascade ={CascadeType.ALL}, mappedBy ="employee", orphanRemoval=true)
    public Collection<EmployeeClaimEntity> getEmployeeClaims(){
        return claimRecords;
    }
    
    public void setEmployeeClaims(Collection<EmployeeClaimEntity> claimRecords){
        this.claimRecords = claimRecords;
    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "employee")
    public Collection<PayrollEntity> getPayRecords() {
        return payRecords;
    }

    public void setPayRecords(Collection<PayrollEntity> payRecords) {
        this.payRecords = payRecords;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    public Collection<LeaveEntity> getLeaveRecords() {
        return leaveRecords;
    }

    public void setLeaveRecords(Collection<LeaveEntity> leaveRecords) {
        this.leaveRecords = leaveRecords;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EmployeeEntity[ id=" + id + " ]";
    }

    /**
     * @return the employee_name
     */
    public String getEmployee_name() {
        return employee_name;
    }

    /**
     * @param employee_name the employee_name to set
     */
    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    /**
     * @return the employee_passNumber
     */
    public String getEmployee_passNumber() {
        return employee_passNumber;
    }

    /**
     * @param employee_passNumber the employee_passNumber to set
     */
    public void setEmployee_passNumber(String employee_passNumber) {
        this.employee_passNumber = employee_passNumber;
    }

    /**
     * @return the employee_address
     */
    public String getEmployee_address() {
        return employee_address;
    }

    /**
     * @param employee_address the employee_address to set
     */
    public void setEmployee_address(String employee_address) {
        this.employee_address = employee_address;
    }

    /**
     * @return the number_of_leaves
     */
    public int getNumber_of_leaves() {
        return number_of_leaves;
    }

    /**
     * @param number_of_leaves the number_of_leaves to set
     */
    public void setNumber_of_leaves(int number_of_leaves) {
        this.number_of_leaves = number_of_leaves;
    }

    /**
     * @return the employee_account_status
     */
    public String getEmployee_account_status() {
        return employee_account_status;
    }

    /**
     * @param employee_account_status the employee_account_status to set
     */
    public void setEmployee_account_status(String employee_account_status) {
        this.employee_account_status = employee_account_status;
    }

    /**
     * @return the previousPosition
     */
    public String getPreviousPosition() {
        return previousPosition;
    }

    /**
     * @param previousPosition the previousPosition to set
     */
    public void setPreviousPosition(String previousPosition) {
        this.previousPosition = previousPosition;
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the employee_passExpiry
     */
    public Timestamp getEmployee_passExpiry() {
        return employee_passExpiry;
    }

    /**
     * @param employee_passExpiry the employee_passExpiry to set
     */
    public void setEmployee_passExpiry(Timestamp employee_passExpiry) {
        this.employee_passExpiry = employee_passExpiry;
    }

    /**
     * @return the employee_contact
     */
    public String getEmployee_contact() {
        return employee_contact;
    }

    /**
     * @param employee_contact the employee_contact to set
     */
    public void setEmployee_contact(String employee_contact) {
        this.employee_contact = employee_contact;
    }

    /**
     * @return the employee_basic
     */
    public double getEmployee_basic() {
        return employee_basic;
    }

    /**
     * @param employee_basic the employee_basic to set
     */
    public void setEmployee_basic(double employee_basic) {
        this.employee_basic = employee_basic;
    }

    /**
     * @return the employee_employedDate
     */
    public Timestamp getEmployee_employedDate() {
        return employee_employedDate;
    }

    /**
     * @param employee_employedDate the employee_employedDate to set
     */
    public void setEmployee_employedDate(Timestamp employee_employedDate) {
        this.employee_employedDate = employee_employedDate;
    }

    /**
     * @return the addressPostal
     */
    public String getAddressPostal() {
        return addressPostal;
    }

    /**
     * @param addressPostal the addressPostal to set
     */
    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the optional
     */
    public String getOptional() {
        return optional;
    }

    /**
     * @param optional the optional to set
     */
    public void setOptional(String optional) {
        this.optional = optional;
    }

    /**
     * @return the account_status
     */
    public String getAccount_status() {
        return account_status;
    }

    /**
     * @param account_status the account_status to set
     */
    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
     * @return the availability
     */
    public Boolean getAvailability() {
        return availability;
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

}
