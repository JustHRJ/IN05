/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean.PS;

import entity.FillerEntity;
import entity.SuppliedFillerEntity;
import entity.SupplierEntity;
import java.io.Serializable;
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
@Named(value = "uploadNewItemManagedBean")
@ViewScoped
public class UploadNewItemManagedBean implements Serializable{
    @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;
    
    private SuppliedFillerEntity newFiller;
     private SupplierEntity supplier;
    private String username = ""; // when log on taken from session
    
    /**
     * Creates a new instance of UploadNewItemManagedBean
     */
    public UploadNewItemManagedBean() {
        newFiller = new SuppliedFillerEntity();
    }
    
     @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            this.setUsername(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString());

            setSupplier(procurementSessionBean.getSupplierByUsername(this.getUsername()));
            System.out.println("@Supplier: Username is " + getUsername());
        }  
    }

    public String uploadFiller() {
        System.out.println("Here");
        if ((procurementSessionBean.getExistingSuppliedFiller(supplier.getCompanyName()+"-"+newFiller.getFillerCode())) != null) {
            System.out.println("Exisitng Filler Code");
            FacesContext.getCurrentInstance().addMessage("formMain:fillerCode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to add filler! Existing Filler Code", "Unable to add filler! Existing Filler Code"));
            return "";
        } else {
            String fillerCodeStr=supplier.getCompanyName()+"-"+newFiller.getFillerCode();
            newFiller.setFillerCode(fillerCodeStr);
            newFiller.setSupplier(supplier);
            if(newFiller.getDescription().length()==0){
                newFiller.setDescription("No Description Available");
            }
            procurementSessionBean.uploadSuppliedItem(newFiller);
            
            newFiller = new SuppliedFillerEntity(); //To reinitialise and create new customer
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Filler uploaded to supplied list successfully!"));
            return "";
        }
    }
    /**
     * @return the newFiller
     */
    public SuppliedFillerEntity getNewFiller() {
        return newFiller;
    }

    /**
     * @param newFiller the newFiller to set
     */
    public void setNewFiller(SuppliedFillerEntity newFiller) {
        this.newFiller = newFiller;
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
    
}
