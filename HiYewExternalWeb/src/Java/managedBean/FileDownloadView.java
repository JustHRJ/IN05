package managedBean;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
public class FileDownloadView {

    private static String FILE = "c:/temp/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    private ProductPurchaseOrder po;
    private ProductQuotation quotation;

    private StreamedContent file;

    public FileDownloadView() {
        po = new ProductPurchaseOrder();
        quotation = new ProductQuotation();
    }

    public StreamedContent getFile(ProductPurchaseOrder newPurchaseOrder, ProductQuotation selectedProductionQuotation) throws FileNotFoundException, IOException, DocumentException {

        po = newPurchaseOrder;
        quotation = selectedProductionQuotation;
        System.out.println("Get File..........." + po.getMailingAddr1());

        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\QiWen\\Documents\\NetBeansProjects\\IN05\\HiYewExternalWeb\\web\\pdf\\pdf_testing1.pdf"));
        // step 3
        document.open();
        // step 4

        //addMetaData(document);
        addTitlePage(document);
        //addContent(document);

        // step 5
        document.close();
       // writer.close();

//        try{
//            Thread.sleep(1000);
//        }catch (Exception e){
//            System.out.println("sleeping....");
//        }
        System.out.println("PDF Created!");

        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/pdf/pdf_testing1.pdf");

        file = new DefaultStreamedContent(stream, "application/pdf", "pdf_testing_outcome_1.pdf");

        return file;
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private void addTitlePage(Document document) throws DocumentException {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

//        System.out.println("Current Date: " + ft.format(dNow));
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Purchase Order: #" + po.getProductPurchaseOrderCustomerID(), catFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Customer Name: " + po.getCustomer().getName(), catFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Delivery Date: " + (po.getDeliveryDate() == null ? "(We will send you an email update.)" : po.getDeliveryDate()), catFont));

        String desc = "";
        for (ProductQuotationDescription pqd : quotation.getProductQuotationDescriptionList()) {
            desc = desc + pqd.getProductQuotationDescNo().toString() + ". " + pqd.getItemName() + " - (Unit price) SGD " + String.format("%.2f", pqd.getQuotedPrice()) + " x " + pqd.getQuantity() + "" + "\r\n";
        }

        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Item(s) details: " + desc, catFont));

        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Total Price (SGD $): " + po.getTotalPrice(), catFont));

        System.out.println("aaaaaaa" + po.getMailingAddr1());
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Mailing Address: " + po.getMailingAddr1() + ", " + po.getMailingAddr2(), catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 1);

        preface.add(new Paragraph(ft.format(dNow), redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
//    List list = new List(true, false, 10);
//    list.add(new ListItem("First point"));
//    list.add(new ListItem("Second point"));
//    list.add(new ListItem("Third point"));
//    subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
