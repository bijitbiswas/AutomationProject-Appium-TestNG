package mobileAutomation.utilities;


import mobileAutomation.Constants;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class BaseTest {

    // Below variable to upload images/files to LambdaTest cloud devices
    protected String[] filePaths;

    @BeforeSuite
    public void beforeSuite() {
        ExtentReportManager.initializeExtentReports();
    }

    @BeforeClass
    public void setUp() {
        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.testcaseName = this.getClass().getSimpleName();

        if (filePaths != null) {
            ArrayList<String> uploadedIds = null;
            String auth = configurationManager.cloudProviderAuth;
            if (configurationManager.driverName.contains(Constants.LAMBDATEST)) {
                uploadedIds = LambdaTestManager.uploadFileToLambdaTest(auth, filePaths);
            } else if (configurationManager.driverName.contains(Constants.BROWSERSTACK)) {
                uploadedIds = BrowserStackManager.uploadFileToBrowserStack(auth, filePaths);
            }
            configurationManager.mediaIds = uploadedIds;
        }

        DriverManager.initializeDriver(configurationManager);
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        ExtentReportManager.startTest(testName, testDescription);
    }

    @DataProvider(name = "getTestData")
    public Object[][] getTestData(Method method) {
        return ExcelManager.getMethodData(method.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        // Log test status and capture screenshots for failures/skips
        ExtentReportManager.logTestResult(result);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown(ITestContext context) {
        int failedCount = context.getFailedTests().size();
        int skippedCount = context.getSkippedTests().size();
        boolean testPassed = (failedCount == 0 && skippedCount == 0);
        DriverManager.quitDriver(testPassed);
    }

    @AfterSuite
    public void afterSuite() {
        ExtentReportManager.closeExtentReports();
    }



    // Below functions can be called directly in Testcases for Reporting purpose
    protected void logInfo(String message) {
        ExtentReportManager.logInfo(message);
    }

    protected void logPass(String message) {
        ExtentReportManager.logPass(message);
    }

    protected void logPassWithScreenshot(String message) {
        ExtentReportManager.logPassWithScreenshot(message);
    }

    protected void logFail(String message) {
        ExtentReportManager.logFail(message);
    }

    protected void logSkip(String message) {
        ExtentReportManager.logSkip(message);
    }

    protected void logWarning(String message) {
        ExtentReportManager.logWarning(message);
    }

}






