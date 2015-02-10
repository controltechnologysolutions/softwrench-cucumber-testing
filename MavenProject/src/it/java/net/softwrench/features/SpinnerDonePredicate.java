package net.softwrench.features;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;

public class SpinnerDonePredicate implements Predicate<WebDriver> {

	@Override
	public boolean apply(WebDriver driver) {
		List<WebElement> spinners = driver.findElements(By.className("spinner"));
		
		for (WebElement spinner : spinners) {
			if (spinner.isDisplayed())
				return false;
		}
		
		return true;
	}

	
}
