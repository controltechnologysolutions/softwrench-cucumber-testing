package net.softwrench.features;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.SoftWrenchRemoteDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GoToProfile {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private NavigationHelper navHelper;
	
	@Given("^I am logged in$")
	public void i_am_logged_in() throws Throwable {
		navHelper.makeSureImLoggedIn("swadmin", "sw@dm1n", driver);   
	}

	@When("^I click on Profile$")
	public void i_click_on_Profile() throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);

		
		//((RemoteWebDriver)driver).executeScript("function click (buttons) {for (var i=0; i < buttons.length; i++) { if (buttons[i].innerHTML == 'My Profile') {value.click();}}} var buttons = document.getElementsByTagName('button'); click(buttons);");
		
		
	    // Write code here that turns the phrase above into concrete actions
		List<WebElement> profileButton = driver.findElements(By.className("fa-user"));
		if (profileButton == null || profileButton.size() == 0){
			assertTrue(false);
			return;
		}
		WebElement value = profileButton.get(0);
		value.click();
	}

	@Then("^I am brought to the profile page$")
	public void i_am_brought_to_the_profile_page() throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);

		List<WebElement> userNameInputFields = driver.findElements(By.xpath("//input[@ng-model='currentUser.login']"));
		List<WebElement> passwordInputFields = driver.findElements(By.xpath("//input[@ng-model ='currentUser.password']"));
	    List<WebElement> firstNameInputFields = driver.findElements(By.xpath("//input[@ng-model ='currentUser.firstName']"));
	    List<WebElement> lastNameInputFields = driver.findElements(By.xpath("//input[@ng-model ='currentUser.lastName']")); 
	    
	    System.out.println(userNameInputFields);
	    System.out.println(passwordInputFields);
	    System.out.println(firstNameInputFields);
	    System.out.println(lastNameInputFields);
	    
	    
	    assertTrue("Username input fields not found.",!userNameInputFields.isEmpty());
	    
	}
	
}

