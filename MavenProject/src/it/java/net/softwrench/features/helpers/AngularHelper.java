package net.softwrench.features.helpers;

import org.openqa.selenium.WebElement;

public interface AngularHelper {

	public abstract WebElement getFieldSetFromInputForm(String attributeName);

	public abstract WebElement getCrudInputMainElement();

}