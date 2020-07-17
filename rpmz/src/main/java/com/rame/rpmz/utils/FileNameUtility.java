package com.rame.rpmz.utils;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility Class to manipulate the File Name.
 * 
 * @author Ramesh Shanmugavel
 * @version 1.0
 * 
 */

@Slf4j
@Component
public class FileNameUtility {

    /**
     * Method to construct a file Name
     * 
     * @param prefix        the prefix string
     * @param content       the content string
     * @param suffix        the suffix string
     * @param fileExtension the file extension string
     * @return the formatted file name
     * 
     */
    public static String consructFileName(String prefix, String content, String suffix, String fileExtension) {

        log.info("prefix = " + prefix + ", Suffix = " + suffix + ", content = " + content + ", fileExtension = "
                + fileExtension);

        String result = null;

        if ((null != prefix) && (null != content) && (null != suffix) && (null != fileExtension)) {

            result = new StringBuffer().append(prefix).append(content).append(suffix).append(fileExtension).toString();
        }
        log.info("File Name Constructed is {} ", result);
        return result;
    }

    /**
     * Method to trim the File extension
     * 
     * @param fileName
     * @return
     */
    public static String removeExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }

    }

    /**
     * Methd to exract the File Extension.
     * 
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        if (null == fileName) {
            return null;
        }
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        } else {
            return "";
        }

    }

}
