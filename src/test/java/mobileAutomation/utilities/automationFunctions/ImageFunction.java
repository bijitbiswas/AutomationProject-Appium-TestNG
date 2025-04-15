package mobileAutomation.utilities.automationFunctions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.imagecomparison.OccurrenceMatchingOptions;
import io.appium.java_client.imagecomparison.OccurrenceMatchingResult;
import io.appium.java_client.imagecomparison.SimilarityMatchingOptions;
import io.appium.java_client.imagecomparison.SimilarityMatchingResult;
import mobileAutomation.Constants;
import mobileAutomation.utilities.ContextManager;
import mobileAutomation.utilities.Region;
import mobileAutomation.utilities.automationInterfaces.ImageInterface;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
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
        boolean isVisible = isScreenVisible(screenName, matchThreshold);
        Assert.assertTrue(isVisible, "Screen name : BASELINE_"+ screenName +" not visible on screen");
    }

    @Override
    public void validateScreenNotVisible(String screenName, Double matchThreshold) {
        boolean isVisible = isScreenVisible(screenName, matchThreshold);
        Assert.assertFalse(isVisible, "Screen name : BASELINE_"+ screenName +" visible on screen");
    }

    @Override
    public void validateImageVisible(String imageName, Double matchThreshold) {
        boolean isVisible = isImageVisible(imageName, matchThreshold);
        Assert.assertTrue(isVisible, "Image name : BASELINE_"+ imageName +" not visible on screen");
    }

    @Override
    public void validateImageNotVisible(String imageName, Double matchThreshold) {
        boolean isVisible = isImageVisible(imageName, matchThreshold);
        Assert.assertFalse(isVisible, "Image name : BASELINE_"+ imageName +" not visible on screen");
    }

    @Override
    public void clickImage(String imageName, Double matchThreshold, int scalingFactor) {
        Region result = getVisualImageRegion(imageName, matchThreshold, scalingFactor);
        new MobileGeneralFunction(context).tapOnScreen(result.getCenterX(), result.getCenterY());
        System.out.println("=======Clicked visual search of image name : BASELINE_" + imageName + "======");
    }

    @Override
    public Region getVisualImageRegion(String imageName, Double matchThreshold , int scalingFactor ) {
        Rectangle resultRect;
        try {
            resultRect = findImageOccurrence(imageName, matchThreshold).getRect();
        } catch (IOException e) {
            System.out.println("=======Failed visual search of image name : BASELINE_" + imageName +"======");
            throw new RuntimeException(e);
        }
        Region region = new Region();
        region.setTop(resultRect.y);
        region.setLeft(resultRect.x);
        region.setWidth((resultRect.width/scalingFactor));
        region.setHeight((resultRect.height/scalingFactor));
        region.setCenterX(((resultRect.x + (resultRect.width/2))/scalingFactor));
        region.setCenterY(((resultRect.y + (resultRect.height/2))/scalingFactor));
        System.out.println("=======Visual search image x-coordinate : "+ resultRect.x +
                " and y-coordinate : " + resultRect.y + "======");
        return region;
    }




    private OccurrenceMatchingResult findImageOccurrence(String imageName, Double matchThreshold) throws IOException {
        new MobileGeneralFunction(context).sleep(2);

        // Get the execution device name and replace spaces with empty string
        String executionDeviceName = new MobileGeneralFunction(context).getDeviceName().replace(" ", "");
        String imageLocation = (Constants.IMAGE_LOCATOR_PATH + platformName +"/BASELINE_") + imageName + "_" + executionDeviceName + ".png";
        File imageFile = getImageFile(imageLocation);
        System.out.println("=======Started visual search of image name BASELINE_" + imageName + "_" + executionDeviceName +" on screen======");

        String dateTimeStamp = getCurrentDateTimeStamp();

        // Create results directory to store the visual check results
        createResultsDirectory();

        // Enabling visualization to see the image that was matched on screen
        OccurrenceMatchingOptions occurrence = new OccurrenceMatchingOptions();
        occurrence.withEnabledVisualization();
        occurrence.withThreshold(matchThreshold);

        // Find the occurrence of matched image on screen
        OccurrenceMatchingResult result = mobileDriver.findImageOccurrence(mobileDriver.getScreenshotAs(OutputType.FILE), imageFile, occurrence);
        // Save the matching image on screen with a red rectangle highlight
        result.storeVisualization(new File(Constants.IMAGE_RESULTS_FOLDER + dateTimeStamp + "-CHECK_" + imageName +".png"));
        System.out.println("=======Completed visual search of image name : BASELINE_" + imageName + "_" + executionDeviceName +"======");
        return result;
    }

    private boolean isImageVisible(String imageName, Double matchThreshold) {
        boolean isVisible;
        try {
            findImageOccurrence(imageName, matchThreshold);
            isVisible = true;
        } catch (IOException e) {
            System.out.println("=======Failed visual search of image name : BASELINE_" + imageName +"======");
            e.printStackTrace();
            isVisible = false;
        }
        return isVisible;
    }

    private boolean isScreenVisible(String screenName, Double matchThreshold) {
        new MobileGeneralFunction(context).sleep(2);

        // Get the execution device name and replace spaces with empty string
        String executionDeviceName = new MobileGeneralFunction(context).getDeviceName().replace(" ", "");
        String imageLocation = (Constants.IMAGE_LOCATOR_PATH + platformName +"/BASELINE_") + screenName + "_" + executionDeviceName + ".png";
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
            File failureImageFile = new File(Constants.IMAGE_RESULTS_FOLDER + dateTimeStamp + "-FAIL_"+ screenName +".png");
            handleIOException(() -> result.storeVisualization(failureImageFile));
            System.out.println("=======Failed visual check of screen name BASELINE_" + screenName +
                    "_" + executionDeviceName + ". " + "Match score was only: " + result.getScore() +
                    "below the threshold of " + matchThreshold + ". " +
                    "Error screenshot at " + failureImageFile.getAbsolutePath() +"======");
            return false;
        } else {
            File successImageFile = new File(Constants.IMAGE_RESULTS_FOLDER + dateTimeStamp + "-CHECK_" + screenName +".png");
            handleIOException(() -> result.storeVisualization(successImageFile));
            System.out.println("=======Completed visual search of screen name BASELINE_" + screenName +
                    "_"+executionDeviceName+". " + "with match score of " + result.getScore());
            return true;
        }
    }

    private String getCurrentDateTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.EXTENT_REPORT_DATE_TIME_FORMAT);
        return now.format(formatter);
    }

    private void createResultsDirectory() {
        Path path = Paths.get(Constants.IMAGE_RESULTS_FOLDER);
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
