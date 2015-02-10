package net.softwrench.features.sr.general;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.features.SpinnerDonePredicate;
import net.softwrench.features.helpers.Reporter;
import net.softwrench.features.sr.contexts.SRDetailStepContext;
import net.softwrench.util.Constants;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SRGeneralSteps {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private NavigationHelper navHelper;
	
	@Autowired
	private SRDetailStepContext context;
	
	@Autowired
	private Reporter reporter;
	
	private static final Logger logger = Logger.getLogger(SRGeneralSteps.class);
	
	
	@Given("^I am on the service request grid$")
	public void i_am_on_the_service_request_grid() throws Throwable {
		
		navHelper.makeSureImLoggedIn(driver);
		navHelper.goToSRGrid(driver);
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
	}
	
	@Then("^I should see a '(\\w+)' message$")
	public void i_should_see_a_message(String result) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 10); 
		wait.until(new SpinnerDonePredicate());
		
		reporter.takeScreenshot("Submitted a new request for a new entity.");
		
		if (result.equals(Constants.SUCCESS)) {
			try {
				WebElement successMsg = driver.findElement(By.id("divsuccessmessagedetail"));
				WebElement successList = driver.findElement(By.id("divsuccessmessagelist"));

				logger.info("Message in success message box: " + successMsg.getText());
				logger.info("Message in success message list: " + successList.getText());
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
		assertTrue("There should be an error message, but there is not. Classes are " + classes, !classes.contains("ng-hide"));
	}
	
}
