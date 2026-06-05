package mobileAutomation.testcases;

import mobileAutomation.pages.SampleLoginBasePage;
import mobileAutomation.BaseTest;
import org.testng.annotations.Test;

public class SampleMobileParallelCheckTest extends BaseTest {

    SampleLoginBasePage sampleLoginPg;

    @Test(
            description = "This is a parallel check test"
    )
    public void parallelCheckTest() {

        sampleLoginPg = new SampleLoginBasePage(getDriverContext());

        sampleLoginPg.sleep(5);

    }
}
