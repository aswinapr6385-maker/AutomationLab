package com.qa.qaautomationlabs.pages;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.Page;
import com.selfhealing.healing.HealingEngine;
import com.selfhealing.pageobjects.BasePage;

public class login_page extends BasePage {

    private static final File SOURCE_FILE = new File("src/test/java/com/qa/qaautomationlabs/pages/login_page.java");

    private String email      = "//input[@id='email']";
    private String password   = "//input[@id='password']";
    private String lognbtn    = "//button[@id='loginBtn']";
    private String pageheader = "(//nav[@class='breadcrumb bg-light mb-30'])[1]";
    public  String shopnow    = "//a[@title='Shop Kids Fashion']";

    // ── Constructors ──────────────────────────────────────────────
    public login_page(Page page) {
        this(page, new HealingEngine());
    }

    public login_page(Page page, HealingEngine healingEngine) {
        super(page, healingEngine);
    }

    // ── Java 8 Attribute Helper ───────────────────────────────────
    private Map<String, String> attrs(String... keyValues) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i + 1 < keyValues.length; i += 2) {
            map.put(keyValues[i], keyValues[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }

    // ── Actions ───────────────────────────────────────────────────
    public String gethomepageTitile() {
        String actualtitle = page.title();
        System.out.println("Title: " + actualtitle);
        return actualtitle;
    }

    public String gethomepageuturl() {
        String actualurl = page.url();
        System.out.println("URL: " + actualurl);
        return actualurl;
    }

    public void dologin(String username, String pass) {
        fillWithHealing("email", email, username, "Email input",
                attrs("id", "email"),
                SOURCE_FILE, loc -> this.email = loc);

        fillWithHealing("password", password, pass, "Password input",
                attrs("id", "password"),
                SOURCE_FILE, loc -> this.password = loc);

        clickWithHealing("lognbtn", lognbtn, "Login button",
                attrs("id", "loginBtn"),
                SOURCE_FILE, loc -> this.lognbtn = loc);
    }

    public String textcontent() {
        String textContent = getTextWithHealing("pageheader", pageheader, "Breadcrumb header",
                attrs("class", "breadcrumb bg-light mb-30"),
                SOURCE_FILE, loc -> this.pageheader = loc);
        System.out.println("header is: " + textContent);
        return textContent;
    }

    // ── Cart navigation ───────────────────────────────────────────
    public Addtocart_page cart() {
        clickWithHealing("shopnow", shopnow, "Shop Kids Fashion link",
                attrs("title", "Shop Kids Fashion"),
                SOURCE_FILE, loc -> this.shopnow = loc);
        System.out.println("Clicked Shop Now");
        return new Addtocart_page(page, healingEngine);
    }
}