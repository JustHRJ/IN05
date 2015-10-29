/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Metal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface MetalSessionBeanLocal {

    public List<String> getMetalBySubString(String str);

    public Metal getMetalByName(String metalName);
    
}
