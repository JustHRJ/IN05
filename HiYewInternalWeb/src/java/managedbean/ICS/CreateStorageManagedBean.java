/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.ICS;

import entity.RackEntity;
import entity.ShelveEntity;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.stateless.HiYewICSSessionBeanLocal;

/**
 *
 * @author K.guoxiang
 */
@Named(value = "createStorageManagedBean")
@ViewScoped
public class CreateStorageManagedBean implements Serializable {

    @EJB
    private HiYewICSSessionBeanLocal hiYewICSSessionBean;

    private RackEntity newRack;
    private ShelveEntity newShelve;

    private String nextRackID;
    private String nextShelveID;
    private int numOfShelvesForRack;
    private String rackLocationLevel;
    private String rackZone;

    /**
     * Creates a new instance of CreateStorageManagedBean
     */
    public CreateStorageManagedBean() {
        newRack = new RackEntity();

    }

    @PostConstruct
    public void init() {
        nextRackID = hiYewICSSessionBean.getNextIDForRack();
        System.out.println(nextRackID);
    }

    public void createRack(ActionEvent event) {
        newRack.setRackID(getNextRackID());
        newRack.setStatus("Not Full");
        newRack.setLocation("Level " + rackLocationLevel + ", " + rackZone);
        ArrayList<ShelveEntity> shelves = new ArrayList<ShelveEntity>();
        newRack.setShelves(shelves);
        hiYewICSSessionBean.createRack(getNewRack());

        double avgHeightPerShelve = Math.floor((newRack.getHeight() - (3 * numOfShelvesForRack)) / numOfShelvesForRack);
        double rackRemainingHeight = newRack.getHeight() - (3 * numOfShelvesForRack);
        System.out.println("Average Height Per Shelve: " + avgHeightPerShelve);

        for (int i = 1; i <= numOfShelvesForRack; i++) {
            System.out.println("Loop  " + i);
            System.out.println("RackID = " + nextRackID);
            nextShelveID = hiYewICSSessionBean.getNextIDForShelve(nextRackID);
            newShelve = new ShelveEntity();
            newShelve.setShelveID(nextShelveID);
            newShelve.setShelveLength(newRack.getLength());
            newShelve.setWidth(newRack.getWidth());
            if (i != numOfShelvesForRack) {
                newShelve.setHeight(avgHeightPerShelve);
                rackRemainingHeight = rackRemainingHeight - avgHeightPerShelve;
            } else {
                newShelve.setHeight(rackRemainingHeight);
            }
            newShelve.setStatus("Not Full");
            newShelve.setRack(hiYewICSSessionBean.getExistingRack(nextRackID));
            hiYewICSSessionBean.createShelve(newShelve);
        }

        setNewRack(new RackEntity()); //To reinitialise and create new rack
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New Rack created successfully!"));

    }

    /**
     * @return the newRack
     */
    public RackEntity getNewRack() {
        return newRack;
    }

    /**
     * @param newRack the newRack to set
     */
    public void setNewRack(RackEntity newRack) {
        this.newRack = newRack;
    }

    /**
     * @return the newShelve
     */
    public ShelveEntity getNewShelve() {
        return newShelve;
    }

    /**
     * @param newShelve the newShelve to set
     */
    public void setNewShelve(ShelveEntity newShelve) {
        this.newShelve = newShelve;
    }

    /**
     * @return the nextRackID
     */
    public String getNextRackID() {
        return nextRackID;
    }

    /**
     * @param nextRackID the nextRackID to set
     */
    public void setNextRackID(String nextRackID) {
        this.nextRackID = nextRackID;
    }

    /**
     * @return the nextShelveID
     */
    public String getNextShelveID() {
        return nextShelveID;
    }

    /**
     * @param nextShelveID the nextShelveID to set
     */
    public void setNextShelveID(String nextShelveID) {
        this.nextShelveID = nextShelveID;
    }

    /**
     * @return the numOfShelvesForRack
     */
    public int getNumOfShelvesForRack() {
        return numOfShelvesForRack;
    }

    /**
     * @param numOfShelvesForRack the numOfShelvesForRack to set
     */
    public void setNumOfShelvesForRack(int numOfShelvesForRack) {
        this.numOfShelvesForRack = numOfShelvesForRack;
    }

    /**
     * @return the rackLocationLevel
     */
    public String getRackLocationLevel() {
        return rackLocationLevel;
    }

    /**
     * @param rackLocationLevel the rackLocationLevel to set
     */
    public void setRackLocationLevel(String rackLocationLevel) {
        this.rackLocationLevel = rackLocationLevel;
    }

    /**
     * @return the rackZone
     */
    public String getRackZone() {
        return rackZone;
    }

    /**
     * @param rackZone the rackZone to set
     */
    public void setRackZone(String rackZone) {
        this.rackZone = rackZone;
    }
}
