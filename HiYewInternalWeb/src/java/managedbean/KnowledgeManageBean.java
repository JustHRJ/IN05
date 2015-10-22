/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

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

/**
 *
 * @author JustHRJ
 */
@Named(value = "knowledgeManageBean")
@ViewScoped
public class KnowledgeManageBean implements Serializable {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private List<Vector> results = new ArrayList<Vector>();
    private String inputFile;
    private List<FillerEntity> fillerRecords;
    private List<FillerEntity> filteredFillers;
    private List<Metal> MetalRecords;
    private FillerEntity selectedFiller;
    private Metal selectedMetal;
    private int copper;
    private List<Vector> results2 = new ArrayList<Vector>();
    private String metalName = "";
    private List<String> fillerList = new ArrayList<String>();
    private List<String> fillerChosen = new ArrayList<String>();
    private DualListModel<String> fillerDisplay;

    /**
     * Creates a new instance of KnowledgeManageBean
     */
    public KnowledgeManageBean() {
    }

    @PostConstruct
    public void init() {
        setFillerList(hiYewSystemBean.retrieveFillerNames());
        System.out.println("stuck here");
        setFillerChosen(new ArrayList<String>());
        System.out.println("stuck here2");
        System.out.println("stuck here3");
    }

    public void fillerList() {
        results = hiYewSystemBean.transferFillerInfo();

    }

    public void metalList() {
        results2 = hiYewSystemBean.transferMetalInfo();
    }

    public List<FillerEntity> getAllFillers() {
        fillerRecords = hiYewSystemBean.fillerRecords();
        return fillerRecords;
    }

    public List<Metal> getAllMetals() {
        MetalRecords = hiYewSystemBean.metalRecords();
        return MetalRecords;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void deleteFiller() {
        hiYewSystemBean.deleteFiller(selectedFiller);
    }

    public void deleteMetal() {
        hiYewSystemBean.deleteMetal(selectedMetal);
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

    public FillerEntity getRetrieveFiller() {
        return selectedFiller;
    }

    public Metal getRetrieveMetal() {
        return selectedMetal;
    }

    public void readFile() throws IOException {
        setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        List<Vector> result = read();
        hiYewSystemBean.addFillers(result);
    }

    public void readFile2() throws IOException {
        setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        List<Vector> result = read2();
        hiYewSystemBean.addMetal(result);
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
        setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        write();
    }

    public void write() throws IOException, WriteException, BiffException {
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
        setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        fillerList();
        write2();
    }

    public void writeFile3() throws IOException, WriteException, BiffException {
        setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        metalList();
        write3();
    }

    public void write2() throws IOException, WriteException, BiffException {

        Workbook workbook = Workbook.getWorkbook(new File(inputFile));
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet sheet1 = copy.createSheet("FillerInformation", 0);

        jxl.write.Label number3 = new jxl.write.Label(0, 0, "ID");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(1, 0, "NAME");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(2, 0, "GOLD");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(3, 0, "SILVER");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(4, 0, "BRONZE");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(5, 0, "IRON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(6, 0, "COPPER");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(7, 0, "TITANIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(8, 0, "PLATINIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(9, 0, "ALUMINIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(10, 0, "TOPAZ");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(11, 0, "PLASTIC");
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

    public void write3() throws IOException, WriteException, BiffException {

        Workbook workbook = Workbook.getWorkbook(new File(inputFile));
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet sheet1 = copy.createSheet("MetalInformation", 1);

        jxl.write.Label number3 = new jxl.write.Label(0, 0, "ID");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(1, 0, "NAME");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(2, 0, "GOLD");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(3, 0, "SILVER");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(4, 0, "BRONZE");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(5, 0, "IRON");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(6, 0, "COPPER");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(7, 0, "TITANIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(8, 0, "PLATINIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(9, 0, "ALUMINIUM");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(10, 0, "TOPAZ");
        sheet1.addCell(number3);

        number3 = new jxl.write.Label(11, 0, "PLASTIC");
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
    public List<FillerEntity> getFillerRecords() {
        return fillerRecords;
    }

    /**
     * @param fillerRecords the fillerRecords to set
     */
    public void setFillerRecords(List<FillerEntity> fillerRecords) {
        this.fillerRecords = fillerRecords;
    }

    /**
     * @return the filteredFillers
     */
    public List<FillerEntity> getFilteredFillers() {
        return filteredFillers;
    }

    /**
     * @param filteredFillers the filteredFillers to set
     */
    public void setFilteredFillers(List<FillerEntity> filteredFillers) {
        this.filteredFillers = filteredFillers;
    }

    /**
     * @return the selectedFiller
     */
    public FillerEntity getSelectedFiller() {
        return selectedFiller;
    }

    /**
     * @param selectedFiller the selectedFiller to set
     */
    public void setSelectedFiller(FillerEntity selectedFiller) {
        this.selectedFiller = selectedFiller;
    }

    public void updateFiller() {
        System.out.println("here");

        hiYewSystemBean.editFiller(selectedFiller);
    }

    public void updateMetal() {
        hiYewSystemBean.editMetal(selectedMetal);
    }

    /**
     * @return the copper
     */
    public int getCopper() {
        return copper;
    }

    /**
     * @param copper the copper to set
     */
    public void setCopper(int copper) {
        this.copper = copper;
    }

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
            fillerList = hiYewSystemBean.FillersNotAssociated(metalName);
            fillerChosen = hiYewSystemBean.FillersAssociated(metalName);
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
        return hiYewSystemBean.metalNames();
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
        hiYewSystemBean.createPairings(metalName, fillerChosen);
    }
}
