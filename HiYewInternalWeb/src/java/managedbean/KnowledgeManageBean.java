/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.FillerEntity;
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
import session.stateful.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "knowledgeManageBean")
@RequestScoped
public class KnowledgeManageBean {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private List<Vector> results = new ArrayList<Vector>();
    private String inputFile;
    private List<FillerEntity> fillerRecords;
    private List<FillerEntity> filteredFillers;
    private FillerEntity selectedFiller;
    private int copper;
    /**
     * Creates a new instance of KnowledgeManageBean
     */
    public KnowledgeManageBean() {
    }

    
  
    
    public void fillerList() {
        results = hiYewSystemBean.transferFillerInfo();

    }

    public List<FillerEntity> getAllFillers() {
        fillerRecords = hiYewSystemBean.fillerRecords();
        return fillerRecords;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
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
                    CellType type = cell.getType();

                    im.add(cell.getContents());

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

    public void readFile() throws IOException {

        setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        List<Vector> result = read();
        hiYewSystemBean.addFillers(result);
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

    public void write2() throws IOException, WriteException, BiffException {

        if (results != null) {
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

            int rows = results.size();
            for (int i = 1; i < rows; i++) {
                Vector im = results.get(i);
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
            copy.removeSheet(1);
            System.out.println("done1");
            copy.write();

            copy.close();
        }
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

    public void updateFiller(RowEditEvent event) {
        System.out.println("here");
        System.out.println(((FillerEntity)event.getObject()).getCopper());
        hiYewSystemBean.editFiller((FillerEntity) event.getObject());
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
}
