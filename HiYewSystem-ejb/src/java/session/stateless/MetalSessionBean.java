/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Metal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author User
 */
@Stateless
public class MetalSessionBean implements MetalSessionBeanLocal {
    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<String> getMetalBySubString(String str){
        Query query = em.createQuery("Select m.metalName FROM Metal AS m where m.metalName LIKE '%"+ str + "%' ");
        return query.getResultList();
    }
    
    @Override
    public Metal getMetalByName(String metalName){
        Metal metal = em.find(Metal.class, metalName);
        return metal;
    }
}
