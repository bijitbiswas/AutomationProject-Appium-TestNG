package mobileAutomation.utilities.automationInterfaces;

public interface ImageInterface {

    void validateScreenVisible(String screenName, Double matchThreshold);

    void validateScreenNotVisible(String screenName, Double matchThreshold);

    void validateImageVisible(String imageName, Double matchThreshold);

    void validateImageNotVisible(String imageName, Double matchThreshold);

    void clickImage(String imageName);





}
