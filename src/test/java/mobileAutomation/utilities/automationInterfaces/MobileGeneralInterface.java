package mobileAutomation.utilities.automationInterfaces;

public interface MobileGeneralInterface {

    void navigateBack();

    String getDeviceName();

    String getPlatformName();

    boolean isPlatform(String platformName);

    void sleep(int timeInSecs);

}
