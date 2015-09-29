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
@Named(value = "viewLowStockManagedBean")
@ViewScoped
public class ViewLowStockManagedBean implements Serializable {

    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    private List<ItemEntity> lowStockItems;
    private List<ItemEntity> filteredLowStockItems;
    private int numOfLowStockItems;
    private ItemEntity selectedItem;

    /**
     * Creates a new instance of ViewLowStockManagedBean
     */
    public ViewLowStockManagedBean() {
        lowStockItems = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        lowStockItems = hiYewICSSessionBean.getAllLowStockItems();
        numOfLowStockItems = lowStockItems.size();
    }

    /**
     * @return the lowStockItems
     */
    public List<ItemEntity> getLowStockItems() {
        return lowStockItems;
    }

    /**
     * @param lowStockItems the lowStockItems to set
     */
    public void setLowStockItems(List<ItemEntity> lowStockItems) {
        this.lowStockItems = lowStockItems;
    }

    /**
     * @return the numOfLowStockItems
     */
    public int getNumOfLowStockItems() {
        return numOfLowStockItems;
    }

    /**
     * @param numOfLowStockItems the numOfLowStockItems to set
     */
    public void setNumOfLowStockItems(int numOfLowStockItems) {
        this.numOfLowStockItems = numOfLowStockItems;
    }

    /**
     * @return the filteredLowStockItems
     */
    public List<ItemEntity> getFilteredLowStockItems() {
        return filteredLowStockItems;
    }

    /**
     * @param filteredLowStockItems the filteredLowStockItems to set
     */
    public void setFilteredLowStockItems(List<ItemEntity> filteredLowStockItems) {
        this.filteredLowStockItems = filteredLowStockItems;
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
