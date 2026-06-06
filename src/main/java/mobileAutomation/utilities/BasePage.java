package mobileAutomation.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import mobileAutomation.actionUtilities.automationFunctions.*;
import mobileAutomation.actionUtilities.automationInterfaces.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

// BasePage is the main controller to send the context(driver, wait etc.) to the actual implementation
public class BasePage implements
        InteractionInterface,
        ValidationInterface,
        MobileGeneralInterface,
        ImageInterface,
        ReportingInterface
{

    protected AppiumDriver mobileDriver;
    private final InteractionInterface interactionInterface;
    private final ValidationInterface validationInterface;
    private final MobileGeneralInterface mobileGeneralInterface;
    private final ImageInterface imageInterface;
    private final ReportingInterface reportingInterface;
    private final Double MATCH_THRESHOLD = Constants.IMAGE_MATCH_THRESHOLD;
    private final int SCALING_FACTOR = Constants.IMAGE_SCALING_FACTOR;

    public BasePage(ContextManager context) {

        this.mobileDriver = context.getAppiumDriver();
        // To initialize the page elements in a generic way
        PageFactory.initElements(new AppiumFieldDecorator(mobileDriver), this);


        // Below Interfaces will Delegate to the Implementation function
        this.interactionInterface = new InteractionFunction(context);
        this.validationInterface = new ValidationFunction(context);
        this.mobileGeneralInterface = new MobileGeneralFunction(context);
        this.imageInterface = new ImageFunction(context);
        this.reportingInterface = new ReportingFunction(context);
    }


    // ================== Interaction Functions ==================

    /**
     * Clicks on the given WebElement.
     *
     * @param element the element to click
     */
    public void click(WebElement element) {
        interactionInterface.click(element);
    }

    /**
     * Clicks on a WebElement after substituting a dynamic value into its locator.
     *
     * @param element      the element whose locator contains a placeholder
     * @param dynamicValue the value to replace the placeholder with
     */
    public void click(WebElement element, String dynamicValue) {
        interactionInterface.click(element, dynamicValue);
    }

    /**
     * Finds an element by XPath and clicks it.
     *
     * @param xpath the XPath expression to locate the element
     */
    public void clickByXpath(String xpath) {
        interactionInterface.clickByXpath(xpath);
    }

    /**
     * Finds an element by its visible text and clicks it.
     *
     * @param elementText the visible text of the element to click
     */
    public void clickByText(String elementText) {
        interactionInterface.clickByText(elementText);
    }

    /**
     * Finds an element by its resource ID and clicks it.
     *
     * @param elementId the resource ID of the element to click
     */
    public void clickById(String elementId) {
        interactionInterface.clickById(elementId);
    }

    /**
     * Finds an element by its accessibility ID and clicks it.
     *
     * @param elementId the accessibility ID of the element to click
     */
    public void clickByAccessibilityId(String elementId) {
        interactionInterface.clickByAccessibilityId(elementId);
    }

    /**
     * Clears the given input element and types the specified text into it.
     *
     * @param element the input element to type into
     * @param text    the text to enter
     */
    public void type(WebElement element, String text) {
        interactionInterface.type(element, text);
    }

    /**
     * Repeatedly swipes up the screen until the given element becomes visible.
     *
     * @param element the element to scroll into view
     */
    public void swipeUpUntilVisible(WebElement element) {
        interactionInterface.swipeUpUntilVisible(element);
    }

    /**
     * Repeatedly swipes up the screen until the element (resolved with a dynamic value) becomes visible.
     *
     * @param element      the element whose locator contains a placeholder
     * @param dynamicValue the value to replace the placeholder with
     */
    public void swipeUpUntilVisible(WebElement element, String dynamicValue) {
        interactionInterface.swipeUpUntilVisible(element, dynamicValue);
    }


    // ================== Validation Functions ==================

    /**
     * Waits for the given element to become visible using the default timeout.
     *
     * @param element the element to wait for
     */
    public void waitForElementToBeVisible(WebElement element) {
        validationInterface.waitForElementToBeVisible(element);
    }

    /**
     * Waits for the given element to become visible within the specified timeout.
     *
     * @param element       the element to wait for
     * @param timeoutInSecs maximum time in seconds to wait
     */
    public void waitForElementToBeVisible(WebElement element, int timeoutInSecs) {
        validationInterface.waitForElementToBeVisible(element, timeoutInSecs);
    }

    /**
     * Waits for the given element to disappear from view using the default timeout.
     *
     * @param element the element to wait for invisibility
     */
    public void waitForElementToBeInvisible(WebElement element) {
        validationInterface.waitForElementToBeInvisible(element);
    }

    /**
     * Waits for the given element to disappear from view within the specified timeout.
     *
     * @param element       the element to wait for invisibility
     * @param timeoutInSecs maximum time in seconds to wait
     */
    public void waitForElementToBeInvisible(WebElement element, int timeoutInSecs) {
        validationInterface.waitForElementToBeInvisible(element, timeoutInSecs);
    }

    /**
     * Returns whether the given element is currently visible on screen.
     *
     * @param element the element to check
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisible(WebElement element) {
        return validationInterface.isElementVisible(element);
    }

    /**
     * Returns whether an element with the given resource ID is currently visible on screen.
     *
     * @param elementId the resource ID of the element
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisibleById(String elementId) {
        return validationInterface.isElementVisibleById(elementId);
    }

    /**
     * Returns whether an element with the given accessibility ID is currently visible on screen.
     *
     * @param elementId the accessibility ID of the element
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisibleByAccessibilityId(String elementId) {
        return validationInterface.isElementVisibleByAccessibilityId(elementId);
    }

    /**
     * Returns whether an element with the given visible text is currently visible on screen.
     *
     * @param elementText the visible text of the element
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisibleByText(String elementText) {
        return validationInterface.isElementVisibleByText(elementText);
    }

    /**
     * Returns whether an element matching the given XPath is currently visible on screen.
     *
     * @param xpath the XPath expression to locate the element
     * @return true if the element is visible, false otherwise
     */
    public boolean isElementVisibleByXpath(String xpath) {
        return validationInterface.isElementVisibleByXpath(xpath);
    }

    /**
     * Returns whether the given element is in a clickable state.
     *
     * @param element the element to check
     * @return true if the element is clickable, false otherwise
     */
    public boolean isElementClickable(WebElement element) {
        return validationInterface.isElementClickable(element);
    }

    /**
     * Asserts that the text of the given element matches the expected text, failing the test if not.
     *
     * @param element      the element whose text to validate
     * @param expectedText the expected text value
     */
    public void validateElementText(WebElement element, String expectedText) {
        validationInterface.validateElementText(element, expectedText);
    }

    /**
     * Asserts that the expected text is visible somewhere on the current screen, failing the test if not.
     *
     * @param expectedText the text expected to be present on screen
     */
    public void validateText(String expectedText) {
        validationInterface.validateText(expectedText);
    }


    // ================== Mobile General Functions ==================

    /**
     * Navigates back to the previous screen using the device back action.
     */
    public void navigateBack() {
        mobileGeneralInterface.navigateBack();
    }

    /**
     * Hides the on-screen keyboard if it is currently displayed.
     */
    public void hideKeyboard() {
        mobileGeneralInterface.hideKeyboard();
    }

    /**
     * Returns the name of the device under test.
     *
     * @return the device name string
     */
    public String getDeviceName() {
        return mobileGeneralInterface.getDeviceName();
    }

    /**
     * Returns the platform name of the device under test (e.g. "Android" or "iOS").
     *
     * @return the platform name string
     */
    public String getPlatformName() {
        return mobileGeneralInterface.getPlatformName();
    }

    /**
     * Returns whether the device under test is running the specified platform.
     *
     * @param platformName the platform name to check (e.g. "Android" or "iOS")
     * @return true if the current platform matches, false otherwise
     */
    public boolean isPlatform(String platformName) {
        return mobileGeneralInterface.isPlatform(platformName);
    }

    /**
     * Pauses test execution for the given number of seconds.
     *
     * @param timeInSecs the number of seconds to sleep
     */
    public void sleep(int timeInSecs) {
        mobileGeneralInterface.sleep(timeInSecs);
    }

    /**
     * Performs a tap gesture at the specified screen coordinates.
     *
     * @param xCoordinate the x-axis coordinate of the tap point
     * @param yCoordinate the y-axis coordinate of the tap point
     */
    public void tapOnScreen(int xCoordinate, int yCoordinate) {
        mobileGeneralInterface.tapOnScreen(xCoordinate, yCoordinate);
    }

    /**
     * Performs a swipe gesture from one screen coordinate to another.
     *
     * @param startX the x-axis coordinate of the swipe start point
     * @param startY the y-axis coordinate of the swipe start point
     * @param endX   the x-axis coordinate of the swipe end point
     * @param endY   the y-axis coordinate of the swipe end point
     */
    public void swipeOnScreenWithCoordinate(int startX, int startY, int endX, int endY) {
        mobileGeneralInterface.swipeOnScreenWithCoordinate(startX, startY, endX, endY);
    }

    /**
     * Performs a swipe-up gesture on the screen.
     */
    public void swipeUp() {
        mobileGeneralInterface.swipeUp();
    }

    /**
     * Performs a swipe-down gesture on the screen.
     */
    public void swipeDown() {
        mobileGeneralInterface.swipeDown();
    }


    // ================== Image Functions ==================

    /**
     * Asserts that a full-screen reference image is visible using the given match threshold, failing the test if not.
     *
     * @param screenName     the name of the reference screen image file
     * @param matchThreshold the minimum similarity score (0.0–1.0) required for a match
     */
    public void validateScreenVisible(String screenName, Double matchThreshold) {
        imageInterface.validateScreenVisible(screenName, matchThreshold);
    }

    /**
     * Asserts that a full-screen reference image is visible using the default match threshold, failing the test if not.
     *
     * @param screenName the name of the reference screen image file
     */
    public void validateScreenVisible(String screenName) {
        imageInterface.validateScreenVisible(screenName, MATCH_THRESHOLD);
    }

    /**
     * Asserts that a full-screen reference image is NOT visible using the given match threshold, failing the test if it is found.
     *
     * @param screenName     the name of the reference screen image file
     * @param matchThreshold the minimum similarity score (0.0–1.0) required for a match
     */
    public void validateScreenNotVisible(String screenName, Double matchThreshold) {
        imageInterface.validateScreenNotVisible(screenName, matchThreshold);
    }

    /**
     * Asserts that a full-screen reference image is NOT visible using the default match threshold, failing the test if it is found.
     *
     * @param screenName the name of the reference screen image file
     */
    public void validateScreenNotVisible(String screenName) {
        imageInterface.validateScreenNotVisible(screenName, MATCH_THRESHOLD);
    }

    /**
     * Asserts that a reference image is visible on the current screen using the given match threshold, failing the test if not.
     *
     * @param imageName      the name of the reference image file
     * @param matchThreshold the minimum similarity score (0.0–1.0) required for a match
     */
    public void validateImageVisible(String imageName, Double matchThreshold) {
        imageInterface.validateImageVisible(imageName, matchThreshold);
    }

    /**
     * Asserts that a reference image is visible on the current screen using the default match threshold, failing the test if not.
     *
     * @param imageName the name of the reference image file
     */
    public void validateImageVisible(String imageName) {
        imageInterface.validateImageVisible(imageName, MATCH_THRESHOLD);
    }

    /**
     * Asserts that a reference image is NOT visible on the current screen using the given match threshold, failing the test if it is found.
     *
     * @param imageName      the name of the reference image file
     * @param matchThreshold the minimum similarity score (0.0–1.0) required for a match
     */
    public void validateImageNotVisible(String imageName, Double matchThreshold) {
        imageInterface.validateImageNotVisible(imageName, matchThreshold);
    }

    /**
     * Asserts that a reference image is NOT visible on the current screen using the default match threshold, failing the test if it is found.
     *
     * @param imageName the name of the reference image file
     */
    public void validateImageNotVisible(String imageName) {
        imageInterface.validateImageNotVisible(imageName, MATCH_THRESHOLD);
    }

    /**
     * Locates a reference image on screen and returns its bounding region using the given threshold and scaling factor.
     *
     * @param imageName      the name of the reference image file
     * @param matchThreshold the minimum similarity score (0.0–1.0) required for a match
     * @param scalingFactor  the factor by which the screenshot is scaled before matching
     * @return a {@link Region} representing the bounding box of the matched image on screen
     */
    public Region getVisualImageRegion(String imageName, Double matchThreshold , int scalingFactor ) {
        return imageInterface.getVisualImageRegion(imageName, matchThreshold, scalingFactor);
    }

    /**
     * Locates a reference image on screen using default threshold and scaling factor, and returns its bounding region.
     *
     * @param imageName the name of the reference image file
     * @return a {@link Region} representing the bounding box of the matched image on screen
     */
    public Region getVisualImageRegion(String imageName ) {
        return imageInterface.getVisualImageRegion(imageName, MATCH_THRESHOLD, SCALING_FACTOR);
    }

    /**
     * Locates a reference image on screen and taps its center using the given threshold and scaling factor.
     *
     * @param imageName      the name of the reference image file
     * @param matchThreshold the minimum similarity score (0.0–1.0) required for a match
     * @param scalingFactor  the factor by which the screenshot is scaled before matching
     */
    public void clickImage(String imageName, Double matchThreshold, int scalingFactor) {
        imageInterface.clickImage(imageName, matchThreshold, scalingFactor);
    }

    /**
     * Locates a reference image on screen using default threshold and scaling factor, and taps its center.
     *
     * @param imageName the name of the reference image file
     */
    public void clickImage(String imageName) {
        imageInterface.clickImage(imageName, MATCH_THRESHOLD, SCALING_FACTOR);
    }


    // ================== Reporting Functions ==================

    /**
     * Logs a success step in the Extent Report with the given label and attaches a screenshot.
     *
     * @param labelName the label text to display in the report for this step
     */
    public void addSuccessLabelWithScreenshot(String labelName) {
        reportingInterface.addSuccessLabelWithScreenshot(labelName);
    }

    /**
     * Logs a success step in the Extent Report with the given label (no screenshot attached).
     *
     * @param labelName the label text to display in the report for this step
     */
    public void addSuccessLabel(String labelName) {
        reportingInterface.addSuccessLabel(labelName);
    }
}
