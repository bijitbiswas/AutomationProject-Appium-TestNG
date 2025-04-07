package mobileAutomation.utilities;

import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContextManager {

    public AppiumDriver mobileDriver;
    public String driverName;
    public String platformName;
    public WebDriverWait wait;
    public FluentWait<AppiumDriver> fluentWait;
    public ExtentTest extentTest;


}
