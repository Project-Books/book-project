package com.karankumar.bookproject.backend.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public final class CsvUtils {
    private CsvUtils() {}

    public static final String TEXT_CSV = "text/csv";

    public static <T> List<T> read(InputStream inputStream, Class<T> classType) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        MappingIterator<T> iterator = mapper.readerFor(classType).with(schema).readValues(inputStream);
        return iterator.readAll();
    }
}
