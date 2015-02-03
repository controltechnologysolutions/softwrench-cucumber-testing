package net.softwrench.features.sr.details;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.softwrench.features.sr.contexts.DialogSelection;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OwnerAndGroupPopupStefDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private DialogSelection selection;
	
	private ByAngular byAngular;
	private WebElement lookupModel;
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		byAngular = new ByAngular(driver);
		this.scenario = scenario;
	}


	@When("^I click on the '(\\w+)' button$")
	public void i_click_on_the_owner_button(String button) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		List<WebElement> inputMains = driver.findElements(By.xpath("//*[@elementid='crudInputMain']"));
		List<WebElement> fieldRepeat = inputMains.get(0).findElements(byAngular.repeater("fieldMetadata in nonTabFields(displayables)"));
		
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement fieldset : fieldRepeat) {
			String attribute = null;
			try {
				// I think we can't test if attribute is there, so need to catch the exception
				attribute = ngModel.retrieveAsString(fieldset, "fieldMetadata.attribute");
			} catch (WebDriverException ex) {
				continue;
			}
			
			if (!attribute.equals(button)) 
				continue;
			
			if (fieldset.findElements(By.xpath(".//*[@ng-click='showLookupModal(fieldMetadata)']")).size() > 0) {
				fieldset.findElement(By.xpath(".//*[@ng-click='showLookupModal(fieldMetadata)']")).click();
				
				WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
				
				lookupModel = null;
				for (WebElement elem : inputMains.get(0).findElements(By.className("modal"))) {
					if (elem.isDisplayed())
						lookupModel = elem;
				}
				
				assertTrue("Lookup Dialog is not displayed.",  lookupModel != null);
			}
		}
	}

	@When("^I filter the list in column '(.+?)' with '(\\w+)'$")
	public void i_filter_the_list_in_the_popup_with_filterstring(String column, String filterstring) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		if (lookupModel.findElements(By.xpath(".//*[@ng-model='" + column + "']")).size() < 1) {
	    	byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
	    	scenario.embed(screenshot, "image/png");
	    	assertTrue("Can't find input field.", false);
	    }
		
		lookupModel.findElement(By.xpath(".//input[@ng-model='" + column + "']")).sendKeys(filterstring);
		lookupModel.findElement(By.xpath(".//span[@ng-click='lookupModalSearch()']")).click();;
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
	}

	@When("^I click on the (\\d+) result$")
	public void i_click_on_the_result(int nr) throws Throwable {
		List<WebElement> results = lookupModel.findElements(byAngular.repeater("option in lookupObj.options"));
		
		if (results.size() >= nr-1) {
			WebElement result = results.get(nr-1);
			List<WebElement> cells = result.findElements(By.tagName("td")); 
			selection.setColumnA(cells.get(0).getText());
			selection.setColumnB(cells.get(1).getText());
			results.get(nr-1).click();
		}
	}

	@Then("^I should see a warning that the '(.+?)'$")
	public void i_should_see_a_warning_that_the_owner_is_disabled(String msg) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		List<WebElement> dialogs = driver.findElements(By.className("modal-dialog"));
		
		if (!dialogs.isEmpty()) {
			
			for (WebElement dialog : dialogs) {
				if (dialog.isDisplayed()) {
					WebElement body = dialog.findElement(By.className("modal-body"));
					assertEquals(msg.trim(), body.getText().trim());
					
					dialog.findElement(By.tagName("button")).click();
					return;
				}
			}			
		}
		
		fail("There is no alert dialog shown.");
		
	}
	
	@Then("^I should see that the '(.+)?' fields are filled$")
	public void i_should_see_that_the_owner_description_fields_are_filled(String fieldstring) throws Throwable {
		String[] fieldArray = fieldstring.split(",");
		
		List<String> fields = Arrays.asList(fieldArray);
		
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement inputMain = driver.findElement(By.xpath("//*[@elementid='crudInputMain']"));
		
		List<WebElement> fieldRepeat = inputMain.findElements(byAngular.repeater("fieldMetadata in nonTabFields(displayables)"));
		
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement fieldSet : fieldRepeat) {
			
			try {
				String attribute = ngModel.retrieveAsString(fieldSet, "fieldMetadata.attribute");
				if (!fields.contains(attribute)) {
					continue;
				}
				
			} catch(Exception ex) {
				// in some cases the variable might be missing and we can't test for this
				continue;
			}

			List<WebElement> inputs = null;
			if (fieldSet.findElements(By.tagName("input")).size() > 0)
				inputs = fieldSet.findElements(By.tagName("input"));
			
			if (inputs != null) {
				for (WebElement input : inputs) {
					if (input.isDisplayed()) {
						System.out.println("input " + input.getAttribute("ng-model") + " has value " + input.getAttribute("value"));
						if (input.getAttribute("ng-model").equals("lookupAssociationsCode[fieldMetadata.attribute]"))
							assertEquals(selection.getColumnA(), input.getAttribute("value"));
						if (input.getAttribute("ng-model").equals("lookupAssociationsDescription[fieldMetadata.attribute]"))
							assertEquals(selection.getColumnB(), input.getAttribute("value"));
					}
				}
			}
			
		}
	} 

	@Then("^the field '(\\w+)' is disabled\\.$")
	public void the_field_group_is_disabled(String field) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement inputMain = driver.findElement(By.xpath("//*[@elementid='crudInputMain']"));
		
		List<WebElement> fieldRepeat = inputMain.findElements(byAngular.repeater("fieldMetadata in nonTabFields(displayables)"));
		
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement fieldSet : fieldRepeat) {
			
			try {
				String attribute = ngModel.retrieveAsString(fieldSet, "fieldMetadata.attribute");
				if (!field.equals(attribute))
					continue;
				
				List<WebElement> inputs = null;
				if (fieldSet.findElements(By.tagName("input")).size() > 0)
					inputs = fieldSet.findElements(By.tagName("input"));
				
				if (inputs != null) {
					for (WebElement input : inputs) {
						if (input.isEnabled() && !input.getAttribute("type").equals("hidden"))
							fail("Input field for " + field + " should be disabled.");
					}
				}
				
			} catch(Exception ex) {
				// in some cases the variable might be missing and we can't test for this
				continue;
			}
		}
	}

}
