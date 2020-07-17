package com.rame.rpmz.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IDS76FileNameProcessor implements Processor {

    public static final String ORIGINAL_FILE_NAME = "CamelFileNameOriginal";

    @Override
    public void process(Exchange exchange) throws Exception {

        try {           
            log.info("File Name only = {}", exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY, String.class));
            String originalFileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
            exchange.getIn().setHeader(ORIGINAL_FILE_NAME, originalFileName);
            String newFileName = originalFileName.substring(originalFileName.lastIndexOf("_")+1, originalFileName.length());
            exchange.getIn().setHeader(Exchange.FILE_NAME, newFileName);
            log.info("New File Name = {}", exchange.getIn().getHeader(Exchange.FILE_NAME, String.class));
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
}