/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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
import session.stateful.HiYewSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "knowledgeManageBean")
@RequestScoped
public class KnowledgeManageBean implements Serializable {

    @EJB
    private HiYewSystemBeanLocal hiYewSystemBean;
    private List<Vector> results = new ArrayList<Vector>();
    private String inputFile;

    /**
     * Creates a new instance of KnowledgeManageBean
     */
    public KnowledgeManageBean() {
    }

    public void fillerList() {
        results = hiYewSystemBean.transferFillerInfo();

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
            for (int j = 0; j < sheet.getRows(); j++) {
                Vector im = new Vector();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell = sheet.getCell(i, j);
                    CellType type = cell.getType();
                    if (type == CellType.NUMBER) {
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

    public void readFile() throws IOException {

        KnowledgeManageBean test = new KnowledgeManageBean();

        test.setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        List<Vector> result = test.read();
        hiYewSystemBean.addFillers(result);
    }

    public void writeFile() throws IOException, WriteException, BiffException {
        KnowledgeManageBean test = new KnowledgeManageBean();
        test.setInputFile("C:\\Users\\JustHRJ\\Desktop\\Book1.xls");
        test.write();
    }

    public void write() throws IOException, WriteException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File(inputFile));
        WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
        WritableSheet sheet1 = copy.getSheet(0);
        int rows = sheet1.getRows();
        if (rows == 0) {
            for (int i = 0; i < 10; i++) {
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
        setInputFile("C:\\Users\\QiWen\\Desktop\\Book1.xls");
        fillerList();
        write2();
    }

    public void write2() throws IOException, WriteException, BiffException {

        if (results != null) {
            Workbook workbook = Workbook.getWorkbook(new File(inputFile));
            WritableWorkbook copy = Workbook.createWorkbook(new File(inputFile), workbook);
            WritableSheet sheet1 = copy.createSheet("FillerInformation", 0);
            System.out.println("here");

            // size is 0.. why?
            int rows = results.size();
            for (int i = 0; i < rows; i++) {
                Vector im = results.get(i);
                int cols = im.size();
                System.out.println(cols);
                for (int j = 0; j < cols; j++) {
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

}
