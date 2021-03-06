/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.FillerComposition;
import entity.FillerEntity;
import entity.Metal;
import java.util.List;
import java.util.Vector;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author JustHRJ
 */
@Remote
public interface KnowledgeSystemBeanRemoteInterface {

    public void addMetal(List<Vector> metals);

    public List<Metal> metalRecords();

    public void editMetal(Metal metal);

    public void deleteMetal(Metal metal);

    public List<String> retrieveFillerNames();

    public List<String> metalNames();

    public boolean createPairings(String metal, List<String> fillerChosen);

    public List<String> FillersNotAssociated(String metalName);

    public List<String> FillersAssociated(String metalName);

    public void addFillers(List<Vector> fillers);

    public List<Vector> transferFillerInfo();

    public List<Vector> transferMetalInfo();

    public List<FillerComposition> fillerRecords();

    public void deleteFiller(FillerComposition filler);

    public void editFiller(FillerComposition filler);

    public void addMatch(List<Vector> results);

    public List<Vector> transferMatchingInfo();

    public void addNewFiller(FillerComposition filler);

    public void addNewMetal(Metal metal);

    public void addNewFiller(FillerComposition fillerC, FillerEntity filler);

    public FillerComposition retrieveFiller(FillerEntity filler);

    public boolean checkFillerID(String id);

    public boolean checkFillerName(String id);

    public boolean checkMetalName(String id);
    
}
