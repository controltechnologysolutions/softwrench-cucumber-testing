package net.softwrench.features.sr.general;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.softwrench.features.exceptions.ElementNotShownException;
import net.softwrench.features.exceptions.NoSuchElementException;
import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.features.helpers.Reporter;
import net.softwrench.features.sr.contexts.CreationContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;

public class SRGeneralCreateSteps {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private AngularHelper angularHelper;
	
	@Autowired
	private DetailsHelper detailsHelper;
	
	@Autowired
	private Reporter reporter;
	
	@Autowired
	private CreationContext creationContext;
	
	private ByAngular byAngular;
	
	@Before
	public void init(Scenario scenario) {
		byAngular = new ByAngular(driver);
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
	
	@When("^I fill in the '(.+?)' and submit$")
	public void i_fill_in_the_summary_field_with_Cucumber_Test_date_and_the_description_with_Who_loves_cucumbers(String fields) throws Throwable {
		List<String> fieldNames = Arrays.asList(fields.split(","));
		
		List<WebElement> repeatedElements = angularHelper.getRepeatedElements(creationContext.getNewItemComposition(), "fieldMetadata in nonTabFields(displayables)");
	   
		int fieldCounter = 0;
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement element : repeatedElements) {
			String attribute = ngModel.retrieveAsString(element, "fieldMetadata.attribute");
			if (fieldNames.contains(attribute)) {
				WebElement inputElement = null;
				if (!element.findElements(By.tagName("input")).isEmpty())
					inputElement = element.findElement(By.tagName("input"));
				else if (!element.findElements(By.tagName("textarea")).isEmpty())
					inputElement = element.findElement(By.tagName("textarea"));
				
				if (inputElement == null)
					throw new NoSuchElementException("Could not find input element for attribute: " + attribute + ".");
				inputElement.sendKeys("Cucumber " + attribute.toUpperCase() + " " + new Date());
				fieldCounter++;
			}
		}
		assertTrue("Could not fill all specified fields. Expected " + fieldNames.size() + " fields but found " + fieldCounter + ".", fieldNames.size() == fieldCounter);
		
		WebElement submitBtn = detailsHelper.findButton(creationContext.getNewItemComposition(), "save");
		if (submitBtn == null || !submitBtn.isDisplayed())
			throw new ElementNotShownException("Submit button is not displayed.");
		
		submitBtn.findElement(By.tagName("i")).click();
	}
}
