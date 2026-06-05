package mobileAutomation.utilities.automationInterfaces;

import org.openqa.selenium.WebElement;

public interface InteractionInterface {

    void click(WebElement element);

    void click(WebElement element, String dynamicValue);

    void clickByXpath(String xpath);

    void clickByText(String elementText);

    void clickById(String elementId);

    void clickByAccessibilityId(String elementId);

    void type(WebElement element, String text);

    void swipeUpUntilVisible(WebElement element);

    void swipeUpUntilVisible(WebElement element, String dynamicValue);

}
