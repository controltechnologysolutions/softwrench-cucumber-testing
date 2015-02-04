package net.softwrench.features.sr.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;



import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;



import cucumber.api.java.en.Then;

public class OpenCorrectSRDetailsStepDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private SRDetailStepContext context;
	
	@Value( "${sr.header.heading.label}" )
	private String headingLabel;
	
	private static final Logger logger = Logger.getLogger(OpenCorrectSRDetailsStepDef.class);
	

	@Then("^I should see the record for the service SR I clicked on$")
	public void i_should_see_the_record_for_the_service_SR_I_clicked_on() throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 20); // wait for a maximum of 5 seconds
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id = 'crudbodyform']/ng-switch/div/div[2]/h5")));
		
		List<WebElement> cells = driver.findElements(By.xpath("//form[@id = 'crudbodyform']/ng-switch/div/div[2]/h5"));
		if (cells.isEmpty())
			assertTrue("No header element found.", false);
		
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
		
			assertEquals(context.getSelectedId(), srNr);
			assertEquals(context.getSelectedTitle(), srSummary);
		}
	}
}
