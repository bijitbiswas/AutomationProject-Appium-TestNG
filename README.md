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
| Path                               | Purpose |
|------------------------------------| --- |
| `apps/`                            | Sample Android (`*.apk`) and iOS (`*.app`) binaries used for local runs. |
| `config/config.properties`         | Central place to choose driver targets and define capabilities for all environments. |
| `MobileTestSuites/*.xml`           | Ready-made TestNG suites for Android/iOS. |
| `src/test/java/mobileAutomation/*` | Automation code: constants, page objects, utilities, drivers, managers, and tests. |
| `TestReport/`                      | Generated ExtentReports (time-stamped per run). |
| `VisualCheckResults/`              | Visual comparison output from image-based assertions. |

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
1. Create a page class under `src/test/java/mobileAutomation/pages/` by extending `BasePage` and encapsulating the interactions you need.
   ```java
   package mobileAutomation.pages;

   import io.appium.java_client.pagefactory.AndroidFindBy;
   import io.appium.java_client.pagefactory.iOSXCUITFindBy;
   import mobileAutomation.utilities.BasePage;
   import org.openqa.selenium.WebElement;

   public class LoginPage extends BasePage {

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
2. Call the page methods from your TestNG scripts, wiring in media uploads, Excel data, and visual checks as needed.
   ```java
   package mobileAutomation.tests;

   import mobileAutomation.pages.LoginPage;
   import mobileAutomation.utilities.BaseTest;
   import org.testng.annotations.Test;

   import java.util.Map;

   public class LoginTest extends BaseTest {

       {
           filePaths = new String[]{
                   "src/test/java/mobileAutomation/testFiles/WELCOME_BANNER.png"
           };
       }

       @Test(dataProvider = "getTestData")
       public void loginWithValidCredentials(String username, String password) {
           LoginPage loginPage = new LoginPage();
           loginPage.login(username, password);
           loginPage.validateScreenVisible("LoginSuccess");
       }
   }
   ```
- **BrowserStack/LambdaTest media:** setting `filePaths` pushes custom assets before the remote session starts. See `Uploading Custom Media to Cloud Sessions` for deeper configuration tips.
- **Excel data:** the shared `getTestData` provider supplies each test invocation with a row keyed to the method name. Details live in `Data-Driven Execution`.
- **Visual assertions:** call helpers like `validateScreenVisible` once a flow completes. Baseline management is covered in `Visual Assertion Baselines`.

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

### Uploading Custom Media to Cloud Sessions
- Add the assets you need (PDFs, images, etc.) to the repository or ensure they are reachable from the JVM running the tests.
- In any test class that extends `BaseTest`, assign `filePaths` with absolute or project-relative locations. For example:
  ```java
  {
      filePaths = new String[]{
              "src/test/java/mobileAutomation/testFiles/SAMPLE_PDF_FILE.pdf",
              "src/test/java/mobileAutomation/testFiles/SAMPLE_IMAGE_FILE.png"
      };
  }
  ```
- During `@BeforeClass`, the framework uploads those files to the active cloud using REST APIs (`/app-automate/upload-media` for BrowserStack, `/media/upload` for LambdaTest) and captures the returned media IDs.
- The media IDs are automatically injected into the session capabilities (`appium:browserstack.uploadMedia` or `lt:Options.uploadMedia`), so your custom files are ready on the remote device before test steps execute.
- Leave `filePaths` unset to skip uploads on runs that don't require additional media.

### Data-Driven Execution
- The `@DataProvider` in `BaseTest` pulls values from `src/test/java/mobileAutomation/testData/Testdata.xlsx`. Add rows keyed by the test method name to extend scenarios.

### Visual Assertion Baselines
- Baseline screenshots live under `src/test/java/mobileAutomation/imageLocators/<Platform>/`.
- Filenames follow `BASELINE_<Name>_<DeviceNameWithoutSpaces>.png`.
- On first run without an existing baseline, the framework captures one automatically and skips the assertion; subsequent runs compare against it and store diffs in `VisualCheckResults/`.

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