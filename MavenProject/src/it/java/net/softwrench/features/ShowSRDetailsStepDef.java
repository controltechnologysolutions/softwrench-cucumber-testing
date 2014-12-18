package net.softwrench.features;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.util.Configuration;
import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ShowSRDetailsStepDef {

	@Autowired
	private WebDriver driver;
	
	@Autowired
	private NavigationHelper navHelper;

	@Given("^I am on the service request grid$")
	public void i_am_on_the_service_request_grid() throws Throwable {
		navHelper.makeSureImLoggedIn("swadmin", "sw@dm1n", driver);
		navHelper.goToSRGrid(driver);
	}

	@When("^I click on row (\\d+) in the grid$")
	public void i_click_on_a_row_in_the_grid(int rownumber) throws Throwable {
		throw new PendingException();
		
//		WebDriverWait wait = new WebDriverWait(driver, 5); // wait for a maximum of 5 seconds
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr/td[3]")));
//		
//		List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr[" + rownumber + "]/td[3]"));
//		if (cells.size() > 0)
//			cells.get(0).click();
//		else
//			throw new PendingException("No data row " + rownumber + ".");
	}

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
