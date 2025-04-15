package mobileAutomation.utilities;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import mobileAutomation.Constants;
import mobileAutomation.utilities.automationFunctions.*;
import mobileAutomation.utilities.automationInterfaces.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

// PageActionsManager is the main controller to send the context(driver, wait etc.) to the actual implementation
public class PageActionManager implements
        InteractionInterface,
        ValidationInterface,
        MobileGeneralInterface,
        ImageInterface,
        ReportingInterface
{

    private final InteractionInterface interactionInterface;
    private final ValidationInterface validationInterface;
    private final MobileGeneralInterface mobileGeneralInterface;
    private final ImageInterface imageInterface;
    private final ReportingInterface reportingInterface;
    private final Double MATCH_THRESHOLD = Constants.IMAGE_MATCH_THRESHOLD;
    private final int SCALING_FACTOR = Constants.IMAGE_SCALING_FACTOR;

    public PageActionManager(ContextManager context) {
        // To initialize the page elements in a generic way
        PageFactory.initElements(new AppiumFieldDecorator(context.mobileDriver), this);

        // Below Interfaces will Delegate to the Implementation function
        this.interactionInterface = new InteractionFunction(context);
        this.validationInterface = new ValidationFunction(context);
        this.mobileGeneralInterface = new MobileGeneralFunction(context);
        this.imageInterface = new ImageFunction(context);
        this.reportingInterface = new ReportingFunction(context);
    }


    // ================== Interaction Functions ==================
    public void click(WebElement element) {
        interactionInterface.click(element);
    }

    public void clickByText(String elementText) {
        interactionInterface.clickByText(elementText);
    }

    public void clickById(String elementId) {
        interactionInterface.clickById(elementId);
    }

    public void clickByAccessibilityId(String elementId) {
        interactionInterface.clickByAccessibilityId(elementId);
    }

    public void type(WebElement element, String text) {
        interactionInterface.type(element, text);
    }



    // ================== Validation Functions ==================

    public void waitForElementToBeVisible(WebElement element) {
        validationInterface.waitForElementToBeVisible(element);
    }

    public void waitForElementToBeVisible(WebElement element, int timeoutInSecs) {
        validationInterface.waitForElementToBeVisible(element, timeoutInSecs);
    }

    public void waitForElementToBeInvisible(WebElement element) {
        validationInterface.waitForElementToBeInvisible(element);
    }

    public void waitForElementToBeInvisible(WebElement element, int timeoutInSecs) {
        validationInterface.waitForElementToBeInvisible(element, timeoutInSecs);
    }

    public boolean isElementVisible(WebElement element) {
        return validationInterface.isElementVisible(element);
    }

    public boolean isElementVisibleById(String elementId) {
        return validationInterface.isElementVisibleById(elementId);
    }

    public boolean isElementVisibleByAccessibilityId(String elementId) {
        return validationInterface.isElementVisibleByAccessibilityId(elementId);
    }

    public boolean isElementVisibleByText(String elementText) {
        return validationInterface.isElementVisibleByText(elementText);
    }

    public boolean isElementVisibleByXpath(String xpath) {
        return validationInterface.isElementVisibleByXpath(xpath);
    }

    public boolean isElementClickable(WebElement element) {
        return validationInterface.isElementClickable(element);
    }

    public void validateElementText(WebElement element, String expectedText) {
        validationInterface.validateElementText(element, expectedText);
    }

    public void validateText(String expectedText) {
        validationInterface.validateText(expectedText);
    }


    // ================== Mobile General Functions ==================
    public void navigateBack() {
        mobileGeneralInterface.navigateBack();
    }

    public String getDeviceName() {
        return mobileGeneralInterface.getDeviceName();
    }

    public String getPlatformName() {
        return mobileGeneralInterface.getPlatformName();
    }

    public boolean isPlatform(String platformName) {
        return mobileGeneralInterface.isPlatform(platformName);
    }

    public void sleep(int timeInSecs) {
        mobileGeneralInterface.sleep(timeInSecs);
    }

    public void tapOnScreen(int xCoordinate, int yCoordinate) {
        mobileGeneralInterface.tapOnScreen(xCoordinate, yCoordinate);
    }


    // ================== Image Functions ==================
    public void validateScreenVisible(String screenName, Double matchThreshold) {
        imageInterface.validateScreenVisible(screenName, matchThreshold);
    }

    public void validateScreenVisible(String screenName) {
        imageInterface.validateScreenVisible(screenName, MATCH_THRESHOLD);
    }

    public void validateScreenNotVisible(String screenName, Double matchThreshold) {
        imageInterface.validateScreenNotVisible(screenName, matchThreshold);
    }

    public void validateScreenNotVisible(String screenName) {
        imageInterface.validateScreenNotVisible(screenName, MATCH_THRESHOLD);
    }

    public void validateImageVisible(String imageName, Double matchThreshold) {
        imageInterface.validateImageVisible(imageName, matchThreshold);
    }

    public void validateImageVisible(String imageName) {
        imageInterface.validateImageVisible(imageName, MATCH_THRESHOLD);
    }

    public void validateImageNotVisible(String imageName, Double matchThreshold) {
        imageInterface.validateImageNotVisible(imageName, matchThreshold);
    }

    public void validateImageNotVisible(String imageName) {
        imageInterface.validateImageNotVisible(imageName, MATCH_THRESHOLD);
    }

    public Region getVisualImageRegion(String imageName, Double matchThreshold , int scalingFactor ) {
        return imageInterface.getVisualImageRegion(imageName, matchThreshold, scalingFactor);
    }

    public Region getVisualImageRegion(String imageName ) {
        return imageInterface.getVisualImageRegion(imageName, MATCH_THRESHOLD, SCALING_FACTOR);
    }

    public void clickImage(String imageName, Double matchThreshold, int scalingFactor) {
        imageInterface.clickImage(imageName, matchThreshold, scalingFactor);
    }

    public void clickImage(String imageName) {
        imageInterface.clickImage(imageName, MATCH_THRESHOLD, SCALING_FACTOR);
    }


    // ================== Reporting Functions ==================
    public void addSuccessLabelWithScreenshot(String labelName) {
        reportingInterface.addSuccessLabelWithScreenshot(labelName);
    }

    public void addSuccessLabel(String labelName) {
        reportingInterface.addSuccessLabel(labelName);
    }
}
