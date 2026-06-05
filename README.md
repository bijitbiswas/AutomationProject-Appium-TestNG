# Mobile Automation Framework (Appium + TestNG)

Comprehensive mobile UI automation framework built with Appium 2 and TestNG. It supports Android and iOS testing on local devices/emulators as well as BrowserStack and LambdaTest clouds, and layers in data-driven execution, visual validation, and rich reporting.

## Key Features
- Unified driver lifecycle that starts/stops a local Appium server and handles BrowserStack/LambdaTest sessions automatically.
- Page Object Model with reusable interaction, validation, mobile utility, and image-recognition helpers.
- Data-driven tests backed by Excel (`Testdata.xlsx`) and TestNG data providers.
- ExtentReports HTML reports with automatic screenshots for pass/fail and optional media uploads to BrowserStack and LambdaTest.
- Built-in helpers to push custom media to your remote device session before each run.
- Visual assertions via the Appium Images plugin with baseline management and diff artifacts.
- One-stop mobile automation stack that spans local simulators/emulators, plugged-in devices, and BrowserStack/LambdaTest real-device clouds for both Android and iOS.

## Cloud Device Integrations
Harness built-in integrations with the leading real device clouds—upload custom media, stream live sessions, and report results without leaving the framework.

<p align="center">
  <img src="ReadmeImages/browserstack-logo.png" alt="BrowserStack Integration" width="180" height="80" />
  <img src="ReadmeImages/lambdatest-logo.png" alt="LambdaTest Integration" width="180" height="80" />
</p>

## Platform Coverage
- **Local Simulators/Emulators:** Spin up Android Emulator or iOS Simulator sessions backed by a managed Appium 2 server, with shared capabilities loaded from `config/config.properties`.
- **Physical Devices:** Plug in real hardware, override desired capabilities, and leverage the same Page Object and reporting layers without code changes.
- **Cloud Device Farms:** Seamlessly launch the same tests on BrowserStack and LambdaTest with pre-run media uploads, automatic capability enrichment, and status updates pushed back to their dashboards.

## Prerequisites
- Java 18+ and Maven 3.8+ in your `PATH`.
- Node.js 18+ and Appium 2.x (`npm install -g appium`).
- Appium Images plugin for visual checks
    - Windows local runs install it automatically when you launch the framework.
    - macOS users install it manually via `sudo appium plugin install images`.
- Xcode + iOS simulators (macOS only) and/or Android SDK + emulators for local runs.
- BrowserStack and/or LambdaTest credentials (optional, for cloud execution).

## Project Structure

