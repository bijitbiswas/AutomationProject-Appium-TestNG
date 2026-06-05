package mobileAutomation.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 1;
    private int retryCount = 0;

    public boolean isRetrying() {
        return retryCount > 0;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.println("Retrying test: " + result.getName() +
                    " (attempt " + retryCount + " of " + MAX_RETRY_COUNT + ")");
            return true;
        }
        return false;
    }
}
