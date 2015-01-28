package net.softwrench.features.sr.details;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.softwrench.SoftWrenchRemoteDriver;
import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;

public class RequiredFieldsStepDef {

	@Autowired
	private SoftWrenchRemoteDriver driver;
	
	@Autowired
	private SRDetailStepContext context;
	
	private ByAngular byAngular;
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		byAngular = new ByAngular(driver);
		this.scenario = scenario;
	}

	@Then("^I should see that all fields that have an asterics are filled\\.$")
	public void i_should_see_that_all_fields_that_have_an_asterics_are_filled() throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		List<WebElement> inputMains = driver.findElements(By.xpath("//*[@elementid='crudInputMain']"));
		List<WebElement> fieldRepeat = inputMains.get(0).findElements(byAngular.repeater("fieldMetadata in nonTabFields(displayables)"));
		
		byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
		scenario.embed(screenshot, "image/png");
		
		int reqNr = 0;
		for (WebElement fieldset : fieldRepeat) {
			// does field have a label
			if (fieldset.findElements(By.tagName("label")).size() > 0 && fieldset.findElements(By.className("requiredfieldmark")).size() > 0) {
				
				// is it required
				WebElement requiredSpan = fieldset.findElement(By.className("requiredfieldmark"));
				if (requiredSpan.isDisplayed()) {
					WebElement inputElem = null;
					if (fieldset.findElements(By.xpath(".//input")).size() > 0) {
						inputElem = fieldset.findElement(By.xpath(".//input"));
					}
					else if (fieldset.findElements(By.xpath(".//select")).size() > 0) {
						inputElem = fieldset.findElement(By.xpath(".//select"));
					}
					else if (fieldset.findElements(By.xpath(".//textarea")).size() > 0) {
						inputElem = fieldset.findElement(By.xpath(".//textarea"));
					}
					
					if (inputElem != null) {
						String displayedText = inputElem.getAttribute("value");
						System.out.println(fieldset.findElement(By.tagName("label")).getText() + " " + displayedText);
						reqNr++;
						assertTrue("No text is displayed.", displayedText.length() > 0);
					}
					else {
						System.out.println("Can't find input element.");
					}
				}
			}
		}
		
		System.out.println("Required Fields: " + reqNr);
	}
}
