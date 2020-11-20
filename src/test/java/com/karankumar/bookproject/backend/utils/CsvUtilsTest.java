package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.dto.GoodreadsBookImport;
import com.karankumar.bookproject.backend.util.CsvUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvUtilsTest {

    @Test
    void testReadWhenInputStreamIsCsv() throws IOException {
        InputStream inputStream = getResourceInputStream("goodreadsImportSample.csv");
        List<GoodreadsBookImport> goodreadsBookImports =
                CsvUtils.read(inputStream, GoodreadsBookImport.class);
        assertEquals(5, goodreadsBookImports.size());
    }

    private InputStream getResourceInputStream(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }
}
