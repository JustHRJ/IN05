/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean.PS;

import entity.FillerEntity;
import entity.SuppliedFillerEntity;
import java.io.Serializable;
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
@Named(value = "suppliedFillerDetailsManagedBean")
@ViewScoped
public class SuppliedFillerDetailsManagedBean implements Serializable{
    @EJB
    private ProcurementSessionBeanLocal procurementSessionBean;

    private SuppliedFillerEntity selectedFiller;
    /**
     * Creates a new instance of SuppliedFillerDetailsManagedBean
     */
    public SuppliedFillerDetailsManagedBean() {
         if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedSuppliedFiller") != null) {
            selectedFiller = (SuppliedFillerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedSuppliedFiller");
        } else {
            selectedFiller = new SuppliedFillerEntity();
        }
    }
    
      public void updateItemDetails() {
          System.out.println("updateItemDetails: "+selectedFiller.getFillerCode());
        if (selectedFiller.getFillerCode() != null) {
            procurementSessionBean.updateFillerDetails(selectedFiller);
            FacesContext.getCurrentInstance().addMessage("upperMessages", new FacesMessage("Item " + selectedFiller.getFillerCode() + "'s details updated successfully!"));
            System.out.println("here4");
        }
    }
      
       public String deleteItem() {
       
        if (selectedFiller.getFillerCode().length() > 0) {
            procurementSessionBean.deleteFiller(selectedFiller);
            FacesContext.getCurrentInstance().addMessage("upperMessages", new FacesMessage("Filler " + selectedFiller.getFillerCode() + "'s quantity deleted successfully!"));
            return "s-view-catalogue?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage("upperMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Warning!","Unable to delete!"));
            return "";
        }
        
    }

    /**
     * @return the selectedFiller
     */
    public SuppliedFillerEntity getSelectedFiller() {
        return selectedFiller;
    }

    /**
     * @param selectedFiller the selectedFiller to set
     */
    public void setSelectedFiller(SuppliedFillerEntity selectedFiller) {
        this.selectedFiller = selectedFiller;
    }
    
}
