package com.qa.qaautomationlab.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.qaautomationlabs.base.Base_test_addtocart;
import com.qa.qaautomationlabs.pages.Addtocart_page;

public class Addtocart_test extends Base_test_addtocart {

    @Test
    public void addtocart() {
        // 1. Click "Shop Now" → returns Addtocart_page
        Addtocart_page cartPage = lp.cart();

        System.out.println("in home page after login");

        // 2. Add to cart → returns product name
        String actualProduct = cartPage.addtocart();

        // 3. Assert
        Assert.assertEquals(actualProduct.trim(), "A Line Frock");
    }
}