```
AutomationProject-Appium-TestNG/
│
├── apps/                                          # Android (.apk) and iOS (.app) binaries for local runs
├── config/
│   └── config.properties                          # Driver selection and all capability definitions
├── MobileTestSuites/
│   ├── SampleAndroidSuite.xml                     # TestNG suite for Android
│   └── SampleIOSSuite.xml                         # TestNG suite for iOS
│
├── src/
│   ├── main/java/mobileAutomation/
│   │   └── utilities/                             # Framework core — available to both main and test
│   │       ├── BaseManager.java                   # Lifecycle logic: beforeSuite/beforeClass/beforeMethod etc.
│   │       ├── BasePage.java                      # Base class for all page objects
│   │       ├── BrowserStackManager.java           # BrowserStack upload and session status helpers
│   │       ├── ConfigurationManager.java          # Reads config.properties and parses capabilities
│   │       ├── Constants.java                     # Shared constants (driver names, URLs, etc.)
│   │       ├── ContextManager.java                # Holds AppiumDriver, waits, and ExtentTest per class
│   │       ├── DriverManager.java                 # Creates, resets, and quits the Appium driver
│   │       ├── ExcelManager.java                  # Reads test data rows from Testdata.xlsx
│   │       ├── LambdaTestManager.java             # LambdaTest upload and session status helpers
│   │       ├── Region.java                        # Region model for image-based assertions
│   │       ├── ReportingManager.java              # ExtentReports setup, test creation, and logging
│   │       ├── RetryAnalyzer.java                 # IRetryAnalyzer — retries failed tests once
│   │       ├── RetryListener.java                 # IAnnotationTransformer — auto-attaches RetryAnalyzer
│   │       ├── ServerManager.java                 # Starts/stops the local Appium server (once per suite)
│   │       ├── automationFunctions/               # Reusable action and assertion helpers
│   │       │   ├── GeneralFunction.java
│   │       │   ├── ImageFunction.java
│   │       │   ├── InteractionFunction.java
│   │       │   ├── MobileGeneralFunction.java
│   │       │   ├── ReportingFunction.java
│   │       │   └── ValidationFunction.java
│   │       └── automationInterfaces/              # Interfaces implemented by automationFunctions
│   │           ├── ImageInterface.java
│   │           ├── InteractionInterface.java
│   │           ├── MobileGeneralInterface.java
│   │           ├── ReportingInterface.java
│   │           └── ValidationInterface.java
│   │
│   └── test/java/mobileAutomation/
│       ├── BaseTest.java                          # TestNG annotations (@BeforeSuite … @AfterSuite) — extend this
│       ├── pages/                                 # Page Object classes (extend BasePage)
│       │   ├── SampleLoginBasePage.java
│       │   └── SampleMobileBasePage.java
│       ├── testcases/                             # Test classes (extend BaseTest)
│       │   ├── SampleMobileAndroidTest.java
│       │   ├── SampleMobileIOSTest.java
│       │   └── SampleMobileParallelCheckTest.java
│       ├── testData/
│       │   └── Testdata.xlsx                      # Excel workbook — one sheet per test class, rows keyed by method name
│       ├── testFiles/                             # Files uploaded to cloud device sessions (PDFs, images, etc.)
│       │   ├── SAMPLE_IMAGE_FILE.png
│       │   └── SAMPLE_PDF_FILE.pdf
│       └── imageLocators/                         # Visual baseline screenshots (optional — only for image assertions)
│           ├── Android/
│           │   └── BASELINE_<Name>_<DeviceId>.png
│           └── iOS/
│               └── BASELINE_<Name>_<DeviceId>.png
│
├── TestReport/                                    # Generated ExtentReports HTML (timestamped per run)
├── VisualCheckResults/                            # Image comparison output (CHECK_* pass / FAIL_* mismatch)
└── logs/
    └── AppiumServer.log                           # Local Appium server log
```

## Configuration
1. Open `config/config.properties`; every target platform reads from this single file.
2. Set `DriverName` to one of `Android`, `iOS`, `BrowserStack-Android`, `BrowserStack-iOS`, `LambdaTest-Android`, or `LambdaTest-iOS`.
3. Update the matching capability JSON blob with real device identifiers, platform versions, credentials, and app references.

- **Simulators/Emulators and Physical devices:** keep `DriverName` as `Android` or `iOS` and set `deviceName`/`platformVersion` at your local emulator or simulator. Supply `appPackage`/`bundleId` or `app` paths appropriate for local binaries, `udid` (iOS), and any other required capabilities for the plugged-in hardware.
- **BrowserStack:** switch `DriverName` to `BrowserStack-Android` or `BrowserStack-iOS`, fill `userName`, `accessKey`, `app` (either uploaded ID or `bs://` handle), and optional build metadata.
- **LambdaTest:** switch `DriverName` accordingly, provide `userName`, `accessKey`, `app`, and remote device attributes; optional `build` labels help you track sessions in the dashboard.
- The capability blocks map directly to targets in `config.properties`:
  - `AndroidCapabilities` → Android emulator or plugged-in device.
  - `iOSCapabilities` → iOS simulator or real device.
  - `BrowserstackCapabilities` → BrowserStack cloud sessions.
  - `LambdaTestCapabilities` → LambdaTest cloud sessions.

