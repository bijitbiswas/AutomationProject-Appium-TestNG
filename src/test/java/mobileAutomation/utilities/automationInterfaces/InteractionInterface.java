package mobileAutomation.utilities.automationInterfaces;

import org.openqa.selenium.WebElement;

public interface InteractionInterface {

    void click(WebElement element);

    void clickByText(String elementText);

    void clickById(String elementId);

    void clickByAccessibilityId(String elementId);

    void type(WebElement element, String text);
}
