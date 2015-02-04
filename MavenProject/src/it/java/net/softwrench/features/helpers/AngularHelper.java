package net.softwrench.features.helpers;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface AngularHelper {

	public abstract WebElement getFieldSetFromInputForm(String attributeName);

	public abstract WebElement getCrudInputMainElement();

	public abstract List<WebElement> filterFieldSets(FieldSetFilter filter);

}