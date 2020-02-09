package test.model;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import utils.Enums.BrowserType;
import utils.Enums.ExecTestFlag;
import utils.Enums.Operation;

public class ILabTestModel {

    String testCaseId = null, testCaseDescr = null;
    List<String> testData = null;
    ExecTestFlag execTestF = null;
    BrowserType browserType = null;
    List<Operation> operationList = null;
    Utils utils = null;

    public ILabTestModel() {
        this.operationList = new ArrayList<>();
        this.testData = new ArrayList<>();
        utils = new Utils();
    }

    public ILabTestModel(String testCaseId, String testCaseDescr, List<String> testData, ExecTestFlag execTestF, BrowserType browserType, List<Operation> operationList) {
        this.testCaseId = testCaseId;
        this.testCaseDescr = testCaseDescr;
        this.testData = new ArrayList<>();
        this.testData.addAll(testData);
        this.execTestF = execTestF;
        this.browserType = browserType;
        this.operationList = new ArrayList<>();
        this.operationList.addAll(operationList);

        utils = new Utils();
    }

    // test case ID setter and getter
    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestCaseId() {
        return this.testCaseId;
    }

    // test case description setter and getter
    public void setTestCaseDescr(String testCaseDescr) {
        this.testCaseDescr = testCaseDescr;
    }

    public String getTestCaseDescr() {
        return this.testCaseDescr;
    }

    // execute test flag setter and getter
    public void setExecTestFlag(ExecTestFlag execTestF) {
        this.execTestF = execTestF;
    }

    public ExecTestFlag getExecTestFlag() {
        return this.execTestF;
    }

    // test data setter and getter
    public void setTestData(List<String> testData) {
        this.testData.addAll(testData);
    }
    
    public void addToTestData(String data) {
        this.testData.add(data);
    }

    public List<String> getTestData() {
        return this.testData;
    }

    // browser type setter and getter
    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public BrowserType getBrowserType() {
        return this.browserType;
    }

    // operation list setter and getter
    public void setOperationList(List<Operation> operationList) {

        try {
            if (operationList != null) {
                this.operationList.addAll(operationList);
            } else {
                System.err.println("[ILab Test Model Warning] Failed to set operation list, provided list is empty.");
            }
        } catch (Exception ex) {
            System.err.println("[ILab Test Model Error] Failed to set operation list, error - " + ex.getMessage());
        }
    }

    public void setOperationListParamByIndex(String param, int paramIndex) {
        try {
            if (this.operationList != null) {
                this.operationList.get(paramIndex).setOperationValue(param);
            } else {
                System.err.println("[ILab Test Model Error] Failed to set operation list param by index, operation list is null.");
            }
            this.operationList.get(paramIndex).setOperationValue(param);
        } catch (Exception ex) {
            System.err.println("[ILab Test Model Error] Failed to set operation list param by index, error - " + ex.getMessage());
        }
    }

    public List<Operation> getOperationList() {
        return this.operationList;
    }

    public String operationListToString() {
        String returnStr = null;

        List<Operation> operationList = this.getOperationList();

        if (operationList != null) {
            for (Operation opr : operationList) {
                if (utils.emptyString(returnStr)) {
                    returnStr = "[" + opr.toString() + ", " + opr.getOperationValue() + "]";
                } else {
                    returnStr += "; " + "[" + opr.toString() + ", " + opr.getOperationValue() + "]";
                }
            }
        }

        return returnStr;
    }

    @Override
    public String toString() {
//        return "Test Case ID : " + this.getTestCaseId() + " - Execute Flag : " + this.getExecTestFlag() + " - BrowserType : " + this.getBrowserType() + " - Operations List : " + this.operationListToString();
        return "Test Case ID : " + this.getTestCaseId() + " - Test Case Description : " + this.getTestCaseDescr()  + " - Execute Flag : " + this.getExecTestFlag() + " - Test Data : " + this.getTestData();
    }

}
