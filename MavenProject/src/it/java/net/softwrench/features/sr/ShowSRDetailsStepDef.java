package net.softwrench.features.sr;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Then;

public class ShowSRDetailsStepDef {

	@Autowired
	private WebDriver driver;
	
	@Then("^I should see the details for the service SR I clicked on$")
	public void i_should_see_the_details_for_the_service_SR_I_clicked_on()
			throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 5); // wait for a maximum of 5 seconds
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ticketid")));
		
		WebElement ticketId = driver.findElement(By.className("ticketid"));

		assertTrue(ticketId.findElement(By.tagName("h5")) != null);

		WebElement errorMsg = driver
				.findElement(By.id("diverrormessagedetail"));
		if (errorMsg != null) {
			assertTrue(errorMsg.getAttribute("class").contains("ng-hide"));
		}

	}
}
