package net.softwrench.features.sr.details;

import java.util.List;

import net.softwrench.features.filters.FilterService;
import net.softwrench.features.helpers.AngularHelper;
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
	   
	    WebElement parent = statusFilterField.findElement(By.xpath(".//.."));
	    statusFilterField.sendKeys("closed");
		
	    WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		parent.findElement(By.xpath(".//preceding-sibling::span[1]")).click();
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
	   
		filterHelper.selectFilter(columnnr, FilterService.NEQ);
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
	}

	@Given("^I click on the worklog tab$")
	public void i_click_on_the_worklog_tab() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^I click on the SR worklog button$")
	public void i_click_on_the_SR_worklog_button() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@When("^I fill in the '(.+?)'$")
	public void i_fill_in_the_summary_field_with_Cucumber_Test_date_and_the_description_with_Who_loves_cucumbers(String fields) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

}
