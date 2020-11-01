package com.karankumar.bookproject.backend.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public final class CsvUtils {
    public static final String TEXT_CSV = "text/csv";
    private static final CsvMapper csvMapper;

    static {
        csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
    }

    private CsvUtils() {}

    public static <T> List<T> read(InputStream inputStream, Class<T> classType) throws IOException {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        MappingIterator<T> iterator =
                csvMapper.readerFor(classType).with(schema).readValues(inputStream);
        return iterator.readAll();
    }
}
