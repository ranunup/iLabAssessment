/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import test.model.ILabTestModel;
import utils.Enums.ExecTestFlag;

/**
 *
 * @author ndoda
 */
public class DatabaseReader {
    Connection con;
    
    public DatabaseReader() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/iLab","root","Zmahlanza@1");   
        } catch (SQLException ex) {
            System.err.println("[Database Reader Error] Failed to establish database connection, error - " + ex.getMessage());
        }
    }
    
    public List<ILabTestModel> getTestCasesFromDB () {
        
        List<ILabTestModel> testCases = null;
        try {
            
            if (con!=null) {
                
                ILabTestModel testCase = new ILabTestModel();
                testCases = new ArrayList<>();
                
                Statement stmt = con.createStatement();
                
                String prevTestSuiteID = null;
                List<String> testData = new ArrayList<>();
                String key, value;
                
                ResultSet rs = stmt.executeQuery("SELECT suite.ts_id, suite.ts_description, t_case.tc_command, t_case.tc_value "
                                                + "FROM test_suite suite "
                                                + "LEFT JOIN test_case t_case on suite.ts_id = t_case.tc_id "
                                                + "WHERE suite.exec_flag = true");
                
                while (rs.next()) {
                    
                    // getting the initial test case
                    if (prevTestSuiteID==null) {
                        prevTestSuiteID = rs.getString("ts_id");
                        
                        testCase = this.initTestCase(rs.getString("ts_id"), rs.getString("ts_description"));
                        
                    } else {
                        // if we encounter a new test case
                        // we want to add the previous one to our test case list then prepare to complile a new test case
                        if (!rs.getString("ts_id").equals(prevTestSuiteID)) {
                            
                            prevTestSuiteID = rs.getString("ts_id");
                            testCase.setTestData(testData);
                            testCases.add(testCase);
                            
                            testData = new ArrayList<>();
                            testCase = this.initTestCase(rs.getString("ts_id"), rs.getString("ts_description"));
                        }
                    }
                    
                    key = rs.getString("tc_command");
                    value = rs.getString("tc_value");
                    
                    testData.add(key + "-" + value);
                    
                    // if we've reached the end of our result set 
                    // and we do not have the current test case on our list of test cases then we want to add it
                    if (rs.isLast()) {
                        testCase.setTestData(testData);
                        if (!testCases.contains(testCase)) {
                            testCases.add(testCase);
                        }
                    }
                }
                
                con.close();
            }
            
        } catch (SQLException ex) {
            System.err.println("[Database Reader Error] Failed to get test case from database, error - " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("[Database Reader Error] Failed to get test case from database, error - " + ex.getMessage());
        }
        
        return testCases;
    }
    
    private ILabTestModel initTestCase(String testCaseID, String testCaseDescr) {
        
        try {
            ILabTestModel testCase = new ILabTestModel();
            
            testCase.setTestCaseId(testCaseID);
            testCase.setTestCaseDescr(testCaseDescr);
            testCase.setExecTestFlag(ExecTestFlag.Y);
            
            return testCase;
            
        } catch (Exception ex) {
            System.err.println("[Database Reader Error] Failed to initialize test case, error - " + ex.getMessage());
        }
        
        return null;
    }
}
