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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author: Jitcheong
 */
@Entity
public class Metal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String metalName;
    private int gold;
    private int silver;
    private int bronze;
    private int iron;
    private int copper;
    private int titanium;
    private int platinium;
    private int aluminium;
    private int topaz;
    private int plastic;
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
     * @return the gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @param gold the gold to set
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * @return the silver
     */
    public int getSilver() {
        return silver;
    }

    /**
     * @param silver the silver to set
     */
    public void setSilver(int silver) {
        this.silver = silver;
    }

    /**
     * @return the bronze
     */
    public int getBronze() {
        return bronze;
    }

    /**
     * @param bronze the bronze to set
     */
    public void setBronze(int bronze) {
        this.bronze = bronze;
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
     * @return the titanium
     */
    public int getTitanium() {
        return titanium;
    }

    /**
     * @param titanium the titanium to set
     */
    public void setTitanium(int titanium) {
        this.titanium = titanium;
    }

    /**
     * @return the platinium
     */
    public int getPlatinium() {
        return platinium;
    }

    /**
     * @param platinium the platinium to set
     */
    public void setPlatinium(int platinium) {
        this.platinium = platinium;
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
     * @return the topaz
     */
    public int getTopaz() {
        return topaz;
    }

    /**
     * @param topaz the topaz to set
     */
    public void setTopaz(int topaz) {
        this.topaz = topaz;
    }

    /**
     * @return the plastic
     */
    public int getPlastic() {
        return plastic;
    }

    /**
     * @param plastic the plastic to set
     */
    public void setPlastic(int plastic) {
        this.plastic = plastic;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
}
