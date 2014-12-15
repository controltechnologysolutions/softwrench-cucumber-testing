package net.softwrench;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class ShowSRDetailsStepDef {

	private final WebDriver driver;

	public ShowSRDetailsStepDef(SharedDriver driver) {
		this.driver = driver;
	}

	@Given("^I am on the service request grid$")
	public void i_am_on_the_service_request_grid() throws Throwable {
		driver.get("http://10.50.100.125/softwrench");
		WebElement element = driver.findElement(By.name("userName"));
		element.sendKeys("swadmin");
		WebElement element2 = driver.findElement(By.name("password"));
		element2.sendKeys("sw@dm1n");

		WebElement form = driver.findElement(By.tagName("form"));
		form.submit();
	}

	@When("^I click on a row in the grid$")
	public void i_click_on_a_row_in_the_grid() throws Throwable {
		try {
			Thread.sleep(2000); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		WebElement cell = driver.findElement(By.xpath("//tbody/tr/td[3]"));
		cell.click();
	}

	@Then("^I should see the details for the service SR I clicked on$")
	public void i_should_see_the_details_for_the_service_SR_I_clicked_on()
			throws Throwable {
		try {
			Thread.sleep(2000); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		WebElement ticketId = driver.findElement(By.className("ticketid"));

		assertTrue(ticketId.findElement(By.tagName("h5")) != null);

		WebElement errorMsg = driver
				.findElement(By.id("diverrormessagedetail"));
		if (errorMsg != null) {
			assertTrue(errorMsg.getAttribute("class").contains("ng-hide"));
		}

	}
}
