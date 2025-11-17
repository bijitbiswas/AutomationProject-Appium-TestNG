package mobileAutomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import mobileAutomation.utilities.BasePage;
import org.openqa.selenium.WebElement;

public class SampleMobileBasePage extends BasePage {

    @iOSXCUITFindBy(xpath = "//*[@value='%s']/preceding-sibling::XCUIElementTypeImage[@name='Product Image']")
    @AndroidFindBy(xpath = "//*[@text='%s']/preceding-sibling::android.widget.ImageView[@content-desc='Product Image']")
    private WebElement itemImage;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Cart-tab-item']")
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Displays number of items in your cart']")
    private WebElement cartBadge;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='AddToCart']")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Tap to add product to cart']")
    private WebElement addToCartButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Remove Item']")
    @AndroidFindBy(accessibility = "Removes product from cart")
    private WebElement removeItemButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='ProceedToCheckout']")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Confirms products for checkout']")
    private WebElement checkOutButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Full Name')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/fullNameET')]")
    private WebElement fullNameField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Address Line 1')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/address1ET')]")
    private WebElement addressLine1;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'City')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/cityET')]")
    private WebElement city;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Zip Code')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/zipET')]")
    private WebElement zipCode;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Country')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/countryET')]")
    private WebElement country;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='To Payment']")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Saves user info for checkout']")
    private WebElement toPaymentButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Full Name')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/nameET')]")
    private WebElement nameField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Card Number')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/cardNumberET')]")
    private WebElement cardNumberField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Expiration Date')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/expirationDateET')]")
    private WebElement expirationDateField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'Security Code')]/following::XCUIElementTypeTextField[1]")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/securityCodeET')]")
    private WebElement securityCodeField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Review Order']")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Saves payment info and launches screen to review checkout data']")
    private WebElement reviewOrderButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeImage[@name='BackButton Icons']/preceding-sibling::XCUIElementTypeButton")
    private WebElement productBackButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Place Order']")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Completes the process of checkout']")
    private WebElement placeOrderButton;




    public void addItemToCart(String itemName) {
        swipeDown();
        swipeUpUntilVisible(itemImage, itemName);
        click(itemImage, itemName);
        swipeUpUntilVisible(addToCartButton);
        click(addToCartButton);
        if (isPlatform("iOS")) {
            click(productBackButton);
        } else {
            navigateBack();
        }
        addSuccessLabel("Item "+itemName+" added to cart");
    }

    public void viewCartAndVerifyItems(String... items) {
        click(cartBadge);
        waitForElementToBeVisible(removeItemButton, 3);
        for (String item : items) {
            validateText(item);
        }
        addSuccessLabelWithScreenshot("Items verified in cart");
    }

    public void removeItemFromCart(String itemName) {
        String androidXpath = "//android.widget.TextView[@text='"+itemName+"']/../following-sibling::*[contains(@resource-id,'addToCartLL')]/*[@text='Remove Item']";
        String iOSXpath = "//XCUIElementTypeStaticText[@name='"+itemName+"']/following-sibling::XCUIElementTypeButton[@name='Remove Item']";
        String removeXpath = isPlatform("iOS") ? iOSXpath : androidXpath;
        clickByXpath(removeXpath);
    }

    public void checkoutCart() {

        click(checkOutButton);
        type(fullNameField, "John Doe");
        type(addressLine1, "123 Main St");
        hideKeyboard();
        type(city, "New York");
        hideKeyboard();
        type(zipCode, "10001");
        hideKeyboard();
        type(country, "USA");
        hideKeyboard();
        swipeUp();
        click(toPaymentButton);
    }

    public void enterPaymentDetails() {
        type(nameField, "John Doe");
        type(cardNumberField, "41111111111111111");
        type(expirationDateField, "12/25");
        type(securityCodeField, "123");
        hideKeyboard();
        click(reviewOrderButton);
    }

    public void verifyOrderDetailsAndPlaceOrder() {
        validateText("John Doe");
        validateText("123 Main St");
        validateText("New York");
        swipeUp();
        validateText("10001");
        validateText("USA");

        swipeUp();
        validateText("4111 1111 1111 1111");
        validateText("12/25");
        validateText("Billing address is the same as shipping address");
        click(placeOrderButton);

        validateText("Checkout Complete");
        validateText("Thank you for your order");
    }
}
