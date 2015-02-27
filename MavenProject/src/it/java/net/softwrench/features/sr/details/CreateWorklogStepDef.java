package net.softwrench.features.sr.details;

import java.util.List;

import net.softwrench.features.exceptions.CouldNotSelectFilterException;
import net.softwrench.features.exceptions.ElementNotShownException;
import net.softwrench.features.exceptions.NoSuchElementException;
import net.softwrench.features.exceptions.UnexpectedErrorMessageException;
import net.softwrench.features.filters.FilterService;
import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.features.helpers.ErrorMessage;
import net.softwrench.features.helpers.FilterHelper;
import net.softwrench.features.helpers.MessageDetector;
import net.softwrench.features.sr.contexts.CreationContext;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CreateWorklogStepDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private AngularHelper angularHelper;
	
	@Autowired
	private FilterHelper filterHelper;
	
	@Autowired
	private DetailsHelper detailsHelper;

	@Autowired
	private CreationContext creationContext;
	
	@Autowired
	private MessageDetector msgDetector;
	
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		this.scenario = scenario;
	}

	@Given("^I click on the (\\d+) record that is not closed in the grid$")
	public void i_click_on_the_record_that_is_not_closed_in_the_grid(int rownumber) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		List<WebElement> filterInputs = filterHelper.getFilterInputFields();
	   
	   // filter grid to see only not closed tickets
	   AngularModelAccessor ngModel = new AngularModelAccessor(driver);
	
	   WebElement statusFilterField = null;
	   int columnnr;
	   for (columnnr = 1; columnnr <= filterInputs.size(); columnnr++) {
		   String filteredField = null;
		   try {
				filteredField = ngModel.retrieveAsString(filterInputs.get(columnnr - 1), "column.attribute");
			} catch (Exception e) {
				continue;
			}
		   
		   if (filteredField.equals("status")) {
			   statusFilterField = filterInputs.get(columnnr - 1);
			   break;
		   }
	   }
	   
	    statusFilterField.sendKeys("closed");
		
	    msgDetector.checkForErrorMessage("Service Request Grid", "I filtered the status column for records that are not \"closed\".", scenario);

	    boolean selected = filterHelper.selectFilter(columnnr, FilterService.NEQ);
	    if (!selected)
	    	throw new CouldNotSelectFilterException("Filter NEQ could not be selected.");

	    // without this there is a not attached to dom exception
	    filterInputs = filterHelper.getFilterInputFields();
	    statusFilterField = filterInputs.get(columnnr - 1);
	 			
	    statusFilterField.sendKeys(Keys.ENTER);
	   
		// select given row number
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr[" + rownumber + "]/td[3]"));
		if (cells.size() > 0) {
			WebElement idCell = cells.get(0);
			idCell.click();
		}
		else
			throw new PendingException("No data row " + rownumber + ".");
		
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
	}

	@When("^I click on the SR worklog button$")
	public void i_click_on_the_SR_worklog_button() throws ElementNotShownException {
		try {
			detailsHelper.clickOnNewItemButton("additem");
		} catch (NoSuchElementException ex) {
			throw new ElementNotShownException("Form to add new worklog is not displayed.", ex);
		}
	}

	

}
