/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean.PS;

import entity.ProcurementBidEntity;
import entity.StorageInfoEntity;
import entity.SupplierEntity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.ProcurementSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "viewPBRequestManagedBean")
@ViewScoped
public class ViewPBRequestManagedBean implements Serializable{
    @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;
    
    private List<ProcurementBidEntity> pbList;
    private List<ProcurementBidEntity> filteredPBs;
    private ProcurementBidEntity selectedPB;
    private SupplierEntity supplier;
    private String username = ""; // when log on taken from session
    private double inputPrice;
    
    /**
     * Creates a new instance of ViewPBRequestManagedBean
     */
    public ViewPBRequestManagedBean() {
        pbList = new ArrayList<>();
    }
    
    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            this.setUsername(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString());

            setSupplier(procurementSessionBean.getSupplierByUsername(this.getUsername()));
            System.out.println("@Supplier: Username is " + getUsername());
        }
        pbList = procurementSessionBean.getBidsOpenForSupplier(supplier.getCompanyName());   
    }

    /**
     * @return the pbList
     */
    public List<ProcurementBidEntity> getPbList() {
        return pbList;
    }

    /**
     * @param pbList the pbList to set
     */
    public void setPbList(List<ProcurementBidEntity> pbList) {
        this.pbList = pbList;
    }

    /**
     * @return the supplier
     */
    public SupplierEntity getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
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
    
    public String formatDate(Timestamp date) {
        if (date != null) {
            Date date1 = new Date(date.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(date1);
        } else {
            return "";
        }
    }

    /**
     * @return the filteredPBs
     */
    public List<ProcurementBidEntity> getFilteredPBs() {
        return filteredPBs;
    }

    /**
     * @param filteredPBs the filteredPBs to set
     */
    public void setFilteredPBs(List<ProcurementBidEntity> filteredPBs) {
        this.filteredPBs = filteredPBs;
    }
    
    public void openDialog() {
        System.out.println("here at openDialog" + selectedPB.getId());

    }
    
     public String updatePrice() {
         
         procurementSessionBean.updateQuotedPrice(selectedPB, inputPrice);
         procurementSessionBean.updateAcceptStatus(selectedPB, "Quoted");
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Price Updated!"));     
         pbList = procurementSessionBean.getBidsOpenForSupplier(supplier.getCompanyName());
         return "s-view-PB-request?faces-redirect=true";
    }

    /**
     * @return the selectedPB
     */
    public ProcurementBidEntity getSelectedPB() {
        return selectedPB;
    }

    /**
     * @param selectedPB the selectedPB to set
     */
    public void setSelectedPB(ProcurementBidEntity selectedPB) {
        this.selectedPB = selectedPB;
    }

    /**
     * @return the inputPrice
     */
    public double getInputPrice() {
        return inputPrice;
    }

    /**
     * @param inputPrice the inputPrice to set
     */
    public void setInputPrice(double inputPrice) {
        this.inputPrice = inputPrice;
    }
    
    public String acceptedOrRejected(ProcurementBidEntity pb){
        if(pb.getStatus().equalsIgnoreCase("Closed")){
            return "true";
        }
        if((pb.getIfAccept().equalsIgnoreCase("Accepted"))||(pb.getIfAccept().equalsIgnoreCase("Rejected"))){
            return "true";
        }
        else{
            return "false";
        }
    }
    
}
