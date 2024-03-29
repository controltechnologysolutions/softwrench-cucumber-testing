package net.softwrench.features.sr.general;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;

import net.softwrench.NavigationHelper;
import net.softwrench.features.exceptions.NoSuchElementException;
import net.softwrench.features.exceptions.UnexpectedErrorMessageException;
import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.features.helpers.ErrorMessage;
import net.softwrench.features.helpers.GridHelper;
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
	private GridHelper gridHelper;
	
	@Autowired
	private SRDetailStepContext context;
	
	@Autowired
	private DetailsHelper detailsHelper;
	
	@Resource(name="tabsProperties")
	private Properties tabsProperties;
	
	@Autowired
	private Reporter reporter;
	
	private static final Logger logger = Logger.getLogger(SRGeneralSteps.class);
	private Map<String, String> tabNames;
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		this.scenario = scenario;
		tabNames = new HashMap<String, String>();
		for (Entry<Object, Object> entry : tabsProperties.entrySet()) {
			tabNames.put(entry.getKey().toString(), entry.getValue().toString());
		}
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
		
		context.setRowClickedOn(rownumber);
		gridHelper.clickOnRow(rownumber);
		
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
		
		if (classes.contains("ng-hide"))
			ResultProvider.INSTANCE.addTestInfo(scenario, "There should be an error message, but there is not." , null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
		assertTrue("There should be an error message, but there is not. Classes are " + classes, !classes.contains("ng-hide"));
	}
	
	@Given("^I click on the '(\\w+?)' tab$")
	public void i_click_on_the_worklog_tab(String tab) throws NoSuchElementException, UnexpectedErrorMessageException {
		ErrorMessage msg = msgDetector.detectErrorMessage();
		detailsHelper.clickOnTab(tabNames.get(tab));
		ErrorMessage msg2 = msgDetector.detectErrorMessage();
		
		if ((msg == null && msg2 != null) || (msg2 != null && !msg.getTitle().equals(msg2.getTitle())))
			msgDetector.checkForErrorMessage("SR record", "I clicked on the " + tab + " tab.", scenario);
	}
}
