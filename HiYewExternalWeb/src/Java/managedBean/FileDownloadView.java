package managedBean;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
public class FileDownloadView {

    private StreamedContent file;

    public FileDownloadView() throws FileNotFoundException {
    }

    public StreamedContent getFile() throws FileNotFoundException, IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\QiWen\\Documents\\NetBeansProjects\\IN05\\HiYewExternalWeb\\web\\pdf\\pdf_testing1.pdf"));
        // step 3
        document.open();
        // step 4
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream("C:\\Users\\QiWen\\Documents\\NetBeansProjects\\IN05\\HiYewExternalWeb\\web\\newjsf.xhtml"), null);
        // step 5
        document.close();

        System.out.println("PDF Created!");

        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/pdf/pdf_testing1.pdf");
        file = new DefaultStreamedContent(stream, "application/pdf", "pdf_testing_outcome_1.pdf");
        return file;
    }
}
