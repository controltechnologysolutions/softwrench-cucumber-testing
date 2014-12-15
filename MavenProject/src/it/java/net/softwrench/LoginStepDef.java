package net.softwrench;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginStepDef {

	private final WebDriver driver;
	private Scenario scenario;

	public LoginStepDef(SharedDriver driver) {
		this.driver = driver;
	}
	
	@Before
	public void before(Scenario scenario) {
		this.scenario = scenario;
	}

	@Given("^I am on the login page of softWrench$")
	public void i_am_on_the_login_page_of_softWrench() throws Throwable {
		driver.get("http://10.50.100.125/softwrench");
	}

	@When("^I enter my correct username and password$")
	public void i_enter_my_correct_username_and_password() throws Throwable {
		WebElement element = driver.findElement(By.name("userName"));
		element.sendKeys("swadmin");
		WebElement element2 = driver.findElement(By.name("password"));
		element2.sendKeys("sw@dm1n");

		WebElement form = driver.findElement(By.tagName("form"));
		form.submit();
	}

	@Then("^I should get loggedon to softWrench$")
	public void i_should_get_loggedon_to_softWrench() throws Throwable {
		scenario.write("Tested logging in with swadmin credentials.");
		assertTrue(driver.findElement(By.className("fa-sign-out")) != null);
	}
}
