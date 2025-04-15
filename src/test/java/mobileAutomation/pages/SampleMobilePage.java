package mobileAutomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.PageActionManager;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class SampleMobilePage extends PageActionManager {

    public SampleMobilePage(ContextManager context) {
        super(context);
    }

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='View menu']")
    private WebElement menuBarButton;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='Login Menu Item']")
    private WebElement loginMenuItem;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'id/nameET')]")
    private WebElement usernameField;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'id/passwordET')]")
    private WebElement passwordField;

    @iOSXCUITFindBy(xpath = "")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Tap to login with given credentials']")
    private WebElement loginButton;


    @iOSXCUITFindBy(xpath = "//*[@value='Sauce Labs Backpack - Black']/preceding-sibling::XCUIElementTypeImage[@name='Product Image']")
    @AndroidFindBy(xpath = "//*[@text='Sauce Labs Backpack']/preceding-sibling::android.widget.ImageView[@content-desc='Product Image']")
    private WebElement sauceLabBackpackImage;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Cart-tab-item']")
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Displays number of items in your cart']")
    private WebElement cartBadge;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeCell/XCUIElementTypeStaticText[1]")
    @AndroidFindBy(id = "titleTV")
    private WebElement productTitle;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Remove Item']")
    @AndroidFindBy(accessibility = "Removes product from cart")
    private WebElement removeItemButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'urlET')]")
    private WebElement urlInputField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Go To Site']")
    private WebElement goToSiteButton;


    public void login(String userName, String password) {
        click(menuBarButton);
        click(loginMenuItem);
        type(usernameField, userName);
        type(passwordField, password);
        click(loginButton);
        validateText("Products");
    }

    public void addItemToCart() {
        String productName = "Sauce Labs Backpack";
        if (isPlatform("iOS")) {
            productName = "Sauce Labs Backpack - Black";
        }
        sleep(3);
        validateScreenVisible("LandingPage");
        click(sauceLabBackpackImage);

        if (isPlatform("Android")) {
//            // Additionally selecting one more item for android app
//            clickImage("AddMoreItemButton");
            clickByText("Add to cart");
        } else
            clickByText("Add To Cart");
        click(cartBadge);
        validateElementText(productTitle, productName);
        addSuccessLabelWithScreenshot("Item added to cart");
    }

    public void removeItemFromCart() {
        click(removeItemButton);
        validateText("No Items");
        addSuccessLabel("Item removed from cart");
    }

    public void checkWebView() {
        if (isPlatform("iOS")) {
            clickByAccessibilityId("More-tab-item");
            clickByAccessibilityId("Webview-menu-item");
            Assert.assertTrue(isElementVisible(goToSiteButton));
        } else {
            clickById("menuIV");
            clickByText("WebView");
            Assert.assertTrue(isElementVisibleById("goBtn"));
        }

        type(urlInputField,"https://www.google.com");
        navigateBack();
        sleep(5);
    }


}
