package ru.clevertec.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.entity.data.CarDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author S.Leshkevich
 * @version 1.0
 * the class is designed to generate files
 */
public class FileHandler {

    private static final Logger logger = LogManager.getLogger(FileHandler.class);
    public static final String TEMPLATE_PDF = "src/main/resources/Clevertec_Template.pdf";
    public static final Font FONT_HEAD_TABLE = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11);
    public static final Font FONT_BODY_TABLE = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
    public static final Font FONT_NAME_TABLE = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14);

    /**
     * method for generating a file of type .pdf
     *
     * @param filename   file name
     * @param carDTOList list CarDTO
     */
    public void writeTableToFilePDF(final String filename, List<CarDTO> carDTOList, String tableName) {
        Document document = new Document(PageSize.A4, 30, 0, 200, 0);
        Paragraph paragraph = new Paragraph(tableName, FONT_NAME_TABLE);


        try (FileOutputStream fileOutputStream = new FileOutputStream(filename, true)) {
            var writer = PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            PdfReader reader = new PdfReader(TEMPLATE_PDF);
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            PdfContentByte contentByte = writer.getDirectContentUnder();
            contentByte.addTemplate(page, 0, 0);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(90);

            Field[] fields = CarDTO.class.getDeclaredFields();
            for (Field field : fields) {
                table.addCell(new PdfPCell(new Phrase(field.getName().toUpperCase(), FONT_HEAD_TABLE)));
            }

            carDTOList.forEach(carDTO -> {
                table.addCell(new PdfPCell(new Phrase(carDTO.brand(), FONT_BODY_TABLE)));
                table.addCell(new PdfPCell(new Phrase(carDTO.model(), FONT_BODY_TABLE)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(carDTO.bodyType()).toLowerCase(), FONT_BODY_TABLE)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(carDTO.engineCapacity()), FONT_BODY_TABLE)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(carDTO.fuelType()).toLowerCase(), FONT_BODY_TABLE)));
            });

            document.add(paragraph);
            document.add(new Paragraph("\n"));
            document.add(table);
        } catch (DocumentException | IOException e) {
            logger.info(e.getMessage());
        } finally {
            document.close();
        }
    }
}
