package ru.testmepls;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import ru.testmepls.model.Info;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTest {
    ClassLoader cl = FileTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/Desktop.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("Desktop.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().contains(".csv")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                    List<String[]> content = reader.readAll();
                    String[] row = content.get(0);
                    assertThat(row[0]).isEqualTo("Period");
                }
            }
        }
    }

    @Test
    void xlsTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/Desktop.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("Desktop.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().contains(".xls")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);
                    assertThat(xls.excel.getSheetAt(0)
                            .getRow(53)
                            .getCell(0)
                            .getStringCellValue())
                            .isEqualTo("HOCO X5 Черный кабель USB 2.4A (TYPE C) 1м");
                }
            }
        }
    }

    @Test
    void pdfTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/Desktop.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("Desktop.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().contains(".pdf")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    System.out.println("");
                    assertThat(pdf.text).contains("Windows & Linux keymap");
                }
            }
        }
    }

    String jsonFile = "nikulin.json";

    @Test
    void jsonTest() throws Exception {
        File file = new File("src/test/resources/"+ jsonFile);
        ObjectMapper objectMapper = new ObjectMapper();
        Info info = objectMapper.readValue(file, Info.class);
        assertThat(info.name + " " + info.surname).isEqualTo("Yuriy Nikulin");
        assertThat(info.age).isEqualTo(101);
        assertThat(info.mother.isActress).isTrue();
    }
}


