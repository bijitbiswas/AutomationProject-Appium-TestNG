package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.automationInterfaces.ValidationInterface;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.function.Consumer;

public class ValidationFunction implements ValidationInterface {

    AppiumDriver mobileDriver;
    WebDriverWait wait;
    FluentWait<AppiumDriver> fluentWait;
    int SHORT_WAIT = 2;

    public ValidationFunction(ContextManager context) {
        this.mobileDriver = context.mobileDriver;
        this.wait = context.wait;
        this.fluentWait = context.fluentWait;
    }


    @Override
    public WebElement waitForElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.refreshed(ExpectedConditions
                .visibilityOf(element)));
    }

    @Override
    public WebElement waitForElementToBeVisible(WebElement element, int timeoutInSecs) {
        WebDriverWait newWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(timeoutInSecs));
        return newWait.until(ExpectedConditions.refreshed(ExpectedConditions
                .visibilityOf(element)));
    }

    @Override
    public boolean isElementVisible(WebElement element) {
        return conditionCheck( webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOf(element))), "visible");
    }

    @Override
    public boolean isElementVisibleById(String elementId) {
        return conditionCheck( webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.id(elementId)))), "visibleById");
    }

    @Override
    public boolean isElementVisibleByAccessibilityId(String elementId) {
        return conditionCheck( webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.accessibilityId(elementId)))), "visibleByAccessibilityId");
    }

    @Override
    public boolean isElementVisibleByText(String textValue) {
        String xpathExpression = "//*[contains(@text,'" + textValue + "')] | //*[contains(@value,'" + textValue + "')]";
        return conditionCheck( webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.xpath(xpathExpression)))), "visibleByText");
    }

    @Override
    public boolean isElementVisibleByXpath(String xpathExpression) {
        return conditionCheck( webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .visibilityOfElementLocated(AppiumBy.xpath(xpathExpression)))), "visibleByXpath");
    }

    @Override
    public boolean isElementClickable(WebElement element) {
        return conditionCheck( webDriverWait ->
                webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions
                        .elementToBeClickable(element))), "clickable");
    }

    @Override
    public void validateElementText(WebElement element, String expectedText) {
        System.out.println("=======Validating element text to be "+expectedText+"=======");
        String elementText;
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .visibilityOf(element)));
            elementText = actionElement.getText().isEmpty()?"": actionElement.getText();
        } catch (Exception e){
            System.out.println("=======Failed to find element to click======");
            e.printStackTrace();
            throw e;
        }
        Assert.assertEquals(elementText, expectedText, "Element text "+expectedText+"is not displayed on element");
        System.out.println("=======Element text "+expectedText+" is displayed on element=======");
    }

    @Override
    public void validateText(String expectedText) {
        System.out.println("=======Validating element text to be "+expectedText+" on screen=======");
        String xpathExpression = "//*[contains(@text,'" + expectedText + "')] | //*[contains(@value,'" + expectedText + "')]";
        Assert.assertTrue(isElementVisibleByXpath(xpathExpression), "Element text "+expectedText+" is not displayed on screen");
        System.out.println("=======Element text "+expectedText+" is displayed on screen=======");
    }

    private boolean conditionCheck(Consumer<WebDriverWait> func, String checkType) {
        WebDriverWait newWait = new WebDriverWait(mobileDriver, Duration.ofSeconds(SHORT_WAIT));
        boolean checkFlag = false;
        try {
            func.accept(newWait);
            checkFlag = true;
        } catch (Exception ignored) {
        }
        System.out.println("=======Element "+checkType+" is : "+checkFlag+"=======");
        return checkFlag;
    }

}
