package net.softwrench;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

//@Component
//@Scope("cucumber-glue")
public class SoftWrenchRemoteDriver extends RemoteWebDriver {
	
	@Autowired
	private Environment env;	
	
	
	@PostConstruct
	public void init() throws MalformedURLException {
		CommandExecutor exec = new HttpCommandExecutor(new URL(env.getProperty("grid.instance")));
		this.setCommandExecutor(exec);
		this.startSession( DesiredCapabilities.firefox());
		this.manage().window().setSize(new Dimension(1024, 768));
	}

	public SoftWrenchRemoteDriver() throws MalformedURLException 
	{
		super();
	}
	
	@PreDestroy
	public void destroy() {
		this.quit();
	}
}
