/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean.PS;

import entity.ProcurementBidEntity;
import entity.SuppliedFillerEntity;
import entity.SupplierEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.RowEditEvent;
import session.stateless.ProcurementSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "viewSuppliedFillersManagedBean")
@ViewScoped
public class ViewSuppliedFillersManagedBean implements Serializable{
    @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;
 private List<SuppliedFillerEntity> sfList;
    private List<SuppliedFillerEntity> filteredSFList;
    private SuppliedFillerEntity selectedSuppliedFiller;
    private SupplierEntity supplier;
    private String username = ""; // when log on taken from session
    /**
     * Creates a new instance of ViewSuppliedFillersManagedBean
     */
    public ViewSuppliedFillersManagedBean() {
        sfList = new ArrayList<>();
    }
    
     @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username") != null) {
            this.setUsername(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username").toString());

            setSupplier(procurementSessionBean.getSupplierByUsername(this.getUsername()));
            System.out.println("@Supplier: Username is " + getUsername());
        }
        sfList = procurementSessionBean.getAllSuppliedFillers(supplier);
    }

    /**
     * @return the sfList
     */
    public List<SuppliedFillerEntity> getSfList() {
        return sfList;
    }

    /**
     * @param sfList the sfList to set
     */
    public void setSfList(List<SuppliedFillerEntity> sfList) {
        this.sfList = sfList;
    }

    /**
     * @return the filteredSFList
     */
    public List<SuppliedFillerEntity> getFilteredSFList() {
        return filteredSFList;
    }

    /**
     * @param filteredSFList the filteredSFList to set
     */
    public void setFilteredSFList(List<SuppliedFillerEntity> filteredSFList) {
        this.filteredSFList = filteredSFList;
    }

    /**
     * @return the selectedSuppliedFiller
     */
    public SuppliedFillerEntity getSelectedSuppliedFiller() {
        return selectedSuppliedFiller;
    }

    /**
     * @param selectedSuppliedFiller the selectedSuppliedFiller to set
     */
    public void setSelectedSuppliedFiller(SuppliedFillerEntity selectedSuppliedFiller) {
        this.selectedSuppliedFiller = selectedSuppliedFiller;
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
    
 public String passSelectedItemToNext() {
        System.out.println(this.selectedSuppliedFiller.getFillerCode());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedSuppliedFiller", this.selectedSuppliedFiller);
        //  FacesContext.getCurrentInstance().getExternalContext().getFlash().put("SelectedItem", this.selectedItem);
        System.out.println("here2");
        return "s-suppliedFiller-details?faces-redirect=true";
    }
    
}
