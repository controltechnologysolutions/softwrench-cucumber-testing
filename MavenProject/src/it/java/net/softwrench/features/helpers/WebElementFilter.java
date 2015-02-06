package net.softwrench.features.helpers;

import org.openqa.selenium.WebElement;

@FunctionalInterface
public interface WebElementFilter {
	public boolean filterElement(WebElement element);
}
