package mobileAutomation.testcases;

import mobileAutomation.pages.SamplePage;
import mobileAutomation.utilities.DriverManager;
import org.testng.annotations.Test;

public class SampleTest extends DriverManager{

    SamplePage page;

    @Test(
            groups = {"Smoke", "Regression"},
            description = "This is a sample TestNG test"
    )
    public void addAndRemoveCartItem() {
        System.out.println("Starting Sample Test");

        page = new SamplePage(getDriverContext());

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
