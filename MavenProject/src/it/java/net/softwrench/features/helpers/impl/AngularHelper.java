package net.softwrench.features.helpers.impl;

import org.openqa.selenium.WebElement;

public interface AngularHelper {

	public abstract WebElement getFieldSetFromInputForm(String attributeName);

	public abstract WebElement getCrudInputMainElement();

}