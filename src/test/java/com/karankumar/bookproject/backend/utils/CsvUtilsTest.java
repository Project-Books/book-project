/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.dto.GoodreadsBookImport;
import com.karankumar.bookproject.backend.util.CsvUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvUtilsTest {

    @Test
    void testReadWhenInputStreamIsCsv() throws IOException {
        InputStream inputStream = getResourceInputStream("goodreadsImportSample.csv");
        List<GoodreadsBookImport> goodreadsBookImports =
                CsvUtils.read(inputStream, GoodreadsBookImport.class);
        assertThat(goodreadsBookImports.size()).isEqualTo(5);
    }

    private InputStream getResourceInputStream(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }
}
