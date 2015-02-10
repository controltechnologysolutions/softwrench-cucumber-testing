package net.softwrench.features.sr.details;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.softwrench.features.exceptions.CouldNotSelectFilterException;
import net.softwrench.features.exceptions.ElementNotShownException;
import net.softwrench.features.exceptions.NoSuchElementException;
import net.softwrench.features.filters.FilterService;
import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.features.helpers.FilterHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
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

	private WebElement newItemCompo;

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
		
	    WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);

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

	@Given("^I click on the worklog tab$")
	public void i_click_on_the_worklog_tab() throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement tab = detailsHelper.findTab("worklog_");
	    
	    if (tab == null)
	    	throw new NoSuchElementException("Can't find tab worklog_.");
	    
	    tab.findElement(By.tagName("a")).click();
	}

	@When("^I click on the SR worklog button$")
	public void i_click_on_the_SR_worklog_button() throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement addButton = detailsHelper.findButton(null, "additem");
		
		if (addButton == null)
			throw new NoSuchElementException("Can't find button additem.");
		
		addButton.findElement(By.tagName("i")).click();
		
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		newItemCompo = driver.findElement(By.id("crudInputNewItemComposition"));
		if (!newItemCompo.isDisplayed())
			throw new ElementNotShownException("Form to add new worklog is not displayed.");
	}

	@When("^I fill in the '(.+?)' and submit$")
	public void i_fill_in_the_summary_field_with_Cucumber_Test_date_and_the_description_with_Who_loves_cucumbers(String fields) throws Throwable {
		List<String> fieldNames = Arrays.asList(fields.split(","));
		
		List<WebElement> repeatedElements = angularHelper.getRepeatedElements(newItemCompo, "fieldMetadata in nonTabFields(displayables)");
	   
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
		
		WebElement submitBtn = detailsHelper.findButton(newItemCompo, "save");
		if (submitBtn == null || !submitBtn.isDisplayed())
			throw new ElementNotShownException("Submit button is not displayed.");
		
		submitBtn.findElement(By.tagName("i")).click();
	}

}
