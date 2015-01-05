package net.softwrench;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PreDestroy;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class SoftWrenchRemoteDriver extends RemoteWebDriver {

	public SoftWrenchRemoteDriver() throws MalformedURLException 
	{
		super(new URL("http://10.50.100.148:4444/wd/hub"), DesiredCapabilities.firefox());
	}
	
	@PreDestroy
	public void destroy() {
		this.quit();
	}
}
