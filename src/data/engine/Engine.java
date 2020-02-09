package data.engine;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import data.reader.DatabaseReader;
import java.util.List;

import data.reader.ExcelReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import test.model.ILabTestModel;
import test.model.TestCaseResult;

public class Engine {

    SeleniumDriver selenium;

    private final ConfigProperties conf;
    private ExtentReports extentReport;

    public Engine() {
        this.conf = new ConfigProperties();
    }

    public void runTests() {
        try {
            selenium = new SeleniumDriver();

            System.out.println("[INFO] Running selenium tests using " + conf.getTestExecutionMethod() + " method on " + conf.getWebDriver()  + " browser.");

            switch (conf.getTestExecutionMethod()) {
                case "database":
                    this.executeILabDBTests();
                    break;
                case "flat_file":
                    this.executeILabExcelTests();
                    break;
                default:
                    throw new Exception("[Test Runner Error] Unrecognized test execution method : " + conf.getTestExecutionMethod());
            }

            selenium.stopDriver();

        } catch (Exception ex) {
            System.err.println("[Test Runner Error] Failed to run tests, error - " + ex.getMessage());
        }
    }

    private void executeILabDBTests() {
        try {
            DatabaseReader dbReader = new DatabaseReader();

            List<ILabTestModel> testCases = dbReader.getTestCasesFromDB();

            if (testCases != null && testCases.size() > 0) {
                this.execTests(testCases);
            } else {
                System.err.println("[Test Runner Warning] Failed to execute iLab database tests, there are no test cases to execute.");
            }

        } catch (Exception ex) {
            System.err.println("[Test Runner Error] Failed to execute iLab database tests, error : " + ex.getMessage());
        }
    }

    private void executeILabExcelTests() {
        try {

            ExcelReader reader = new ExcelReader();

            List<ILabTestModel> iLabTestList = reader.getTestCaseList();

            if (iLabTestList != null && iLabTestList.size() > 0) {
                this.execTests(iLabTestList);
            } else {
                System.err.println("[Test Runner Warning] Failed to execute iLab excel tests, there are no test cases to execute.");
            }

        } catch (Exception ex) {
            System.err.println("[Test Runner Error ] Failed to execute ILab tests, error - " + ex.getMessage());
        }
    }

    private void execTests(List<ILabTestModel> testCases) {

        try {

            // initialize extent reports
            this.initExtentReports();

            // execute test steps
            testCases.forEach((testCase) -> {
                this.processSteps(testCase);
            });

            // write reports to disk
            this.flushExtentReports();
        } catch (Exception ex) {
            System.err.println("[Test Runner Error] Failed to executes tests, error - " + ex.getMessage());
        }
    }
    
    private void processSteps(ILabTestModel testCase) {
        try {

            System.out.println("[INFO] Processing test case with ID : " + testCase.getTestCaseId() + " with data : " + testCase.getTestData().toString());

            ExtentTest extentTest = extentReport.startTest(testCase.getTestCaseId(), testCase.getTestCaseDescr());
            for (String test : testCase.getTestData()) {
                System.out.println("[INFO] Processing Test : " + test);
                String[] keyVal = test.split("-");

                TestCaseResult testRes;

                switch (keyVal[0]) {
                    case "navigateTo":
                        testRes = selenium.goToURL(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully navigated to : " + keyVal[1]);
                            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(testRes.getScreenshotLocation()));

                        } else {
                            System.err.println("[Test Runner Error] Failed to navigate to URL : " + keyVal[1]);
                            extentTest.log(LogStatus.FAIL, "Failed to navigate to URL : " + keyVal[1]);
                        }
                        break;

                    case "clickByHrefText":
                        testRes = selenium.selectLinkByText(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully clicked by link text : " + keyVal[1]);
                            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(testRes.getScreenshotLocation()));

                        } else {
                            System.err.println("[Test Runner Error] Failed to click by link text : " + keyVal[1]);
                            extentTest.log(LogStatus.FAIL, "Failed to click by link text : " + keyVal[1]);
                        }
                        break;

                    case "clickHrefAfterText":
                        testRes = selenium.clickHrefAfterText(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully clicked href after text : " + keyVal[1]);
                        } else {
                            System.err.println("[Test Runner Error] Failed to click href after text : " + keyVal[1]);
                            extentTest.log(LogStatus.FAIL, "Failed to click href after text : " + keyVal[1]);
                        }
                        break;

                    case "clickButton":
                        testRes = selenium.clickButtonByText(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully clicked button with text : " + keyVal[1]);
                            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(testRes.getScreenshotLocation()));
                        } else {
                            System.err.println("[Test Runner Error] Failed to click button by text : " + keyVal[1]);
                            extentTest.log(LogStatus.FAIL, "Failed to click button with text : " + keyVal[1]);
                        }
                        break;

                    case "clickInputByValue":
                        testRes = selenium.clickByInputValue(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully clicked input by value : " + keyVal[1]);
                            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(testRes.getScreenshotLocation()));
                        } else {
                            System.err.println("[Test Runner Error] Failed to click by input value : " + keyVal[1]);
                            extentTest.log(LogStatus.FAIL, "Failed to click input by value : " + keyVal[1]);
                        }
                        break;

                    case "insertTextById":
                        testRes = selenium.insertTextByID(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully inserted text : " + keyVal[1]);
                            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(testRes.getScreenshotLocation()));
                        } else {
                            System.err.println("[Test Runner Error] Failed to insert text : " + keyVal[1] + " by ID");
                            extentTest.log(LogStatus.FAIL, "Fauled to insert text : " + keyVal[1]);
                        }
                        break;
                    case "insertCellNoById":
                        testRes = this.selenium.insertCellNumByID(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully inserted cell number using ID : " + keyVal[1]);
                            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(testRes.getScreenshotLocation()));
                        } else {
                            System.err.println("[Test Runner Error] Failed to insert cell number by ID : " + keyVal[1] + " by ID");
                            extentTest.log(LogStatus.FAIL, "Failed to insert cell number using ID : " + keyVal[1]);
                        }
                        break;

                    case "validateErrorText":
                        testRes = selenium.verifyTextOnPage(keyVal[1]);
                        if (testRes.getTestResult()) {
                            extentTest.log(LogStatus.PASS, "Successfully vaidated error text : " + keyVal[1]);
                            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(testRes.getScreenshotLocation()));
                        } else {
                            System.err.println("[Test Runner Error] Failed to verify text : " + keyVal[1] + " on page");
                            extentTest.log(LogStatus.FAIL, "Failed to vaidate error text : " + keyVal[1]);
                        }
                        break;
                }
            }

            extentReport.endTest(extentTest);

        } catch (Exception ex) {
            System.err.println("[Test Runner Error] Failed to process test steps, error - " + ex.getMessage());
        }
    }

    // initialize extent reports
    private void initExtentReports() {

        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date now = new Date();

            String dateStr = dateFormat.format(now);

            String reportFileName = "iLabTestReport_" + dateStr + ".html";

            String testReport = System.getProperty("user.dir") + this.conf.getTestReportDir() + reportFileName;

            this.extentReport = new ExtentReports(testReport, false);

        } catch (Exception ex) {
            System.err.println("[Personal Loan Error] Failed to initialize extent reports, error - " + ex.getMessage());
        }
    }

    // write extent reports
    private void flushExtentReports() {

        try {
            this.extentReport.flush();

        } catch (Exception ex) {
            System.err.println("[Personal Loan Error] Failed to flush extent reports, error - " + ex.getMessage());
        }
    }
}
