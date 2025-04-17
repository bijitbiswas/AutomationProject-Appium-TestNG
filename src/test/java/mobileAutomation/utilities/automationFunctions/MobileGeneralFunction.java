package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumDriver;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.automationInterfaces.MobileGeneralInterface;
import org.json.JSONObject;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class MobileGeneralFunction extends GeneralFunction implements MobileGeneralInterface {

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
        println("Navigated back in application");
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
        println("Device name is : " + deviceName);
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

    @Override
    public void tapOnScreen(int xCoordinate, int yCoordinate) {

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(),
                xCoordinate, yCoordinate));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        sleep(1);
        mobileDriver.perform(List.of(tap));
        sleep(1);

        println("Tapped on screen at coordinates: (" + xCoordinate + ", " + yCoordinate + ")");
    }

    @Override
    public void swipeOnScreenWithCoordinate(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        sleep(1);
        mobileDriver.perform(List.of(swipe));
        sleep(1);
        println("Swiped from coordinate (" + startX + "," + startY + ") to (" + endX + "," + endY + ")");
    }

    @Override
    public void swipeUp() {
        Dimension size = mobileDriver.manage().window().getSize();
        int startY = (int) (size.height * 0.70);
        int endY = (int) (size.height * 0.30);
        int startX = (int) (size.width * 0.60);

        swipeOnScreenWithCoordinate(startX, startY, startX, endY);
    }

}
