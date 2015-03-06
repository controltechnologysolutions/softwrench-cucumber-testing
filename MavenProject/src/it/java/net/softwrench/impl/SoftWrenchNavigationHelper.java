package net.softwrench.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import net.softwrench.NavigationHelper;
import net.softwrench.util.Constants;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@Scope("cucumber-glue")

public class SoftWrenchNavigationHelper implements NavigationHelper {
	
	@Autowired
	private Environment env;	
	
	private static final Logger logger = Logger.getLogger(SoftWrenchNavigationHelper.class);
	
	
	private String testEnvironment;
	private String username;
	private String password;
	
	 
	@PostConstruct
	public void beforeScenario() {
		testEnvironment = env.getProperty("test.instance");
		username = env.getProperty("test.user.username");
		password = env.getProperty("test.user.password");
	}
	
	@Override
	public void makeSureImLoggedIn(String user, String pw, WebDriver driver) {
		logger.debug("Logging in to " + testEnvironment + " as " + user);
		driver.get(testEnvironment);
		
		WebDriverWait wait = new WebDriverWait(driver, 10); 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userName")));
		
		List<WebElement> logout = driver.findElements(By.className(Constants.LOGOUT_ICON));
		if (logout.size() > 0)
			return;
		
		WebElement element = driver.findElement(By.name("userName"));
		element.sendKeys(user);
		WebElement element2 = driver.findElement(By.name("password"));
		element2.sendKeys(pw);

		WebElement form = driver.findElement(By.tagName("form"));
		form.submit();
		
	}
	
	@Override
	public void makeSureImLoggedIn(RemoteWebDriver driver) {
		makeSureImLoggedIn(username, password, driver);	
	}
	
	@Override
	public void goToSRGrid(RemoteWebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 10); 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(Constants.SR_ICON)));
		
		// click on menu SR
		// might not be necessary?
		((RemoteWebDriver)driver).executeScript("function clickIfSR(buttons) {for (var i=0; i < buttons.length; i++) { if (buttons[i].innerHTML == 'Service Requests') {value.click();}}} var buttons = document.getElementsByTagName('button'); clickIfSR(buttons);");
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ApplicationMenuItemDefinition_SR")));
		// click on SR Grid menu entry
		((RemoteWebDriver)driver).executeScript("document.getElementById('ApplicationMenuItemDefinition_SR').click();");
	}
}
