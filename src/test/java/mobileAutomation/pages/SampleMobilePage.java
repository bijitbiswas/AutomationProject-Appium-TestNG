package mobileAutomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.PageActionManager;
import org.openqa.selenium.WebElement;

public class SampleMobilePage extends PageActionManager {

    public SampleMobilePage(ContextManager context) {
        super(context);
    }

    @iOSXCUITFindBy(xpath = "//*[@value='%s']/preceding-sibling::XCUIElementTypeImage[@name='Product Image']")
    @AndroidFindBy(xpath = "//*[@text='%s']/preceding-sibling::android.widget.ImageView[@content-desc='Product Image']")
    private WebElement itemImage;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Cart-tab-item']")
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Displays number of items in your cart']")
    private WebElement cartBadge;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@value='Add To Cart']")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Tap to add product to cart']")
    private WebElement addToCartButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Remove Item']")
    @AndroidFindBy(accessibility = "Removes product from cart")
    private WebElement removeItemButton;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Confirms products for checkout']")
    private WebElement checkOutButton;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/fullNameET')]")
    private WebElement fullNameField;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/address1ET')]")
    private WebElement addressLine1;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/cityET')]")
    private WebElement city;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/zipET')]")
    private WebElement zipCode;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'/countryET')]")
    private WebElement country;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Saves user info for checkout']")
    private WebElement toPaymentButton;



    public void addItemToCart(String itemName) {
        swipeUpUntilVisible(itemImage, itemName);
        click(itemImage, itemName);
        click(addToCartButton);
        navigateBack();
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

        clickByXpath(androidXpath);
    }

    public void checkoutCart() {
        click(checkOutButton);
        type(fullNameField, "John Doe");
        type(addressLine1, "123 Main St");
        type(city, "New York");
        type(zipCode, "10001");
        type(country, "USA");
        click(toPaymentButton);
    }
}
