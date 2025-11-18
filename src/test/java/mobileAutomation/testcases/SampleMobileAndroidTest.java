package mobileAutomation.testcases;

import mobileAutomation.pages.SampleMobileBasePage;
import mobileAutomation.pages.SampleLoginBasePage;
import mobileAutomation.utilities.BaseTest;
import org.testng.annotations.Test;

public class SampleMobileAndroidTest extends BaseTest {

    {
        filePaths = new String[]{
                "src/test/java/mobileAutomation/testFiles/SAMPLE_PDF_FILE.pdf",
                "src/test/java/mobileAutomation/testFiles/SAMPLE_IMAGE_FILE.png"
        };
    }

    SampleLoginBasePage sampleLoginPg;
    SampleMobileBasePage sampleMobilePg;

    private final String item1 = "Sauce Labs Backpack (violet)";
    private final String item2 = "Sauce Labs Backpack (orange)";

    @Test(
            groups = {"Smoke", "Regression"},
            dataProvider = "getTestData",
            description = "Login and add items to cart"
    )
    public void addItemsToCart(String userName, String password) {

        sampleLoginPg = new SampleLoginBasePage();
        sampleMobilePg = new SampleMobileBasePage();

        sampleLoginPg.login(userName, password);

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
