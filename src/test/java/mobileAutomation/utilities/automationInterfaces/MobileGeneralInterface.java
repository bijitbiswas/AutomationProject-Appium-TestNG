package mobileAutomation.utilities.automationInterfaces;

public interface MobileGeneralInterface {

    void navigateBack();

    String getDeviceName();

    String getPlatformName();

    boolean isPlatform(String platformName);

    void sleep(int timeInSecs);

    void tapOnScreen(int xCoordinate, int yCoordinate);

    void swipeOnScreenWithCoordinate(int startX, int startY, int endX, int endY);

    void swipeUp();


}
