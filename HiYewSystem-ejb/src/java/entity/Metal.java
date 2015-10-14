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
 * @author: Jitcheong
 */
@Entity
public class Metal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String metalName;

    public Metal() {
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
    
}
