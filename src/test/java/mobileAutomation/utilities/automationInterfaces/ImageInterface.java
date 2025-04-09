package mobileAutomation.utilities.automationInterfaces;

import mobileAutomation.utilities.Region;

public interface ImageInterface {

    void validateScreenVisible(String screenName, Double matchThreshold);

    void validateScreenNotVisible(String screenName, Double matchThreshold);

    void validateImageVisible(String imageName, Double matchThreshold);

    void validateImageNotVisible(String imageName, Double matchThreshold);

    void clickImage(String imageName, Double matchThreshold, int scalingFactor);

    Region getVisualImageRegion(String imageName, Double matchThreshold , int scalingFactor );



}
