package mobileAutomation.actionUtilities.automationInterfaces;

public interface MobileGeneralInterface {

    void navigateBack();

    void hideKeyboard();

    String getDeviceName();

    String getPlatformName();

    boolean isPlatform(String platformName);

    void sleep(int timeInSecs);

    void tapOnScreen(int xCoordinate, int yCoordinate);

    void swipeOnScreenWithCoordinate(int startX, int startY, int endX, int endY);

    void swipeUp();

    void swipeDown();


}
