package mobileAutomation.utilities.automationInterfaces;

import org.openqa.selenium.WebElement;

public interface ValidationInterface {

    WebElement waitForElementToBeVisible(WebElement element);

    WebElement waitForElementToBeVisible(WebElement element, int timeoutInSecs);

    boolean isElementVisible(WebElement element);

    boolean isElementVisibleById(String elementId);

    boolean isElementVisibleByAccessibilityId(String elementId);

    boolean isElementVisibleByText(String elementText);

    boolean isElementVisibleByXpath(String xpath);

    boolean isElementClickable(WebElement element);

    void validateElementText(WebElement element, String expectedText);

    void validateText(String expectedText);

}
