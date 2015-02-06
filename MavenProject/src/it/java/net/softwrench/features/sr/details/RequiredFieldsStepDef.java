package net.softwrench.features.sr.details;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.WebElementFilter;
import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Then;

public class RequiredFieldsStepDef {

	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private SRDetailStepContext context;
	
	@Autowired
	private AngularHelper angularHelper;
	
	private static final Logger logger = Logger.getLogger(RequiredFieldsStepDef.class);
	

	@Then("^I should see that all fields that have an asterics are filled\\.$")
	public void i_should_see_that_all_fields_that_have_an_asterics_are_filled() throws Throwable {

		WebElementFilter filter = (WebElement we) -> we.findElements(By.className("requiredfieldmark")).size() > 0 && we.findElement(By.className("requiredfieldmark")).isDisplayed();
		
		List<WebElement> requiredFields = angularHelper.filterFieldSets(filter);
		
		for (WebElement required : requiredFields) {
			WebElement inputElem = null;
			if (required.findElements(By.xpath(".//input")).size() > 0) {
				inputElem = required.findElement(By.xpath(".//input"));
			}
			else if (required.findElements(By.xpath(".//select")).size() > 0) {
				inputElem = required.findElement(By.xpath(".//select"));
			}
			else if (required.findElements(By.xpath(".//textarea")).size() > 0) {
				inputElem = required.findElement(By.xpath(".//textarea"));
			}
			
			if (inputElem != null) {
				String displayedText = inputElem.getAttribute("value");
				logger.info(required.findElement(By.tagName("label")).getText() + " " + displayedText);
				assertTrue("No text is displayed.", displayedText.length() > 0);
			}
			else {
				logger.info("Can't find input element.");
			}
		}
		
		logger.info("Required Fields: " + requiredFields.size());
	}
}
