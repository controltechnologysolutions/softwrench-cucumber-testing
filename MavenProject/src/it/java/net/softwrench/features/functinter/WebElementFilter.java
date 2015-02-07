package net.softwrench.features.functinter;

import org.openqa.selenium.WebElement;

@FunctionalInterface
public interface WebElementFilter {
	public boolean filterElement(WebElement element);
}
