package com.rame.rpmz;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import com.rame.rpmz.route.FileMoveRoute;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

@Disabled
public class SimpleMockTest extends CamelTestSupport {

    @Value("${file.inbox}")
    private String inbox;

    @Test
    @Disabled
    public void testMock() throws Exception {
        // getMockEndpoint("mock:result").expectedBodiesReceived("Hello World");

        template.sendBodyAndHeader(inbox, "Hello World", Exchange.FILE_NAME, "hello.txt");

        Thread.sleep(2000);

        File target = new File("target/outbox/hello.txt");

        assertTrue("File Not Moved", target.exists());

        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);

    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new FileMoveRoute();
    }

    @Override
    public void setUp() throws Exception {
        
    }

}