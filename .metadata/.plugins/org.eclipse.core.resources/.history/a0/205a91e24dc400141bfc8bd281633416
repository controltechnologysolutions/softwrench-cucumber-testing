package net.softwrench;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.softwrench.features.sr.general.SRGeneralSteps;

import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class SoftWrenchRemoteDriver extends RemoteWebDriver {
	
	@Autowired
	private Environment env;	
	
	private static final Logger logger = Logger.getLogger(SoftWrenchRemoteDriver.class);
	
	@PostConstruct
	public void init() throws MalformedURLException {
		CommandExecutor exec = new HttpCommandExecutor(new URL(env.getProperty("grid.instance")));
		this.setCommandExecutor(exec);
		// default is firefox
		Capabilities capabilities = DesiredCapabilities.firefox();
		String browsertype = env.getProperty("test.browser");
		logger.info("Testing in " + browsertype);
		if (browsertype.equals("IE")) 
			capabilities = DesiredCapabilities.internetExplorer();
		else if (browsertype.equals("Chrome"))
			capabilities = DesiredCapabilities.chrome();
		
		this.startSession( capabilities );
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
