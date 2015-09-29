/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.ItemEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.HiYewICSSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "viewInventoryManagedBean")
@ViewScoped
public class ViewInventoryManagedBean implements Serializable {

    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;
    private List<ItemEntity> itemList;
    private List<ItemEntity> filteredItems;
    private ItemEntity selectedItem;

    /**
     * Creates a new instance of ViewInventoryManagedBean
     */
    public ViewInventoryManagedBean() {
        itemList = new ArrayList<>();
        selectedItem = new ItemEntity();
    }

    @PostConstruct
    public void init() {
        itemList = hiYewICSSessionBean.getAllItems();
    }

    /**
     * @return the itemList
     */
    public List<ItemEntity> getItemList() {
        return itemList;
    }

    /**
     * @param itemList the itemList to set
     */
    public void setItemList(List<ItemEntity> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return the filteredItems
     */
    public List<ItemEntity> getFilteredItems() {
        return filteredItems;
    }

    /**
     * @param filteredItems the filteredItems to set
     */
    public void setFilteredItems(List<ItemEntity> filteredItems) {
        this.filteredItems = filteredItems;
    }

    /**
     * @return the selectedItem
     */
    public ItemEntity getSelectedItem() {
        return selectedItem;
    }

    /**
     * @param selectedItem the selectedItem to set
     */
    public void setSelectedItem(ItemEntity selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String passSelectedItemToNext() {
        System.out.println(this.selectedItem.getItemCode());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedItem", this.selectedItem);
        //  FacesContext.getCurrentInstance().getExternalContext().getFlash().put("SelectedItem", this.selectedItem);
        System.out.println("here2");
        return "itemDetails?faces-redirect=true";
    }

}
