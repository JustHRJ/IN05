/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerEntity;
import entity.Metal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author K.guoxiang
 */
@Local
public interface HiYewDSSSessionBeanLocal {

    public List<FillerEntity> getListOfMatchedFillers(Metal metal);

    public int getAvailableQty(FillerEntity filler);
    
}
