package mobileAutomation;

import mobileAutomation.utilities.BaseManager;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class BaseTest extends BaseManager {

    @BeforeSuite
    public void onBeforeSuite() {
        beforeSuite();
    }

    @BeforeClass
    public void onBeforeClass() {
        beforeClass();
    }

    @BeforeMethod
    public void onBeforeMethod(ITestResult result) {
        beforeMethod(result);
    }

    @DataProvider(name = "getTestData")
    public String[][] onDataProvider(Method method) {
        return dataProvider(method);
    }

    @AfterMethod
    public void onAfterMethod(ITestResult result) {
        afterMethod(result);
    }

    @AfterClass(alwaysRun = true)
    public void onAfterClass(ITestContext context) {
        afterClass(context);
    }

    @AfterSuite
    public void onAfterSuite() {
        afterSuite();
    }
}
