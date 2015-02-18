package net.softwrench.features.sr.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cucumber.api.java.en.Then;

public class OpenCorrectSRDetailsStepDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private SRDetailStepContext context;
	
	@Autowired
	private DetailsHelper detailsHelper;
	
	@Value( "${sr.header.heading.label}" )
	private String headingLabel;
	
	@Value( "${sr.header.heading.summary}" )
	private String headingSummary;
	
	@Then("^I should see the record for the service SR I clicked on$")
	public void i_should_see_the_record_for_the_service_SR_I_clicked_on() throws Throwable {
		DetailsHelper.HeaderInfo info = detailsHelper.getRecordNr(headingLabel, headingSummary);
		assertEquals(context.getSelectedId(), info.recordNr);
		assertEquals(context.getSelectedTitle(), info.summary);
	}
}
