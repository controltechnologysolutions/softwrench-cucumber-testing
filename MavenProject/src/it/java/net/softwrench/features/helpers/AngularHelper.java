package net.softwrench.features.helpers;

import java.util.ArrayList;
import java.util.List;

import net.softwrench.features.functinter.WebElementFilter;

import org.openqa.selenium.WebElement;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

public interface AngularHelper {

	public abstract WebElement getFieldSetFromInputForm(String attributeName);

	public abstract WebElement getCrudInputMainElement();

	public abstract List<WebElement> filterFieldSets(WebElementFilter filter);

	public abstract List<WebElement> applyFilter(List<WebElement> list, WebElementFilter filter);

	public abstract List<WebElement> getRepeatedElements(WebElement root, String repeaterString);

}