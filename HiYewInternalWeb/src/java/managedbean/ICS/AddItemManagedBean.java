/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.ItemEntity;
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

    private ItemEntity newItem;

    /**
     * Creates a new instance of AddItemManagedBean
     */
    public AddItemManagedBean() {
        newItem = new ItemEntity();

    }

    public String createItem(ActionEvent event) {
        System.out.println("Here");
        if ((hiYewICSSessionBean.getExistingItem(newItem.getItemCode())) != null) {
            FacesContext.getCurrentInstance().addMessage("formMain:itemCode", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to add item! Existing Item Code", "Unable to add item! Existing Item Code"));
            return "";
        }else if ((hiYewICSSessionBean.getExistingItemByName(newItem.getItemName()))!= null){
            FacesContext.getCurrentInstance().addMessage("formMain:itemName", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unable to add item! Existing Item Name", "Unable to add item! Existing Item Name"));
            return "";
        }
        else{
          hiYewICSSessionBean.createItem(newItem);
          newItem = new ItemEntity(); //To reinitialise and create new customer
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item added to inventory successfully!"));
             return "viewInventory?faces-redirect=true";
        }
    }

    /**
     * @return the newItem
     */
    public ItemEntity getNewItem() {
        return newItem;
    }

    /**
     * @param newItem the newItem to set
     */
    public void setNewItem(ItemEntity newItem) {
        this.newItem = newItem;
    }

}
