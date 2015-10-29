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
    private String id;
    private String name;
    private int copper;
    private int zinc;
    private int lead;
    private int aluminium;
    private int carbon;
    private int iron;
    private int nickel;
    private int manganese;
    private int silicon;
    private int chromium;

    public FillerComposition() {
    }

    public FillerComposition(String name, int copper, int silver, int gold, int bronze, int topaz, int iron, int alluminium, int titanium, int plastic, int platinium) {
        this.name = name;
        this.copper = copper;
        this.zinc = silver;
        this.lead = gold;
        this.aluminium = bronze;
        this.carbon = topaz;
        this.iron = iron;
        this.nickel = alluminium;
        this.manganese = titanium;
        this.silicon = plastic;
        this.chromium = platinium;
    }
    
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
     * @return the zinc
     */
    public int getZinc() {
        return zinc;
    }

    /**
     * @param silver the zinc to set
     */
    public void setZinc(int zinc) {
        this.zinc = zinc;
    }

    /**
     * @return the lead
     */
    public int getLead() {
        return lead;
    }

    /**
     * @param gold the lead to set
     */
    public void setLead(int lead) {
        this.lead = lead;
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
    
}
