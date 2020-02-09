package data.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.engine.ConfigProperties;
import test.model.ILabTestModel;
import utils.Enums.ExecTestFlag;
import utils.Utils;

public class ExcelReader {

    ConfigProperties conf = null;
    Utils utils = null;
    Workbook workbook = null;

    public ExcelReader() {
        conf = new ConfigProperties();
        utils = new Utils();
    }

    public List<ILabTestModel> getTestCaseList() {

        List<ILabTestModel> testCaseList = new ArrayList<>();

        try {

            File fileDir = new File("data/iLabTests/");

            File[] testFiles = fileDir.listFiles();

            for (int i = 0; i < testFiles.length; i++) {

                if (testFiles[i].isFile()) {

                    String fileName = testFiles[i].getName();

                    String fileExt = utils.getFileExtFromStr(fileName);

                    if (utils.Str1IsStr2(fileExt, "xls") || utils.Str1IsStr2(fileExt, "xlsx")) {
                        testCaseList.addAll(this.getTestCasesFromWorkbook(fileDir + "//" + fileName));
                    }
                }
            }

        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to get test case list, error " + ex.getMessage());
        }
        return testCaseList;
    }

    private List<ILabTestModel> getTestCasesFromWorkbook(String fileName) {
        List<ILabTestModel> testCases = null;

        try {

            FileInputStream excelFile = new FileInputStream(new File(fileName));
            this.workbook = new XSSFWorkbook(excelFile);

            String sheetName = "Data";
            Sheet sheet = this.getSheetByName(sheetName);

            if (sheet != null) {
                testCases = new ArrayList<>();
                testCases.addAll(this.getTestDataFromSheet(sheet));
            } else {
                System.err.println("[Excel Reader Error] Could not get sheet sheet by name - " + sheetName);
            }
        } catch (IOException ex) {
            System.err.println("[Excel Reader Error] Failed to get test cases from workbook IO exception, error - " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to get test cases from workbook, error - " + ex.getMessage());
        }
        
        return testCases;
    }

    private List<ILabTestModel> getTestDataFromSheet(Sheet sheet) {

        List<ILabTestModel> testCases = new ArrayList<>();

        try {
            Iterator<Row> rowIterator = sheet.iterator();
            boolean testSuiteRow = false, newRowF = false;
            ILabTestModel testScenario = new ILabTestModel();
            String testCaseId = null, testCaseDescr = null;
            ExecTestFlag execFlag = null;
            HashMap<Integer, String> testCase = new HashMap<>();
            List<String> data = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();

                Iterator<Cell> columnIterator = currentRow.iterator();

                nextRow:
                while (columnIterator.hasNext()) {
                    Cell currentCell = columnIterator.next();

                    if (!utils.emptyString(this.getCellStringValue(currentCell))) {
                        switch (currentCell.getColumnIndex()) {
                            case 0:
                                testSuiteRow = testSuiteHeaderRow(currentCell.getStringCellValue());

                                if (testSuiteRow) {
                                    testCase = new HashMap<>();
                                } else {
                                    newRowF = true;
                                    ExecTestFlag testExecFlag = resolveTestExecuteFlag(currentCell.getStringCellValue());
                                    if (testExecFlag == ExecTestFlag.N) {
                                        break nextRow;
                                    } else {
                                        execFlag = testExecFlag;
                                    }
                                }
                                break;
                            case 1:
                                if (!testSuiteRow) {
                                    testCaseId = currentCell.getStringCellValue();
                                }
                                break;
                            case 2:
                                if (!testSuiteRow) {
                                    testCaseDescr = currentCell.getStringCellValue();
                                }
                                break;
                            default:
                                if (testSuiteRow) {
                                    testCase.put(currentCell.getColumnIndex(), currentCell.getStringCellValue() + "-");
                                } else {
                                    
                                    if (newRowF) {
                                        data = new ArrayList<>();
                                        testScenario = new ILabTestModel();
                                        newRowF = false;
                                    }
                                    
                                    String key = testCase.get(currentCell.getColumnIndex());
                                    String value = currentCell.getStringCellValue();
                                    data.add(key + value);
                                    
                                    if (data.size() == testCase.size()) {
                                        testScenario.setTestCaseId(testCaseId);
                                        testScenario.setTestCaseDescr(testCaseDescr);
                                        testScenario.setExecTestFlag(execFlag);
                                        testScenario.setTestData(data);
                                        testCases.add(testScenario);
                                    }                                   
                                }
                        }
                    }
                }
                testSuiteRow = false;
            }

        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to get test data from sheet " + sheet.getSheetName() + ", error - " + ex.getMessage());
        }

        return testCases;
    }

    // if the current value is either Y or N then we know that we're not dealing with a test suite header row
    private boolean testSuiteHeaderRow(String value) {

        try {
            for (ExecTestFlag execFlag : ExecTestFlag.values()) {

                if (execFlag.toString().equals(value)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to determine test suite header, error - " + ex.getMessage());
        }

        return true;
    }

    private ExecTestFlag resolveTestExecuteFlag(String executeFlag) {
        try {

            for (ExecTestFlag execFlag : ExecTestFlag.values()) {

                if (execFlag.toString().equals(executeFlag)) {
                    return execFlag;
                }
            }
        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to resolve execute falg, error - " + ex.getMessage());
        }
        return null;
    }

    private String getCellStringValue(Cell cell) {
        try {
            return cell.getStringCellValue();
        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to get cell string value, error - " + ex.getMessage());
        }
        return null;
    }

    public List<File> getFilesInDir() {
        try {

        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to get files in dir, error - " + ex.getMessage());
        }

        return null;
    }

    private Sheet getSheetByName(String sheetName) {

        Sheet resSheet = null;
        try {

            return this.workbook.getSheet(sheetName);

        } catch (Exception ex) {
            System.err.println("[Excel Reader Error] Failed to get sheet by name, error - " + ex.getMessage());
        }
        return resSheet;
    }
}
