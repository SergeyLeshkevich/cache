package ru.clevertec.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.entity.data.CarDTO;
import ru.clevertec.enums.BodyType;
import ru.clevertec.enums.Fuel;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileHandlerTest {

    public static final String TEST_FILE = "src/test/resources/Test.pdf";
    public static final String TABLE_NAME = "Test table";
    private FileHandler fileHandler;

    @BeforeEach
    void init() {
        fileHandler = new FileHandler();
    }

    @Test
    void checkFilePDF() {
        List<CarDTO> carDTOList = new ArrayList<>();
        carDTOList.add(new CarDTO("Toyota", "Camry", BodyType.SEDAN, 2.5, Fuel.DIESEL));
        carDTOList.add(new CarDTO("Honda", "Civic", BodyType.SEDAN, 1.8, Fuel.DIESEL));

        fileHandler.writeTableToFilePDF(TEST_FILE, carDTOList, TABLE_NAME);

        File file = new File(TEST_FILE);
        assertTrue(file.exists());
    }

    @Test
    void checkWriteTableToFilePDF() {
        // given
        String expected = " \n" +
                " \n" +
                " \n" +
                " \n" +
                " \n" +
                " \n" +
                " \n" +
                "Test table\n" +
                " \n" +
                " \n" +
                "BRAND MODEL BODYTYPE ENGINECAPACITY FUELTYPE\n" +
                "Toyota Camry sedan 2.4 petrol\n" +
                "BMW X5 sedan 3.0 diesel";
        List<CarDTO> carDTOList = new ArrayList<>();
        CarDTO car1 = new CarDTO("Toyota", "Camry", BodyType.SEDAN, 2.4, Fuel.PETROL);
        CarDTO car2 = new CarDTO("BMW", "X5", BodyType.SEDAN, 3.0, Fuel.DIESEL);
        carDTOList.add(car1);
        carDTOList.add(car2);

        // when
        fileHandler.writeTableToFilePDF(TEST_FILE, carDTOList, TABLE_NAME);

        // then
        String actual = ReadPDF(TEST_FILE);
        assertThat(actual).isEqualTo(expected);
    }

    private static String ReadPDF(String fileName) {
        StringBuilder str = new StringBuilder();
        try {

            PdfReader reader = new PdfReader(fileName);
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                String str2 = PdfTextExtractor.getTextFromPage(reader, i);
                str.append(str2);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return str.toString();
    }
}
