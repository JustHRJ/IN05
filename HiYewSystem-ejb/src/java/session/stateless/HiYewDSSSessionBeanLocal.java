/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerEntity;
import entity.Metal;
import entity.WeldJob;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public Metal getExistingMetal(String metalName);

    public double getWireVolume(FillerEntity item);

    public int quantityNeeded(FillerEntity filler, double surfaceAreaToweld,int qty);

    public FillerEntity getExistingItem(String fillerCode);

    public List<WeldJob> getSimilarPastProjects(String metal1, String metal2, String weldingType);

    public Integer getDifferenceDays(Timestamp t1, Timestamp t2);

    public Integer deriveAverageDaysNeededForWeldJob(ArrayList<WeldJob> similarWeldJobs, double surfaceVolToWeld, int qtyToWeld);

    public double getAvgManpowerCostPerDay();
    
}
