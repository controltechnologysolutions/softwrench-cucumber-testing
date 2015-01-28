package net.softwrench.features.sr;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.softwrench.NavigationHelper;
import net.softwrench.SoftWrenchRemoteDriver;
import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateSRStepDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private NavigationHelper navHelper;
	
	private ByAngular byAngular;
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		byAngular = new ByAngular(driver);
		this.scenario = scenario;
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
					ex.printStackTrace();
					assertTrue(false);
				}
			}
			
		}
		
		WebElement submitBtn = driver.findElement(By.xpath("//button[@data-original-title='Submit']"));
		submitBtn.click();
		
	}

	@Then("^I should see a '(\\w+)' message$")
	public void i_should_see_a_message(String result) throws Throwable {
		if (result.equals(Constants.SUCCESS)) {
			try {
				WebElement successMsg = driver.findElement(By.xpath("//div[@ng-show='hasSuccessDetail']"));
				WebDriverWait wait = new WebDriverWait(driver, 5); // wait for a maximum of 5 seconds
				wait.until(ExpectedConditions.visibilityOf(successMsg));
				
				String classes = successMsg.getAttribute("class");
				assertTrue("Success Message is not displayed. Classes are " + classes, !classes.contains("ng-hide"));
				return;
			} catch(Exception e) {
				byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshot, "image/png");
			}
		}
		
		WebElement errorMsg = driver.findElement(By.xpath("//div[@ng-show='hasValidationError']"));	
		String classes = errorMsg.getAttribute("class");
		assertTrue("There should be an error message, but there is not. Classes are " + classes, !classes.contains("ng-hide"));
	}
	
	
}
