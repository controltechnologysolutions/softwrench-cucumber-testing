package net.softwrench.features.helpers.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import net.softwrench.features.exceptions.NoSuchColumnException;
import net.softwrench.features.exceptions.NoSuchFilterException;
import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.FilterHelper;

import org.openqa.selenium.By;
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
public class FilterHelperImpl implements FilterHelper {

	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private AngularHelper angularHelper;
	
	private ByAngular byAngular;

	private List<WebElement> filterInputs;
	
	@PostConstruct
	public void init() {
		byAngular = new ByAngular(driver);
	}
	
	/* (non-Javadoc)
	 * @see net.softwrench.features.helpers.impl.FilterHelper#getNrOfFilters(int)
	 */
	@Override
	public int getNrOfFilters(int columnnr) throws NoSuchColumnException {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		List<WebElement> filters = getFilters(columnnr);
		return filters.size();
	}
	
	@Override
	public boolean selectFilter(int columnnr, int filternr) throws NoSuchColumnException, NoSuchFilterException {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		List<WebElement> filters = getFilters(columnnr);
		
		if (filters.size() < filternr)
			throw new NoSuchFilterException("There is no filter at position " + filternr);
		
		WebElement filter = filters.get(filternr - 1);
		WebElement parent = filter.findElement(By.xpath(".//.."));
		
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		parent.findElement(By.xpath(".//preceding-sibling::span[1]")).click();
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (!filter.isDisplayed())
			return false;
		
		filter.click();
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		return true;
	}
	
	@Override
	public boolean selectFilter(int columnnr, String filtername) throws NoSuchColumnException, NoSuchFilterException {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		List<WebElement> filters = getFilters(columnnr);
		
		WebElement filter = null;
		for (int i = 0; i < filters.size(); i++ ) {
			
			String currentFiltername = getFilterName(columnnr, i + 1);
			
			if (currentFiltername != null && currentFiltername.equals(filtername))
				filter = filters.get(i);
		}
		
		if (filter == null)
			throw new NoSuchFilterException("There exists no filter with name " + filtername + ".");
		
		WebElement parent = filter.findElement(By.xpath(".//.."));
		
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		parent.findElement(By.xpath(".//preceding-sibling::span[1]")).click();
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		if (!filter.isDisplayed())
			return false;
		
		filter.click();
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		return true;
	}
	
	@Override
	public String getFilterName(int columnnr, int filternr) throws NoSuchColumnException, NoSuchFilterException {
		List<WebElement> filters = getFilters(columnnr);

		if (filters.size() < filternr)
			throw new NoSuchFilterException("There is no filter at position " + filternr);
		
		WebElement filter = filters.get(filternr - 1);
		
		AngularModelAccessor ngModel = new AngularModelAccessor(driver);
		try {
			return ngModel.retrieveAsString(filter, "searchOperations()[" + (filternr-1) + "].id");
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	private List<WebElement> getFilters(int columnnr) throws NoSuchColumnException {
		filterInputs = getFilterInputFields();
		
		if (filterInputs.size() < columnnr) {
			throw new NoSuchColumnException("Column nr " + columnnr + " is not visible or does not exist.");
		}
		
		WebElement column = filterInputs.get(columnnr - 1);
		WebElement parent = column.findElement(By.xpath(".//.."));
		return parent.findElements(byAngular.repeater("operation in searchOperations()"));
	}
	
	/* (non-Javadoc)
	 * @see net.softwrench.features.helpers.impl.FilterHelper#getFilterInputFields()
	 */
	@Override
	public List<WebElement> getFilterInputFields() {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		WebElement header = driver.findElement(By.id("listgridthread"));
		List<WebElement> searchInputs = header.findElements(By.xpath(".//input[@type='search']"));
		searchInputs = angularHelper.applyFilter(searchInputs, we -> we.isDisplayed());
		
		return searchInputs;
	}
}
