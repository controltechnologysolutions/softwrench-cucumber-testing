package net.softwrench.features.sr.details;

import net.softwrench.features.helpers.DetailsHelper;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.PendingException;
import cucumber.api.java.en.When;

public class CreateCommunicationStepDef {
	
	@Autowired
	private DetailsHelper detailsHelper;

	@When("^I click on the SR communication button$")
	public void i_click_on_the_SR_communication_button() throws Throwable {
		detailsHelper.clickOnNewItemButton("additem");
	}
	
}
