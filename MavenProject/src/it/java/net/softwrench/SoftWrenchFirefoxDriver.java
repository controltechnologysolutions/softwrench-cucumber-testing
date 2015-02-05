package net.softwrench;

import javax.annotation.PreDestroy;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

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
