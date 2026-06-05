package mobileAutomation.utilities;

import org.testng.ITestContext;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class BaseManager {

    // Override in test class to upload files/images to cloud devices before driver init
    protected String[] filePaths;
    private static final ReportingManager reportingManager = new ReportingManager();
    private final ConfigurationManager configurationManager = new ConfigurationManager();
    private final DriverManager driverManager = new DriverManager(configurationManager);

    protected void beforeSuite() {
        System.out.println("********@BeforeSuite********");
        if (isLocalDriver()) {
            ServerManager.startServer();
        }
        reportingManager.setupExtentReport(configurationManager);
    }

    protected void beforeClass() {
        System.out.println("********Started @BeforeClass********");

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

        driverManager.createDriver();
    }

    protected void beforeMethod(ITestResult result) {
        System.out.println("********Started @BeforeMethod********");
        driverManager.resetDriver(result);
        driverManager.getDriverContext().setExtentTest(reportingManager.createTest(result));
    }

    protected Object[][] dataProvider(Method method) {
        return ExcelManager.getMethodData(method.getName());
    }

    protected void afterMethod(ITestResult result) {
        System.out.println("********Started @AfterMethod********");
        reportingManager.updateStatusToReport(result, driverManager.getDriverContext().getExtentTest(),
                driverManager.getDriverContext().getAppiumDriver());
    }

    protected void afterClass(ITestContext context) {
        int failedCount = context.getFailedTests().size();
        int skippedCount = context.getSkippedTests().size();
        boolean testPassed = (failedCount == 0 && skippedCount == 0);
        driverManager.quitDriver(testPassed);
    }

    protected void afterSuite() {
        if (isLocalDriver()) {
            ServerManager.stopServer();
        }
        ReportingManager.closeExtentReports();
    }

    private boolean isLocalDriver() {
        String driverName = configurationManager.driverName;
        return driverName.equals(Constants.ANDROID) || driverName.equals(Constants.IOS);
    }

    public ContextManager getDriverContext() {
        return driverManager.getDriverContext();
    }
}
