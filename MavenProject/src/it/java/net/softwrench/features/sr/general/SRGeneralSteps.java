package net.softwrench.features.sr.general;

import java.util.List;

import net.softwrench.NavigationHelper;
import net.softwrench.SoftWrenchRemoteDriver;
import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class SRGeneralSteps {
	
	@Autowired
	private SoftWrenchRemoteDriver driver;
	
	@Autowired
	private NavigationHelper navHelper;
	
	@Autowired
	private SRDetailStepContext context;
	

	@Given("^I am on the service request grid$")
	public void i_am_on_the_service_request_grid() throws Throwable {
		navHelper.makeSureImLoggedIn("swadmin", "sw@dm1n", driver);
		navHelper.goToSRGrid(driver);
	}
	
	@When("^I click on row (\\d+) in the grid$")
	public void i_click_on_a_row_in_the_grid(int rownumber) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 10); // wait for a maximum of 5 seconds
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr/td[3]")));
		
		List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr[" + rownumber + "]/td[3]"));
		if (cells.size() > 0) {
			WebElement idCell = cells.get(0);
			// get id of SR
			context.setSelectedId(idCell.getText());
			// get title of SR
			WebElement titleCell = driver.findElement(By.xpath("//tbody/tr[" + rownumber + "]/td[4]"));
			context.setSelectedTitle(titleCell.getText());
			
			idCell.click();
		}
		else
			throw new PendingException("No data row " + rownumber + ".");
	}
	
}
