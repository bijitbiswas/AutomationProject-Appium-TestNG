package mobileAutomation.testcases;

import mobileAutomation.pages.SampleLoginPage;
import mobileAutomation.pages.SampleMobilePage;
import mobileAutomation.utilities.DriverManager;
import org.testng.annotations.Test;

public class SampleMobileIOSTest extends DriverManager{

    SampleLoginPage sampleLoginPg;
    SampleMobilePage sampleMobilePg;

    private final String item1 = "Sauce Labs Backpack - Violet";
    private final String item2 = "Sauce Labs Backpack - Orange";

    @Test(
            groups = {"Smoke", "Regression"},
            description = "Login and add items to cart"
    )
    public void addItemsToCart() {

        sampleLoginPg = new SampleLoginPage(getDriverContext());
        sampleMobilePg = new SampleMobilePage(getDriverContext());

        sampleLoginPg.login(null, null);

        sampleMobilePg.addItemToCart(item1);

        sampleMobilePg.addItemToCart(item2);

        sampleMobilePg.viewCartAndVerifyItems(item1, item2);

    }

    @Test(
            groups = {"Sanity"},
            description = "Remove item and checkout"
    )
    public void removeItemAndCheckout() {

        sampleMobilePg.removeItemFromCart(item1);

        sampleMobilePg.checkoutCart();

        sampleMobilePg.enterPaymentDetails();

        sampleMobilePg.verifyOrderDetailsAndPlaceOrder();

    }
}
