package org.thinkbigthings.desktop.controller;

import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileWriterTaskTest {

    private FileWriterService service;

    private File tempFile;
    private long numLines;

    @BeforeAll
    public static void setupPlatform() {

        // calls to Task will fail if the Platform is not available, "Toolkit not initialized"
        // because calls to Task.update() will make calls into the Platform
        Platform.startup(() -> {});
    }

    @AfterAll
    public static void teardownPlatform() {
        Platform.exit();
    }

    @BeforeEach
    public void setup() throws IOException {

        tempFile = Files.createTempFile(Paths.get("build" ), "test.", ".txt").toFile();
        numLines = 100;

        service = new FileWriterService();
        service.setFile(tempFile);
        service.setNumLines(numLines);
    }

    @Test
    @DisplayName("Filewriter task writes file")
    void testWriteFile() throws Exception {

        service.createTask().run();

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(numLines, lines.size());
    }
}