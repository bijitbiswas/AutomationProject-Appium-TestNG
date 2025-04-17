package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import mobileAutomation.Constants;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.automationInterfaces.ValidationInterface;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.function.Consumer;

public class ValidationFunction extends GeneralFunction implements ValidationInterface {

    AppiumDriver mobileDriver;
    WebDriverWait wait;
    FluentWait<AppiumDriver> fluentWait;
    GeneralFunction generalFunction = new GeneralFunction();

    public ValidationFunction(ContextManager context) {
        this.mobileDriver = context.mobileDriver;
        this.wait = context.wait;
        this.fluentWait = context.fluentWait;
    }


    private boolean conditionCheck(Consumer<WebDriverWait> func, String checkType) {
        WebDriverWait newWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(Constants.SHORT_WAIT));
        boolean checkFlag = false;
        try {
            func.accept(newWait);
            checkFlag = true;
        } catch (Exception ignored) {
        }
        println("Element " + checkType + " is : " + checkFlag);
        return checkFlag;
    }

    private void waitForElementVisibleWithWait(WebElement element, WebDriverWait customWait) {
        String elementName = generalFunction.getElementName(element);
        try {
            customWait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
            println("Element " + elementName + " visible on screen");
        } catch (Exception e) {
            println("Failed waiting for element " + elementName + " to be visible");
            throw e;
        }
    }

    private void waitForElementInvisibleWithWait(WebElement element, WebDriverWait customWait) {
        String elementName = generalFunction.getElementName(element);
        try {
            customWait.until(ExpectedConditions.refreshed(ExpectedConditions.invisibilityOf(element)));
            println("Element " + elementName + " invisible on screen");
        } catch (Exception e) {
            println("Failed waiting for element " + elementName + " to be invisible");
            throw e;
        }
    }

    @Override
    public void waitForElementToBeVisible(WebElement element) {
        waitForElementVisibleWithWait(element, wait);
    }

    @Override
    public void waitForElementToBeVisible(WebElement element, int timeoutInSecs) {
        WebDriverWait newWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(timeoutInSecs));
        waitForElementVisibleWithWait(element, newWait);
    }

    @Override
    public void waitForElementToBeInvisible(WebElement element) {
        waitForElementInvisibleWithWait(element, wait);
    }

    @Override
    public void waitForElementToBeInvisible(WebElement element, int timeoutInSecs) {
        WebDriverWait newWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(timeoutInSecs));
        waitForElementInvisibleWithWait(element, newWait);
    }

    @Override
    public boolean isElementVisible(WebElement element) {
        return conditionCheck(webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOf(element))), "visible");
    }

    @Override
    public boolean isElementVisibleById(String elementId) {
        return conditionCheck(webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.id(elementId)))), "visibleById");
    }

    @Override
    public boolean isElementVisibleByAccessibilityId(String elementId) {
        return conditionCheck(webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.accessibilityId(elementId)))), "visibleByAccessibilityId");
    }

    @Override
    public boolean isElementVisibleByText(String textValue) {
        String xpathExpression = "//*[contains(@text,'" + textValue + "')] | //*[contains(@value,'" + textValue + "')]";
        return conditionCheck(webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.xpath(xpathExpression)))), "visibleByText");
    }

    @Override
    public boolean isElementVisibleByXpath(String xpathExpression) {
        return conditionCheck(webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.xpath(xpathExpression)))), "visibleByXpath");
    }

    @Override
    public boolean isElementClickable(WebElement element) {
        return conditionCheck(webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .elementToBeClickable(element))), "clickable");
    }

    @Override
    public void validateElementText(WebElement element, String expectedText) {
        println("Validating element text to be " + expectedText);
        String elementText;
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .visibilityOf(element)));
            elementText = actionElement.getText().isEmpty() ? "" : actionElement.getText();
        } catch (Exception e) {
            println("Failed to find element to get check element text");
            e.printStackTrace();
            throw e;
        }
        Assert.assertEquals(elementText, expectedText, "Element text " + expectedText + "is not displayed on element");
        println("Element text " + expectedText + " is displayed on element");
    }

    @Override
    public void validateText(String expectedText) {
        println("Validating element text to be " + expectedText + " on screen");
        String xpathExpression = "//*[contains(@text,'" + expectedText + "')] | //*[contains(@value,'" + expectedText + "')]";
        Assert.assertTrue(isElementVisibleByXpath(xpathExpression), "Element text " + expectedText + " is not displayed on screen");
        println("Element text " + expectedText + " is displayed on screen");
    }

}
