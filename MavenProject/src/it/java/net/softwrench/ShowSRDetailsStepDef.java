package net.softwrench;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ShowSRDetailsStepDef {

	@Autowired
	private WebDriver driver;

	@Given("^I am on the service request grid$")
	public void i_am_on_the_service_request_grid() throws Throwable {
		driver.get("http://10.50.100.125/softwrench");
		WebElement element = driver.findElement(By.name("userName"));
		element.sendKeys("swadmin");
		WebElement element2 = driver.findElement(By.name("password"));
		element2.sendKeys("sw@dm1n");

		WebElement form = driver.findElement(By.tagName("form"));
		form.submit();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		
		WebElement gridButton = driver.findElement(By.className(Constants.SR_ICON));
		gridButton.click();
		WebElement gridLink = driver.findElement(By.id("ApplicationMenuItemDefinition_SR"));
		gridLink.click();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@When("^I click on row (\\d+) in the grid$")
	public void i_click_on_a_row_in_the_grid(int rownumber) throws Throwable {
		try {
			Thread.sleep(2000); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr[" + rownumber + "]/td[3]"));
		if (cells.size() > 0)
			cells.get(0).click();
		else
			throw new PendingException("No data row " + rownumber + ".");
	}

	@Then("^I should see the details for the service SR I clicked on$")
	public void i_should_see_the_details_for_the_service_SR_I_clicked_on()
			throws Throwable {
		try {
			Thread.sleep(2000); // wait 2 seconds to load.
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
