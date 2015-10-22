/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author JustHRJ
 */
@Entity
public class FillerComposition implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int copper;
    private int silver;
    private int gold;
    private int bronze;
    private int topaz;
    private int iron;
    private int alluminium;
    private int titanium;
    private int plastic;
    private int platinium;

    public FillerComposition() {
    }

    public FillerComposition(String name, int copper, int silver, int gold, int bronze, int topaz, int iron, int alluminium, int titanium, int plastic, int platinium) {
        this.name = name;
        this.copper = copper;
        this.silver = silver;
        this.gold = gold;
        this.bronze = bronze;
        this.topaz = topaz;
        this.iron = iron;
        this.alluminium = alluminium;
        this.titanium = titanium;
        this.plastic = plastic;
        this.platinium = platinium;
    }
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FillerComposition)) {
            return false;
        }
        FillerComposition other = (FillerComposition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FillerEntity[ id=" + id + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the alluminium
     */
    public int getAlluminium() {
        return alluminium;
    }

    /**
     * @param alluminium the alluminium to set
     */
    public void setAlluminium(int alluminium) {
        this.alluminium = alluminium;
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
    
}
