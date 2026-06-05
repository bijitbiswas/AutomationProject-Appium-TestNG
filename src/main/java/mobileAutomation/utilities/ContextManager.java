package mobileAutomation.utilities;

import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContextManager {

    private AppiumDriver appiumDriver;
    private String driverName;
    private WebDriverWait wait;
    private FluentWait<AppiumDriver> fluentWait;
    private ExtentTest extentTest;

    /**
     * Initializes all driver-related fields atomically. Called once per @BeforeClass by DriverManager.
     */
    public void initialize(AppiumDriver appiumDriver, WebDriverWait wait,
                           FluentWait<AppiumDriver> fluentWait, String driverName) {
        this.appiumDriver = appiumDriver;
        this.wait = wait;
        this.fluentWait = fluentWait;
        this.driverName = driverName;
    }

    /**
     * Nulls out driver-related fields after the browser is quit. Called by DriverManager.quitDriver().
     */
    public void clear() {
        this.appiumDriver = null;
        this.wait = null;
        this.fluentWait = null;
    }

    /**
     * Sets the current test's Extent report node. Called once per @BeforeMethod by BaseTest.
     */
    public void setExtentTest(ExtentTest extentTest) {
        this.extentTest = extentTest;
    }

    public AppiumDriver getAppiumDriver()              { return appiumDriver; }
    public String getDriverName()               { return driverName; }
    public WebDriverWait getWait()               { return wait; }
    public FluentWait<AppiumDriver> getFluentWait() { return fluentWait; }
    public ExtentTest getExtentTest()            { return extentTest; }
}
