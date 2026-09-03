package com.qa.qaautomationlabs.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config_File {
	Properties prop;
	public Properties inti_prop() throws IOException {
		FileInputStream ip = new FileInputStream("./src/test/resources/ Config/config.properties");
		prop = new Properties();
		prop.load(ip);
		return prop;

	}
	
}
