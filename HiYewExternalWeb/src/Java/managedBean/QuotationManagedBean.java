/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.Quotation;
import entity.QuotationDescription;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.QuotationSessionBeanLocal;

/**
 *
 * @author: Jit Cheong
 */
@Named(value = "quotationManagedBean")
@ViewScoped
public class QuotationManagedBean implements Serializable {

    @EJB
    private QuotationSessionBeanLocal quotationSessionBean;

    private String username = "";
    private String date = "";
    private String quotationNo = "";
    
    private ArrayList <QuotationDescription> cacheList;
    private Quotation newQuotation;
    private QuotationDescription newQuotationDesc;

    /**
     * Creates a new instance of QuotationManagedBean
     */
    public QuotationManagedBean() {
        newQuotation = new Quotation();
        newQuotationDesc = new QuotationDescription();
        cacheList = new ArrayList <>();
       
    }

    @PostConstruct
    public void init() {

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user") != null) {
            this.setUsername(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user").toString());
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("qNo") != null) {
            quotationNo = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("qNo").toString();
        }else{
            quotationNo = quotationSessionBean.getQuotationNo(username);
        }
        
        date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        System.out.println(date);
    }
    
    public void createQuotation() {
        quotationSessionBean.createQuotation(newQuotation);
    }
    
    public void addToCacheList(ActionEvent event){
        newQuotation.setQuotationNo(quotationNo);
        cacheList.add(newQuotationDesc);
        System.out.println("items: " + cacheList.size());
        System.out.println("items: " + cacheList.get(0).getItemDesc() + " " + cacheList.get(0).getQty());
        newQuotationDesc = new QuotationDescription();
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
     * @return the newQuotation
     */
    public Quotation getNewQuotation() {
        return newQuotation;
    }

    /**
     * @param newQuotation the newQuotation to set
     */
    public void setNewQuotation(Quotation newQuotation) {
        this.newQuotation = newQuotation;
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
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the cacheList
     */
    public ArrayList<QuotationDescription> getCacheList() {
        return cacheList;
    }

    /**
     * @param cacheList the cacheList to set
     */
    public void setCacheList(ArrayList<QuotationDescription> cacheList) {
        this.cacheList = cacheList;
    }

    /**
     * @return the newQuotationDesc
     */
    public QuotationDescription getNewQuotationDesc() {
        return newQuotationDesc;
    }

    /**
     * @param newQuotationDesc the newQuotationDesc to set
     */
    public void setNewQuotationDesc(QuotationDescription newQuotationDesc) {
        this.newQuotationDesc = newQuotationDesc;
    }

}
