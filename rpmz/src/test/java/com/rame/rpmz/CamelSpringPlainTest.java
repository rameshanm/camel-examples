package com.rame.rpmz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CamelSpringPlainTest {

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;
  
    @Test
    @Disabled
    public void testPositive() throws Exception {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());

        template.sendBodyAndHeader("file://target/test/input", "Hello World", Exchange.FILE_NAME, "hello.txt");

        Thread.sleep(2000);

        File target = new File("target/test/output/hello.txt");
        // assertTrue("File not moved", target.exists());
        String content = camelContext.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);
    }
}