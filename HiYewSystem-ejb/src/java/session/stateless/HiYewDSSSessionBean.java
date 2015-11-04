/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerEntity;
import entity.Metal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author K.guoxiang
 */
@Stateless
public class HiYewDSSSessionBean implements HiYewDSSSessionBeanLocal {
    
    @PersistenceContext(unitName = "HiYewSystem-ejbPU")
    private EntityManager em;

    
    @Override
    public List<FillerEntity> getListOfMatchedFillers(Metal metal){
        ArrayList<FillerEntity> matchedFillers = new ArrayList<FillerEntity>();
        matchedFillers.addAll(metal.getFillers());
        return matchedFillers;
    }
    
    @Override
    public int getAvailableQty(FillerEntity filler){
        return filler.getQuantity() - filler.getBookedQuantity();
    }


}
