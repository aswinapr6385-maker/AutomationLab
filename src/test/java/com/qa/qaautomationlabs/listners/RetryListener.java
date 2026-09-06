package com.qa.qaautomationlabs.listners;

	import org.testng.IAnnotationTransformer;
	import org.testng.annotations.ITestAnnotation;

	import com.qa.qaautomationlabs.base.RetryAnalyzer;

	import java.lang.reflect.Constructor;
	import java.lang.reflect.Method;

	public class RetryListener implements IAnnotationTransformer {

	    @Override
	    public void transform(
	            ITestAnnotation annotation,
	            Class testClass,
	            Constructor testConstructor,
	            Method testMethod) {

	        annotation.setRetryAnalyzer(RetryAnalyzer.class);
	    }
	}

