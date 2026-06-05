package mobileAutomation.utilities.automationInterfaces;

public interface ReportingInterface {

    /**
     * Logs a success step in the Extent Report with the given label and attaches a screenshot.
     *
     * @param labelName the label text to display in the report for this step
     */
    void addSuccessLabelWithScreenshot(String labelName);

    /**
     * Logs a success step in the Extent Report with the given label (no screenshot attached).
     *
     * @param labelName the label text to display in the report for this step
     */
    void addSuccessLabel(String labelName);
}
