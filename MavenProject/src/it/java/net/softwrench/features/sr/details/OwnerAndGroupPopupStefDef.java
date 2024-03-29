package net.softwrench.features.sr.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.MessageDetector;
import net.softwrench.features.helpers.Reporter;
import net.softwrench.features.sr.contexts.DialogSelection;
import net.softwrench.jira.ResultProvider;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OwnerAndGroupPopupStefDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private DialogSelection selection;
	
	@Autowired
	private AngularHelper angularHelper;
	
	@Autowired
	private MessageDetector msgDetector;
	
	@Autowired
	private Reporter reporter;
	
	private static final Logger logger = Logger.getLogger(OwnerAndGroupPopupStefDef.class);
	
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
		WebElement fieldset = angularHelper.getFieldSetFromInputForm(button);
			
		if (fieldset.findElements(By.xpath(".//*[@ng-click='showLookupModal(fieldMetadata)']")).size() > 0) {
			fieldset.findElement(By.xpath(".//*[@ng-click='showLookupModal(fieldMetadata)']")).click();
			
			WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
			
			lookupModel = null;
			for (WebElement elem : angularHelper.getCrudInputMainElement().findElements(By.className("modal"))) {
				if (elem.isDisplayed())
					lookupModel = elem;
			}
			
			msgDetector.checkForErrorMessage("New Service Request", null, scenario);
			assertTrue("Lookup Dialog is not displayed.",  lookupModel != null);
		}
		
	}

	@Then("^I filter the list in column '(.+?)' with '(\\w+)'$")
	public void i_filter_the_list_in_the_popup_with_filterstring(String column, String filterstring) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (lookupModel.findElements(By.xpath(".//*[@ng-model='" + column + "']")).size() < 1) {
	    	reporter.takeScreenshot("Can't find input field.");
	    	assertTrue("Can't find input field.", false);
	    }
		
		lookupModel.findElement(By.xpath(".//input[@ng-model='" + column + "']")).sendKeys(filterstring);
		lookupModel.findElement(By.xpath(".//span[@ng-click='lookupModalSearch()']")).click();;

		msgDetector.checkForErrorMessage("New SR", "I opened the lookup for owner/owner group and filtered column " + column + " by " + filterstring, scenario);
	}

	@Then("^I click on the (\\d+) result$")
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
					
					if (msg.trim().equals(body.getText().trim()))
						ResultProvider.INSTANCE.addTestInfo(scenario, "The alert message is incorrect. Expected " + msg + ", but found " + body.getText().trim(), null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
					assertEquals(msg.trim(), body.getText().trim());
					
					dialog.findElement(By.tagName("button")).click();
					return;
				}
			}			
		}
		ResultProvider.INSTANCE.addTestInfo(scenario, "There is no alert showing after I selected an owner/ownergroup.", null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
		fail("There is no alert dialog shown.");
		
	}
	
	@Then("^I should see that the '(.+)?' fields are filled$")
	public void i_should_see_that_the_owner_description_fields_are_filled(String fieldstring) throws Throwable {
		String[] fieldArray = fieldstring.split(",");
		
		List<String> fields = Arrays.asList(fieldArray);
		
		for (String field : fields) {
			WebElement fieldSet = angularHelper.getFieldSetFromInputForm(field);
			
			List<WebElement> inputs = null;
			if (fieldSet.findElements(By.tagName("input")).size() > 0)
				inputs = fieldSet.findElements(By.tagName("input"));
			
			if (inputs != null) {
				for (WebElement input : inputs) {
					if (input.isDisplayed()) {
						logger.info("input " + input.getAttribute("ng-model") + " has value " + input.getAttribute("value"));
						if (input.getAttribute("ng-model").equals("lookupAssociationsCode[fieldMetadata.attribute]")) {
							if (!selection.getColumnA().equals(input.getAttribute("value")))
								ResultProvider.INSTANCE.addTestInfo(scenario, "Lookup Code is expected to be \"" + selection.getColumnA() + "\" but is \"" + input.getAttribute("value") + "\"", null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
							assertEquals(selection.getColumnA(), input.getAttribute("value"));
						}
						if (input.getAttribute("ng-model").equals("lookupAssociationsDescription[fieldMetadata.attribute]")) {
							if (!selection.getColumnB().equals(input.getAttribute("value")))
								ResultProvider.INSTANCE.addTestInfo(scenario, "Lookup Description is expected to be \"" + selection.getColumnB() + "\" but is \"" + input.getAttribute("value") + "\"", null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
							assertEquals(selection.getColumnB(), input.getAttribute("value"));
						}
					}
				}
			}
		}
	} 

	@Then("^the field '(\\w+)' is disabled\\.$")
	public void the_field_group_is_disabled(String field) throws Throwable {
		WebElement fieldSet = angularHelper.getFieldSetFromInputForm(field);

		List<WebElement> inputs = null;
		if (fieldSet.findElements(By.tagName("input")).size() > 0)
			inputs = fieldSet.findElements(By.tagName("input"));
		
		if (inputs != null) {
			for (WebElement input : inputs) {
				if (input.isEnabled() && !input.getAttribute("type").equals("hidden")) {
					ResultProvider.INSTANCE.addTestInfo(scenario, "Input field for " + field + " should be disabled.", null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
					fail("Input field for " + field + " should be disabled.");
				}
			}
		}
	}

}
