package nl.hsac.fitnesse.fixture;

import fitnesse.junit.FitNesseRunner;
import nl.hsac.fitnesse.junit.HsacFitNesseRunner;
import org.junit.runner.RunWith;

/**
 * Test class to run HsacAcceptanceTests wiki suite.
 */
@RunWith(HsacFitNesseRunner.class)
@FitNesseRunner.Suite("HsacAcceptanceTests")
// Here we use an explicit FilesSectionCopy (without default) because we DO want to get galenExamples
@HsacFitNesseRunner.FilesSectionCopy(addDefaultExcludes = false,
        exclude = {"testResults", "testProgress",
                "screenshots", "pagesources", "downloads", "galen-reports", "fileFixture", "httpPostExamples",
                "test", "Desktop.ini", ".DS_Store"})
@FitNesseRunner.Partition(count = 2, index = 0)
@FitNesseRunner.OutputDir("target/fitnesse-results/index0")
public class AcceptanceTest {
}
