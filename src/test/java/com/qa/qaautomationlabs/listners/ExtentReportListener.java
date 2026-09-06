package com.qa.qaautomationlabs.listners;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Page;

import com.qa.qaautomationlabs.base.Base_test;
import com.qa.qaautomationlabs.base.Base_test_addtocart;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;

    private static final Map<String, ExtentTest> testMap =
            new ConcurrentHashMap<>();

    private static final Map<String, Integer> attemptMap =
            new ConcurrentHashMap<>();

    private static String screenshotPath;

    @Override
    public void onStart(ITestContext context) {

        String outputFolder = "test-output";

        new File(outputFolder).mkdirs();

        screenshotPath = outputFolder + "/screenshots";

        new File(screenshotPath).mkdirs();

        String reportPath =
                outputFolder + "/TestExecutionReport.html";

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(reportPath);

        sparkReporter.config().setDocumentTitle(
                "QA Automation Labs Test Report");

        sparkReporter.config().setReportName(
                "Test Execution Report");

        extent = new ExtentReports();

        extent.attachReporter(sparkReporter);

        extent.setSystemInfo(
                "Project",
                "QA Automation Labs");

        extent.setSystemInfo(
                "Framework",
                "Playwright + Java + TestNG");

        extent.setSystemInfo(
                "Environment",
                "QA");
    }

    @Override
    public void onTestStart(ITestResult result) {

        String testKey = getTestKey(result);

        int attempt =
                attemptMap.getOrDefault(testKey, 0) + 1;

        attemptMap.put(testKey, attempt);

        ExtentTest test = testMap.get(testKey);

        if (test == null) {

            test = extent.createTest(
                    getDisplayName(result));

            testMap.put(testKey, test);

            test.log(
                    Status.INFO,
                    "Initial execution started."
            );

        } else {

            test.log(
                    Status.WARNING,
                    "Retry " + (attempt - 1) + "/2 started."
            );
        }

        test.log(
                Status.INFO,
                "Execution Attempt: " + attempt
        );

        addTestParameters(test, result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        String testKey = getTestKey(result);

        ExtentTest test = testMap.get(testKey);

        int attempt =
                attemptMap.getOrDefault(testKey, 1);

        test.log(
                Status.PASS,
                "Test Passed on Attempt " + attempt
        );

        takeScreenshot(
                result,
                test,
                "PASS",
                attempt
        );

        /*
         * Test finished successfully.
         * We can now remove its tracking information.
         */
        attemptMap.remove(testKey);
        testMap.remove(testKey);
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String testKey = getTestKey(result);

        ExtentTest test = testMap.get(testKey);

        int attempt =
                attemptMap.getOrDefault(testKey, 1);

        test.log(
                Status.FAIL,
                "Test Failed on Attempt " + attempt
        );

        if (result.getThrowable() != null) {

            test.fail(result.getThrowable());
        }

        takeScreenshot(
                result,
                test,
                "FAIL",
                attempt
        );

        /*
         * RetryAnalyzer allows:
         *
         * Attempt 1 → Retry 1
         * Attempt 2 → Retry 2
         * Attempt 3 → Final Failure
         *
         * We don't remove the test here because
         * another attempt may follow.
         */
        Boolean willRetry =
                (Boolean) result.getAttribute("willRetry");

        if (Boolean.FALSE.equals(willRetry)) {

            test.log(
                    Status.FAIL,
                    "FINAL FAILURE - Maximum retries exhausted."
            );

            attemptMap.remove(testKey);
            testMap.remove(testKey);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        String testKey = getTestKey(result);

        ExtentTest test = testMap.get(testKey);

        if (test != null) {

            test.log(
                    Status.SKIP,
                    "Test Skipped"
            );

            if (result.getThrowable() != null) {

                test.skip(result.getThrowable());
            }
        }

        attemptMap.remove(testKey);
        testMap.remove(testKey);
    }

    private void addTestParameters(
            ExtentTest test,
            ITestResult result) {

        Object[] parameters =
                result.getParameters();

        if (parameters != null && parameters.length > 0) {

            StringBuilder data =
                    new StringBuilder();

            for (Object parameter : parameters) {

                if (data.length() > 0) {
                    data.append(" | ");
                }

                data.append(String.valueOf(parameter));
            }

            test.log(
                    Status.INFO,
                    "Test Data: " + data
            );
        }
    }

    private void takeScreenshot(
            ITestResult result,
            ExtentTest test,
            String status,
            int attempt) {

        try {

            Page page = getPage(result);

            if (page == null) {

                test.warning(
                        "Screenshot not captured because Page is null."
                );

                return;
            }

            String testName =
                    result.getMethod().getMethodName();

            String safeTestName =
                    testName.replaceAll(
                            "[^a-zA-Z0-9._-]",
                            "_"
                    );

            String fileName =
                    safeTestName
                    + "_"
                    + status
                    + "_attempt"
                    + attempt
                    + ".png";

            String filePath =
                    screenshotPath
                    + File.separator
                    + fileName;

            page.screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(Paths.get(filePath))
                            .setFullPage(true)
            );

            test.addScreenCaptureFromPath(
                    filePath
            );

            test.log(
                    Status.INFO,
                    "Screenshot captured: " + fileName
            );

        } catch (Exception e) {

            test.warning(
                    "Unable to capture screenshot: "
                    + e.getMessage()
            );
        }
    }

    private Page getPage(ITestResult result) {

        Object testInstance =
                result.getInstance();

        if (testInstance instanceof Base_test) {

            return ((Base_test) testInstance).page;

        }

        if (testInstance instanceof Base_test_addtocart) {

            return ((Base_test_addtocart) testInstance).page;
        }

        return null;
    }

    private String getTestKey(ITestResult result) {

        String methodName =
                result.getMethod().getMethodName();

        Object[] parameters =
                result.getParameters();

        StringBuilder key =
                new StringBuilder(methodName);

        if (parameters != null) {

            for (Object parameter : parameters) {

                key.append("_")
                   .append(String.valueOf(parameter));
            }
        }

        return key.toString();
    }

    private String getDisplayName(ITestResult result) {

        String methodName =
                result.getMethod().getMethodName();

        Object[] parameters =
                result.getParameters();

        if (parameters == null ||
                parameters.length == 0) {

            return methodName;
        }

        StringBuilder name =
                new StringBuilder(methodName);

        name.append(" [");

        for (int i = 0;
             i < parameters.length;
             i++) {

            if (i > 0) {
                name.append(", ");
            }

            name.append(
                    String.valueOf(parameters[i])
            );
        }

        name.append("]");

        return name.toString();
    }

    @Override
    public void onFinish(ITestContext context) {

        if (extent != null) {

            extent.flush();
        }
    }
}