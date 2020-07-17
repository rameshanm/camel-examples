package com.rame.rpmz.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

/**
 * IDS60FileTransferTest
 */

@SpringBootTest
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
@Slf4j
public class IDS60FileTransferTest {

    // @Autowired
    // private Environment env;
   
    // @Autowired
    // private CamelContext camelContext;

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new IDS60RouteBuilder();
    }
        
    @Test
    @DisplayName("Test the IDS60 Succes Route")
    public void testSuccessRoute() throws Exception {

        // MockEndpoint resultEndpoint = getSuccessEndPoint();
        String fileName = "AEG_DWH_Agent_20200421172619.csv";
        
        Path from = Paths.get("src/test/resources/" + fileName);
        Path to = Paths.get("target/test/ids60/input/" + fileName);
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                
        Thread.sleep(4000);

        int result = new File("target/test/ids60/nas-output").list().length;

        assertNotEquals(0, result);
        assertEquals(2, result);
        // resultEndpoint.assertIsSatisfied();
    }

    // private MockEndpoint getSuccessEndPoint() {
    //     MockEndpoint resultEndpoint = camelContext.getEndpoint(env.getProperty("rpmz.route.ids76.aeg.dest"), MockEndpoint.class);
    //     log.info(" Checkif it is a mock endpoint in the properties file " + resultEndpoint);
    //     resultEndpoint.expectedMessageCount(1); 
    //     resultEndpoint.message(0).header("errorMessage").isNull();
    //     return resultEndpoint;
    // }
  

    @BeforeAll
    public static void setupOnce() throws Exception {

        log.info("creating temp directories for test");
        
        Files.createDirectories(Paths.get("target/test/ids60/input/"));
        Files.createDirectories(Paths.get("target/test/ids60/nas-output/"));
    }

    @AfterAll
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
