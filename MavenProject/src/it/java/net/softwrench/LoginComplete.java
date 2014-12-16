package net.softwrench;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.softwrench.util.Configuration;
import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginComplete {
	
	private final WebDriver driver;
	
	public LoginComplete(SharedDriver driver) {
		this.driver = driver;
	}
	
	@Before
	public void beforeScenario() {
		driver.get(Configuration.SOFTWRENCH_URL);
	}

	@Given("^I filled '(\\w+)' and '(\\S+)'$")
	public void i_filled_username_and_password(String username, String password) throws Throwable {
		WebElement element = driver.findElement(By.name("userName"));
		element.sendKeys(username);
		WebElement element2 = driver.findElement(By.name("password"));
		element2.sendKeys(password);
	}

	@When("^I clicked on Login$")
	public void i_clicked_on_Login() throws Throwable {
		WebElement form = driver.findElement(By.tagName("form"));
		form.submit();
	}

	@Then("^I should see '(\\w+)' message$")
	public void i_should_see_success_or_failure_message(String result) throws Throwable {
		List<WebElement> warnings = driver.findElements(By.className("text-danger"));
		boolean correctPage = driver.getCurrentUrl().startsWith(Configuration.SOFTWRENCH_URL + Configuration.SIGNIN_PAGE);
	    
		String successOrFailure = correctPage && warnings.size() > 0 ? Constants.FAILURE : null;
			   
		if (successOrFailure == null) {
			successOrFailure = driver.findElement(By.className("fa-sign-out")) != null ? Constants.SUCCESS : null;
		}
	    assertEquals(successOrFailure, result);
	}
	
	@After
	public void afterScenario() {
		driver.get(Configuration.SOFTWRENCH_URL + Constants.LOGOUT_URL);
	}

}
