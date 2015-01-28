package net.softwrench.features;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.softwrench.util.Configuration;
import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginComplete {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private Environment env;	
	
	private String testEnvironment;
	
	@Before
	public void beforeScenario() {
		testEnvironment = env.getProperty("test.instance");
		System.out.println("Go to test environment: " + testEnvironment);
		driver.get(testEnvironment);
	}

	@Given("^I filled '(\\w+)' and '(\\S+)'$")
	public void i_filled_username_and_password(String username, String password) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 10); // wait for a maximum of 5 seconds
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("userName")));
		
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
		boolean correctPage = driver.getCurrentUrl().startsWith(testEnvironment + Configuration.SIGNIN_PAGE);
	    
		String successOrFailure = correctPage && warnings.size() > 0 ? Constants.FAILURE : null;
			   
		if (successOrFailure == null) {
			successOrFailure = driver.findElement(By.className("fa-sign-out")) != null ? Constants.SUCCESS : null;
		}
	    assertEquals(successOrFailure, result);
	}
	
	@After
	public void afterScenario() {
		System.out.println("Logging out.");
		driver.get(testEnvironment + Constants.LOGOUT_URL);
	}

}
