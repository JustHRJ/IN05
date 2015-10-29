/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.FillerComposition;
import entity.FillerEntity;
import entity.Metal;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;
import session.stateless.HiYewSystemBeanLocal;
import session.stateless.KnowledgeSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "knowledgeManageBean")
@ViewScoped
public class KnowledgeManageBean implements Serializable {

    @EJB
    private KnowledgeSystemBeanLocal knowledgeSystemBean;

    private List<Vector> results = new ArrayList<Vector>();
    private String inputFile;
    private List<FillerComposition> fillerRecords;
    private List<FillerComposition> filteredFillers;
    private List<Metal> MetalRecords;
    private FillerComposition selectedFiller;
    private Metal selectedMetal;
    private List<Vector> result3 = new ArrayList<Vector>();
    private List<Vector> results2 = new ArrayList<Vector>();
    private String metalName = "";
    private List<String> fillerList = new ArrayList<String>();
    private List<String> fillerChosen = new ArrayList<String>();
    private DualListModel<String> fillerDisplay;
    private String knowledge = "";
    private boolean FillerAdd = false;
    private boolean MetalAdd = false;

    /**
     * Creates a new instance of KnowledgeManageBean
     */
    public KnowledgeManageBean() {
    }

    @PostConstruct
    public void init() {
        setFillerList(knowledgeSystemBean.retrieveFillerNames());
        System.out.println(FillerAdd);
        setFillerChosen(new ArrayList<String>());
        selectedFiller = new FillerComposition();
        selectedMetal = new Metal();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ItemToPassToComposition") != null) {
            selectedFiller = knowledgeSystemBean.retrieveFiller((FillerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ItemToPassToComposition"));
        }

    }

    public void addNewFillerInfo() throws IOException {
        if (selectedFiller.getName().isEmpty()) {

        } else {
            boolean check = countPercentage(selectedFiller);
            if (check) {
                knowledgeSystemBean.addNewFiller(selectedFiller);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/kms-filler-knowledge.xhtml");
            } else {
                FacesMessage msg = new FacesMessage("Filler Composition does not add up.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    private boolean countPercentageM(Metal filler) {
        int sum = 0;
        sum += filler.getAluminium() + filler.getCarbon() + filler.getChromium() + filler.getCopper() + filler.getIron() + filler.getLead() + filler.getManganese() + filler.getNickel() + filler.getSilicon() + filler.getZinc();
        if (sum != 100) {
            return false;
        } else {
            return true;
        }

    }

    private boolean countPercentage(FillerComposition filler) {
        int sum = 0;
        sum += filler.getAluminium() + filler.getCarbon() + filler.getChromium() + filler.getCopper() + filler.getIron() + filler.getLead() + filler.getManganese() + filler.getNickel() + filler.getSilicon() + filler.getZinc();
        if (sum != 100) {
            return false;
        } else {
            return true;
        }

    }

    public void addNewFillerInfoInv() throws IOException {
        boolean check = countPercentage(selectedFiller);
        if (check) {
            FillerEntity filler = (FillerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ItemToPassToComposition");
            knowledgeSystemBean.addNewFiller(selectedFiller, filler);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ItemToPassToComposition");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/ics-view-inventory.xhtml");

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please check for composition total - needs to be 100%", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void addNewMetalInfo() throws IOException {
        if (selectedMetal.getMetalName().isEmpty()) {

        } else {
            boolean check = countPercentageM(selectedMetal);
            if (check) {
                knowledgeSystemBean.addNewMetal(selectedMetal);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/HiYewInternalWeb/kms-metal-knowledge.xhtml");
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please check for composition total - needs to be 100%", "" );
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }
    }

    public void fillerList() {
        results = knowledgeSystemBean.transferFillerInfo();

    }

    public void processAdd() {
        System.out.println("did it come here");
        if (knowledge.equals("Filler")) {
            System.out.println("filler is captured");
            setFillerAdd(true);
            System.out.println(FillerAdd);
            setMetalAdd(false);
        } else if (knowledge.equals("Metal")) {
            setFillerAdd(false);
            setMetalAdd(true);
        } else {
            System.out.println("not working");
            setFillerAdd(false);
            setMetalAdd(false);
        }
    }

    public void metalMatchingList() {
        result3 = knowledgeSystemBean.transferMatchingInfo();
    }

    public void metalList() {
        results2 = knowledgeSystemBean.transferMetalInfo();
    }

    public List<FillerComposition> getAllFillers() {
        fillerRecords = knowledgeSystemBean.fillerRecords();
        return fillerRecords;
    }

    public List<Metal> getAllMetals() {
        MetalRecords = knowledgeSystemBean.metalRecords();
        return MetalRecords;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void deleteFiller() {
        knowledgeSystemBean.deleteFiller(selectedFiller);
    }

    public void deleteMetal() {
        knowledgeSystemBean.deleteMetal(selectedMetal);
    }

    private List<Vector> read() throws IOException {
        File inputWorkbook = new File(inputFile);
        List<Vector> results = new ArrayList<Vector>();
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            System.out.println(sheet.getColumns());
            System.out.println(sheet.getRows());
            for (int j = 1; j < sheet.getRows(); j++) {
                Vector im = new Vector();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell = sheet.getCell(i, j);

                    if (cell.getContents().isEmpty()) {
                        im.add(0);
                    } else {
                        im.add(cell.getContents());
                    }
                }
                results.add(im);
            }
            w.close();
            if (results.isEmpty()) {
                return null;
            }
            return results;
        } catch (BiffException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FillerComposition getRetrieveFiller() {
        return selectedFiller;
    }

    public Metal getRetrieveMetal() {
        return selectedMetal;
    }

    public void readFile() throws IOException {
        setInputFile("C:\\Users\\User\\Desktop\\Book1.xls");
        List<Vector> result = read();
        knowledgeSystemBean.addFillers(result);
    }

    public void readFile2() throws IOException {
        setInputFile("C:\\Users\\User\\Desktop\\Book1.xls");
        List<Vector> result = read2();
        knowledgeSystemBean.addMetal(result);
    }

    private List<Vector> read2() throws IOException {
        File inputWorkbook = new File(inputFile);
        List<Vector> results = new ArrayList<Vector>();
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(1);
            // Loop over first 10 column and lines

            System.out.println(sheet.getColumns());
            System.out.println(sheet.getRows());
            for (int j = 1; j < sheet.getRows(); j++) {
                Vector im = new Vector();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell = sheet.getCell(i, j);

                    if (cell.getContents().isEmpty()) {
                        im.add(0);
                    } else {
                        im.add(cell.getContents());
                    }
                }
                results.add(im);
            }
            w.close();
            if (results.isEmpty()) {
                return null;
            }
            return results;
        } catch (BiffException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeFile() throws IOException, WriteException, BiffException {
        setInputFile("C:\\Users\\User\\Desktop\\Book1.xls");
        write();
    }

    private void write() throws IOException, WriteException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File(inputFile));
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet sheet1 = copy.getSheet(0);
        int rows = sheet1.getRows();
        if (rows == 1) {
            for (int i = 0; i < sheet1.getColumns(); i++) {
                jxl.write.Number number = new jxl.write.Number(i, rows, 2);
                sheet1.addCell(number);
            }
        } else {
            for (int i = 0; i < sheet1.getColumns(); i++) {
                jxl.write.Number number = new jxl.write.Number(i, rows, 2);
                sheet1.addCell(number);
            }
        }
        System.out.println("done");
        copy.write();
        copy.close();
    }

    public void writeFile2() throws IOException, WriteException, BiffException {
        setInputFile("C:\\Users\\User\\Desktop\\Book1.xls");
        fillerList();
        write2();
    }

    public void writeFile3() throws IOException, WriteException, BiffException {
        setInputFile("C:\\Users\\User\\Desktop\\Book1.xls");
        metalList();
        write3();
    }

    private void write2() throws IOException, WriteException, BiffException {

        Workbook workbook = Workbook.getWorkbook(new File(inputFile));
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet sheet1 = copy.createSheet("FillerInformation", 0);

        jxl.write.Label number3 = new jxl.write.Label(0, 0, "ID");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(1, 0, "NAME");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(2, 0, "COPPER");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(3, 0, "ZINC");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(4, 0, "IRON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(5, 0, "LEAD");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(6, 0, "ALUMINIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(7, 0, "CARBON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(8, 0, "NICKEL");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(9, 0, "MANGANESE");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(10, 0, "SILICON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(11, 0, "CHROMIUM");
        sheet1.addCell(number3);
        if (results != null) {

            int rows = results.size();
            for (int i = 1; i <= rows; i++) {
                Vector im = results.get(i - 1);
                int cols = im.size();
                System.out.println(cols);
                jxl.write.Label number1 = new jxl.write.Label(0, i, im.get(0).toString());
                sheet1.addCell(number1);
                jxl.write.Label number2 = new jxl.write.Label(1, i, im.get(1).toString());
                sheet1.addCell(number2);

                for (int j = 2; j < cols; j++) {
                    jxl.write.Number number = new jxl.write.Number(j, i, Integer.parseInt(im.get(j).toString()));
                    sheet1.addCell(number);
                }

            }
        }
        copy.removeSheet(1);
        System.out.println("done1");
        copy.write();

        copy.close();

    }

    private void write3() throws IOException, WriteException, BiffException {

        Workbook workbook = Workbook.getWorkbook(new File(inputFile));
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet sheet1 = copy.createSheet("MetalInformation", 1);

        jxl.write.Label number3 = new jxl.write.Label(0, 0, "NAME");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(1, 0, "COPPER");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(2, 0, "ZINC");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(3, 0, "IRON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(4, 0, "LEAD");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(5, 0, "ALUMINIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(6, 0, "CARBON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(7, 0, "NICKEL");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(8, 0, "MANGANESE");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(9, 0, "SILICON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(10, 0, "CHROMIUM");
        sheet1.addCell(number3);
        if (results2 != null) {

            int rows = results2.size();
            for (int i = 1; i <= rows; i++) {
                Vector im = results2.get(i - 1);
                int cols = im.size();
                System.out.println(cols);
                jxl.write.Label number1 = new jxl.write.Label(0, i, im.get(0).toString());
                sheet1.addCell(number1);
                jxl.write.Label number2 = new jxl.write.Label(1, i, im.get(1).toString());
                sheet1.addCell(number2);

                for (int j = 2; j < cols; j++) {
                    jxl.write.Number number = new jxl.write.Number(j, i, Integer.parseInt(im.get(j).toString()));
                    sheet1.addCell(number);
                }

            }
        }

        copy.removeSheet(2);
        System.out.println("done1");
        copy.write();

        copy.close();

    }

    /**
     * @return the fillerRecords
     */
    public List<FillerComposition> getFillerRecords() {
        return fillerRecords;
    }

    /**
     * @param fillerRecords the fillerRecords to set
     */
    public void setFillerRecords(List<FillerComposition> fillerRecords) {
        this.fillerRecords = fillerRecords;
    }

    /**
     * @return the filteredFillers
     */
    public List<FillerComposition> getFilteredFillers() {
        return filteredFillers;
    }

    /**
     * @param filteredFillers the filteredFillers to set
     */
    public void setFilteredFillers(List<FillerComposition> filteredFillers) {
        this.filteredFillers = filteredFillers;
    }

    /**
     * @return the selectedFiller
     */
    public FillerComposition getSelectedFiller() {
        return selectedFiller;
    }

    /**
     * @param selectedFiller the selectedFiller to set
     */
    public void setSelectedFiller(FillerComposition selectedFiller) {
        this.selectedFiller = selectedFiller;
    }

    public void updateFiller() {
        System.out.println("here");
        boolean check = countPercentage(selectedFiller);
        if (check) {
            knowledgeSystemBean.editFiller(selectedFiller);
            FacesMessage msg = new FacesMessage("Filler is updated");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("Filler edit is improper");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void updateMetal() {
        boolean check = countPercentageM(selectedMetal);
        if (check) {
            knowledgeSystemBean.editMetal(selectedMetal);
        }
    }

    /**
     * @return the copper
     */
    /**
     * @return the results2
     */
    public List<Vector> getResults2() {
        return results2;
    }

    /**
     * @param results2 the results2 to set
     */
    public void setResults2(List<Vector> results2) {
        this.results2 = results2;
    }

    /**
     * @return the MetalRecords
     */
    public List<Metal> getMetalRecords() {
        return MetalRecords;
    }

    /**
     * @param MetalRecords the MetalRecords to set
     */
    public void setMetalRecords(List<Metal> MetalRecords) {
        this.MetalRecords = MetalRecords;
    }

    /**
     * @return the selectedMetal
     */
    public Metal getSelectedMetal() {
        return selectedMetal;
    }

    public List<FillerEntity> viewMatch() {
        if (selectedMetal != null) {
            List<FillerEntity> result = new ArrayList<FillerEntity>();
            for (Object o : selectedMetal.getFillers()) {
                FillerEntity f = (FillerEntity) o;
                result.add(f);
            }
            if (result.isEmpty()) {
                return null;
            } else {
                return result;
            }
        } else {
            return null;
        }
    }

    /**
     * @param selectedMetal the selectedMetal to set
     */
    public void setSelectedMetal(Metal selectedMetal) {
        this.selectedMetal = selectedMetal;
    }

    /**
     * @return the fillerList
     */
    public List<String> getFillerList() {
        return fillerList;
    }

    /**
     * @param fillerList the fillerList to set
     */
    public void setFillerList(List<String> fillerList) {
        this.fillerList = fillerList;
    }

    /**
     * @return the fillerChosen
     */
    public List<String> getFillerChosen() {
        return fillerChosen;
    }

    /**
     * @param fillerChosen the fillerChosen to set
     */
    public void setFillerChosen(List<String> fillerChosen) {
        this.fillerChosen = fillerChosen;
    }

    /**
     * @return the fillerDisplay
     */
    public DualListModel<String> getFillerDisplay() {
        System.out.println("here1");
        if (metalName.isEmpty() || metalName.equals("")) {

        } else {
            fillerList = knowledgeSystemBean.FillersNotAssociated(metalName);
            fillerChosen = knowledgeSystemBean.FillersAssociated(metalName);
        }
        setFillerDisplay(new DualListModel<String>(fillerList, fillerChosen));
        System.out.println("here2");
        return fillerDisplay;
    }

    /**
     * @param fillerDisplay the fillerDisplay to set
     */
    public void setFillerDisplay(DualListModel<String> fillerDisplay) {
        this.fillerDisplay = fillerDisplay;
    }

    public List<String> getRetrieveMetalName() {
        return knowledgeSystemBean.metalNames();
    }

    /**
     * @return the metalName
     */
    public String getMetalName() {
        return metalName;
    }

    /**
     * @param metalName the metalName to set
     */
    public void setMetalName(String metalName) {
        this.metalName = metalName;
    }

    public void createPairings() {
        fillerChosen = fillerDisplay.getTarget();
        knowledgeSystemBean.createPairings(metalName, fillerChosen);
    }

    public void readFile3() throws IOException {
        setInputFile("C:\\Users\\User\\Desktop\\Book1.xls");
        List<Vector> result = read3();
        knowledgeSystemBean.addMatch(result);
    }

    public List<Vector> read3() throws IOException {
        File inputWorkbook = new File(inputFile);
        List<Vector> results = new ArrayList<Vector>();
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(2);
            // Loop over first 10 column and lines

            System.out.println(sheet.getColumns());
            System.out.println(sheet.getRows());
            for (int j = 1; j < sheet.getRows(); j++) {
                Vector im = new Vector();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell = sheet.getCell(i, j);

                    if (cell.getContents().isEmpty()) {
                        im.add(0);
                    } else {
                        im.add(cell.getContents());
                    }
                }
                results.add(im);
            }
            w.close();
            if (results.isEmpty()) {
                return null;
            }
            return results;
        } catch (BiffException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeFile4() throws IOException, WriteException, BiffException {
        setInputFile("C:\\Users\\User\\Desktop\\Book1.xls");
        metalMatchingList();
        write4();
    }

    private void write4() throws IOException, WriteException, BiffException {

        Workbook workbook = Workbook.getWorkbook(new File(inputFile));
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet sheet1 = copy.createSheet("MetalMatching", 2);

        jxl.write.Label number3 = new jxl.write.Label(0, 0, "MetalName");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(1, 0, "Filler ID");
        sheet1.addCell(number3);

        if (result3 != null) {
            int rows = result3.size();
            for (int i = 1; i <= rows; i++) {
                Vector im = result3.get(i - 1);
                int cols = im.size();
                System.out.println(cols);
                jxl.write.Label number4 = new jxl.write.Label(0, 1, im.get(0).toString());
                sheet1.addCell(number4);
                for (int j = 1; j < cols; j++) {
                    jxl.write.Number number = new jxl.write.Number(j, i, Integer.parseInt(im.get(j).toString()));
                    sheet1.addCell(number);
                }

            }
        }
        copy.removeSheet(3);
        System.out.println("done metal pairing");
        copy.write();

        copy.close();

    }

    /**
     * @return the result3
     */
    public List<Vector> getResult3() {
        return result3;
    }

    /**
     * @param result3 the result3 to set
     */
    public void setResult3(List<Vector> result3) {
        this.result3 = result3;
    }

    /**
     * @return the knowledge
     */
    public String getKnowledge() {
        return knowledge;
    }

    /**
     * @param knowledge the knowledge to set
     */
    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    /**
     * @return the FillerAdd
     */
    public boolean isFillerAdd() {
        return FillerAdd;
    }

    /**
     * @param FillerAdd the FillerAdd to set
     */
    public void setFillerAdd(boolean FillerAdd) {
        this.FillerAdd = FillerAdd;
    }

    /**
     * @return the MetalAdd
     */
    public boolean isMetalAdd() {
        return MetalAdd;
    }

    /**
     * @param MetalAdd the MetalAdd to set
     */
    public void setMetalAdd(boolean MetalAdd) {
        this.MetalAdd = MetalAdd;
    }

}