## Initial Setup
1. Install prerequisites listed above and verify `mvn -v` and `appium -v`.
2. (macOS only) From the project root, install the images plugin once:
   ```bash
   sudo appium plugin install images
   ```
3. Configure `config/config.properties` as outlined in the **Configuration** section, including driver selection, capability details, and any optional wait settings.
4. (Optional) Place additional app binaries under `apps/` and reference them in the capability JSON.
5. If you plan to upload media to LambdaTest or BrowserStack during tests, populate the `filePaths` array in your test class (extends `BaseTest`) with the files you want to send.

## Writing Your First Test

### 1. Create a Page class

Add a class under `src/test/java/mobileAutomation/pages/` that extends `BasePage`. Declare your locators with dual `@AndroidFindBy` / `@iOSXCUITFindBy` annotations and expose business-level methods. Pass the `ContextManager` received from the test class to the `super` constructor.

```java
package mobileAutomation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import mobileAutomation.utilities.BasePage;
import mobileAutomation.utilities.ContextManager;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    public LoginPage(ContextManager context) {
        super(context);
    }

    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc='username']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@name='username']")
    private WebElement usernameField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc='password']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@name='password']")
    private WebElement passwordField;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='login']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='login']")
    private WebElement loginButton;

    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        hideKeyboard();
        click(loginButton);
    }
}
```

### 2. BaseTest — wiring TestNG annotations to BaseManager

`BaseTest` lives in `src/test/java/mobileAutomation/` and is the only class that carries TestNG annotations. It extends `BaseManager` (which holds all lifecycle logic) and each annotated method does nothing but delegate to the matching `BaseManager` method. **You never modify this class** — it is the fixed bridge between TestNG and the framework.

```java
package mobileAutomation;

import mobileAutomation.utilities.BaseManager;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class BaseTest extends BaseManager {

    @BeforeSuite
    public void onBeforeSuite() {
        beforeSuite();                        // starts Appium server (local) + initialises ExtentReports
    }

    @BeforeClass
    public void onBeforeClass() {
        beforeClass();                        // uploads media to cloud, creates the Appium driver
    }

    @BeforeMethod
    public void onBeforeMethod(ITestResult result) {
        beforeMethod(result);                 // resets driver on retry, opens a new ExtentTest node
    }

    @DataProvider(name = "getTestData")
    public Object[][] onDataProvider(Method method) {
        return dataProvider(method);          // reads rows from Testdata.xlsx keyed by method name
    }

    @AfterMethod
    public void onAfterMethod(ITestResult result) {
        afterMethod(result);                  // logs pass/fail/skip + screenshots to the report
    }

    @AfterClass(alwaysRun = true)
    public void onAfterClass(ITestContext context) {
        afterClass(context);                  // quits the Appium driver, marks cloud session status
    }

    @AfterSuite
    public void onAfterSuite() {
        afterSuite();                         // stops Appium server (local) + flushes ExtentReports
    }
}
```

> **Why this split?** `BaseManager` holds all the logic and lives in `src/main` so it can be shared freely. `BaseTest` stays in `src/test` because TestNG annotations must be on a class that the test runner discovers — keeping them here avoids coupling the framework core to the test scope.

### 3. Create a Test class

Add a class under `src/test/java/mobileAutomation/testcases/` that extends `BaseTest`. Instantiate page objects inside test methods by passing `getDriverContext()`.

```java
package mobileAutomation.testcases;

import mobileAutomation.BaseTest;
import mobileAutomation.pages.LoginPage;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(
            groups = {"Smoke", "Regression"},
            dataProvider = "getTestData",
            description = "Login with valid credentials"
    )
    public void loginWithValidCredentials(String username, String password) {
        LoginPage loginPage = new LoginPage(getDriverContext());
        loginPage.login(username, password);
    }
}
```

