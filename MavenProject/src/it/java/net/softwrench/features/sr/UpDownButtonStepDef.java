package net.softwrench.features.sr;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.softwrench.features.exceptions.RecordNumberDoesNotExist;
import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.GridHelper;
import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UpDownButtonStepDef {
	
	protected static final String PREVIOUS = "ng-click='crawl(0)'";
	protected static final String NEXT = "ng-click='crawl(1)'";
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private AngularHelper angularHelper;
	
	@Autowired
	private GridHelper gridHelper;
	
	@Autowired
	private SRDetailStepContext context;
	
	
	@Value( "${sr.grid.oftotalpages.label}" )
	private String ofTotalLabel;

	private List<String> ids;
	

	@Given("^I remember the order of records in the grid$")
	public void i_remember_the_order_of_records_in_the_grid() throws Throwable {
		ids = gridHelper.getResults(1);
	}

	@Then("^the '(up|down)' button should be '(enabled|disabled)'$")
	public void the_up_button_should_be_disabled(String updown, String disenabled) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		String buttonXPath = null;
		
		if (updown.equals("up"))
			buttonXPath = NEXT;
		else
			buttonXPath = PREVIOUS;
		
		WebElement button = driver.findElement(By.xpath("//button[@" + buttonXPath + "]"));
		
		boolean enabled = disenabled.equals("enabled");
		assertTrue("Button " + updown + " should be " + disenabled + " but is not.", enabled == button.isEnabled());
	}

	@Then("^I should see the '(previous|next)' record$")
	public void i_should_see_the_next_record(String prevOrNext) throws Throwable {
	    int expectedRecordNr = context.getRowClickedOn();
	    
	    if (prevOrNext.equals("previous"))
	    	expectedRecordNr++;
	    else
	    	expectedRecordNr--;
	    
	    if (ids.size() < expectedRecordNr || expectedRecordNr < 0)
	    	throw new RecordNumberDoesNotExist("Record " + expectedRecordNr + " does not exist.");
	    
	    String expectedId = ids.get(expectedRecordNr - 1);
	    
	    
	}
	
	@When("^I click on row (first|last) in the grid$")
	public void i_click_on_row_last_in_the_grid() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
}
