package com.qa.qaautomationlabs.listners;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import com.microsoft.playwright.Page;

import com.qa.qaautomationlabs.base.Base_test;
import com.qa.qaautomationlabs.base.Base_test_addtocart;
import com.qa.qaautomationlabs.base.RetryAnalyzer;

public class ExtentReportListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;

    private static final Map<String, ExtentTest> testMap =
            new ConcurrentHashMap<>();

    private static final Map<String, Integer> attemptMap =
            new ConcurrentHashMap<>();

    private static String screenshotPath;


    // ============================================================
    // START ENTIRE SUITE
    // ============================================================

    @Override
    public void onStart(ISuite suite) {

        String outputFolder = "test-output";

        new File(outputFolder).mkdirs();

        screenshotPath = outputFolder + "/screenshots";

        new File(screenshotPath).mkdirs();

        String reportPath =
                outputFolder + "/TestExecutionReport.html";

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(reportPath);

        sparkReporter.config().setDocumentTitle(
                "QA Automation Labs - Test Execution Report");

        sparkReporter.config().setReportName(
                "QA Automation Labs Test Execution");

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


    // ============================================================
    // TEST START
    // ============================================================

    @Override
    public void onTestStart(ITestResult result) {

        String testKey = getTestKey(result);

        int attempt =
                attemptMap.getOrDefault(testKey, 0) + 1;

        attemptMap.put(testKey, attempt);

        ExtentTest test =
                testMap.get(testKey);

        if (test == null) {

            test = extent.createTest(
                    getDisplayName(result));

            testMap.put(testKey, test);

            test.log(
                    Status.INFO,
                    "Initial execution started.");

        } else {

            test.log(
                    Status.WARNING,
                    "Retry "
                            + (attempt - 1)
                            + "/"
                            + RetryAnalyzer.MAX_RETRY_COUNT
                            + " started.");
        }

        test.log(
                Status.INFO,
                "Execution Attempt: " + attempt);

        addTestParameters(test, result);
    }


    // ============================================================
    // TEST SUCCESS
    // ============================================================

    @Override
    public void onTestSuccess(ITestResult result) {

        String testKey =
                getTestKey(result);

        ExtentTest test =
                testMap.get(testKey);

        int attempt =
                attemptMap.getOrDefault(testKey, 1);

        test.log(
                Status.PASS,
                "Test Passed on Attempt " + attempt);

        takeScreenshot(
                result,
                test,
                "PASS",
                attempt);

        attemptMap.remove(testKey);

        testMap.remove(testKey);
    }


    // ============================================================
    // TEST FAILURE
    // ============================================================

    @Override
    public void onTestFailure(ITestResult result) {

        String testKey =
                getTestKey(result);

        ExtentTest test =
                testMap.get(testKey);

        int attempt =
                attemptMap.getOrDefault(testKey, 1);

        takeScreenshot(
                result,
                test,
                "FAIL",
                attempt);


        if (attempt <= RetryAnalyzer.MAX_RETRY_COUNT) {

            test.log(
                    Status.WARNING,
                    "Attempt "
                            + attempt
                            + " FAILED. "
                            + "Retry "
                            + attempt
                            + "/"
                            + RetryAnalyzer.MAX_RETRY_COUNT
                            + " will be executed.");

            if (result.getThrowable() != null) {

                test.log(
                        Status.WARNING,
                        result.getThrowable().toString());
            }

        } else {

            test.log(
                    Status.FAIL,
                    "FINAL FAILURE - Maximum retries exhausted.");

            if (result.getThrowable() != null) {

                test.fail(result.getThrowable());
            }

            attemptMap.remove(testKey);

            testMap.remove(testKey);
        }
    }


    // ============================================================
    // TEST SKIPPED
    // ============================================================

    @Override
    public void onTestSkipped(ITestResult result) {

        String testKey =
                getTestKey(result);

        ExtentTest test =
                testMap.get(testKey);

        if (test != null) {

            test.log(
                    Status.SKIP,
                    "Test Skipped.");

            if (result.getThrowable() != null) {

                test.skip(result.getThrowable());
            }
        }

        attemptMap.remove(testKey);

        testMap.remove(testKey);
    }


    // ============================================================
    // TEST PARAMETERS
    // ============================================================

    private void addTestParameters(
            ExtentTest test,
            ITestResult result) {

        Object[] parameters =
                result.getParameters();

        if (parameters != null
                && parameters.length > 0) {

            StringBuilder data =
                    new StringBuilder();

            for (Object parameter : parameters) {

                if (data.length() > 0) {

                    data.append(" | ");
                }

                data.append(
                        String.valueOf(parameter));
            }

            test.log(
                    Status.INFO,
                    "Test Data: " + data);
        }
    }


    // ============================================================
    // SCREENSHOT
    // ============================================================

    private void takeScreenshot(
            ITestResult result,
            ExtentTest test,
            String status,
            int attempt) {

        try {

            Page page =
                    getPage(result);

            if (page == null) {

                test.warning(
                        "Screenshot not captured because Page is null.");

                return;
            }

            String testName =
                    result.getMethod()
                          .getMethodName();

            String displayName =
                    getDisplayName(result);

            String safeName =
                    displayName.replaceAll(
                            "[^a-zA-Z0-9._-]",
                            "_");

            if (safeName.length() > 100) {

                safeName =
                        safeName.substring(0, 100);
            }

            String fileName =
                    safeName
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
                            .setPath(
                                    Paths.get(filePath))
                            .setFullPage(true));

            test.addScreenCaptureFromPath(
            		"screenshots/" + fileName);

            test.log(
                    Status.INFO,
                    "Screenshot captured: "
                            + fileName);

        } catch (Exception e) {

            test.warning(
                    "Unable to capture screenshot: "
                            + e.getMessage());
        }
    }


    // ============================================================
    // GET PAGE
    // ============================================================

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


    // ============================================================
    // TEST KEY
    // ============================================================

    private String getTestKey(ITestResult result) {

        String methodName =
                result.getMethod()
                      .getQualifiedName();

        Object[] parameters =
                result.getParameters();

        return methodName
                + Arrays.deepToString(parameters);
    }


    // ============================================================
    // DISPLAY NAME
    // ============================================================

    private String getDisplayName(ITestResult result) {

        String methodName =
                result.getMethod()
                      .getMethodName();

        Object[] parameters =
                result.getParameters();

        if (parameters == null
                || parameters.length == 0) {

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
                    String.valueOf(
                            parameters[i]));
        }

        name.append("]");

        return name.toString();
    }


    // ============================================================
    // FINISH ENTIRE SUITE
    // ============================================================

    @Override
    public void onFinish(ISuite suite) {

        if (extent != null) {

            extent.flush();
        }

        testMap.clear();

        attemptMap.clear();
    }
}