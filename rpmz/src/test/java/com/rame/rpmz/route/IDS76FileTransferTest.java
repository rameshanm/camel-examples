package com.rame.rpmz.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ContextConfiguration
@TestPropertySource("classpath:application-test.properties")
@Slf4j
public class IDS76FileTransferTest {

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new IDS76RouteBuilder();
    }

    @Test
    @DisplayName("Test the IDS76 Route")
    public void testSuccessRoute() throws Exception {

        String fileName = "IDS_76_Distribution.Fund.Yield.csv";
        Path from = Paths.get("src/test/resources/" + fileName);
        Path to = Paths.get("target/test/ids76/nas-source/" + fileName);
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                
        Thread.sleep(4000);

        int result = new File("target/test/ids76/scm-output").list().length;

        assertNotEquals(0, result);
        assertEquals(1, result);
    }
    
    @BeforeAll
    public static void setupOnce() throws Exception {

        log.info("creating temp directories for test");
        
        Files.createDirectories(Paths.get("target/test/ids76/nas-source/"));
        Files.createDirectories(Paths.get("target/test/ids76/scm-output/"));
    }

    @AfterAll
    @Disabled
    public static void teardownOnce() throws Exception {
        log.info("Deleting temp directories");

        // File testDir = new File("target/test");
        // deleteDirectory(testDir);
    }
    

     /**
     * Deletes File or Directory
     * 
     * @param dir
     * @return
     */
    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        } // either file or an empty directory
        return dir.delete();
    }
    
}