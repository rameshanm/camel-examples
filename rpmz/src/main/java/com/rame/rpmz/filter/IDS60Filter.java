package com.rame.rpmz.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("ids60Filter")
public class IDS60Filter implements GenericFileFilter<File> {

    @Value("#{'${incoming.ids60.files}'.split('|')}")
    private List<String> accptedFiles;

    @Override
    public boolean accept(GenericFile<File> file) {

        log.info("Check if file is acceptable {}", file.getFileName());
        List<Pattern> patterns = new ArrayList<Pattern>();
        patterns.add(Pattern.compile("AEG_DWH_Agent_.*.csv"));
        patterns.add(Pattern.compile("AEG_DWH_PolicyHolder_Transaction_.*.csv"));
        patterns.add(Pattern.compile("AEG_DWH_PolicyHolder_Valuation_.*.csv"));
        patterns.add(Pattern.compile("AEP_DWH_Agent_.*.csv"));
        patterns.add(Pattern.compile("AEP_DWH_PolicyHolder_Transaction_.*.csv"));
        patterns.add(Pattern.compile("AEP_DWH_PolicyHolder_Valuation_.*.csv"));
        if(!file.isDirectory()) {
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(file.getFileName());
                log.info("File name {} matches Pattern {} - {} ", file.getFileName(), pattern, matcher.matches());
                return matcher.matches();
            }
        }
        return false;
    }
    
}