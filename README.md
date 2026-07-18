# Mobile Automation Framework (Appium + TestNG)

Comprehensive mobile UI automation framework built with Appium 2 and TestNG. It supports Android and iOS testing on local devices/emulators as well as BrowserStack and LambdaTest clouds, and layers in data-driven execution, visual validation, and rich reporting.

## Key Features

- **Unified Driver Lifecycle** — Automatically starts and stops a local Appium server per suite, so tests need no manual server management.
- **Page Object Model (POM)** — Clean separation between test logic and UI interactions via page classes
- **Interface-driven design** — Interaction, Validation, Reporting, and Web actions are defined as interfaces and injected into pages through `BasePage`, making the contract explicit and implementations swappable
- **Data-driven testing** — Test data loaded from Excel (`.xlsx`) via Apache POI, mapped to test methods by name through a `@DataProvider`
- **Extent HTML Reports** — Step-level pass/fail logging with screenshots captured automatically on each test method completion
- **Visual Validation** — Image-based assertions via the Appium Images plugin compare against stored baselines and write diff artifacts to `VisualCheckResults/` for easy triage.
- **Retry Mechanism** — Failed tests are automatically retried once via `RetryAnalyzer` and `RetryListener`, reducing noise from transient flakiness without any changes to test code.
- **Cross-Platform Support (iOS & Android)** — A single framework and shared Page Object layer covers Android emulators/physical devices and iOS simulators/real devices, with dual `@AndroidFindBy` / `@iOSXCUITFindBy` locators enabling the same page classes to run on both platforms.
- **CI/CD ready** — `Jenkinsfile` included with parameterised browser and suite selection, dynamic config generation, and report publishing
- **Cloud Device Farm Integration** — Seamlessly targets BrowserStack and LambdaTest real-device clouds with automatic capability enrichment, pre-run media uploads, and session status reporting.

---

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

---

## Prerequisites

| Requirement | Details | Notes |
|---|---|---|
| **Java 18+** | JDK 18 or higher in your `PATH` | Verify with `java -version` |
| **Maven 3.8+** | Apache Maven 3.8 or higher in your `PATH` | Verify with `mvn -v` |
| **Node.js 18+** | Node.js 18 or higher | Required to run Appium |
| **Appium 2.x** | Install globally via `npm install -g appium` | Verify with `appium -v` |
| **Appium Images Plugin** | For visual validation checks | **Windows:** installed automatically on framework launch. **macOS:** run `sudo appium plugin install images` manually |
| **Android SDK** | Android SDK with at least one emulator configured | Required for Android local runs |
| **Xcode + iOS Simulator** | Xcode with iOS simulators (macOS only) | Required for iOS local runs |
| **BrowserStack Credentials** | `userName` and `accessKey` from your BrowserStack account | Optional — cloud execution only |
| **LambdaTest Credentials** | `userName` and `accessKey` from your LambdaTest account | Optional — cloud execution only |

---

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

---

## Initial Setup
1. Install prerequisites listed above and verify `mvn -v` and `appium -v`.
2. (macOS only) From the project root, install the images plugin once:
   ```bash
   sudo appium plugin install images
   ```
3. Configure `config/config.properties` as outlined in the **Configuration** section, including driver selection, capability details, and any optional wait settings.
4. (Optional) Place additional app binaries under `apps/` and reference them in the capability JSON.
5. If you plan to upload media to LambdaTest or BrowserStack during tests, populate the `filePaths` array in your test class (extends `BaseTest`) with the files you want to send.

---

## Writing Your First Test

### Step 1: Create a Page Class

Create a page class extending `BasePage`. Declare your locators with dual `@AndroidFindBy` / `@iOSXCUITFindBy` annotations and expose business-level methods. Pass the `ContextManager` received from the test class to the `super` constructor.

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

### Step 2: Create a BaseTest Class

Create a `BaseTest` extending `BaseManager` and call the delegated TestNG lifecycle methods. **You never modify this class** — it is the fixed bridge between TestNG and the framework.

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

> `BaseManager` owns all the logic. `BaseTest` (in `src/test`) is purely a thin TestNG adapter — it only maps annotations to `BaseManager` calls. This keeps framework infrastructure free of TestNG lifecycle coupling.

### Step 3: Create a Test Class

Create a test class extending `BaseTest`.

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

### Step 4: Add Test Data

Add test data to Testdata.xlsx in a sheet named with test name like `loginWithValidCredentials`. Add a row for every `@Test` method that uses `dataProvider = "getTestData"`, with the method name in the first column followed by the parameter values.

```
Sheet name : Sheet1
┌──────────────────────────┬──────────────┬──────────────┐
│ TestCaseName             │ username     │ password     │
├──────────────────────────┼──────────────┼──────────────┤
│ loginWithValidCredentials│ standard_user│ secret_sauce │
└──────────────────────────┴──────────────┴──────────────┘
```

### Step 5: Register in Suite XML

Add the test class to a suite file:

```xml
<test name="Login Tests">
    <classes>
        <class name="mobileAutomation.testcases.LoginTest"/>
    </classes>
</test>
```

### Step 6: Upload test files to cloud sessions (optional)

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

### Step 7: Add image-based locators or visual baselines (optional)

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

---

## Configuration
1. Open / Create `config/config.properties` at root project directory.
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

Sample `config/config.properties`:
```properties
# 'DriverName' values should be either of 'Android', 'iOS', 'BrowserStack-Android', 'BrowserStack-iOS', 'LambdaTest-Android', 'LambdaTest-iOS'
DriverName               = Android

AndroidCapabilities      = { 'deviceName':'emulator-5554', 'platformVersion':'13.0', 'appPackage':'com.saucelabs.mydemoapp.android', 'appActivity':'com.saucelabs.mydemoapp.android.view.activities.SplashActivity', 'noReset':'false'}
iOSCapabilities          = { 'deviceName':'', 'platformVersion':'18.2', 'udid':'29EA159B-7E7F-4323-A1FD-6E2AB1XXXXXX', 'bundleId':'com.saucelabs.mydemo.app.ios', 'noReset':'false'}
BrowserstackCapabilities = {'userName':'', 'accessKey':'', 'app':'', 'deviceName':'Samsung Galaxy S25', 'platformVersion':'15.0', 'buildName':'Android Build'}
LambdaTestCapabilities   = {'userName':'', 'accessKey':'', 'app':'', 'deviceName':'', 'platformVersion':'', 'build':'iOS Build'}

# Wait time in seconds to wait for an element
WaitTime=10
```

---

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

## License

This project is made available for **trial and evaluation purposes only**.

- You may use, run, and modify this framework for personal learning, internal evaluation, or proof-of-concept work.
- Redistribution, sublicensing, or use in commercial products without explicit written permission from the author is not permitted.
- This software is provided **as is**, without warranty of any kind. The author is not liable for any damages arising from its use.

For commercial licensing or extended use, contact the project maintainer via the GitHub repository.

**See [LICENSE](./LICENSE)**

---

Contact: biswas.bijit1994@gmail.com
*Built with Appium 10.0.0 · TestNG 7.11.0 · ExtentReports 5.1.2*