/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.CustomerPO;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.stateless.CustomerPOSessionBeanLocal;

/**
 *
 * @author JitCheong
 */
@Named(value = "adminPOManagedBean")
@ViewScoped
public class AdminCustomerPOManagedBean implements Serializable{

    @EJB
    private CustomerPOSessionBeanLocal customerPOSessionBean;

    private Integer year = Calendar.getInstance().get(Calendar.YEAR); //by default, get current year product orders
    private Map<String, String> years;
    private Date deliveryDate;

    private ArrayList<CustomerPO> customerPOs;

    /**
     * Creates a new instance of AdminCustomerPOManagedBean
     */
    public AdminCustomerPOManagedBean() {

        years = new HashMap<>();
        customerPOs = new ArrayList<>();
    }

    @PostConstruct
    public void init() {

        years.put("2014", "2014");
        years.put("2015", "2015");
        customerPOs = new ArrayList<>(customerPOSessionBean.getCustomerPOsByYear(year));
    }

    public void filterByYear() {
        System.out.println(year);
        //System.out.println("value change");
        customerPOs = new ArrayList<>(customerPOSessionBean.getCustomerPOsByYear(year));
    }

    public String formatDate(Timestamp t) {
        if (t != null) {
            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
            return sd.format(t.getTime());
        }
        return "";
    }
    
    
    public Timestamp formatDateToTimestamp(Date date){
        Timestamp t = new Timestamp(date.getTime());
        return t;
    }

    public void onEditRow(RowEditEvent event) {
        CustomerPO p = (CustomerPO) event.getObject();//gives me unedited value
        p.setDeliveryDate(formatDateToTimestamp(deliveryDate));
        customerPOSessionBean.conductMerge(p);
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the years
     */
    public Map<String, String> getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(Map<String, String> years) {
        this.years = years;
    }

    /**
     * @return the customerPOs
     */
    public ArrayList<CustomerPO> getCustomerPOs() {
        return customerPOs;
    }

    /**
     * @param customerPOs the customerPOs to set
     */
    public void setCustomerPOs(ArrayList<CustomerPO> customerPOs) {
        this.customerPOs = customerPOs;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

}
