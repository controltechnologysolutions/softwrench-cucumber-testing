package net.softwrench.features.helpers.impl;

import java.util.Arrays;

import net.softwrench.features.exceptions.UnexpectedErrorMessageException;
import net.softwrench.features.helpers.ErrorMessage;
import net.softwrench.features.helpers.MessageDetector;
import net.softwrench.features.helpers.Reporter;
import net.softwrench.jira.ResultProvider;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.Scenario;

/**
 * This class helps with detecting/finding displayed message boxes.
 * @author jdamerow
 *
 */
@Component
@Scope("cucumber-glue")
public class MessageDetectorImpl implements MessageDetector {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private Reporter reporter;
	
	/* (non-Javadoc)
	 * @see net.softwrench.features.helpers.impl.MessageDetector#detectErrorMessage()
	 */
	@Override
	public ErrorMessage detectErrorMessage() {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		WebElement errorMsg = driver.findElement(By.id("diverrormessagedetail"));	
		
		if (errorMsg.isDisplayed()) {		
			String title = errorMsg.findElement(By.tagName("span")).getText();
			reporter.addMessage("Error message detected: " + title);
			reporter.takeScreenshot();
			
			String stacktrace = "";
			if (errorMsg.findElements(By.xpath(".//img[@ng-click='openModal()']")).size() > 0) {
				errorMsg.findElement(By.xpath(".//img[@ng-click='openModal()']")).click();
				
				WebElement errorModal = driver.findElement(By.id("errorModal"));
				WebElement stackTraceBox = errorModal.findElement(By.className("modal-body"));
				reporter.addMessage("Stack trace is: \n");
				stacktrace = stackTraceBox.findElement(By.tagName("textarea")).getAttribute("value");
				reporter.addMessage(stacktrace);
			}
			
			return new ErrorMessage(title, stacktrace);
		}
		
		return null;
	}
	
	@Override
	public void checkForErrorMessage(String page, String steps, Scenario scenario) throws UnexpectedErrorMessageException {
		ErrorMessage msg = detectErrorMessage();
		if (msg != null) {
			StringBuffer msgBuffer = new StringBuffer();
			msgBuffer.append("On the page \"" + page + "\", the followwing error was shown: " + msg.getTitle());
			msgBuffer.append("\n");
			if (steps != null) {
				msgBuffer.append(steps);
			}
			ResultProvider.INSTANCE.addTestInfo(scenario, msgBuffer.toString() , msg.getStacktrace(), Arrays.asList(driver.getScreenshotAs(OutputType.BYTES)));
			throw new UnexpectedErrorMessageException(msg.getTitle());
		}
	}

}
