/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Customer;
import java.util.List;
import java.util.Vector;
import javax.ejb.Local;

/**
 *
 * @author JustHRJ
 */
@Local
public interface TransferSystemBeanLocal {

    public void addProjects(List<Vector> result);

    public void addWelds(List<Vector> result);

    public void connectProjectWelds(List<Vector> result);


    
}
