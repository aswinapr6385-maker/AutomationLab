package com.qa.qaautomationlabs.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class Addtocart_page {
	Page page;
	
	public String product = "//a[normalize-space()='A Line Frock']";
	public String addtocart = "//button[normalize-space()='Add to Cart']";
	public String viewcart = "//a[@id='cartdesk']//i[@class='fas fa-shopping-cart']";
	public String verifycart = "A Line Frock";
	
	public  Addtocart_page(Page page) {
		this.page = page;
}
	public String addtocart() {

	    System.out.println("Starting add to cart");
	    System.out.println("Current URL: " + page.url());
	    System.out.println("Current Title: " + page.title());
	    

	 

	    page.click(product);
	    System.out.println("Clicked product");

	    page.click(addtocart);
	    System.out.println("Clicked Add to Cart");

	    page.click(viewcart);
	    System.out.println("Clicked View Cart");

	    Locator cartProduct = page.getByText(verifycart);
	    System.out.println("Created locator");

	    String textContent = cartProduct.textContent();
	    System.out.println("Got text: " + textContent);

	    return textContent;
	}
}