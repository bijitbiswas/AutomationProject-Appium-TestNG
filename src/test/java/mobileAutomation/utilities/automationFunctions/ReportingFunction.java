package mobileAutomation.utilities.automationFunctions;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.appium.java_client.AppiumDriver;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.ReportingManager;
import mobileAutomation.utilities.automationInterfaces.ReportingInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportingFunction implements ReportingInterface {

    AppiumDriver mobileDriver;
    ExtentTest extentTest;

    String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    String reportFolderLocation = "TestReport/Report_" + time;

    public ReportingFunction(ContextManager context) {
        this.mobileDriver = context.mobileDriver;
        this.extentTest = context.extentTest;
    }

    @Override
    public void addSuccessLabel(String labelName) {
        extentTest.log(Status.PASS, MarkupHelper.createLabel(labelName, ExtentColor.GREEN));
    }

    @Override
    public void addSuccessLabelWithScreenshot(String labelName) {
        String screenshotPath = new ReportingManager().captureScreenshot(mobileDriver, labelName);
        extentTest.log(Status.PASS, MarkupHelper.createLabel(labelName, ExtentColor.GREEN),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
        );
    }
}
