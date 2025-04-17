package mobileAutomation.utilities;

import mobileAutomation.utilities.automationFunctions.GeneralFunction;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public class ConfigurationManager extends GeneralFunction {

    {
        loadCapabilities();
    }

    static Properties properties;
    final public String driverName = getDriverName();
    final public String platformName = getPlatformName();
    final DesiredCapabilities androidCapabilities = loadCapabilities("AndroidCapabilities");
    final DesiredCapabilities iOSCapabilities = loadCapabilities("iOSCapabilities");
    final DesiredCapabilities browserstackCapabilities = loadBrowserstackCapabilities();
    final Long waitTime = getWaitTime();

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

    private String getPlatformName() {
        if (driverName.contains("Browserstack")) {
            assert properties != null;
            return properties.getProperty("PlatformName");
        } else
            return driverName;
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
                capabilities.setCapability(key, keyValue.toString());
            }
        }
        return capabilities;
    }

    private DesiredCapabilities loadBrowserstackCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        HashMap<String, Object> bsOptions = new HashMap<>();

        assert properties != null;
        if (properties.getProperty("BrowserstackCapabilities") != null) {
            String browserStackCapabilities = properties.getProperty("BrowserstackCapabilities");
            JSONObject jsonObject = new JSONObject(browserStackCapabilities);

            for (String key : jsonObject.keySet()) {
                Object keyValue = jsonObject.get(key);
                if (Objects.equals(key, "userName") || Objects.equals(key, "accessKey") ||
                        Objects.equals(key, "buildName") || Objects.equals(key, "projectName")) {
                    if (key.equals("buildName") && keyValue.toString().isEmpty()) {
                        keyValue = "Appium Build";
                    }
                    bsOptions.put(key, keyValue);
                } else
                    capabilities.setCapability(key, keyValue);
            }
            bsOptions.put("appiumVersion", "2.6.0");
            capabilities.setCapability("locale", "en_US");
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("gpsEnabled", true);
            capabilities.setCapability("browserstack.idleTimeout", 160);
            capabilities.setCapability("browserstack.timezone", "Los_Angeles");
            capabilities.setCapability("bstack:options", bsOptions);
        }
        return capabilities;
    }

}
