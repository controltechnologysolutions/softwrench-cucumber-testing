package net.softwrench.impl;

import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.util.Configuration;
import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Override
	public void goToSRGrid(WebDriver driver) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		WebElement gridButton = driver.findElement(By.className(Constants.SR_ICON));
		gridButton.click();
		WebElement gridLink = driver.findElement(By.id("ApplicationMenuItemDefinition_SR"));
		gridLink.click();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
