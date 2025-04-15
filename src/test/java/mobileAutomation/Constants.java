package mobileAutomation;

public final class Constants {

    public static final String BROWSERSTACK_URL = "https://hub-cloud.browserstack.com/wd/hub";
    public static final String APPIUM_LOCAL_HOST_SERVER_URL = "http://localhost:4723/";
    public static final String TEST_DATA_EXCEL_PATH = "src/test/java/mobileAutomation/testData/Testdata.xlsx";
    public static final String TEST_DATA_EXCEL_SHEET_NAME = "Sheet1";
    public static final String EXTENT_REPORT_NAME = "Automation Execution Report";
    public static final String EXTENT_REPORT_DOCUMENT_TITLE = "Automation Test Report";
    public static final String EXTENT_REPORT_FOLDER_WITH_PREFIX = "TestReport/Report_";
    public static final String EXTENT_REPORT_DATE_TIME_FORMAT = "yyyy-MM-dd HH-mm-ss";
    public static final String IMAGE_LOCATOR_PATH = "src/test/java/mobileAutomation/imageLocators/";
    public static final String IMAGE_RESULTS_FOLDER = "VisualCheckResults/";


    public static final int FLUENT_WAIT_POLLING_TIME_IN_SECS = 1;

    public static final Double IMAGE_MATCH_THRESHOLD = 0.75;
    public static final int IMAGE_SCALING_FACTOR = 1;

    public static final int SHORT_WAIT = 2;

}
