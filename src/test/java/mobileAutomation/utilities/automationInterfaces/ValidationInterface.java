package mobileAutomation.utilities.automationInterfaces;

import org.openqa.selenium.WebElement;

public interface ValidationInterface {

    void waitForElementToBeVisible(WebElement element);

    void waitForElementToBeVisible(WebElement element, int timeoutInSecs);

    void waitForElementToBeInvisible(WebElement element);

    void waitForElementToBeInvisible(WebElement element, int timeoutInSecs);

    boolean isElementVisible(WebElement element);

    boolean isElementVisibleById(String elementId);

    boolean isElementVisibleByAccessibilityId(String elementId);

    boolean isElementVisibleByText(String elementText);

    boolean isElementVisibleByXpath(String xpath);

    boolean isElementClickable(WebElement element);

    void validateElementText(WebElement element, String expectedText);

    void validateText(String expectedText);

}
