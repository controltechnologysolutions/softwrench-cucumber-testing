package net.softwrench;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

//@Component
//@Scope("cucumber-glue")
public class SoftWrenchFirefoxDriver extends FirefoxDriver{

	@Autowired
	private Environment env;	
	
	@PostConstruct
	public void init() {
		this.manage().window().setSize(new Dimension(1024, 800));
	}
	
	@PreDestroy
	public void destroy() {
		this.quit();
	}
	
}
