package mobileAutomation.testcases;

import mobileAutomation.pages.SampleMobilePage;
import mobileAutomation.pages.SampleLoginPage;
import mobileAutomation.utilities.DriverManager;
import org.testng.annotations.Test;

public class SampleMobileAndroidTest extends DriverManager{

    SampleLoginPage sampleLoginPg;
    SampleMobilePage sampleMobilePg;

    @Test(
            groups = {"Smoke", "Regression"},
            dataProvider = "getTestData",
            description = "Login and add items to cart"
    )
    public void addItemsToCart(String userName, String password) {

        sampleLoginPg = new SampleLoginPage(getDriverContext());
        sampleMobilePg = new SampleMobilePage(getDriverContext());

        sampleLoginPg.login(userName, password);

        sampleMobilePg.addItemToCart("Sauce Labs Backpack (violet)");

        sampleMobilePg.addItemToCart("Sauce Labs Backpack (orange)");

        sampleMobilePg.viewCartAndVerifyItems("Sauce Labs Backpack (violet)", "Sauce Labs Backpack (orange)");

    }

    @Test(
            groups = {"Sanity"},
            description = "Remove item and checkout"
    )
    public void removeItemAndCheckout() {
        sampleMobilePg.removeItemFromCart("Sauce Labs Backpack (violet)");

        sampleMobilePg.checkoutCart();
    }
}
