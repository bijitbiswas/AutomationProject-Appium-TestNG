package mobileAutomation.actionUtilities.automationFunctions;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.ReportingManager;
import mobileAutomation.actionUtilities.automationInterfaces.ReportingInterface;
import org.openqa.selenium.WebDriver;

public class ReportingFunction extends GeneralFunction implements ReportingInterface {

    WebDriver webDriver;
    ExtentTest extentTest;

    public ReportingFunction(ContextManager context) {
        this.webDriver = context.getAppiumDriver();
        this.extentTest = context.getExtentTest();
    }

    @Override
    public void addSuccessLabel(String labelName) {
        extentTest.log(Status.PASS, MarkupHelper.createLabel(labelName, ExtentColor.GREEN));
        println("Success label added: " + labelName);
    }

    @Override
    public void addSuccessLabelWithScreenshot(String labelName) {
        String screenshotPath = new ReportingManager().captureScreenshot(webDriver, labelName);
        extentTest.log(Status.PASS, MarkupHelper.createLabel(labelName, ExtentColor.GREEN),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
        );
        println("Success label with screenshot added: " + labelName);
    }
}
