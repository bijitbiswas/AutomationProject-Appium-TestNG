package mobileAutomation.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import mobileAutomation.Constants;
import mobileAutomation.utilities.automationFunctions.GeneralFunction;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExtentReportManager extends GeneralFunction {

    private static final String time = new SimpleDateFormat(Constants.EXTENT_REPORT_DATE_TIME_FORMAT).format(new Date());
    private static final String reportFolderLocation = Constants.EXTENT_REPORT_FOLDER_WITH_PREFIX;
    private static ExtentReports extent;
    private static final Map<String, ExtentTest> testMap = new HashMap<>();


    public static void initializeExtentReports() {
        String suiteName = "SampleSuite";//context.getCurrentXmlTest().getSuite().getName();
        String reportPath = System.getProperty("user.dir") + "/" + reportFolderLocation + "/" +
                suiteName + "_" + time + ".html";
        println("Report will be generated at " + reportPath);

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle(suiteName + " " + Constants.EXTENT_REPORT_DOCUMENT_TITLE);
        sparkReporter.config().setReportName(suiteName + " " + Constants.EXTENT_REPORT_NAME);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Driver", "iOS");
    }

//    public ExtentTest createTest(ITestResult result) {
//        String methodName = result.getMethod().getMethodName();
//        test = extent.createTest(methodName);
//        test.log(Status.INFO, MarkupHelper.createLabel(methodName + " STARTED ", ExtentColor.BLUE));
//        test.assignCategory(result.getMethod().getGroups());
//        return test;
//    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            throw new IllegalStateException("ExtentReports not initialized. Call initializeExtentReports() first.");
        }
        return extent;
    }

    public static void startTest(String testName, String testDescription) {
        ExtentTest test = getInstance().createTest(testName, testDescription);
        testMap.put(getCurrentThreadId(), test);
        logInfo("Starting test: " + testName);
    }

    public static ExtentTest getTest() {
        return testMap.get(getCurrentThreadId());
    }

    private static String getCurrentThreadId() {
        return Thread.currentThread().getName() + "-" + Thread.currentThread().hashCode();
    }

    public static void closeExtentReports() {
        if (extent != null) {
            extent.flush();
            extent = null;
        }
    }



    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().info(message);
        }
        System.out.println("******* EXTENT_REPORT : Added INFO label: "+message+" *******");
    }

    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
        }
        System.out.println("******* EXTENT_REPORT : Added PASS label: "+message+" *******");
    }

    public static void logPassWithScreenshot(String message) {
        if (getTest() != null) {
            getTest().pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
            getTest().pass(addScreenshot(message + " Screenshot"));
        }
        System.out.println("******* EXTENT_REPORT : Added PASS label with screenshot : "+message+" *******");
    }

    public static void logFail(String message) {
        if (getTest() != null) {
            getTest().fail(MarkupHelper.createLabel(message, ExtentColor.RED));
            getTest().fail(addScreenshot("Failed Screenshot"));
        }
        System.out.println("******* EXTENT_REPORT : Added FAIL label with screenshot : "+message+" *******");
    }

    public static void logSkip(String message) {
        if (getTest() != null) {
            getTest().skip(MarkupHelper.createLabel(message, ExtentColor.YELLOW));
        }
    }

    public static void logWarning(String message) {
        if (getTest() != null) {
            getTest().warning(MarkupHelper.createLabel(message, ExtentColor.ORANGE));
        }
    }

    public static void logTestResult(ITestResult result) {
        ExtentTest test = getTest();
        String resultName = result.getName();
        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.SUCCESS -> logPass("Test Passed : " + resultName );
                case ITestResult.FAILURE -> logFail("Test failed : " + resultName + result.getThrowable().toString());
                case ITestResult.SKIP -> logSkip("Test skipped : " + resultName + result.getThrowable().getMessage());
                default -> logInfo("Test status: " + resultName + result.getStatus());
            }
        }
    }

    private static Media addScreenshot(String title) {
        return MediaEntityBuilder.createScreenCaptureFromBase64String(
                Objects.requireNonNull(captureScreenshot()), title).build();
    }

    private static String captureScreenshot() {
        try {
            return DriverManager.getMobileDriver().getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logWarning("Could not capture screenshot: " + e.getMessage());
            return null;
        }
    }


}
