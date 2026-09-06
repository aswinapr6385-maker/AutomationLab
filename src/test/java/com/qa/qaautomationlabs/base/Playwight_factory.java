package com.qa.qaautomationlabs.base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.google.common.io.Files;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class Playwight_factory {
	
	Playwright playwight;
	Browser browser;
	BrowserContext bc;
	static Page page; 
	public Page initbrowserWithAuth(Properties prop){
		String browsername = prop.getProperty("browser").trim();
		System.out.println("browser name is :"+browsername);
		System.out.println("Auth file path: "
	            + Paths.get("applogin.json").toAbsolutePath());

	    System.out.println("Auth file exists: "
	            + Paths.get("applogin.json").toFile().exists());
		playwight =  Playwright.create();
		switch (browsername.toLowerCase()) {
		case "chromium":
			browser= playwight.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			
			break;
		 
		case "chrome":
			browser = playwight.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
				
				break;
		
		case "firefox":
		browser= playwight.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
					
					break;
		case "webkit":
			browser= playwight.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
						
						break;

		default:
			System.out.println("invalid browser......");
			break;
		}
		bc = browser.newContext(
	            new Browser.NewContextOptions()
	                    .setStorageStatePath(
	                            Paths.get("applogin.json")));

		
		page  = bc.newPage();
		page.navigate(prop.getProperty("URL").trim());
		System.out.println("isLoggedIn: "
		        + page.evaluate(
		                "() => localStorage.getItem('isLoggedIn')"
		        ));
		return page;
	}
	public static String takeScreenshot(
            String testName,
            int attempt) {

        try {

            String screenshotFolder =
                    "./test-output/screenshots/";


            // Create folder if it doesn't exist
            Path folderPath =
                    Paths.get(screenshotFolder);

            if (!folderPath.toFile().exists()) {

                folderPath.toFile().mkdirs();
            }


            // Clean test name
            String cleanTestName =
                    testName.replaceAll(
                            "[^a-zA-Z0-9_-]",
                            "_"
                    );


            String fileName =
                    cleanTestName
                    + "_attempt_"
                    + attempt
                    + ".png";


            String screenshotPath =
                    screenshotFolder
                    + fileName;


            if (page == null) {

                System.out.println(
                        "Page is null. "
                        + "Screenshot cannot be taken."
                );

                return null;
            }


            page.screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(
                                    Paths.get(
                                            screenshotPath
                                    )
                            )
                            .setFullPage(true)
            );


            System.out.println(
                    "Screenshot saved: "
                    + screenshotPath
            );


            return screenshotPath;


        } catch (Exception e) {

            System.out.println(
                    "Failed to take screenshot: "
                    + e.getMessage()
            );

            return null;
        }
    }
	
	

}