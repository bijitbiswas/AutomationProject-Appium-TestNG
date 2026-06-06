package mobileAutomation.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.appium.java_client.AppiumDriver;
import mobileAutomation.actionUtilities.automationFunctions.GeneralFunction;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportingManager extends GeneralFunction {

    private static final String time = new SimpleDateFormat(Constants.EXTENT_REPORT_DATE_TIME_FORMAT).format(new Date());
    private static String reportFolderLocation = Constants.EXTENT_REPORT_FOLDER_WITH_PREFIX;
    private static ExtentReports extent;


    public void setupExtentReport(ConfigurationManager configuration) {

        // If executed from Jenkins Report_ folder should not have timestamp as it will be
        // difficult to generate HTML  Report in Jenkins
        if(configuration.isJenkinsRun) {
            reportFolderLocation = reportFolderLocation + "Folder";
        } else {
            reportFolderLocation = reportFolderLocation + time;
        }

        // Get only the suite name
        String suiteName = Paths.get("Bijit_Automation")
                .getFileName()
                .toString().replace(".xml", "");

        String reportPath = System.getProperty("user.dir") + "/" + reportFolderLocation + "/" +
                suiteName + ".html";
        println("Report will be generated at "+reportPath);

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle(suiteName + " " + Constants.EXTENT_REPORT_DOCUMENT_TITLE);
        sparkReporter.config().setReportName(suiteName + " " + Constants.EXTENT_REPORT_NAME);
        sparkReporter.config().setTheme(Theme.STANDARD);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Browser", configuration.driverName);
    }

    public ExtentTest createTest(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(methodName);
        test.log(Status.INFO, MarkupHelper.createLabel(methodName + " STARTED ", ExtentColor.BLUE));
        test.assignCategory(result.getMethod().getGroups());
        return test;
    }

    public void updateStatusToReport(ITestResult result, ExtentTest test, AppiumDriver appiumDriver) {
        String resultName = result.getName();
        String finalImagePath = captureScreenshot(appiumDriver, "Final Step " + resultName);
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

    public static void closeExtentReports() {
        if (extent != null) {
            extent.flush();
            extent = null;
        }
    }

    public String captureScreenshot(WebDriver webDriver, String screenshotName) {
        String filename = screenshotName.replace("[^a-zA-Z0-9 ]", "") +".png";
        try {
            File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(reportFolderLocation + "/"+ filename));
        } catch (Exception e) {
            println("ERROR : Failed to capture screenshot to TestReport [failed to copy/image already exists ]: "
                    + e.getMessage());
        }
        println("Screenshot captured : " + filename);
        return filename;
    }


}
