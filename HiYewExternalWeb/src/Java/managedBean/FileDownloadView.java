package managedBean;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
public class FileDownloadView {

    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static Font small = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static Font catFontUnderLine = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD + Font.UNDERLINE);

    private ProductPurchaseOrder po;
    private ProductQuotation quotation;

    private StreamedContent file;

    private static String totalQuantity;

    public FileDownloadView() {
        po = new ProductPurchaseOrder();
        quotation = new ProductQuotation();
    }

    public StreamedContent getFile(ProductPurchaseOrder newPurchaseOrder, ProductQuotation selectedProductionQuotation) throws FileNotFoundException, IOException, DocumentException {

        po = newPurchaseOrder;
        quotation = selectedProductionQuotation;

        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Temp\\hiyew_po_" + quotation.getProductQuotationNo() + ".pdf"));
        // step 3
        document.open();
        // step 4

//        addMetaData(document);
        addTitlePage(document);
        //addContent(document);

        // step 5
        document.close();
        writer.close();

        System.out.println("PDF Created!");
        InputStream stream = new FileInputStream("C:\\Temp\\hiyew_po_" + quotation.getProductQuotationNo() + ".pdf");
        file = new DefaultStreamedContent(stream, "application/pdf", "hiyew_po_" + quotation.getProductQuotationNo() + ".pdf");

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

    private void addTitlePage(Document document) throws DocumentException, IOException {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        Paragraph preface = new Paragraph();

        Image image = Image.getInstance("C:\\Users\\JustHRJ\\Desktop\\Final Presentation\\IN05\\HiYewExternalWeb\\web\\images\\logo6.png");
       
        document.add(image);

        addEmptyLine(preface, 1);

   
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Dear " + po.getCustomer().getName() + ", ", small));
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Purchase Order #" + po.getProductPurchaseOrderCustomerID(), small));
        addEmptyLine(preface, 1);

        createTable(preface, quotation.getProductQuotationDescriptionList());
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Total Quantity: " + totalQuantity));
        preface.add(new Paragraph("Total Price (SGD): $ " + String.format("%.2f", po.getTotalPrice())));
        preface.add(new Paragraph("Delivery Date: " + (po.getDeliveryDate() == null ? "(We will send you an email update)" : po.getDeliveryDate()), small));
        preface.add(new Paragraph("Mailing Address: " + po.getMailingAddr1() + ", " + po.getMailingAddr2(), small));

//        String desc = "";
//        for (ProductQuotationDescription pqd : quotation.getProductQuotationDescriptionList()) {
//            desc = desc + pqd.getProductQuotationDescNo().toString() + ". " + pqd.getItemName() + " - (Unit price) SGD " + String.format("%.2f", pqd.getQuotedPrice()) + " x " + pqd.getQuantity() + "" + "\r\n";
//        }
//        
//        preface.add(new Paragraph("Item(s) details: " + desc, subFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generated: " + new Date(), redFont));
        document.add(preface);
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor(null, catFont);
        anchor.setName("");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 0);

//        Paragraph subPara = new Paragraph("", subFont);
        Section subCatPart = catPart.addSection("");
        subCatPart.add(new Paragraph("Hello"));

//        subPara = new Paragraph(null, subFont);
        subCatPart = catPart.addSection("");
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 2);
        subCatPart.add(paragraph);

        // add a table
        //createTable(subCatPart);
        // now add all this to the document
        document.add(catPart);

//        // Next section
//        anchor = new Anchor("Second Chapter", catFont);
//        anchor.setName("Second Chapter");
//
//        // Second parameter is the number of the chapter
//        catPart = new Chapter(new Paragraph(anchor), 1);
//
//        subPara = new Paragraph("Subcategory", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("This is a very important message"));
//
//        // now add all this to the document
//        document.add(catPart);
    }

    private static void createTable(Paragraph preface, List<ProductQuotationDescription> pqdList) throws BadElementException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

//        t.setBorderColor(BaseColor.GRAY);
//        t.setPadding(4);
//        t.setSpacing(4);
//        t.setBorderWidth(1);
        PdfPCell c1 = new PdfPCell(new Phrase("Item Name"));
        c1.setGrayFill(2);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Quoted Unit Price (SGD)"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Quantity"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);
        table.setHeaderRows(1);

        String desc = "";
        for (ProductQuotationDescription pqd : pqdList) {
            table.addCell(pqd.getItemName());
            table.addCell("$ " + String.format("%.2f", pqd.getQuotedPrice()));
            table.addCell(pqd.getQuantity().toString());
            totalQuantity = pqd.getProductQuotationDescNo().toString();
//            desc = desc + pqd.getProductQuotationDescNo().toString() + ". " + pqd.getItemName() + " - (Unit price) SGD " + String.format("%.2f", pqd.getQuotedPrice()) + " x " + pqd.getQuantity() + "" + "\r\n";
        }

//        table.addCell("1.0");
//        table.addCell("1.1");
//        table.addCell("1.2");
//        table.addCell("2.1");
//        table.addCell("2.2");
//        table.addCell("2.3");
        preface.add(table);

    }

    private static void createList(Section subCatPart) {
//        List list = new List(true, false, 10) {};
//        list.add(new ListItem("First point"));
//        list.add(new ListItem("Second point"));
//        list.add(new ListItem("Third point"));
//        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
