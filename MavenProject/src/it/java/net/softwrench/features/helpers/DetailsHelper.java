package net.softwrench.features.helpers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

public interface DetailsHelper {

	public abstract WebElement findTab(String tabId);

	public abstract WebElement findButton(WebElement root, String buttontype);

}