package net.softwrench.features.helpers.impl;

import java.util.List;

import javax.annotation.PostConstruct;

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
	public WebElement getCrudInputMainElement() {
		if (inputMain == null)
			inputMain = driver.findElements(By.xpath("//*[@elementid='crudInputMain']")).get(0);
		
		return inputMain;
	}
}
