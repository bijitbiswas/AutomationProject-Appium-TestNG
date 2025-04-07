package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.imagecomparison.SimilarityMatchingOptions;
import io.appium.java_client.imagecomparison.SimilarityMatchingResult;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.automationInterfaces.ImageInterface;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageFunction implements ImageInterface {

    AppiumDriver mobileDriver;
    WebDriverWait wait;
    FluentWait<AppiumDriver> fluentWait;
    ContextManager context;
    private final String platformName;
    private final String imageLocatorsPath = "src/test/java/mobileAutomation/imageLocators/";
    private final String resultsPath = "VisualCheckResults/";
    private final String dateTimePattern = "yyyy-MM-dd_HH-mm-ss";
    String dateTimeStamp = getCurrentDateTimeStamp();

    public ImageFunction(ContextManager context) {
        this.context = context;
        this.mobileDriver = context.mobileDriver;
        this.wait = context.wait;
        this.fluentWait = context.fluentWait;
        this.platformName = context.platformName;
    }

    @Override
    public void validateScreenVisible(String screenName, Double matchThreshold) {
        Boolean isVisible = isScreenVisible(screenName, matchThreshold);
        Assert.assertTrue(isVisible, "Screen name : BASELINE_"+ screenName +" not visible on screen");
    }

    @Override
    public void validateScreenNotVisible(String screenName, Double matchThreshold) {
        Boolean isVisible = isScreenVisible(screenName, matchThreshold);
        Assert.assertFalse(isVisible, "Screen name : BASELINE_"+ screenName +" visible on screen");
    }

    @Override
    public void validateImageVisible(String imageName, Double matchThreshold) {

    }

    @Override
    public void validateImageNotVisible(String imageName, Double matchThreshold) {

    }

    @Override
    public void clickImage(String imageName) {

    }


    private String getCurrentDateTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return now.format(formatter);
    }

    private void createResultsDirectory() {
        Path path = Paths.get(resultsPath);
        handleIOException(() -> {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        });
    }

    private File getImageFile(String imageLocation) {
        File imageFile;
        try {
            imageFile = new File(imageLocation);
        } catch (Exception e) {
            System.out.println("=======WARNING ⚠ ⚠ ⚠ Please check if image filename have execution device name ⚠ ⚠ ⚠ =====");
            e.printStackTrace();
            throw new RuntimeException("Image file should be in format BASELINE_<imageName>_<executionDeviceNameWithoutSpace>.png");
        }
        return imageFile;
    }


    private Boolean isScreenVisible(String screenName, Double matchThreshold) {

        // Get the execution device name and replace spaces with empty string
        String executionDeviceName = new MobileGeneralFunction(context).getDeviceName().replace(" ", "");
        String imageLocation = (imageLocatorsPath + platformName +"/BASELINE_") + screenName + "_" + executionDeviceName + ".png";
        File imageFile = getImageFile(imageLocation);
        System.out.println("=======Started visual search of screen name BASELINE_" + screenName + "_" + executionDeviceName +" on screen======");

        // If no baseline image exists for this check, we should create a baseline image
        if (!imageFile.exists()) {
            System.out.println("=======No baseline found for screen name BASELINE_" + screenName + "_" + executionDeviceName + ". " +
                    "Capturing baseline and skipping validation======");
            File newBaseline = mobileDriver.getScreenshotAs(OutputType.FILE);
            handleIOException(() -> FileUtils.copyFile(newBaseline, new File(imageLocation)));
            return true;
        }

        // Create results directory to store the visual check results
        createResultsDirectory();

        // Enabling visualization to see the differences if fails
        SimilarityMatchingOptions similarityMatching = new SimilarityMatchingOptions();
        similarityMatching.withEnabledVisualization();

        SimilarityMatchingResult result;
        try {
            result = mobileDriver.getImagesSimilarity(imageFile, mobileDriver.getScreenshotAs(OutputType.FILE), similarityMatching);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // If the similarity score is below the threshold, returning false
        if (result.getScore() < matchThreshold) {
            File failureImageFile = new File(resultsPath + dateTimeStamp + "-FAIL_"+ screenName +".png");
            handleIOException(() -> result.storeVisualization(failureImageFile));
            System.out.println("=======Failed visual check of screen name BASELINE_" + screenName +
                    "_" + executionDeviceName + ". " + "Match score was only: " + result.getScore() +
                    "below the threshold of " + matchThreshold + ". " +
                    "Error screenshot at " + failureImageFile.getAbsolutePath() +"======");
            return false;
        } else {
            File successImageFile = new File(resultsPath + dateTimeStamp + "-CHECK_" + screenName +".png");
            handleIOException(() -> result.storeVisualization(successImageFile));
            System.out.println("=======Completed visual search of screen name BASELINE_" + screenName +
                    "_"+executionDeviceName+". " + "with match score of " + result.getScore());
            return true;
        }
    }

    @FunctionalInterface
    interface ThrowableRunnable {
        void run() throws IOException;
    }

    private static void handleIOException(ThrowableRunnable x) {
        try {
            x.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
