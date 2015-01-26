package net.softwrench.features.sr.details;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.softwrench.SoftWrenchRemoteDriver;
import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
		
		for (WebElement elem : fieldRepeat) {
			// does field have a label
			if (elem.findElements(By.tagName("label")).size() > 0 && elem.findElements(By.className("requiredfieldmark")).size() > 0) {
				System.out.println("Attribute " + elem.findElement(By.tagName("label")).getText());
				// is it required
				WebElement requiredSpan = elem.findElement(By.className("requiredfieldmark"));
				if (requiredSpan.isDisplayed()) {
					WebElement inputElem = elem.findElement(By.xpath("//*[@ng-model='datamap[fieldMetadata.attribute]']"));
					System.out.println("input elem " + inputElem.getTagName());
					String displayedText = inputElem.getAttribute("value");
					System.out.println("Displayed text: " + displayedText);
					
					assertTrue("No text is displayed.", displayedText.length() > 0);
					return;
				}
			}
		}
		
		System.out.println("No required fields.");
	}
}
