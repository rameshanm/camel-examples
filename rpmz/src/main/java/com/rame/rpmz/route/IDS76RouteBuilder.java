package com.rame.rpmz.route;

import com.rame.rpmz.process.IDS76FileNameProcessor;
import com.rame.rpmz.process.IDS76FileNameProcessor2;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IDS76RouteBuilder extends RouteBuilder {

    private static final String IDS76_CONTROL_FILE_FLOW = "seda:ids76ContolFileFlow";

    @Value("${rpmz.route.ids76.nas.inbound.location}")
    private String ids76naslocation;

    @Value("${rpmz.route.ids76.scm.inbound.destination}")
    private String ids76scmlocation;

    @Value("${rpmz.route.ids76.scm.outbound.source}")
    private String ids76scmsource;

    @Value("${rpmz.route.ids76.nas.outbound.location}")
    private String ids76naslocation2;

    @Autowired
    private IDS76FileNameProcessor processor;

    @Autowired
    private IDS76FileNameProcessor2 processor2;

    
    @Override
    public void configure() throws Exception {

        // errorHandler(deadLetterChannel("log:dead?level=ERROR").useOriginalMessage());

        onException(Exception.class).handled(true).useOriginalMessage().to("direct:error");

        moveFilesToScm();
        moveFilesToNas();
        ids76createControlFile();
    }

    /**
     * Route to move the files from SCM Source Location to NAS drive and also the
     * respective Control should be created.
     * 
     */
    private void moveFilesToNas() {
        from(ids76scmsource).routeId("SCM-2-NAS-IDS76-Router")
            .log(LoggingLevel.INFO, "File ${header.CamelFileName} has arrived")
            .process(processor2)
            .to(ids76naslocation).to(IDS76_CONTROL_FILE_FLOW)
            .log("File renamed as ${header.CamelFileName} and moved to NAS Destination");
    }

    private void ids76createControlFile() {

        from(IDS76_CONTROL_FILE_FLOW)
            .routeId("IDS76-ControlFileRouter")
            .split().tokenize("\n")
            .log(LoggingLevel.INFO, "Number of lines = ${exchangeProperty.CamelSplitSize}")
            .setBody(simple("${exchangeProperty.CamelSplitSize}"))
            .convertBodyTo(String.class)
            .setHeader(Exchange.FILE_NAME, simple("${file:name.noext}_Control.${file:name.ext}"))
            .log(LoggingLevel.INFO, "Control File is created")
            .to(ids76naslocation2).end()
            .log(LoggingLevel.INFO, "Control File has been moved to NAS destination")
            .end();
    }

    private void moveFilesToScm() {
        from(ids76naslocation).routeId("IDS76-NAS-2-SEC-Route")
            .log(LoggingLevel.INFO, "File ${header.CamelFileName} has arrived")
            .process(processor)
            .to(ids76scmlocation)
            .log("File renamed as ${header.CamelFileName} and moved to SCM Location").end();
    }

}