package session.stateless;

import entity.CustomerPO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CustomerPOSessionBeanLocal {

    public void createPO(CustomerPO customerPO);

    public void conductMerge(CustomerPO customerPO);

    public List<CustomerPO> receivedCustomerPO(String username);

}
