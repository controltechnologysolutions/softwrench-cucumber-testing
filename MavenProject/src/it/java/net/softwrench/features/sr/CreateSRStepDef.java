package net.softwrench.features.sr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.softwrench.NavigationHelper;
import net.softwrench.features.helpers.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CreateSRStepDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private NavigationHelper navHelper;
	
	@Autowired
	private Reporter reporter;
	
	
	@Before
	public void init(Scenario scenario) {
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
	}

	@Given("^I click on the new SR button$")
	public void i_click_on_the_new_SR_button() throws Throwable {
		navHelper.makeSureImLoggedIn(driver);
		((RemoteWebDriver)driver).executeScript("var button = document.getElementById('ApplicationMenuItemDefinition_NSR'); button.click();");
		
	}

	

	
	
	
}
