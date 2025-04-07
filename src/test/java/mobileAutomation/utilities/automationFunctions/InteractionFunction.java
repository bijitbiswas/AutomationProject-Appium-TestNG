package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.automationInterfaces.InteractionInterface;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InteractionFunction implements InteractionInterface {

    AppiumDriver mobileDriver;
    WebDriverWait wait;
    FluentWait<AppiumDriver> fluentWait;

    public InteractionFunction(ContextManager context) {
        this.mobileDriver = context.mobileDriver;
        this.wait = context.wait;
        this.fluentWait = context.fluentWait;
    }

    @Override
    public void click(WebElement element) {
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
        element.click();
        System.out.println("=======Element "+elementText+" clicked=======");
    }

    @Override
    public void clickByText(String elementText) {
        WebElement actionElement;
        String xpathExpression = "//*[@text='" + elementText + "'] | //*[@value='" + elementText + "']";
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .elementToBeClickable(AppiumBy.xpath(xpathExpression))));
        } catch (Exception e) {
            System.out.println("=======Failed to find element to click by text======");
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        System.out.println("=======Element " + elementText + " clicked by text=======");
    }

    @Override
    public void clickById(String elementId) {
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .elementToBeClickable(AppiumBy.id(elementId))));
        } catch (TimeoutException e) {
            System.out.println("=======Failed to find element to click by id======");
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        System.out.println("=======Element "+elementId+" clicked by id=======");
    }

    @Override
    public void clickByAccessibilityId(String elementId) {
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .elementToBeClickable(AppiumBy.accessibilityId(elementId))));
        } catch (TimeoutException e) {
            System.out.println("=======Failed to find element to click by accessibilityId======");
            e.printStackTrace();
            throw e;
        }
        actionElement.click();
        System.out.println("=======Element "+elementId+" clicked by accessibilityId=======");
    }

    @Override
    public void type(WebElement element, String text) {
        WebElement actionElement;
        try {
            actionElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions
                    .visibilityOf(element)));
        } catch (TimeoutException e) {
            System.out.println("=======Failed to find element to type======");
            e.printStackTrace();
            throw e;
        }
        actionElement.clear();
        actionElement.sendKeys(text);
        System.out.println("=======Text "+text+" typed on element=======");
    }

}
