package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import mobileAutomation.utilities.Constants;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.automationInterfaces.InteractionInterface;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InteractionFunction extends GeneralFunction implements InteractionInterface {

    AppiumDriver mobileDriver;
    WebDriverWait wait;
    FluentWait<AppiumDriver> fluentWait;
    String driverName;
    ContextManager contextManager;
    GeneralFunction generalFunction = new GeneralFunction();

    public InteractionFunction(ContextManager context) {
        this.mobileDriver = context.getAppiumDriver();
        this.wait = context.getWait();
        this.fluentWait = context.getFluentWait();
        this.driverName = context.getDriverName();
        this.contextManager = context;
    }

    private By getBy(String locatorType, String locatorValue) {
        return switch (locatorType.toLowerCase()) {
            case "xpath" -> AppiumBy.xpath(locatorValue);
            case "id" -> AppiumBy.id(locatorValue);
            case "accessibilityid" -> AppiumBy.accessibilityId(locatorValue);
            case "classname" -> AppiumBy.className(locatorValue);
            default -> throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        };
    }

    @Override
    public void click(WebElement element) {
        String elementName = generalFunction.getElementName(element);
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .visibilityOf(element)));
        } catch (Exception e) {
            println("Failed to find element " + elementName + " to click");
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        println("Element " + elementName + " clicked");
    }

    @Override
    public void click(WebElement element, String dynamicValue) {
        String locator = generalFunction.getDynamicElementLocator(element, dynamicValue);
        String locatorValue = generalFunction.getDynamicElementLocatorValue(element, dynamicValue);

        WebElement actionElement;

        By by = getBy(locator, locatorValue);

        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .visibilityOfElementLocated(by)));
        } catch (Exception e) {
            println("Failed to find element " + locator + " : " + locatorValue + " to click");
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        println("Element " + locator + " : " + locatorValue + " clicked");
    }

    @Override
    public void clickByXpath(String xpath) {
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .elementToBeClickable(AppiumBy.xpath(xpath))));
        } catch (TimeoutException e) {
            println("Failed to find element to click by xpath " + xpath);
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        println("Element " + xpath + " clicked by xpath");
    }

    @Override
    public void clickByText(String elementText) {
        WebElement actionElement;
        String xpathExpression = "//*[@text='" + elementText + "'] | //*[@value='" + elementText + "']";
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .elementToBeClickable(AppiumBy.xpath(xpathExpression))));
        } catch (Exception e) {
            println("Failed to find element to click by text " + elementText);
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        println("Element " + elementText + " clicked by text");
    }

    @Override
    public void clickById(String elementId) {
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .elementToBeClickable(AppiumBy.id(elementId))));
        } catch (TimeoutException e) {
            println("Failed to find element to click by id " + elementId);
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        println("Element " + elementId + " clicked by id");
    }

    @Override
    public void clickByAccessibilityId(String elementId) {
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .elementToBeClickable(AppiumBy.accessibilityId(elementId))));
        } catch (TimeoutException e) {
            println("Failed to find element to click by accessibilityId " + elementId);
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        println("Element " + elementId + " clicked by accessibilityId");
    }

    @Override
    public void type(WebElement element, String text) {
        String elementName = generalFunction.getElementName(element);
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .visibilityOf(element)));
        } catch (TimeoutException e) {
            println("Failed to find element " + elementName + " to type");
            e.printStackTrace();
            throw e;
        }
        actionElement.clear();
        actionElement.sendKeys(text);
        println("Text " + text + " typed on element " + elementName);
    }

    @Override
    public void swipeUpUntilVisible(WebElement element) {
        MobileGeneralFunction mobileGeneralFunction = new MobileGeneralFunction(contextManager);
        ValidationFunction validationFunction = new ValidationFunction(contextManager);
        for (int i = 0; i <= Constants.SWIPE_RETRY_COUNT; i++) {
            if (!validationFunction.isElementVisible(element)) {
                mobileGeneralFunction.swipeUp();
            } else {
                break;
            }
        }
    }

    @Override
    public void swipeUpUntilVisible(WebElement element, String dynamicValue) {
        String locator = generalFunction.getDynamicElementLocator(element, dynamicValue);
        String locatorValue = generalFunction.getDynamicElementLocatorValue(element, dynamicValue);

        for (int i = 0; i <= Constants.SWIPE_RETRY_COUNT; i++) {
            if (!isVisible(locator, locatorValue)) {
                new MobileGeneralFunction(contextManager).swipeUp();
            } else {
                break;
            }
        }
    }

    private boolean isVisible(String locator, String locatorValue) {
        ValidationFunction validationFunction = new ValidationFunction(contextManager);
        return switch (locator.toLowerCase()) {
            case "xpath" -> validationFunction.isElementVisibleByXpath(locatorValue);
            case "id" -> validationFunction.isElementVisibleById(locatorValue);
            case "accessibilityid" -> validationFunction.isElementVisibleByAccessibilityId(locatorValue);
            default -> throw new IllegalArgumentException("Invalid locator type: " + locator);
        };
    }

}
