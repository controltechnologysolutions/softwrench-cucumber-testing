package net.softwrench.features.helpers.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import net.softwrench.features.functinter.WebElementFilter;
import net.softwrench.features.helpers.AngularHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

@Component
@Scope("cucumber-glue")
public class AngularHelperImpl implements AngularHelper {

	@Autowired
	private RemoteWebDriver driver;
	
	private ByAngular byAngular;
	
	private WebElement inputMain;
	
	@PostConstruct
	public void init() {
		byAngular = new ByAngular(driver);
	}
	
	/* (non-Javadoc)
	 * @see net.softwrench.features.helpers.impl.AngularHelper#getFieldSetFromInputForm(java.lang.String)
	 */
	@Override
	public WebElement getFieldSetFromInputForm(String attributeName) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (inputMain == null)
			inputMain = driver.findElements(By.xpath("//*[@elementid='crudInputMain']")).get(0);
			
		List<WebElement> fieldRepeat = inputMain.findElements(byAngular.repeater("fieldMetadata in nonTabFields(displayables)"));
		
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement fieldset : fieldRepeat) {
			String attribute = null;
			try {
				// I think we can't test if attribute is there, so need to catch the exception
				attribute = ngModel.retrieveAsString(fieldset, "fieldMetadata.attribute");
			} catch (WebDriverException ex) {
				continue;
			}
			
			if (attribute.equals(attributeName)) 
				return fieldset;
		}
		
		return null;
	}
	
	@Override
	public List<WebElement> filterFieldSets(WebElementFilter filter) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (inputMain == null)
			inputMain = driver.findElements(By.xpath("//*[@elementid='crudInputMain']")).get(0);
			
		List<WebElement> fieldRepeat = inputMain.findElements(byAngular.repeater("fieldMetadata in nonTabFields(displayables)"));
		
		return applyFilter(fieldRepeat, filter);
	}
	
	@Override
	public WebElement getCrudInputMainElement() {
		if (inputMain == null)
			inputMain = driver.findElements(By.xpath("//*[@elementid='crudInputMain']")).get(0);
		
		return inputMain;
	}
	
	@Override
	public List<WebElement> applyFilter(List<WebElement> list, WebElementFilter filter) {
		List<WebElement> filteredFieldSets = new ArrayList<WebElement>();
		for (WebElement fieldSet : list) {
			if (filter.filterElement(fieldSet))
				filteredFieldSets.add(fieldSet);
		}
		
		return filteredFieldSets;
	}
	
	/**
	 * Method to retrieve a list of repeated elements (using Angular's repeat function).
	 * @param root The element from which to start looking for the given repeater string.
	 * In case root is null, this method uses the driver's findElement function.
	 * @param repeaterString The repeater string to look for (e.g. fieldMetadata in nonTabFields(displayables)).
	 * @return A list of WebElements that are found for the repeater string.
	 */
	@Override
	public List<WebElement> getRepeatedElements(WebElement root, String repeaterString) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (root == null)
			return driver.findElements(byAngular.repeater(repeaterString));
		
		return root.findElements(byAngular.repeater(repeaterString));
	}
}
