package net.softwrench;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class SoftWrenchFirefoxDriver extends FirefoxDriver{

	@Autowired
	private Environment env;	
	
}
