package mobileAutomation.utilities.automationFunctions;

import org.openqa.selenium.WebElement;

public class GeneralFunction {

    String getElementName(WebElement element) {
        String elementString = element.toString();
        String elementName = "";
        if (elementString.contains("->")) {
            String elementStringNext = elementString.split("->")[1];
            int indexOfBrace = elementStringNext.lastIndexOf("]");
            if (indexOfBrace != -1)
                elementName = elementStringNext.substring(0, indexOfBrace).trim();
            else
                elementName = elementStringNext;
        } else if (elementString.contains("By.")) {
            int startIndex = elementString.lastIndexOf("By.") + 3;
            int endIndex = elementString.length() - 2;
            elementName = elementString.substring(startIndex, endIndex).trim();
        }
        return elementName;
    }

    String getDynamicElementLocator(WebElement element, String dynamicValue) {
        String elementName = String.format(getElementName(element), dynamicValue);
        return elementName.split(": ")[0].trim();
    }

    String getDynamicElementLocatorValue(WebElement element, String dynamicValue) {
        String elementName = String.format(getElementName(element), dynamicValue);
        return elementName.split(": ")[1].trim();
    }

    public void println(String message) {
        System.out.println("========"+message+"========");
    }

}
