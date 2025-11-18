package mobileAutomation.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import mobileAutomation.Constants;
import mobileAutomation.utilities.automationFunctions.GeneralFunction;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverManager extends GeneralFunction {
    private static String driverName;
    private static final ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> mobileWait = new ThreadLocal<>();
    private static final ThreadLocal<FluentWait<AppiumDriver>> mobileFluentWait = new ThreadLocal<>();

    public static void initializeDriver(ConfigurationManager configurationManager) {

        driverName = configurationManager.driverName;

        if (mobileDriver.get() == null) {
            boolean isLambdaTest = driverName.equalsIgnoreCase(Constants.LAMBDATEST_ANDROID)
                    || driverName.equalsIgnoreCase(Constants.LAMBDATEST_IOS);
            boolean isBrowserstack = driverName.equalsIgnoreCase(Constants.BROWSERSTACK_ANDROID)
                    || driverName.equalsIgnoreCase(Constants.BROWSERSTACK_IOS);

            if (isLambdaTest) {
                initializeLambdaTestDriver(driverName, configurationManager);
            } else if (isBrowserstack) {
                initializeBrowserstackDriver(driverName, configurationManager);
            } else {
                initializeLocalDriver(driverName, configurationManager);
            }

            mobileWait.set(createWebDriverWait(configurationManager.waitTime));
            mobileFluentWait.set(createFluentWait(configurationManager.waitTime));

            System.out.println("******* Mobile Driver is initialized *******");
        }
    }

    public static AppiumDriver getMobileDriver() {
        if (mobileDriver.get() == null) {
            throw new IllegalStateException("Driver is not initialized. Call initializeDriver() first.");
        }
        return mobileDriver.get();
    }

    public static String getDriverName() {
        return driverName;
    }

    public static WebDriverWait getMobileWait() {
        if(mobileWait.get() == null){
            throw new IllegalStateException("Wait is not initialized. Call initializeDriver() first.");
        }
        return mobileWait.get();
    }

    public static FluentWait<AppiumDriver> getMobileFluentWait() {
        if(mobileFluentWait.get() == null){
            throw new IllegalStateException("Fluent Wait is not initialized. Call initializeDriver() first.");
        }
        return mobileFluentWait.get();
    }

    public static void quitDriver(boolean testPassed) {
        // Mark test status on LambdaTest/BrowserStack
        if (driverName.contains(Constants.LAMBDATEST)) {
            LambdaTestManager.markLambdaTestStatus(getMobileDriver(), testPassed);
        } else if (driverName.contains(Constants.BROWSERSTACK)) {
            BrowserStackManager.markBrowserStackStatus(getMobileDriver(), testPassed);
        }

        // Quit the mobile driver
        getMobileDriver().quit();

        println("Mobile Driver is closed");
        mobileDriver.remove();
        mobileWait.remove();
        mobileFluentWait.remove();
        ServerManager.stopServer();
    }


    private static void initializeLocalDriver(String driverName, ConfigurationManager configurationManager) {
        ServerManager.startServer();
        URL serverUrl = ServerManager.getServer().getUrl();

        if (driverName.equalsIgnoreCase(Constants.ANDROID)) {
            mobileDriver.set(new AndroidDriver(serverUrl, getAndroidCapabilities(configurationManager)));
        } else if (driverName.equalsIgnoreCase(Constants.IOS)) {
            mobileDriver.set(new IOSDriver(serverUrl, getIOSCapabilities(configurationManager)));
        } else {
            throw new IllegalArgumentException("Unsupported local platform: " + driverName);
        }
    }

    private static void initializeLambdaTestDriver(String driverName, ConfigurationManager configurationManager) {

        String lambdaTestAuth = configurationManager.cloudProviderAuth;
        String lambdaTestURL = "https://" + lambdaTestAuth + Constants.LAMBDATEST_GRID_URL;
        URL gridUrl;
        try {
            gridUrl = URI.create(lambdaTestURL).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new RuntimeException("Invalid LambdaTest URL", e);
        }

        if (driverName.equalsIgnoreCase(Constants.LAMBDATEST_ANDROID)) {
            mobileDriver.set(new AndroidDriver(gridUrl, getLambdaTestCapabilities(configurationManager)));
        } else if (driverName.equalsIgnoreCase(Constants.LAMBDATEST_IOS)) {
            mobileDriver.set(new IOSDriver(gridUrl, getLambdaTestCapabilities(configurationManager)));
        } else {
            throw new IllegalArgumentException("Unsupported LambdaTest platform: " + driverName);
        }
    }

    private static void initializeBrowserstackDriver(String driverName, ConfigurationManager configurationManager) {

        URL gridUrl;
        try {
            gridUrl = URI.create(Constants.BROWSERSTACK_URL).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new RuntimeException("Invalid Browserstack URL", e);
        }

        if (driverName.equalsIgnoreCase(Constants.BROWSERSTACK_ANDROID)) {
            mobileDriver.set(new AndroidDriver(gridUrl, getBrowserstackCapabilities(configurationManager)));
        } else if (driverName.equalsIgnoreCase(Constants.BROWSERSTACK_IOS)) {
            mobileDriver.set(new IOSDriver(gridUrl, getBrowserstackCapabilities(configurationManager)));
        } else {
            throw new IllegalArgumentException("Unsupported Browserstack platform: " + driverName);
        }
    }

    private static DesiredCapabilities getAndroidCapabilities(ConfigurationManager configurationManager) {
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

    private static DesiredCapabilities getIOSCapabilities(ConfigurationManager configurationManager) {
        DesiredCapabilities capabilities = configurationManager.iOSCapabilities;
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("appium:automationName", "XCUITest");
        capabilities.setCapability("appium:noReset", true);
        return capabilities;
    }

    private static DesiredCapabilities getBrowserstackCapabilities(ConfigurationManager configurationManager) {

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

    private static DesiredCapabilities getLambdaTestCapabilities(ConfigurationManager configurationManager) {

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

    private static FluentWait<AppiumDriver> createFluentWait(Long waitTimeInSecs) {
        return new FluentWait<>(getMobileDriver())
                .withTimeout(Duration.ofSeconds(waitTimeInSecs))
                .pollingEvery(Duration.ofSeconds(Constants.FLUENT_WAIT_POLLING_TIME_IN_SECS));
    }

    private static WebDriverWait createWebDriverWait(Long waitTimeInSecs) {
        return new WebDriverWait(getMobileDriver(), Duration.ofSeconds(waitTimeInSecs));
    }

}
