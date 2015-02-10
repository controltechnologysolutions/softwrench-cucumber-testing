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
	
	
	private ByAngular byAngular;
	
	@Before
	public void init(Scenario scenario) {
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		byAngular = new ByAngular(driver);
	}

	@Given("^I click on the new SR button$")
	public void i_click_on_the_new_SR_button() throws Throwable {
		navHelper.makeSureImLoggedIn(driver);
		((RemoteWebDriver)driver).executeScript("var button = document.getElementById('ApplicationMenuItemDefinition_NSR'); button.click();");
		
	}

	@When("^I fill in the following fields '(\\w+)' with '(.+)' and submit$")
	public void i_fill_in_the_following_fields_with_values(
			String fields, String values) throws Throwable {
		
		//parse fields
		String[] fieldArray = fields.split(",");
		String[] valueArray = values.split(",");
		
		Map<String, String> fieldValues = new HashMap<String, String>();
		for (int i = 0; i<fieldArray.length; i++) {
			fieldValues.put(fieldArray[i], valueArray[i]);
		}
		
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		List<WebElement> inputMains = driver.findElements(By.xpath("//*[@elementid='crudInputMain']"));
		List<WebElement> fieldRepeat = inputMains.get(0).findElements(byAngular.repeater("fieldMetadata in nonTabFields(displayables)"));
		
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement elem : fieldRepeat) {
			
			WebElement input = null;
			if (elem.findElements(By.tagName("input")).size() > 0)
				input = elem.findElement(By.tagName("input"));
			
			if (input.getAttribute("ng-model") != null && input.getAttribute("ng-model").equals("datamap[fieldMetadata.attribute]")) {			
				try {
					String attribute = ngModel.retrieveAsString(input, "fieldMetadata.attribute");
					
					if (fieldValues.containsKey(attribute)) {
						// in case we're already at the input element
						input.sendKeys(fieldValues.get(attribute) + " " + new Date());
					}
					
				} catch(Exception ex) {
					// in some cases the variable might be missing
				}
			}
			
		}
		
		WebElement submitBtn = driver.findElement(By.xpath("//button[@data-original-title='Submit']"));
		submitBtn.click();
		
	}

	
	
	
}
