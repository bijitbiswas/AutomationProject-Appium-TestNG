package mobileAutomation.utilities;

import io.appium.java_client.AppiumDriver;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class BrowserStackManager {

    static ArrayList<String> uploadFileToBrowserStack(String browserStackAuth, String[] filePaths) {
        System.out.println("🚀 Uploading files to BrowserStack cloud devices...");

        ArrayList<String> uploadedIds = new ArrayList<>();
        RestAssured.baseURI = Constants.BROWSERSTACK_API_URL;

        for (String filePath : filePaths) {
            File file = new File(filePath);

            String browserStackUsername = browserStackAuth.split(":")[0];
            String browserStackAccessKey = browserStackAuth.split(":")[1];
            String customId = file.getName();

            Response response = given()
                    .auth().preemptive().basic(browserStackUsername, browserStackAccessKey)
                    .multiPart("file", file)
                    .multiPart("custom_id", customId)
                    .when()
                    .post("/app-automate/upload-media")
                    .then()
                    .log().all()
                    .extract().response();

            JsonPath jsonResponse = response.jsonPath();
            String uploadedId = jsonResponse.getString("media_url");
            System.out.println("✅ Uploaded: " + filePath + " → " + uploadedId);
            uploadedIds.add(uploadedId);
        }
        return uploadedIds;
    }

    static void markBrowserStackStatus(AppiumDriver mobileDriver, boolean testPassed) {
        String status = testPassed ? "passed" : "failed";
        String remark = testPassed ? "Test passed successfully" : "Test failed";
        ((JavascriptExecutor) mobileDriver).executeScript(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"" +
                        status+"\", \"reason\": \""+remark+"\"}}");
    }
}
