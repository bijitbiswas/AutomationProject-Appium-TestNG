package mobileAutomation.utilities;

import mobileAutomation.actionUtilities.automationFunctions.GeneralFunction;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public class ConfigurationManager extends GeneralFunction {

    {
        loadCapabilities();
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


    /**
     * Below methods are the supporting methods to load the configuration from config.properties file
     */

    private void loadCapabilities() {
        println("Loading configuration from config.properties");
        File src = new File("config" + File.separator + "config.properties");
        try {
            FileInputStream fis = new FileInputStream(src);
            properties = new Properties();
            properties.load(fis);
        } catch (Exception e) {
            println("Exception message : " + e.getMessage());
        }
        println("Configuration loaded successfully");
    }

    private String getDriverName() {
        assert properties != null;
        return properties.getProperty("DriverName");
    }

    private Long getWaitTime() {
        assert properties != null;
        return Long.parseLong(properties.getProperty("WaitTime"));
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
        assert properties != null;
        return Boolean.parseBoolean(properties.getProperty("IsJenkinsRun"));
    }

}
