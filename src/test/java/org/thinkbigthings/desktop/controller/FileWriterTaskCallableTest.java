package org.thinkbigthings.desktop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileWriterTaskCallableTest {

    private FileWriterTaskCallable writer;

    @BeforeEach
    public void setup() throws IOException {

        String tempFile = Files.createTempFile(Paths.get("build" ), "test.", ".txt").toAbsolutePath().toString();
        writer = new FileWriterTaskCallable(100, tempFile);
    }

    @Test
    @DisplayName("Filewriter task writes file")
    void testWriteFile() throws IOException {

        writer.call();

        List<String> lines = Files.readAllLines(new File(writer.getTaskFilename()).toPath());
        assertEquals(writer.getTaskNumLines(), lines.size());
    }
}