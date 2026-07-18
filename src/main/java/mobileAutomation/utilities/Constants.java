package mobileAutomation.utilities;

public final class Constants {

    public static final String BROWSERSTACK_URL = "http://hub.browserstack.com/wd/hub";
    public static final String BROWSERSTACK_API_URL = "https://api-cloud.browserstack.com";
    public static final String LAMBDATEST_GRID_URL = "@mobile-hub.lambdatest.com/wd/hub";
    public static final String LAMBDATEST_API_URL = "https://api.lambdatest.com/mfs/v1.0";
    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";
    public static final String LAMBDATEST = "LambdaTest";
    public static final String LAMBDATEST_ANDROID = "LambdaTest-Android";
    public static final String LAMBDATEST_IOS = "LambdaTest-iOS";
    public static final String BROWSERSTACK = "BrowserStack";
    public static final String BROWSERSTACK_ANDROID = "BrowserStack-Android";
    public static final String BROWSERSTACK_IOS = "BrowserStack-iOS";

    public static final String TEST_DATA_EXCEL_SHEET_NAME = "Sheet1";
    public static final String EXTENT_REPORT_NAME = "Automation Execution Report";
    public static final String EXTENT_REPORT_DOCUMENT_TITLE = "Automation Test Report";
    public static final String EXTENT_REPORT_FOLDER_WITH_PREFIX = "TestReport/Report_";
    public static final String EXTENT_REPORT_DATE_TIME_FORMAT = "yyyy-MM-dd HH-mm-ss";
    public static final String IMAGE_LOCATOR_PATH = "src/test/resources/imageLocators/";
    public static final String IMAGE_RESULTS_FOLDER = "VisualCheckResults/";


    public static final int FLUENT_WAIT_POLLING_TIME_IN_SECS = 1;

    public static final Double IMAGE_MATCH_THRESHOLD = 0.75;
    public static final int IMAGE_SCALING_FACTOR = 1;

    public static final int SHORT_WAIT = 2;
    public static final int SWIPE_RETRY_COUNT = 10;
}
