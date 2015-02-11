package net.softwrench.features;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;

public class SpinnerDonePredicate implements Predicate<WebDriver> {
	
	private static Logger logger = Logger.getLogger(SpinnerDonePredicate.class);

	@Override
	public boolean apply(WebDriver driver) {
		List<WebElement> spinners = driver.findElements(By.className("spinner"));
		
		for (WebElement spinner : spinners) {
			// for some reason the spinner doesn't seem to be attached to the DOM
			// anymore. We can't predict these cases and can't test for them, so
			// we need to catch the exception. If the spinner is not attached to the DOM
			// anymore then it shouldn't be displayed either.
			try {
				if (spinner.isDisplayed())
					return false;
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		
		return true;
	}

	
}
