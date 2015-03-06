package net.softwrench.features.helpers.impl;

import net.softwrench.features.helpers.Reporter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

public class ReporterImpl implements Reporter {

	@Autowired
	private RemoteWebDriver driver;
	
	private Scenario scenario;
	
	@Before
	public void init(Scenario scenario) {
		this.scenario = scenario;
	}
	
	/* (non-Javadoc)
	 * @see net.softwrench.features.helpers.impl.ReportHelper#takeScreenshot(java.lang.String)
	 */
	@Override
	public void takeScreenshot(String msg) {	
		scenario.write(msg);
		takeScreenshot();
	}
	
	@Override
	public void takeScreenshot() {
		byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
		scenario.embed(screenshot, "image/png");
	}
	
	/* (non-Javadoc)
	 * @see net.softwrench.features.helpers.impl.ReportHelper#addMessage(java.lang.String)
	 */
	@Override
	public void addMessage(String msg) {
		scenario.write(msg);
	}
}
