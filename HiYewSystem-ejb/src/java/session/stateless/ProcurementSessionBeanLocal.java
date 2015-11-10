/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.ProcurementBidEntity;
import entity.SuppliedFillerEntity;
import entity.SupplierEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author K.guoxiang
 */
@Local
public interface ProcurementSessionBeanLocal {

    public void deleteItem(ProcurementBidEntity pb);

    public void updatePBStatus(ProcurementBidEntity pb, String status);

    public void updateQuotedPrice(ProcurementBidEntity pb, double price);

    public List<ProcurementBidEntity> getBidsOpenForSupplier(String companyName);

    public List<ProcurementBidEntity> getAllClosedBids();

    public List<ProcurementBidEntity> getAllOpenBids();

    public void createProcurementBid(ProcurementBidEntity pb);

    public List<ProcurementBidEntity> getAllBids();

    public List<String> getAllSuppliers();

    public int getNextBatchNo();

    public List<ProcurementBidEntity> getBidsOverview();

    public List<ProcurementBidEntity> getBidsOfBidRef(int batchNum);

    public void updateAcceptStatus(ProcurementBidEntity pb, String status);

    public SupplierEntity getSupplierByUsername(String username);

    public void updatePBStatusByBatch(int batchRefNum, String status);

    public void updateAllPBStatus();

    public SuppliedFillerEntity getExistingSuppliedFiller(String fillerCode);

    public SuppliedFillerEntity getExistingFillerByName(String fillerName,SupplierEntity supplier);

    public void uploadSuppliedItem(SuppliedFillerEntity suppliedFiller);

    public List<SuppliedFillerEntity> getAllSuppliedFillers(SupplierEntity supplier);

    public void updateFillerDetails(SuppliedFillerEntity item);

    public void deleteFiller(SuppliedFillerEntity filler);

    public List<SuppliedFillerEntity> getAllSuppliedFillers();

    public SupplierEntity getSupplierByCompanyname(String companyName);

}
