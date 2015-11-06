/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.PS;

import entity.SuppliedFillerEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.ProcurementSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "viewSupplierFillersManagedBean")
@ViewScoped
public class ViewSupplierFillersManagedBean implements Serializable{
 @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;
 private List<SuppliedFillerEntity> sfList;
    private List<SuppliedFillerEntity> filteredSFList;
    private SuppliedFillerEntity selectedSuppliedFiller;
    /**
     * Creates a new instance of ViewSupplierFillersManagedBean
     */
    public ViewSupplierFillersManagedBean() {
        sfList = new ArrayList<>();
    }
    
     @PostConstruct
    public void init() {
       
        sfList = procurementSessionBean.getAllSuppliedFillers();
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
    
}
