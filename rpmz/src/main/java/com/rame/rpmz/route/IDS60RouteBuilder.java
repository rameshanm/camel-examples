package com.rame.rpmz.route;

import com.rame.rpmz.process.ErrorProcessor;
import com.rame.rpmz.process.IDS60FileNameProcessor;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IDS60RouteBuilder extends RouteBuilder {

    private final String IDS60_CONTROL_FILE_FLOW = "seda:ids60controlFileCreaion";

    private final String ERROR_FLOW = "direct:error";

    @Value("${rpmz.route.ids60.scm.outbound.source}")
    private String ids60secsource;

    @Value("${rpmz.route.ids60.nas.outbound.destination}")
    private String ids60naslocation;

    @Autowired
    private IDS60FileNameProcessor processor;

    @Autowired
    private ErrorProcessor errorProcessor;

    @Override
    public void configure() throws Exception {
        
        onException(Exception.class)
            .useOriginalMessage()
            .maximumRedeliveries(1)
            .redeliveryDelay(3000)
            .logRetryAttempted(true).retryAttemptedLogLevel(LoggingLevel.ERROR)
            .handled(true)
            .to(ERROR_FLOW);

        moveFilesToNAS();
        createControlFile();
        errorFlow();
    }

    private void errorFlow() {
        from(ERROR_FLOW).routeId("Error-Route")
            .log(LoggingLevel.ERROR, "Exception Occured")
            .process(errorProcessor)
            .end();
    }

    private void moveFilesToNAS() {

        from(ids60secsource)
            .routeId("SCM-2-NAS-IDS60-Router")
            .log(LoggingLevel.INFO, "File ${header.CamelFileName} has arrived")
            .process(processor)
            .to(ids60naslocation)
            .to(IDS60_CONTROL_FILE_FLOW)
            .log("File renamed as ${header.CamelFileName} and moved to NAS Destination");
    }

    private void createControlFile() {

        from(IDS60_CONTROL_FILE_FLOW)
            .routeId("IDS60-ControlFileRouter")
            .split().tokenize("\n")
            .log(LoggingLevel.INFO, "Number of lines = ${exchangeProperty.CamelSplitSize}")
            .setBody(simple("${exchangeProperty.CamelSplitSize}"))
            .convertBodyTo(String.class)
            .setHeader(Exchange.FILE_NAME, simple("${file:name.noext}_Control.${file:name.ext}"))
            .log(LoggingLevel.INFO, "Control File is created")
            .to(ids60naslocation).end()
            .log(LoggingLevel.INFO, "Control File has been moved to NAS destination")
            .end();
    }
    
}