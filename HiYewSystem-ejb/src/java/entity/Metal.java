/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.swing.Box.Filler;

/**
 *
 * @author: Jitcheong
 */
@Entity
public class Metal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String metalName;
    private int chromium;
    private int lead;
    private int carbon;
    private int zinc;
    private int copper;
    private int manganese;
    private int nickel;
    private int aluminium;
    private int silicon;
    private int iron;
    private Collection<FillerEntity> fillers = new ArrayList<FillerEntity>();
    
    public Metal() {
    }

    
    @ManyToMany(cascade ={CascadeType.ALL})
    public Collection<FillerEntity> getFillers(){
        return fillers;
    }
    
    
    public void setFillers(Collection<FillerEntity> fillers){
        this.fillers = fillers;
    }
    
    public Metal(String metalName) {
        this.metalName = metalName;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metalName != null ? metalName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Metal)) {
            return false;
        }
        Metal other = (Metal) object;
        if ((this.metalName == null && other.metalName != null) || (this.metalName != null && !this.metalName.equals(other.metalName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Metal[ id=" + metalName + " ]";
    }

    /**
     * @return the metalName
     */
    public String getMetalName() {
        return metalName;
    }

    /**
     * @param metalName the metalName to set
     */
    public void setMetalName(String metalName) {
        this.metalName = metalName;
    }

    /**
     * @return the chromium
     */
    public int getChromium() {
        return chromium;
    }

    /**
     * @param chromium the chromium to set
     */
    public void setChromium(int chromium) {
        this.chromium = chromium;
    }

    /**
     * @return the lead
     */
    public int getLead() {
        return lead;
    }

    /**
     * @param lead the lead to set
     */
    public void setLead(int lead) {
        this.lead = lead;
    }

    /**
     * @return the carbon
     */
    public int getCarbon() {
        return carbon;
    }

    /**
     * @param carbon the carbon to set
     */
    public void setCarbon(int carbon) {
        this.carbon = carbon;
    }

    /**
     * @return the zinc
     */
    public int getZinc() {
        return zinc;
    }

    /**
     * @param zinc the zinc to set
     */
    public void setZinc(int zinc) {
        this.zinc = zinc;
    }

    /**
     * @return the copper
     */
    public int getCopper() {
        return copper;
    }

    /**
     * @param copper the copper to set
     */
    public void setCopper(int copper) {
        this.copper = copper;
    }

    /**
     * @return the manganese
     */
    public int getManganese() {
        return manganese;
    }

    /**
     * @param manganese the manganese to set
     */
    public void setManganese(int manganese) {
        this.manganese = manganese;
    }

    /**
     * @return the nickel
     */
    public int getNickel() {
        return nickel;
    }

    /**
     * @param nickel the nickel to set
     */
    public void setNickel(int nickel) {
        this.nickel = nickel;
    }

    /**
     * @return the aluminium
     */
    public int getAluminium() {
        return aluminium;
    }

    /**
     * @param aluminium the aluminium to set
     */
    public void setAluminium(int aluminium) {
        this.aluminium = aluminium;
    }

    /**
     * @return the silicon
     */
    public int getSilicon() {
        return silicon;
    }

    /**
     * @param silicon the silicon to set
     */
    public void setSilicon(int silicon) {
        this.silicon = silicon;
    }

    /**
     * @return the iron
     */
    public int getIron() {
        return iron;
    }

    /**
     * @param iron the iron to set
     */
    public void setIron(int iron) {
        this.iron = iron;
    }


    
}
