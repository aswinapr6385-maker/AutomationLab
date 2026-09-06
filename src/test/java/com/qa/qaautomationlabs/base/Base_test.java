package com.qa.qaautomationlabs.base;

import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.microsoft.playwright.Page;
import com.qa.qaautomationlabs.pages.Addtocart_page;
import com.qa.qaautomationlabs.pages.login_page;

public class Base_test {
	public Playwight_factory pf;
	public Page page;
	public login_page lp;
	public Properties prop;
	public Config_File cf;
	public Addtocart_page ap;
	
	
	@BeforeMethod
	public void setup() throws IOException {
		 pf = new Playwight_factory();
		 
	        cf = new Config_File();

	        prop = cf.inti_prop();

	       
	        page = pf.initbrowserWithAuth(prop);
	        lp = new login_page(page);
	        
	        ap = new Addtocart_page(page);
		 
	}
	
	@AfterMethod
	public void teardown() {
		page.context().browser().close();
	}

}
