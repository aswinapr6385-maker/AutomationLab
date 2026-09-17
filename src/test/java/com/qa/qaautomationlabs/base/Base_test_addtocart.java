package com.qa.qaautomationlabs.base;

import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.microsoft.playwright.Page;
import com.qa.qaautomationlabs.pages.Addtocart_page;
import com.qa.qaautomationlabs.pages.login_page;
import com.selfhealing.healing.HealingEngine;
import com.selfhealing.reporting.HealingHistoryStore;
import com.selfhealing.reporting.ReportGenerator;

public class Base_test_addtocart {

    public Page page;
    public login_page lp;
    public Properties prop;
    public Config_File cf;
    public Addtocart_page ap;
    public Loginpage_factory lf;
    public static HealingEngine healingEngine;

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        if (healingEngine == null) {
            healingEngine = new HealingEngine();
        }
    }

    @BeforeMethod
    public void cart_setup() throws IOException {
        lf = new Loginpage_factory();
        cf = new Config_File();
        prop = cf.inti_prop();
        page = lf.initbrowserWithAuth(prop);
        lp = new login_page(page, healingEngine);
        ap = new Addtocart_page(page, healingEngine);
    }

    @AfterMethod
    public void teardown() {
        if (page != null) {
            page.context().browser().close();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        ReportGenerator.generateReport(1,
            HealingHistoryStore.getInstance().getRecords());
    }
}