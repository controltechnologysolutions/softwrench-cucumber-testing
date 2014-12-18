package net.softwrench.impl;

import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.util.Configuration;
import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
public class SoftWrenchNavigationHelper implements NavigationHelper {
	
	@Override
	public void makeSureImLoggedIn(String username, String password, WebDriver driver) {
		driver.get(Configuration.SOFTWRENCH_URL);
		List<WebElement> logout = driver.findElements(By.className(Constants.LOGOUT_ICON));
		if (logout.size() > 0)
			return;
		
		WebElement element = driver.findElement(By.name("userName"));
		element.sendKeys(username);
		WebElement element2 = driver.findElement(By.name("password"));
		element2.sendKeys(password);

		WebElement form = driver.findElement(By.tagName("form"));
		form.submit();
		
	}
	
	@Override
	public void goToSRGrid(WebDriver driver) {
		System.err.println("I am at: " + driver.getCurrentUrl());
		driver.findElements(By.className(Constants.SR_ICON));
		WebDriverWait wait = new WebDriverWait(driver, 5); // wait for a maximum of 5 seconds
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(Constants.SR_ICON)));
		
		WebElement gridButton = driver.findElement(By.className(Constants.SR_ICON));
		gridButton.click();
		
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ApplicationMenuItemDefinition_SR")));
//		
//		WebElement gridLink = driver.findElement(By.id("ApplicationMenuItemDefinition_SR"));
//		gridLink.click();
	}
}
