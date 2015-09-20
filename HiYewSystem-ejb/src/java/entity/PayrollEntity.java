/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Tracykkh
 */
@Entity
public class PayrollEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String months;
    private double bonus;
    private Timestamp payment;
    private String status;
    private Timestamp last_payment;
    private EmployeeEntity employee;

    public Long getId() {
        return id;
    }
    
    @ManyToOne
    public EmployeeEntity getEmployee(){
        return employee;
    }
    public void setEmployee(EmployeeEntity employee){
        this.employee = employee;
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
        if (!(object instanceof PayrollEntity)) {
            return false;
        }
        PayrollEntity other = (PayrollEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "stateful.session.payrollEntity[ id=" + id + " ]";
    }

    /**
     * @return the Month
     */
    public String getMonth() {
        return months;
    }

    /**
     * @param Month the Month to set
     */
    public void setMonth(String months) {
        this.months = months;
    }

    /**
     * @return the bonus
     */
    public double getBonus() {
        return bonus;
    }

    /**
     * @param bonus the bonus to set
     */
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    /**
     * @return the payment
     */
    public Timestamp getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(Timestamp payment) {
        this.payment = payment;
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
     * @return the last_payment
     */
    public Timestamp getLast_payment() {
        return last_payment;
    }

    /**
     * @param last_payment the last_payment to set
     */
    public void setLast_payment(Timestamp last_payment) {
        this.last_payment = last_payment;
    }
    
}