### 4. Add test data

Open `src/test/java/mobileAutomation/testData/Testdata.xlsx`. Each test class gets its own sheet named after the class. Add a row for every `@Test` method that uses `dataProvider = "getTestData"`, with the method name in the first column followed by the parameter values.

```
Sheet name : LoginTest
┌──────────────────────────┬──────────────┬──────────────┐
│ TestCaseName             │ username     │ password     │
├──────────────────────────┼──────────────┼──────────────┤
│ loginWithValidCredentials│ standard_user│ secret_sauce │
└──────────────────────────┴──────────────┴──────────────┘
```

### 5. Upload test files to cloud sessions (optional)

If your test needs custom media available on the remote device (PDFs, images, etc.), drop the files under `src/test/java/mobileAutomation/testFiles/` and assign `filePaths` in an instance initializer block. The framework uploads them before `@BeforeClass` creates the driver session and injects the returned media IDs into the capabilities automatically.

```java
public class LoginTest extends BaseTest {

    {
        filePaths = new String[]{
                "src/test/java/mobileAutomation/testFiles/SAMPLE_PDF_FILE.pdf",
                "src/test/java/mobileAutomation/testFiles/SAMPLE_IMAGE_FILE.png"
        };
    }

    // ... test methods
}
```

Leave `filePaths` unset for local or runs that need no extra media.

### 6. Add image-based locators or visual baselines (optional)

When using image-based element finding or visual comparison helpers, place PNG baseline screenshots under `src/test/java/mobileAutomation/imageLocators/` in the matching platform folder. The filename convention is:

```
BASELINE_<ScreenName>_<DeviceNameWithoutSpaces>.png
```

Example:
```
imageLocators/
├── Android/
│   └── BASELINE_LandingPage_emulator-5554.png
└── iOS/
    └── BASELINE_LandingPage_iPhone16Pro.png
```

On the first run without an existing baseline the framework captures one automatically and skips the assertion; subsequent runs compare against it and write diffs to `VisualCheckResults/`.

## Running Tests
Run everything from the project root.

- Execute a suite via Maven:
  ```bash
  mvn clean test -DsuiteXmlFile=MobileTestSuites/SampleAndroidSuite.xml
  ```
- Filter by TestNG group (e.g., only `Smoke` tests):
  ```bash
  mvn clean test -DsuiteXmlFile=MobileTestSuites/SampleAndroidSuite.xml -Dgroups=Smoke
  ```
- To target BrowserStack or LambdaTest:
  1. Set `DriverName` and credentials/app IDs in `config/config.properties`.
  2. Re-run the same Maven command; the framework provisions the remote driver, uploads any configured media, and reports session status.
- Alternatively, right-click any test class or method annotated with `@Test` in your IDE and choose `Run` for a quick ad-hoc execution.


## Reports & Logs
- ExtentReports HTML output is written to `TestReport/<SuiteName>_<timestamp>.html` and includes step logs plus screenshots for pass/fail events.
- Visual comparison artifacts are stored under `VisualCheckResults/` with `CHECK_` (pass) or `FAIL_` (mismatch) prefixes.
- Local Appium server logs are saved in `logs/AppiumServer.log` for troubleshooting.

## Troubleshooting Tips
- Ensure the Appium Images plugin remains installed after Appium upgrades.
- For iOS, trust the developer certificate and allow WebDriverAgent provisioning if prompted on first launch.
- When using real devices, grant necessary permissions and update capability JSON with valid UDIDs/platform versions.
- BrowserStack/LambdaTest executions require stable network access; see the respective dashboards for live sessions and device logs.

## ⚠️ Trial Version License

This framework can be **tried free for 30 days**.

After the trial period, you must:

- Stop using the framework, or
- Obtain written permission from the author, or
- Purchase a commercial license

**License:** See [LICENSE](./LICENSE)

Contact: biswas.bijit1994@gmail.com