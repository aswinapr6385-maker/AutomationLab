package com.qa.qaautomationlab.test;

import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.microsoft.playwright.BrowserContext;
import com.qa.qaautomationlabs.base.Base_test;
import com.qa.qaautomationlabs.base.Constant;

public class login_Test extends Base_test {

    @Test
    public void logintitle() {
        String title = lp.gethomepageTitile();
        Assert.assertEquals(title, Constant.LOGIN_PAGE_TITILE);
    }

    @Test
    public void loginurl() {
        String url = lp.gethomepageuturl();
        Assert.assertEquals(url, "https://shop.qaautomationlabs.com/");
    }

    @DataProvider
    public Object[][] getvalidcreds() {
        return new Object[][] {
            {"demo@demo.com", "demo"}
        };
    }

    @DataProvider
    public Object[][] getinvalidcreds() {
        return new Object[][] {
            {"demo@demo.coms", "demo"},
            {"demo@demo.com",  "demos"},
            {"",               "demo"},
            {"demo@demo.com",  ""}
        };
    }

    @Test(dataProvider = "getvalidcreds")
    public void dolvalidlogin(String email, String pass) {
        lp.dologin(email, pass);
        String url = lp.gethomepageuturl();
        Assert.assertEquals(url, "https://shop.qaautomationlabs.com/shop.php");
        page.context().storageState(
            new BrowserContext.StorageStateOptions()
                .setPath(Paths.get("applogin.json"))
        );
        System.out.println("Login successful!");
        System.out.println("Storage state saved!");
    }

    @Test(dataProvider = "getinvalidcreds")
    public void doinvalidlogin(String email, String pass) {
        lp.dologin(email, pass);
        String url = lp.gethomepageuturl();
        Assert.assertNotEquals(url, "https://shop.qaautomationlabs.com/shop.php");
    }
}