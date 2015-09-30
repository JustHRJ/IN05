package session.stateless;

import entity.SupplierEntity;
import entity.SupplierPurchaseOrder;
//import entity.Quotation;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SupplierSessionBeanLocal {

    public void createSupplier(SupplierEntity supplier);

    public List<SupplierEntity> getAllSupplier();

    public SupplierEntity getSupplierByUsername(String username);

    public void updateSupplier(SupplierEntity s1);
    
    public String resetSupplierPassword(String username);


    //public void addQuotation(String username, Quotation quotation);

    public String encryptPassword(String password);

    //public void addPurchaseOrder(String username, CustomerPO cpo);

}
