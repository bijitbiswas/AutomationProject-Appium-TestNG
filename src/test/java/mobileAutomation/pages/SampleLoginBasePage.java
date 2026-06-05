package mobileAutomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import mobileAutomation.utilities.BasePage;
import mobileAutomation.utilities.ContextManager;
import org.openqa.selenium.WebElement;

public class SampleLoginBasePage extends BasePage {

    public SampleLoginBasePage(ContextManager context) {
        super(context);
    }

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='More-tab-item']")
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='View menu']")
    private WebElement menuBarButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='Login Button']")
    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='Login Menu Item']")
    private WebElement loginMenuItem;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'id/nameET')]")
    private WebElement usernameField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField")
    @AndroidFindBy(xpath = "//android.widget.EditText[contains(@resource-id,'id/passwordET')]")
    private WebElement passwordField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Login']")
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Tap to login with given credentials']")
    private WebElement loginButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='bob@example.com']")
    private WebElement bobExampleUser;




    public void login(String userName, String password) {
        sleep(3);
        validateScreenVisible("LandingPage");
        click(menuBarButton);
        click(loginMenuItem);
        if (userName != null && password != null) {
            type(usernameField, userName);
            type(passwordField, password);
            hideKeyboard();
        } else {
            click(bobExampleUser);
        }
        click(loginButton);
        String title = isPlatform("iOS") ? "title" : "Products";
        validateText(title);
    }
}
