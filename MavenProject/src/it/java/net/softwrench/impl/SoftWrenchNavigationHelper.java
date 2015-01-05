package net.softwrench.impl;

import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.util.Configuration;
import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
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
