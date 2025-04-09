package mobileAutomation.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportingManager {

    private static final String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    private final String reportFolderLocation = "TestReport/Report_" + time;
    private ExtentReports extent;
    private ExtentTest test;


    public void setupExtentReport(ITestContext context, ConfigurationManager configuration) {

        String suiteName = context.getCurrentXmlTest().getSuite().getName();
        String reportPath = System.getProperty("user.dir") + "/" + reportFolderLocation + "/" +
                suiteName + "_" + time + ".html";
        System.out.println("=======Report will be generated at "+reportPath+"======");

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle(suiteName + " Automation Execution Report");
        sparkReporter.config().setReportName(suiteName + " Automation Execution Report");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Driver", configuration.platformName);
    }

    public ExtentTest createTest(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        test = extent.createTest(methodName);
        test.log(Status.INFO, MarkupHelper.createLabel(methodName + " STARTED ", ExtentColor.BLUE));
        test.assignCategory(result.getMethod().getGroups());
        return test;
    }

    public void updateStatusToReport(AppiumDriver mobileDriver, ITestResult result) {
        String resultName = result.getName();
        String finalImagePath = captureScreenshot(mobileDriver, "Final Step " + resultName);
        Media screenCaptured = MediaEntityBuilder.createScreenCaptureFromPath(finalImagePath).build();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> test.log(Status.PASS, MarkupHelper.createLabel(
                    "Final Step " + resultName + " PASSED ", ExtentColor.BLUE), screenCaptured);
            case ITestResult.FAILURE -> {
                test.log(Status.FAIL, MarkupHelper.createLabel(
                        "Final Step " + resultName + " FAILED ", ExtentColor.RED), screenCaptured);
                test.fail(result.getThrowable());
            }
            default -> {
                test.log(Status.SKIP, MarkupHelper.createLabel(
                        "Final Step " + resultName + " SKIPPED ", ExtentColor.ORANGE), screenCaptured);
                test.skip(result.getThrowable());
            }
        }
    }

    public void closeExtentReport() {
        extent.flush();
    }

    public String captureScreenshot(AppiumDriver mobileDriver, String screenshotName) {
        String filename = screenshotName.replace("[^a-zA-Z0-9 ]", "") +".png";
        try {
            File scrFile = mobileDriver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(reportFolderLocation + "/"+ filename));
        } catch (Exception e) {
            System.out.println("Error copying screenshot to TestReport : " + e.getMessage());
            throw new RuntimeException(e);
        }
        return filename;
    }

}
