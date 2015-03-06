package net.softwrench.features.sr;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.java.en.Then;

public class ShowSRDetailsStepDef {

	@Autowired
	private RemoteWebDriver driver;
	
	@Then("^I should see the details for the service SR I clicked on$")
	public void i_should_see_the_details_for_the_service_SR_I_clicked_on()
			throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		WebElement ticketId = driver.findElement(By.id("crudbodyform"));

		assertTrue(ticketId.findElement(By.tagName("h5")) != null);

		WebElement errorMsg = driver
				.findElement(By.id("diverrormessagedetail"));
		if (errorMsg != null) {
			assertTrue(errorMsg.getAttribute("class").contains("ng-hide"));
		}

	}
}
