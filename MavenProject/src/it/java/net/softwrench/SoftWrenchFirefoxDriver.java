package net.softwrench;

import javax.annotation.PreDestroy;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cucumber.api.java.After;

//@Component
//@Scope("cucumber-glue")
public class SoftWrenchFirefoxDriver extends FirefoxDriver{

	@Autowired
	private Environment env;	
	
	@PreDestroy
	public void destroy() {
		this.quit();
	}
	
}
