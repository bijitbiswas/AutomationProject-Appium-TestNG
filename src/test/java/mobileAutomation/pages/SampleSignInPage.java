package mobileAutomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.PageActionManager;
import org.openqa.selenium.WebElement;

public class SampleSignInPage extends PageActionManager {

    public SampleSignInPage(ContextManager context) {
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


    public void login(String userName, String password) {
        click(menuBarButton);
        click(loginMenuItem);
        type(usernameField, userName);
        type(passwordField, password);
        click(loginButton);
        validateText("Products");
    }
}
