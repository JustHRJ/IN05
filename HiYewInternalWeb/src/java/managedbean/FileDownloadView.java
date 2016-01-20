package managedbean;

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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Customer;
import entity.ProductPurchaseOrder;
import entity.ProductQuotation;
import entity.ProductQuotationDescription;
import entity.Quotation;
import entity.QuotationDescription;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class FileDownloadView implements Serializable{

    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static Font small = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static Font catFontUnderLine = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD + Font.UNDERLINE);

    private String callSign;
    private String blkAddress;
    private String row1Address;
    private String row2Address;
    private String postalCode;
    private Quotation quotation;
    private static String quotationDescription;

    private StreamedContent file;

    private static String totalQuantity;

    public FileDownloadView() {

        quotation = new Quotation();
    }

    public StreamedContent getFile(Quotation selectedQuotation) throws FileNotFoundException, IOException, DocumentException {

        quotation = selectedQuotation;

        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Temp\\hiyew_po_" + quotation.getQuotationNo() + ".pdf"));
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
        InputStream stream = new FileInputStream("C:\\Temp\\hiyew_po_" + quotation.getQuotationNo() + ".pdf");
        file = new DefaultStreamedContent(stream, "application/pdf", "hiyew_po_" + quotation.getQuotationNo() + ".pdf");

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
        SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy");
        Customer c = quotation.getCustomer();
        Paragraph preface = new Paragraph();

        Image image = Image.getInstance("C:\\Users\\JustHRJ\\Desktop\\Final Presentation\\IN05\\HiYewExternalWeb\\web\\images\\logo6.png");
        document.add(image);

        preface.add(new Paragraph("Date: " + ft.format(dNow)));
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("To: " + blkAddress));

        preface.add(new Paragraph("     " + row1Address));

        preface.add(new Paragraph("     " + row2Address));

        preface.add(new Paragraph("     Singapore " + postalCode));
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Attn: " + callSign + " " + c.getName() + ", ", small));

        preface.add(new Paragraph("Quotation Reference No. #" + quotation.getQuotationNo(), small));
        createTable(preface, quotation.getQuotationDescriptions());
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("*Quotation is valid for 30 days as from above date.", smallBold));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("We trust the offer  is favourable to you."));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Should you need any clarifications, please do not hesitate to contact us"));

        addEmptyLine(preface, 2);
        preface.add(new Paragraph("Yours faithfully,"));
        preface.add(new Paragraph("Han Kiat"));

//        preface.add(new Paragraph("Total Quantity: " + totalQuantity));
//        preface.add(new Paragraph("Total Price (SGD): $ " + String.format("%.2f", )));
//        preface.add(new Paragraph("Delivery Date: " + (po.getDeliveryDate() == null ? "(We will send you an email update)" : po.getDeliveryDate()), small));
//        preface.add(new Paragraph("Mailing Address: " + po.getMailingAddr1() + ", " + po.getMailingAddr2(), small));
//        String desc = "";
//        for (ProductQuotationDescription pqd : quotation.getProductQuotationDescriptionList()) {
//            desc = desc + pqd.getProductQuotationDescNo().toString() + ". " + pqd.getItemName() + " - (Unit price) SGD " + String.format("%.2f", pqd.getQuotedPrice()) + " x " + pqd.getQuantity() + "" + "\r\n";
//        }
//        
//        preface.add(new Paragraph("Item(s) details: " + desc, subFont));
        document.add(preface);
        document.newPage();
        document.close();
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

    private static void createTable(Paragraph preface, List<QuotationDescription> pqdList) throws BadElementException, DocumentException {
        PdfPTable table = new PdfPTable(4);
        float[] columnWidths = {1f, 5f, 1.5f, 2f};
        table.setWidths(columnWidths);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell c1 = new PdfPCell(new Phrase("S/No."));
        c1.setGrayFill(2);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Description"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Qty"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount Per Set (SGD)"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c1);

        table.setHeaderRows(1);
        int count = 1;
        String desc ="";
        double totalDue = 0;
        for (QuotationDescription pqd : pqdList) {
            PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(count)));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2);

            c2 = new PdfPCell(new Phrase(pqd.getItemDesc()));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c2);

            c2 = new PdfPCell(new Phrase(pqd.getQty().toString()));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2);

            c2 = new PdfPCell(new Phrase(String.format("%.2f", pqd.getPrice())));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(c2);

            totalDue += pqd.getPrice() * pqd.getQty();

            totalQuantity = pqd.getQty().toString();
            count++;
//            desc = desc + pqd.getProductQuotationDescNo().toString() + ". " + pqd.getItemName() + " - (Unit price) SGD " + String.format("%.2f", pqd.getQuotedPrice()) + " x " + pqd.getQuantity() + "" + "\r\n";
        }
        addEmptyLine(preface, 3);
        PdfPCell c3 = new PdfPCell(new Phrase());
        table.addCell(c3);
        table.addCell(c3);
        c3 = new PdfPCell(new Phrase("GST 7%"));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c3);

        c3 = new PdfPCell(new Phrase(String.format("%.2f", totalDue * 0.07)));
        totalDue += totalDue * 0.07;
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase());
        c4.setBorder(Rectangle.NO_BORDER);
        table.addCell(c4);
        table.addCell(c4);

        c4 = new PdfPCell(new Phrase("Total"));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c4);

        c4 = new PdfPCell(new Phrase(String.format("%.2f", totalDue)));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(c4);

//          table.addCell("1.0");
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

    /**
     * @return the blkAddress
     */
    public String getBlkAddress() {
        return blkAddress;
    }

    /**
     * @param blkAddress the blkAddress to set
     */
    public void setBlkAddress(String blkAddress) {
        this.blkAddress = blkAddress;
    }

    /**
     * @return the row1Address
     */
    public String getRow1Address() {
        return row1Address;
    }

    /**
     * @param row1Address the row1Address to set
     */
    public void setRow1Address(String row1Address) {
        this.row1Address = row1Address;
    }

    /**
     * @return the row2Address
     */
    public String getRow2Address() {
        return row2Address;
    }

    /**
     * @param row2Address the row2Address to set
     */
    public void setRow2Address(String row2Address) {
        this.row2Address = row2Address;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the callSign
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * @param callSign the callSign to set
     */
    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public void showDialog2(Quotation selectedQuotation) {
        System.out.println("Show Dialog - Creating Quotation Document");
        System.out.println(selectedQuotation.getQuotationDescriptions().get(0).getItemDesc());
        setQuotationDescription(selectedQuotation.getQuotationDescriptions().get(0).getItemDesc());
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDialogVar2').show();");
    }

    /**
     * @return the quotationDescription
     */
    public String getQuotationDescription() {
        return quotationDescription;
    }

    /**
     * @param quotationDescription the quotationDescription to set
     */
    public void setQuotationDescription(String quotationDescription) {
        this.quotationDescription = quotationDescription;
    }

}
