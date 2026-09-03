package com.qa.qaautomationlabs.base;

import java.nio.file.Paths;
import java.util.Properties;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class Loginpage_factory {
	Playwright playwight;
	Browser browser;
	BrowserContext bc;
	Page page; 
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
		page.navigate(prop.getProperty("SHOP_URL").trim());
		System.out.println("isLoggedIn: "
		        + page.evaluate(
		                "() => localStorage.getItem('isLoggedIn')"
		        ));
		return page;
	}
	

}


