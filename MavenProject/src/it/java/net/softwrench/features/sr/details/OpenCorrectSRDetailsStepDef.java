package net.softwrench.features.sr.details;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.features.sr.contexts.SRDetailStepContext;
import net.softwrench.jira.ResultProvider;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
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
	
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		this.scenario = scenario;
	}
	

	@Then("^I should see the record for the SR I clicked on$")
	public void i_should_see_the_record_for_the_service_SR_I_clicked_on() throws Throwable {
		DetailsHelper.HeaderInfo info = detailsHelper.getRecordNr(headingLabel, headingSummary);
		if (!context.getSelectedId().equals(info.recordNr) || !context.getSelectedTitle().equals(info.summary)) {
			StringBuffer error = new StringBuffer("Info on SR grid and opened records do not match.");
			error.append("\nClicked on row in SR grid is: " + context.getSelectedId() + " (" + context.getSelectedTitle() + ")");
			error.append("\nCurrent record is: " + info.recordNr + " (" + info.summary + ")");
			ResultProvider.INSTANCE.addTestInfo(scenario, error.toString() , null, Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
		}
		assertEquals(context.getSelectedId(), info.recordNr);
		assertEquals(context.getSelectedTitle(), info.summary);
	}
}
