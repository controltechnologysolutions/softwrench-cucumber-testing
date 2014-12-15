package net.softwrench;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.softwrench.util.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginFailureStepDef {

	private final WebDriver driver;

	public LoginFailureStepDef(SharedDriver driver) {
		this.driver = driver;
	}
	
	@Given("^I am on the login page of softWrench app$")
	public void i_am_on_the_login_page_of_softWrench_app() throws Throwable {
		driver.get(Configuration.SOFTWRENCH_URL);
	}
	
	@When("^I enter an incorrect username or password$")
	public void i_enter_an_incorrect_username_or_password() throws Throwable {
		WebElement element = driver.findElement(By.name("userName"));
		element.sendKeys("xxxx");
		WebElement element2 = driver.findElement(By.name("password"));
		element2.sendKeys("sw@dm1n");
		
		WebElement form = driver.findElement(By.tagName("form"));
		form.submit();

	}

	@Then("^I should get a respective error message\\.$")
	public void i_should_get_a_respective_error_message() throws Throwable {
		List<WebElement> warnings = driver.findElements(By.className("text-danger"));
	    boolean correctPage = driver.getCurrentUrl().startsWith(Configuration.SOFTWRENCH_URL + Configuration.SIGNIN_PAGE);
	    
	    assertTrue(correctPage && warnings.size() > 0);
	}

}
