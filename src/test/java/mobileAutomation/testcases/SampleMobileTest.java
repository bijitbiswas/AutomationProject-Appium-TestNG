package mobileAutomation.testcases;

import mobileAutomation.pages.SampleMobilePage;
import mobileAutomation.utilities.DriverManager;
import org.testng.annotations.Test;

public class SampleMobileTest extends DriverManager{

    SampleMobilePage page;

    @Test(
            groups = {"Smoke", "Regression"},
            dataProvider = "getTestData",
            description = "This is a sample TestNG test"
    )
    public void addAndRemoveCartItem(String userName, String password) {
        System.out.println("Starting Sample Test");

        page = new SampleMobilePage(getDriverContext());

        page.login(userName, password);

        page.addItemToCart();

        page.removeItemFromCart();

    }

    @Test(
            groups = {"Sanity"},
            description = "This is another sample TestNG test"
    )
    public void checkWebView() {
        page.checkWebView();
    }

}
