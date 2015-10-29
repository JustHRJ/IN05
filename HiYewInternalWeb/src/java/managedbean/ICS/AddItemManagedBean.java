/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.FillerEntity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import session.stateless.HiYewICSSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "addItemManagedBean")
@RequestScoped
public class AddItemManagedBean implements Serializable {

    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    private FillerEntity newItem;

    /**
     * Creates a new instance of AddItemManagedBean
     */
    public AddItemManagedBean() {
        newItem = new FillerEntity();

    }

    public String createItem() {
        System.out.println("Here");
        if ((hiYewICSSessionBean.getExistingItem(newItem.getFillerCode())) != null) {
            System.out.println("Exisitng Item Code");
            FacesContext.getCurrentInstance().addMessage("formMain:itemCode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to add item! Existing Item Code", "Unable to add item! Existing Item Code"));
            return "";
        }else if ((hiYewICSSessionBean.getExistingItemByName(newItem.getFillerName()))!= null){
            System.out.println("Exisitng Item Name");
            FacesContext.getCurrentInstance().addMessage("formMain:itemName", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unable to add item! Existing Item Name", "Unable to add item! Existing Item Name"));
            return "";
        }
        else{
          hiYewICSSessionBean.createItem(newItem);
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ItemToPassToComposition", this.newItem);
          newItem = new FillerEntity(); //To reinitialise and create new customer
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item added to inventory successfully!"));
             return "ics-add-newItem-Composite?faces-redirect=true";
        }
    }

    /**
     * @return the newItem
     */
    public FillerEntity getNewItem() {
        return newItem;
    }

    /**
     * @param newItem the newItem to set
     */
    public void setNewItem(FillerEntity newItem) {
        this.newItem = newItem;
    }

}
