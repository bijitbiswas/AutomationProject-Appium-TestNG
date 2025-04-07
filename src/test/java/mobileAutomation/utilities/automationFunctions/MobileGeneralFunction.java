package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumDriver;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.automationInterfaces.MobileGeneralInterface;
import org.json.JSONObject;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MobileGeneralFunction implements MobileGeneralInterface {

    AppiumDriver mobileDriver;
    WebDriverWait wait;
    FluentWait<AppiumDriver> fluentWait;
    private final String platformName;
    private final String driverName;

    public MobileGeneralFunction(ContextManager context) {
        this.mobileDriver = context.mobileDriver;
        this.wait = context.wait;
        this.fluentWait = context.fluentWait;
        this.driverName = context.driverName;
        this.platformName = context.platformName;
    }


    @Override
    public void navigateBack() {
        mobileDriver.navigate().back();
        System.out.println("=======Navigated back in application=======");
    }

    @Override
    public String getDeviceName() {
        String deviceName;
        if (driverName.equalsIgnoreCase("BrowserStack")) {
            String response = mobileDriver.executeScript("browserstack_executor: {\"action\": \"getSessionDetails\"}").toString();
            deviceName = new JSONObject(response).get("device").toString();
        } else {
            deviceName = mobileDriver.getCapabilities().getCapability("appium:deviceName").toString();
        }
        System.out.println("=======Device name is : "+ deviceName +"======");
        return deviceName;
    }

    @Override
    public String getPlatformName() {
        return platformName;
    }

    @Override
    public boolean isPlatform(String platformName) {
        return getPlatformName().equalsIgnoreCase(platformName);
    }

    @Override
    public void sleep(int timeInSecs) {
        try {
            Thread.sleep(timeInSecs * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
