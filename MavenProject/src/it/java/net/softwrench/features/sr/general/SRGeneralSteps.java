package net.softwrench.features.sr.general;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.features.exceptions.UnexpectedErrorMessageException;
import net.softwrench.features.helpers.ErrorMessage;
import net.softwrench.features.helpers.MessageDetector;
import net.softwrench.features.helpers.Reporter;
import net.softwrench.features.selenium.SpinnerDonePredicate;
import net.softwrench.features.sr.contexts.SRDetailStepContext;
import net.softwrench.jira.ResultProvider;
import net.softwrench.util.Constants;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SRGeneralSteps {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private NavigationHelper navHelper;
	
	@Autowired
	private MessageDetector msgDetector;
	
	@Autowired
	private SRDetailStepContext context;
	
	@Autowired
	private Reporter reporter;
	
	private static final Logger logger = Logger.getLogger(SRGeneralSteps.class);
	
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		this.scenario = scenario;
	}
	
	
	@Given("^I am on the service request grid$")
	public void i_am_on_the_service_request_grid() throws UnexpectedErrorMessageException {	
		navHelper.makeSureImLoggedIn(driver);
		navHelper.goToSRGrid(driver);
		
		msgDetector.checkForErrorMessage("Service Request Grid", "Clicked on the Service Request Grid Menu Entry.", scenario);
	}
	
	@When("^I click on row (\\d+) in the grid$")
	public void i_click_on_a_row_in_the_grid(int rownumber) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr[" + rownumber + "]/td[3]"));
		if (cells.size() > 0) {
			WebElement idCell = cells.get(0);
			// get id of SR
			context.setSelectedId(idCell.getText());
			// get title of SR
			WebElement titleCell = driver.findElement(By.xpath("//tbody/tr[" + rownumber + "]/td[4]"));
			context.setSelectedTitle(titleCell.getText());
			
			idCell.click();
		}
		else
			throw new PendingException("No data row " + rownumber + ".");
		
		msgDetector.checkForErrorMessage("Service Request Record", "Clicked on row " + rownumber + " in Service Request Grid.", scenario);
	}
	
	@Then("^I should see a '(\\w+)' message$")
	public void i_should_see_a_message(String result) throws Throwable {
		reporter.takeScreenshot("Submitted a new request for a new entity.");
		
		WebDriverWait wait = new WebDriverWait(driver, 10); 
		wait.until(new SpinnerDonePredicate());
		
		reporter.takeScreenshot("Result message.");
		
		if (result.equals(Constants.SUCCESS)) {
			try {
				WebElement successMsg = driver.findElement(By.id("divsuccessmessagedetail"));
				WebElement successList = driver.findElement(By.id("divsuccessmessagelist"));

				logger.info("Message in success message box: " + successMsg.getText());
				logger.info("Message in success message list: " + successList.getText());
				
				if (!successMsg.isDisplayed() && !successList.isDisplayed())
					ResultProvider.INSTANCE.addTestInfo(scenario, "There is no success message displayed." , null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
				assertTrue("Success Message is not displayed.", successMsg.isDisplayed() || successList.isDisplayed());
			} catch(Exception e) {
				logger.error("Exception when checking for success message", e);
				reporter.takeScreenshot(e.getMessage());
				throw e;
			}
			return;
		}
		
		WebElement errorMsg = driver.findElement(By.xpath("//div[@ng-show='hasValidationError']"));	
		String classes = errorMsg.getAttribute("class");
		
		ResultProvider.INSTANCE.addTestInfo(scenario, "There should be an error message, but there is not." , null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
		assertTrue("There should be an error message, but there is not. Classes are " + classes, !classes.contains("ng-hide"));
	}
	
}
