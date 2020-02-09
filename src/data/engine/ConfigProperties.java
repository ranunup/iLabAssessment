/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.engine;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author phumlani
 */
public class ConfigProperties {
    
    private final String propertiesFile = "resources/config.properties";
    private Properties properties;
    
    public ConfigProperties() {
        loadProperties();
    }
    
    public String getWebDriver() {
        try {
            
            return properties.getProperty("webDriver");
            
        } catch(Exception ex) {
            System.err.println("[Conf Properties Error] Failed to get web driver, error - " + ex.getMessage());
        }
        return null;
    }
    
    public String getScreenshotDir() {
        try {
            return properties.getProperty("screenshotDir");
        } catch(Exception ex) {
            System.err.println("[Conf Properties Error] Failed to get screenshot directory, error - " + ex.getMessage());
        }
        return null;
    }
    
    public String getTestReportDir() {
        
        try {
            return properties.getProperty("testReportDir");
        } catch (Exception ex) {
            System.err.println("[Conf Properties Error] Failed to get test report directory, error - " + ex.getMessage());
        }
        
        return null;
    }
    
    public String getTestExecutionMethod() {
        try {
            return properties.getProperty("testExecMethod");
        } catch(Exception ex) {
            System.err.println("[Conf Properties Error] Failed to get test execution method, error - " + ex.getMessage());
        }
        return null;
    }
    
    public String getChromeDriverLocation() {
        try {
            
            return properties.getProperty("chromeDriverLoc");
            
        } catch(Exception ex) {
            System.err.println("[Conf Properties Error] Failed to get chrome driver location, error - " + ex.getMessage());
        }
    	return null;
    }
    
    public String getTestFileLocation() {
        try {
            
            return properties.getProperty("testFileDir");
            
        } catch(Exception ex) {
            System.err.println("[Conf Properties Error] Failed to get test file location, error - " + ex.getMessage());
        }
    	return null;
    }
    
    public InputStream getInStream () {
        return getClass().getClassLoader().getResourceAsStream(propertiesFile);
    }
    
    private void loadProperties() {
        try {
            InputStream inStream = getInStream();
            if(inStream!=null) {
                properties = new Properties();
                properties.load(inStream);
            }
        } catch (Exception ex) {
            System.err.println("[Conf Properties Error] Failed to load properties, error - " + ex.getMessage());
        }
    }
}
