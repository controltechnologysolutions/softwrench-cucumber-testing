package net.softwrench.features.helpers.impl;

import net.softwrench.features.helpers.ErrorMessage;
import net.softwrench.features.helpers.MessageDetector;
import net.softwrench.features.helpers.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

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
		
		WebElement errorMsg = driver.findElement(By.xpath("//div[@ng-show='$root.hasErrorDetail']"));	
		
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

}
