package mobileAutomation.utilities;

import io.appium.java_client.AppiumDriver;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import mobileAutomation.Constants;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class BrowserStackManager {

    static ArrayList<String> uploadFileToBrowserStack(String lambdaTestAuth, String[] filePaths) {
        System.out.println("🚀 Uploading files to LambdaTest cloud devices...");
        ArrayList<String> uploadedIds = new ArrayList<>();
        RestAssured.baseURI = Constants.LAMBDATEST_API_URL;

        for (String filePath : filePaths) {
            File file = new File(filePath);
            String lambdaTestUsername = lambdaTestAuth.split(":")[0];
            String lambdaTestAccessKey = lambdaTestAuth.split(":")[1];

            Response response = given()
                    .auth().preemptive().basic(lambdaTestUsername, lambdaTestAccessKey)
                    .multiPart("media_file", file)
//                    .multiPart("type", "image")
//                    .multiPart("custom_id", "abrakadabra")
                    .when()
                    .post("/media/upload")
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
