package net.softwrench.features.sr.details;

import java.io.File;

import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.util.Constants;

import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class AddAttachmentStepDef {
	
	@Autowired
	private DetailsHelper detailsHelper;
	
	@Autowired
	private RemoteWebDriver driver;

	@Given("^I click on the attachment tab$")
	public void i_click_on_the_attachment_tab() throws Throwable {
		detailsHelper.clickOnTab("attachment_");
	}

	@When("^I click on the add attachment button$")
	public void i_click_on_the_add_attachment_button() throws Throwable {
	    detailsHelper.clickOnNewItemButton("additem");
	}

	@When("^I add an attachment '(yes|no)'$")
	public void i_add_an_attachment(String yesOrNo) throws Throwable {
	   if (yesOrNo.trim().toLowerCase().equals(Constants.YES)) {
		   PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		   Resource[] resources = resolver.getResources("classpath:/imgs/*");
		   
		   // needs at least one image in imgs folder
		   int imgNr = RandomUtils.nextInt(0, resources.length - 1);
		   
		   Resource img = resources[imgNr];
		   File imgFile = img.getFile();
		   
		   WebElement uploadElement = driver.findElement(By.id("uploadBtn"));
		   uploadElement.sendKeys(imgFile.getAbsolutePath());
		   
	   }
	}
}
