/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import session.stateless.TransferSystemBeanLocal;

/**
 *
 * @author JustHRJ
 */
@Named(value = "transfermanageBean")
@RequestScoped
public class TransfermanageBean {

    @EJB
    private TransferSystemBeanLocal TransferSystemBean;

    private String inputFile;

    /**
     * Creates a new instance of TransfermanageBean
     */
    public TransfermanageBean() {
    }

    public void readFile() throws IOException {
        System.out.println("system got into here");
        setInputFile("C:\\Users\\User\\Desktop\\ProjectData.xls");
        List<Vector> result = readProjects();
        TransferSystemBean.addProjects(result);
        System.out.println("new Projects data have been added");
        result = readWelds();
        TransferSystemBean.addWelds(result);
        System.out.println("new Weldjob data have been created");
        result = readConnectProjectWeld();
        TransferSystemBean.connectProjectWelds(result);
        System.out.println("Project and weld jobs have been connected");
        System.out.println("completed");

    }

    public List<Vector> readConnectProjectWeld() throws IOException {
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

    public List<Vector> readWelds() throws IOException {
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

    public List<Vector> readProjects() throws IOException {
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

    /**
     * @return the inputFile
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * @param inputFile the inputFile to set
     */
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

}
