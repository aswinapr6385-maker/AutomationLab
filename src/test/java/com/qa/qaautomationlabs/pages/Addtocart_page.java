package com.qa.qaautomationlabs.pages;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.Page;
import com.selfhealing.healing.HealingEngine;
import com.selfhealing.pageobjects.BasePage;

public class Addtocart_page extends BasePage {

    private static final File SOURCE_FILE = new File("src/test/java/com/qa/qaautomationlabs/pages/Addtocart_page.java");

    public String product    = "//a[normalize-space()='A Line Frock']";
    public String addtocart  = "//button[normalize-space()='Add to Cart']";
    public String viewcart   = "//a[@id='cartdesk']//i[@class='fas fa-shopping-cart']";
    public String verifycart = "//text()[contains(.,'A Line Frock')]/..";

    // ── Constructors ──────────────────────────────────────────────
    public Addtocart_page(Page page) {
        this(page, new HealingEngine());
    }

    public Addtocart_page(Page page, HealingEngine healingEngine) {
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
    public String addtocart() {
        System.out.println("Starting add to cart");
        System.out.println("Current URL: " + page.url());
        System.out.println("Current Title: " + page.title());

        clickWithHealing("product", product, "A Line Frock link",
                attrs("text", "A Line Frock", "tag", "a"),
                SOURCE_FILE, loc -> this.product = loc);
        System.out.println("Clicked product");

        clickWithHealing("addtocart", addtocart, "Add to Cart button",
                attrs("text", "Add to Cart", "tag", "button"),
                SOURCE_FILE, loc -> this.addtocart = loc);
        System.out.println("Clicked Add to Cart");

        clickWithHealing("viewcart", viewcart, "View Cart link",
                attrs("id", "cartdesk", "class", "fas fa-shopping-cart"),
                SOURCE_FILE, loc -> this.viewcart = loc);
        System.out.println("Clicked View Cart");

        String textContent = getTextWithHealing("verifycart", verifycart, "Cart verification text",
                attrs("text", "A Line Frock"),
                SOURCE_FILE, loc -> this.verifycart = loc);
        System.out.println("Got text: " + textContent);

        return textContent;
    }
}