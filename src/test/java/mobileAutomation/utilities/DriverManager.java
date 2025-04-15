package mobileAutomation.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import mobileAutomation.Constants;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {

    private final ContextManager context = new ContextManager();
    private final ReportingManager report = new ReportingManager();
    private ConfigurationManager configurationManager;
    private ServerManager serverManager;

    @BeforeSuite
    public void setupSuite(ITestContext context) {
        System.out.println("=======Executing before suite======");
        configurationManager = new ConfigurationManager();
        serverManager = new ServerManager();

        report.setupExtentReport(context, configurationManager);
    }

    @BeforeClass
    public void createDriver() {
        AppiumDriverLocalService server = serverManager.startServer();

        AppiumDriver mobileDriver = createAppiumDriver(server);
        FluentWait<AppiumDriver> fluentWait = createFluentWait(mobileDriver);
        WebDriverWait wait = createWebDriverWait(mobileDriver);

        context.mobileDriver = mobileDriver;
        context.wait = wait;
        context.fluentWait = fluentWait;
        context.driverName = configurationManager.driverName;
        context.platformName = configurationManager.platformName;
    }

    @BeforeMethod
    public void setupBeforeMethod(ITestResult result) {
        System.out.println("=======Executing before method======");
        context.extentTest = report.createTest(result);
    }

    @DataProvider(name = "getTestData")
    public String[][] getTestData(Method method) {
        return new ExcelManager().getMethodData(method.getName());
    }


    @AfterMethod
    public void addResultToRun(ITestResult result) {
        System.out.println("=======Executing after method======");
        report.updateStatusToReport(getDriverContext().mobileDriver, result);
    }

    @AfterClass(alwaysRun = true)
    public void quitDriver() {
        System.out.println("========Closing Driver========");
        try {
            if (getDriverContext().mobileDriver != null) {
                getDriverContext().mobileDriver.quit();
                System.out.println("========Driver closed successfully========");
            } else
                System.out.println("========Driver is not created or is already closed========");
        }
        finally {
            serverManager.stopServer();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        System.out.println("=======Executing after suite======");
        report.closeExtentReport();
    }



    public ContextManager getDriverContext() {
        return context;
    }

    private AppiumDriver createAppiumDriver(AppiumDriverLocalService server) {
        System.out.println("========Creating " + configurationManager.platformName +" Driver========");

        URL driverURL = getServerURL(server);

        DesiredCapabilities capabilities = getDesiredCapabilities();

        AppiumDriver mobileDriver = null;
        if (configurationManager.platformName.equals("Android"))
            mobileDriver = new AndroidDriver(driverURL, capabilities);
        else if (configurationManager.platformName.equals("iOS"))
            mobileDriver = new IOSDriver(driverURL, capabilities);

        System.out.println("========Driver created successfully========");
        return mobileDriver;
    }

    private URL getServerURL(AppiumDriverLocalService server) {
        return switch (configurationManager.driverName) {
            case "Android", "iOS" -> server.getUrl();
            case "Browserstack" -> {
                try {
                    yield new URL(Constants.BROWSERSTACK_URL);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> throw new IllegalArgumentException("'DriverName' value in config.properties can be either of " +
                    "'Browserstack', 'Android', 'iOS'");
        };
    }

    private DesiredCapabilities getDesiredCapabilities() {
        return switch (configurationManager.driverName) {
            case "Android" -> getAndroidCapabilities();
            case "iOS" -> getIOSCapabilities();
            case "Browserstack" -> getBrowserstackCapabilities();
            default -> throw new IllegalArgumentException("Invalid driver name");
        };
    }

    private DesiredCapabilities getAndroidCapabilities() {
        DesiredCapabilities capabilities = configurationManager.androidCapabilities;
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        return capabilities;
    }

    private DesiredCapabilities getIOSCapabilities() {
        DesiredCapabilities capabilities =  configurationManager.iOSCapabilities;
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("automationName", "XCUITest");
        return capabilities;
    }

    private DesiredCapabilities getBrowserstackCapabilities() {
        DesiredCapabilities capabilities = configurationManager.browserstackCapabilities;
        if (configurationManager.platformName.equals("Android"))
            capabilities.setCapability("platformName", "android");
        else if (configurationManager.platformName.equals("iOS"))
            capabilities.setCapability("platformName", "iOS");
        return capabilities;
    }

    private FluentWait<AppiumDriver> createFluentWait(AppiumDriver mobileDriver) {
        return new FluentWait<>(mobileDriver)
                .withTimeout(Duration.ofSeconds(configurationManager.waitTime))
                .pollingEvery(Duration.ofSeconds(1));
    }

    private WebDriverWait createWebDriverWait(AppiumDriver mobileDriver) {
        return new WebDriverWait(mobileDriver, Duration.ofSeconds(configurationManager.waitTime));
    }

}
