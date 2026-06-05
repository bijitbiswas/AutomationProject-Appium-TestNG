package mobileAutomation.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import mobileAutomation.actionUtilities.automationFunctions.GeneralFunction;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverManager extends GeneralFunction {

    private final ContextManager contextManager = new ContextManager();
    private final ConfigurationManager configurationManager;
    public DriverManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }
    public void createDriver() {

        AppiumDriver appiumDriver = createMobileDriver();
        FluentWait<AppiumDriver> fluentWait = createFluentWait(appiumDriver);
        WebDriverWait wait = createWebDriverWait(appiumDriver);

        contextManager.initialize(appiumDriver, wait, fluentWait, configurationManager.driverName);
        System.out.println("******* Mobile Driver is initialized *******");
    }

    public void quitDriver(boolean testPassed) {

        // Mark test status on LambdaTest/BrowserStack
        if (configurationManager.driverName.contains(Constants.LAMBDATEST)) {
            LambdaTestManager.markLambdaTestStatus(contextManager.getAppiumDriver(), testPassed);
        } else if (configurationManager.driverName.contains(Constants.BROWSERSTACK)) {
            BrowserStackManager.markBrowserStackStatus(contextManager.getAppiumDriver(), testPassed);
        }

        if (contextManager.getAppiumDriver() != null) {
            contextManager.getAppiumDriver().quit();
            contextManager.clear();
            println("Driver closed successfully");
        } else {
            println("Driver is not created or is already closed");
        }
    }

    public void resetDriver(ITestResult result) {
        IRetryAnalyzer analyzer = result.getMethod().getRetryAnalyzer(result);
        boolean isRetrying = analyzer instanceof RetryAnalyzer && ((RetryAnalyzer) analyzer).isRetrying();
        if (isRetrying) {
            quitDriver(false);
            createDriver();
        }
    }

    public ContextManager getDriverContext() {
        return contextManager;
    }

    private AppiumDriver createMobileDriver() {
        String driverName = configurationManager.driverName;

        AppiumDriver appiumDriver = switch (configurationManager.driverName) {
            case Constants.LAMBDATEST_ANDROID, Constants.LAMBDATEST_IOS ->
                    initializeLambdaTestDriver(driverName, configurationManager);
            case Constants.BROWSERSTACK_ANDROID, Constants.BROWSERSTACK_IOS ->
                    initializeBrowserstackDriver(driverName, configurationManager);
            case Constants.ANDROID, Constants.IOS -> initializeLocalDriver(driverName, configurationManager);
            default -> throw new IllegalArgumentException("Unsupported driverName : " + driverName);
        };
        println("Driver created successfully");
        return appiumDriver;
    }

    private AppiumDriver initializeLocalDriver(String driverName, ConfigurationManager configurationManager) {
        URL serverUrl = ServerManager.getServer().getUrl();
        println("Creating " + driverName + " Driver");
        if (driverName.equalsIgnoreCase(Constants.ANDROID)) {
            return new AndroidDriver(serverUrl, getAndroidCapabilities(configurationManager));
        } else if (driverName.equalsIgnoreCase(Constants.IOS)) {
            return new IOSDriver(serverUrl, getIOSCapabilities(configurationManager));
        } else {
            throw new IllegalArgumentException("Unsupported local platform: " + driverName);
        }
    }

    private AppiumDriver initializeLambdaTestDriver(String driverName, ConfigurationManager configurationManager) {

        String lambdaTestAuth = configurationManager.cloudProviderAuth;
        String lambdaTestURL = "https://" + lambdaTestAuth + Constants.LAMBDATEST_GRID_URL;
        URL gridUrl;
        try {
            gridUrl = URI.create(lambdaTestURL).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new RuntimeException("Invalid LambdaTest URL", e);
        }
        println("Creating " + driverName + " Driver");
        if (driverName.equalsIgnoreCase(Constants.LAMBDATEST_ANDROID)) {
            return new AndroidDriver(gridUrl, getLambdaTestCapabilities(configurationManager));
        } else if (driverName.equalsIgnoreCase(Constants.LAMBDATEST_IOS)) {
            return new IOSDriver(gridUrl, getLambdaTestCapabilities(configurationManager));
        } else {
            throw new IllegalArgumentException("Unsupported LambdaTest platform: " + driverName);
        }
    }

    private AppiumDriver initializeBrowserstackDriver(String driverName, ConfigurationManager configurationManager) {

        URL gridUrl;
        try {
            gridUrl = URI.create(Constants.BROWSERSTACK_URL).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new RuntimeException("Invalid Browserstack URL", e);
        }
        println("Creating " + driverName + " Driver");
        if (driverName.equalsIgnoreCase(Constants.BROWSERSTACK_ANDROID)) {
            return new AndroidDriver(gridUrl, getBrowserstackCapabilities(configurationManager));
        } else if (driverName.equalsIgnoreCase(Constants.BROWSERSTACK_IOS)) {
            return new IOSDriver(gridUrl, getBrowserstackCapabilities(configurationManager));
        } else {
            throw new IllegalArgumentException("Unsupported Browserstack platform: " + driverName);
        }
    }

    private DesiredCapabilities getAndroidCapabilities(ConfigurationManager configurationManager) {
        DesiredCapabilities capabilities = configurationManager.androidCapabilities;
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:skipDeviceInitialization", true);
        capabilities.setCapability("appium:ignoreHiddenApiPolicyError", true);
        capabilities.setCapability("appium:appWaitActivity", "*");
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:noReset", true);
        capabilities.setCapability("appium:fullReset", false);
        return capabilities;
    }

    private DesiredCapabilities getIOSCapabilities(ConfigurationManager configurationManager) {
        DesiredCapabilities capabilities = configurationManager.iOSCapabilities;
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("appium:automationName", "XCUITest");
        capabilities.setCapability("appium:noReset", true);
        return capabilities;
    }

    private DesiredCapabilities getBrowserstackCapabilities(ConfigurationManager configurationManager) {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        HashMap<String, Object> browserstackOptions = configurationManager.browserstackCapabilities;
        ArrayList<String> mediaIds = configurationManager.mediaIds;
        String testcaseName = configurationManager.testcaseName;

        if (configurationManager.driverName.equalsIgnoreCase(Constants.BROWSERSTACK_ANDROID))
            capabilities.setCapability("platformName", "android");
        else if (configurationManager.driverName.equalsIgnoreCase(Constants.BROWSERSTACK_IOS))
            capabilities.setCapability("platformName", "iOS");
        else {
            throw new IllegalArgumentException("Unsupported Browserstack platform: " + configurationManager.driverName);
        }

        for (Map.Entry<String, Object> entry : browserstackOptions.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if(key.equals("bstack:options")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> bsOptions = (Map<String, Object>) value;
                assert bsOptions != null;
                bsOptions.put("sessionName", testcaseName);
                bsOptions.put("appiumVersion", "2.19.0");
                capabilities.setCapability(key, bsOptions);
            } else
                capabilities.setCapability(key, value);
        }
        capabilities.setCapability("appium:browserstack.uploadMedia", mediaIds);
        capabilities.setCapability("appium:locale", "en_US");
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:gpsEnabled", true);
        capabilities.setCapability("appium:browserstack.idleTimeout", 160);
        capabilities.setCapability("appium:browserstack.timezone", "Los_Angeles");
        capabilities.setCapability("appium:interactiveDebugging", true);
        return capabilities;
    }

    private DesiredCapabilities getLambdaTestCapabilities(ConfigurationManager configurationManager) {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        HashMap<String, Object> lambdaTestOptions = configurationManager.lambdaTestCapabilities;
        ArrayList<String> mediaIds = configurationManager.mediaIds;
        String testcaseName = configurationManager.testcaseName;

        if (configurationManager.driverName.equalsIgnoreCase(Constants.LAMBDATEST_ANDROID)) {
            lambdaTestOptions.put("platformName", "android");
            lambdaTestOptions.put("project", "Android Project");
        } else if (configurationManager.driverName.equalsIgnoreCase(Constants.LAMBDATEST_IOS)) {
            lambdaTestOptions.put("platformName", "ios");
            lambdaTestOptions.put("project", "iOS Project");
        } else {
            throw new IllegalArgumentException("Unsupported LambdaTest platform: " + configurationManager.driverName);
        }
        lambdaTestOptions.put("name", testcaseName);
        lambdaTestOptions.put("w3c", true);
        lambdaTestOptions.put("isRealMobile", true);
        lambdaTestOptions.put("deviceOrientation", "portrait");
        lambdaTestOptions.put("console", true);
        lambdaTestOptions.put("visual", false); // If true : Takes screenshots which is visible in Meta Data > Media
        lambdaTestOptions.put("devicelog", false);
        lambdaTestOptions.put("uploadMedia", mediaIds);
        lambdaTestOptions.put("network", true);
//        lambdaTestOptions.put("networkProfile", "4g-lte-advanced-good");

        capabilities.setCapability("lt:Options", lambdaTestOptions);
        return capabilities;
    }

    private FluentWait<AppiumDriver> createFluentWait(AppiumDriver appiumDriver) {
        return new FluentWait<>(appiumDriver)
                .withTimeout(Duration.ofSeconds(configurationManager.waitTime))
                .pollingEvery(Duration.ofSeconds(Constants.FLUENT_WAIT_POLLING_TIME_IN_SECS));
    }

    private WebDriverWait createWebDriverWait(AppiumDriver appiumDriver) {
        return new WebDriverWait(appiumDriver, Duration.ofSeconds(configurationManager.waitTime));
    }

}
