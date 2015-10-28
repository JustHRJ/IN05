/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.FillerEntity;
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
    private List<FillerEntity> itemList;
    private List<FillerEntity> filteredItems;
    private FillerEntity selectedItem;

    /**
     * Creates a new instance of ViewInventoryManagedBean
     */
    public ViewInventoryManagedBean() {
        itemList = new ArrayList<>();
        selectedItem = new FillerEntity();
    }

    @PostConstruct
    public void init() {
        itemList = hiYewICSSessionBean.getAllItems();
    }

    /**
     * @return the itemList
     */
    public List<FillerEntity> getItemList() {
        return itemList;
    }

    /**
     * @param itemList the itemList to set
     */
    public void setItemList(List<FillerEntity> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return the filteredItems
     */
    public List<FillerEntity> getFilteredItems() {
        return filteredItems;
    }

    /**
     * @param filteredItems the filteredItems to set
     */
    public void setFilteredItems(List<FillerEntity> filteredItems) {
        this.filteredItems = filteredItems;
    }

    /**
     * @return the selectedItem
     */
    public FillerEntity getSelectedItem() {
        return selectedItem;
    }

    /**
     * @param selectedItem the selectedItem to set
     */
    public void setSelectedItem(FillerEntity selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String passSelectedItemToNext() {
        System.out.println(this.selectedItem.getFillerCode());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedItem", this.selectedItem);
        //  FacesContext.getCurrentInstance().getExternalContext().getFlash().put("SelectedItem", this.selectedItem);
        System.out.println("here2");
        return "ics-item-details?faces-redirect=true";
    }

}
