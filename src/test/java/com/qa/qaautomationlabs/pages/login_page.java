
package com.qa.qaautomationlabs.pages;
import com.microsoft.playwright.Page;

	

public class login_page {
	private Page page;
	private String email = "//input[@id='email']";
	private String password = "//input[@id='password']";
	private String lognbtn = "//button[@id='loginBtn']";
	private String pageheader = "(//nav[@class='breadcrumb bg-light mb-30'])[1]";
	public String shopnow = "//a[@title='Shop Kids Fashion']";
	
	

	public  login_page(Page page) {
		this.page = page;
		
		
		
	}
	
	public String gethomepageTitile() {
		String actualtitle = page.title();
		System.out.println("Title: "+actualtitle);
		return actualtitle;
	}
	
	public String gethomepageuturl() {
		 String actualurl = page.url();
		 System.out.println("URL: "+actualurl);
		 return actualurl;
	

}
	public void dologin(String username,String pass) {
		page.fill(email, username);
		page.fill(password, pass);
		page.click(lognbtn);
		
		
		
		
	}
	public String textcontent() {
		String textContent = page.textContent(pageheader);
		System.out.println("header is:"+textContent);
		return textContent;
	}
	public Addtocart_page cart() {
		page.click(shopnow);
		  System.out.println("Clicked Shop Now");
		return new Addtocart_page(page);
	}
}