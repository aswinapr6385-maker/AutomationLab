package com.qa.qaautomationlab.test;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.qa.qaautomationlabs.base.Base_test_addtocart;
import com.qa.qaautomationlabs.pages.Addtocart_page; 

public class Addtocart_test extends Base_test_addtocart{
	
	
	@Test
	public void addtocart() {

	    Addtocart_page cartPage = lp.cart();

	    System.out.println("in home page after login");

	    String actualProduct = cartPage.addtocart();

	    Assert.assertEquals(actualProduct, " A Line Frock");
	}
} 