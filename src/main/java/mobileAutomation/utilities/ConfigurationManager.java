package mobileAutomation.utilities;

import mobileAutomation.actionUtilities.automationFunctions.GeneralFunction;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public class ConfigurationManager extends GeneralFunction {

    {
        loadConfigurations();
    }

    static Properties properties;
    final public String driverName = getDriverName();
    final Long waitTime = getWaitTime();
    final boolean isJenkinsRun = getIsJenkinsRun();
    final DesiredCapabilities androidCapabilities = loadCapabilities("AndroidCapabilities");
    final DesiredCapabilities iOSCapabilities = loadCapabilities("iOSCapabilities");
    final HashMap<String, Object> lambdaTestCapabilities = loadLambdaTestCapabilities();
    final HashMap<String, Object> browserstackCapabilities = loadBrowserstackCapabilities();
    public String testcaseName;
    public ArrayList<String> mediaIds;
    final String cloudProviderAuth = getCloudProviderAuth();


    // Resolution order (highest priority wins):
    //   1. JVM system property   -DKey=value
    //   2. Environment variable  KEY_NAME=value  (camelCase → UPPER_SNAKE_CASE)
    //   3. config.properties     on the test classpath (src/test/resources/)
    //   4. default.config.properties  bundled in the framework JAR
    private String resolve(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null) return sysProp;

        String envVal = System.getenv(toEnvVarName(key));
        if (envVal != null) return envVal;

        return properties.getProperty(key);
    }

    // BrowserName → BROWSER_NAME,  ApplicationURL → APPLICATION_URL
    private String toEnvVarName(String key) {
        return key.replaceAll("(?<=[a-z])(?=[A-Z])", "_").toUpperCase();
    }

    private void loadConfigurations() {
        properties = new Properties();

        boolean userConfigExists = ConfigurationManager.class.getResource("/config.properties") != null;

        if (userConfigExists) {
            // Option 1: load user's classpath config.properties
            try (InputStream userClasspath = ConfigurationManager.class.getResourceAsStream("/config.properties")) {
                properties.load(userClasspath);
                println("Loaded config.properties from classpath");
            } catch (Exception e) {
                println("Could not load classpath config.properties: " + e.getMessage());
            }
        } else {
            // Option 2: load bundled defaults (lowest priority) — no user config found
            try (InputStream defaults = ConfigurationManager.class.getResourceAsStream("/default.config.properties")) {
                if (defaults != null) {
                    properties.load(defaults);
                    println("No config.properties found in src/test/resources. Loaded defaults from JAR. To OVERRIDE, CREATE src/test/resources/config.properties in your project with the following sample:\n" +
                            "  # 'DriverName' values should be either of 'Android', 'iOS', 'BrowserStack-Android', 'BrowserStack-iOS', 'LambdaTest-Android', 'LambdaTest-iOS'\n" +
                            "  DriverName               = Android\n" +
                            "\n" +
                            "  AndroidCapabilities      = { 'deviceName':'value', 'platformVersion':'value', 'appPackage':'value', 'appActivity':'com.saucelabs.mydemoapp.android.view.activities.SplashActivity', 'noReset':'false'}\n" +
                            "  iOSCapabilities          = { 'deviceName':'value', 'platformVersion':'value', 'udid':'value', 'bundleId':'com.saucelabs.mydemo.app.ios', 'noReset':'false'}\n" +
                            "  # BrowserstackCapabilities = {'userName':'value', 'accessKey':'value', 'app':'value', 'deviceName':'value', 'platformVersion':'value', 'buildName':'Android Build'}\n" +
                            "  # LambdaTestCapabilities   = {'userName':'value', 'accessKey':'value', 'app':'value', 'deviceName':'value', 'platformVersion':'value', 'build':'iOS Build'}\n" +
                            "\n" +
                            "  # Wait time in seconds to wait for an element\n" +
                            "  WaitTime=10");
                }
            } catch (Exception e) {
                println("Could not load default config: " + e.getMessage());
            }
        }
    }

    private String getDriverName() {
        assert properties != null;
        return properties.getProperty("DriverName");
    }

    private Long getWaitTime() {
        return Long.parseLong(resolve("WaitTime"));
    }

    private DesiredCapabilities loadCapabilities(String capabilityName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        assert properties != null;
        if (properties.getProperty(capabilityName) != null) {
            String appiumCapabilities = properties.getProperty(capabilityName);
            JSONObject jsonObject = new JSONObject(appiumCapabilities);
            for (String key : jsonObject.keySet()) {
                Object keyValue = jsonObject.get(key);
                capabilities.setCapability("appium:"+key, keyValue.toString());
            }
        } else {
            throw new IllegalStateException(
                    capabilityName + " is not configured. Provide it via one of:\n" +
                            "  src/test/resources/config.properties  →  " + capabilityName + " = { 'deviceName':'value', 'platformVersion':'value', ... }\n");
        }
        return capabilities;
    }

    private HashMap<String, Object> loadBrowserstackCapabilities() {

        HashMap<String, Object> capabilitiesMap = new HashMap<>();
        HashMap<String, Object> bsOptions = new HashMap<>();

        assert properties != null;
        if (properties.getProperty("BrowserstackCapabilities") != null) {
            String browserStackCapabilities = properties.getProperty("BrowserstackCapabilities");
            JSONObject jsonObject = new JSONObject(browserStackCapabilities);

            for (String key : jsonObject.keySet()) {
                Object keyValue = jsonObject.get(key);
                if (key.matches("userName|accessKey|projectName|buildName|sessionName")) {
                    bsOptions.put(key, keyValue);
                } else
                    capabilitiesMap.put("appium:"+key, keyValue);
            }
            capabilitiesMap.put("bstack:options", bsOptions);
        }
        return capabilitiesMap;
    }

    private HashMap<String, Object> loadLambdaTestCapabilities() {
        HashMap<String, Object> capabilitiesMap = new HashMap<>();

        assert properties != null;
        if (properties.getProperty("LambdaTestCapabilities") != null) {
            String lambdaTestCapabilities = properties.getProperty("LambdaTestCapabilities");
            JSONObject jsonObject = new JSONObject(lambdaTestCapabilities);

            for (String key : jsonObject.keySet()) {
                Object keyValue = jsonObject.get(key);
                if (!Objects.equals(key, "userName") && !Objects.equals(key, "accessKey")) {
                    capabilitiesMap.put(key, keyValue.toString());
                }
            }
        }
        return capabilitiesMap;
    }

    public String getCloudProviderAuth() {
        if (driverName.contains(Constants.LAMBDATEST)) {
            return getLambdaTestAuth();
        } else if (driverName.contains(Constants.BROWSERSTACK)) {
            return getBrowserStackAuth();
        }
        return null;
    }

    private String getLambdaTestAuth() {
        String lambdaTestCapabilities = properties.getProperty("LambdaTestCapabilities");
        JSONObject jsonObject = new JSONObject(lambdaTestCapabilities);
        String userName = jsonObject.getString("userName");
        String accessKey = jsonObject.getString("accessKey");
        return userName + ":" + accessKey;
    }

    private String getBrowserStackAuth() {
        String browserStackCapabilities = properties.getProperty("BrowserstackCapabilities");
        JSONObject jsonObject = new JSONObject(browserStackCapabilities);
        String userName = jsonObject.getString("userName");
        String accessKey = jsonObject.getString("accessKey");
        return userName + ":" + accessKey;
    }

    private boolean getIsJenkinsRun() {
        return Boolean.parseBoolean(resolve("IsJenkinsRun"));
    }

}
