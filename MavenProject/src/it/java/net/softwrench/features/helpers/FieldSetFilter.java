package net.softwrench.features.helpers;

import org.openqa.selenium.WebElement;

@FunctionalInterface
public interface FieldSetFilter {
	public boolean filterElement(WebElement element);
}
