package com.rame.rpmz.process;

import com.rame.rpmz.utils.FileNameUtility;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IDS60FileNameProcessor implements Processor {

    public static final String ORIGINAL_FILE_NAME = "CamelFileNameOriginal";

    @Value("${rpmz.route.companyId}")
    private String companyId;

    @Value("${rpmz.route.companycode}")
    private String companyCode;

    @Override
    public void process(Exchange exchange) throws Exception {
        try {           

            log.info("File Name only = {}", exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY, String.class));
            String originalFileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
            exchange.getIn().setHeader(ORIGINAL_FILE_NAME, originalFileName);
            String newFileName = constructFileName(originalFileName);
            exchange.getIn().setHeader(Exchange.FILE_NAME, newFileName);
            log.info("New File Name = {}", exchange.getIn().getHeader(Exchange.FILE_NAME, String.class));
        
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        
    }

    private String constructFileName(String originalFileName) {

        String fileExtension = FileNameUtility.getExtension(originalFileName);
        log.info("FileExtension = " + fileExtension);
        String fileNoExt = FileNameUtility.removeExtension(originalFileName);
        log.info("File Name without extension = " + fileNoExt);

        // replaces the string with empty string till the first occurance of the underscore char.
        fileNoExt = fileNoExt.replaceFirst(".*?_", "");
        // replaces the string that matches _\\d{14}+ with empty string 
        fileNoExt = fileNoExt.replaceFirst("_\\d{14}+", "");

        log.info("Trimed File Name = {}", fileNoExt);

        String newFileName = null;
        
        String suffix = "";
        String prefix = companyId;
        
        newFileName = FileNameUtility.consructFileName(prefix, fileNoExt, suffix, fileExtension);
        
        return newFileName;
    }

}
