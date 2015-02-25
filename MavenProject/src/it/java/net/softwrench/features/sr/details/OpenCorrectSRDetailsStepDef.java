package net.softwrench.features.sr.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.softwrench.features.sr.contexts.SRDetailStepContext;
import net.softwrench.jira.ResultProvider;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;

public class OpenCorrectSRDetailsStepDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private SRDetailStepContext context;
	
	@Value( "${sr.header.heading.label}" )
	private String headingLabel;
	
	private static final Logger logger = Logger.getLogger(OpenCorrectSRDetailsStepDef.class);
	
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		this.scenario = scenario;
	}
	

	@Then("^I should see the record for the SR I clicked on$")
	public void i_should_see_the_record_for_the_service_SR_I_clicked_on() throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 20); // wait for a maximum of 5 seconds
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id = 'crudbodyform']/ng-switch/div/div[2]/h5")));
		
		List<WebElement> cells = driver.findElements(By.xpath("//form[@id = 'crudbodyform']/ng-switch/div/div[2]/h5"));
		if (cells.isEmpty()) {
			ResultProvider.INSTANCE.addTestInfo(scenario, "The SR record has no header." , "Clicked on SR record nr " + context.getSelectedId() + " but there is no header element.", Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
			assertTrue("No header element found.", false);
		}
		
		String headerTitle = cells.get(0).getText().trim();
		logger.info("Header title " + headerTitle);
		
		Pattern pattern = Pattern.compile(headingLabel + " ([A-z0-9]+).+?Summary: (.+)");
		
		String srNr = "";
		String srSummary = "";
		
		Matcher matcher = pattern.matcher(headerTitle);
		boolean matches = matcher.matches();
		assertTrue("Couldn't find SR nr in header: " + headerTitle, matches);
		
		if (matches) {
			srNr = matcher.group(1);
			srSummary = matcher.group(2);
		
			if (!context.getSelectedId().equals(srNr) || !context.getSelectedTitle().equals(srSummary)) {
				StringBuffer error = new StringBuffer("Info on SR grid and opened records do not match.");
				error.append("\nClicked on row in SR grid is: " + context.getSelectedId() + " (" + context.getSelectedTitle() + ")");
				error.append("\nCurrent record is: " + srNr + " (" + srSummary + ")");
				ResultProvider.INSTANCE.addTestInfo(scenario, error.toString() , null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
			}
			assertEquals(context.getSelectedId(), srNr);
			assertEquals(context.getSelectedTitle(), srSummary);
		}
	}
}
