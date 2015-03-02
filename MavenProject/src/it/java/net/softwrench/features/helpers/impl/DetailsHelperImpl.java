package net.softwrench.features.helpers.impl;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import net.softwrench.features.exceptions.ElementNotShownException;
import net.softwrench.features.exceptions.NoSuchElementException;
import net.softwrench.features.exceptions.TextCouldNotBeFoundException;
import net.softwrench.features.helpers.DetailsHelper;
import net.softwrench.features.sr.contexts.CreationContext;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.paulhammant.ngwebdriver.AngularModelAccessor;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

@Component
@Scope("cucumber-glue")
public class DetailsHelperImpl implements DetailsHelper {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private CreationContext creationContext;
	
	private ByAngular byAngular;
	
	private WebElement crudBody;
	
	private static final Logger logger = Logger.getLogger(DetailsHelperImpl.class);
	
	
	@PostConstruct
	public void init() {
		byAngular = new ByAngular(driver);
	}

	/* (non-Javadoc)
	 * @see net.softwrench.features.helpers.impl.DetailsHelper#findTab(java.lang.String)
	 */
	@Override
	public WebElement findTab(String tabId) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (crudBody == null)
			crudBody = driver.findElement(By.id("crudbodyform"));
		
		List<WebElement> tabs = crudBody.findElements(byAngular.repeater("tab in tabsDisplayables(schema)"));
		
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement tab : tabs) {
			String retrievedTabid = ngModel.retrieveAsString(tab, "tab.tabId");
			if (tabId.equals(retrievedTabid))
				return tab;
		}		
		
		return null;
	}
	
	@Override
	public WebElement findButton(WebElement root, String buttonId) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (root == null)
			root = crudBody == null? driver.findElement(By.id("crudbodyform")): crudBody;
		
		List<WebElement> commands = root.findElements(byAngular.repeater("command in getGridToolbar()"));
		
		WebElement selecteCmd = null;
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		for (WebElement cmd : commands) {
			String retrievedCommandid = ngModel.retrieveAsString(cmd, "command.id");
			if (retrievedCommandid.equals(buttonId))
				selecteCmd = cmd;
		}		
		
		return selecteCmd;
	}
	
	@Override
	public void clickOnTab(String tabid) throws NoSuchElementException {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement tab = findTab(tabid);
	    
	    if (tab == null)
	    	throw new NoSuchElementException("Can't find tab " + tabid);
	    
	    tab.findElement(By.tagName("a")).click();
	}
	
	/**
	 * Method for clicking an an add new item button (e.g. add new worklog or attachment). This method
	 * looks for the specified button and clicks it. Then it looks for an element with id 
	 * crudInputNewItemComposition and makes sure the element is displayed. 
	 * @param button The command id of the button to click.
	 * @throws NoSuchElementException
	 * @throws ElementNotShownException
	 */
	@Override
	public void clickOnNewItemButton(String button) throws NoSuchElementException, ElementNotShownException {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement addButton = findButton(null, button);
		
		if (addButton == null)
			throw new NoSuchElementException("Can't find button additem.");
		
		addButton.findElement(By.tagName("i")).click();
		
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		creationContext.setNewItemComposition(driver.findElement(By.id("crudInputNewItemComposition")));
		if (!creationContext.getNewItemComposition().isDisplayed())
			throw new ElementNotShownException("Form to add new item is not displayed.");
	}
	
	@Override
	public HeaderInfo getRecordNr(String recordPrefix, String summaryPrefix) throws TextCouldNotBeFoundException {
		WebDriverWait wait = new WebDriverWait(driver, 20); // wait for a maximum of 5 seconds
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id = 'crudbodyform']/ng-switch/div/div[2]/h5")));
		
		List<WebElement> cells = driver.findElements(By.xpath("//form[@id = 'crudbodyform']/ng-switch/div/div[2]/h5"));
		if (cells.isEmpty())
			assertTrue("No header element found.", false);
		
		String headerTitle = cells.get(0).getText().trim();
		logger.info("Header title " + headerTitle);
		
		Pattern pattern = Pattern.compile(recordPrefix + " ([A-z0-9]+).+?" + summaryPrefix + " (.+)");
		
		Matcher matcher = pattern.matcher(headerTitle);
		boolean matches = matcher.matches();
		
		if (!matches)
			throw new TextCouldNotBeFoundException("Couldn't find SR nr in header: " + headerTitle);
		
		return new HeaderInfo(matcher.group(1),  matcher.group(2));
	}
		
}
