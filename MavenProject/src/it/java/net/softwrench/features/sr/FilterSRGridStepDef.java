package net.softwrench.features.sr;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.softwrench.features.filters.FilterService;
import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.FilterHelper;
import net.softwrench.features.helpers.Reporter;
import net.softwrench.features.sr.util.SearchResults;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FilterSRGridStepDef {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private AngularHelper angularHelper;
	
	@Autowired
	private FilterHelper filterHelper;
	
	@Autowired
	private FilterService filterService;
	
	@Autowired
	private Reporter reporter;
	
	private Map<String, SearchResults> resultsPerFilter;
	
	@Value( "${sr.grid.oftotalpages.label}" )
	private String ofTotalLabel;
	
	@Value( "${sr.grid.totalitems.label}" )
	private String totalItemsLabel;

	@When("^I enter '(.+?)' into column '(\\d+)'$")
	public void i_enter_into_column(String filterstring, int columnnr) throws Throwable {
		resultsPerFilter = new HashMap<String, SearchResults>();
		
		
		
		
		int filterNr = filterHelper.getNrOfFilters(columnnr);
		
		// select all filter
		for (int i = 2; i <= filterNr; i++) {
			List<WebElement> searchInputs = filterHelper.getFilterInputFields();
			
			if (searchInputs.size() < columnnr) {
				reporter.takeScreenshot("Column nr " + columnnr + " does not exist.");
				fail("Column nr " + columnnr + " does not exist.");
				return;
			}
			WebElement filterField = searchInputs.get(columnnr - 1);
			
			filterField.clear();
			filterField.sendKeys(filterstring);
			if (!filterHelper.selectFilter(columnnr, i))
				continue;
			
			// without this there is a not attached to dom exception
			searchInputs = filterHelper.getFilterInputFields();
			filterField = searchInputs.get(columnnr - 1);
			
			filterField.sendKeys(Keys.ENTER);
			String filtername = filterHelper.getFilterName(columnnr, filterNr);
			
			if (filtername != null)
				resultsPerFilter.put(filtername, new SearchResults(getShownTotelItemsNr(), getResults(columnnr)));
		}
	}
	
	private List<String> getResults(int column) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement grid = driver.findElement(By.id("listgrid"));
		WebElement tbody = grid.findElement(By.tagName("tbody"));
		
		WebElement paginationHeader = driver.findElement(By.id("affixpagination"));
		WebElement currentPageElement = paginationHeader.findElement(By.xpath(".//input[@ng-model='paginationData.pageNumber']"));
		WebElement ofLabel = currentPageElement.findElement(By.xpath(".//following-sibling::label[1]"));
		
		int totalPages = new Integer(ofLabel.getText().substring(ofTotalLabel.length()).trim());
		List<String> results = new ArrayList<String>();
		// iterate over all pages
		for (int page = 1; page <= totalPages; page++) {
			WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
			
			// go through all results on the page
			List<WebElement> rows = tbody.findElements(By.tagName("tr"));
			
			for (WebElement row : rows) {
				List<WebElement> cells = angularHelper.applyFilter(row.findElements(By.tagName("td")), we -> we.isDisplayed());
				WebElement cell = cells.get(column - 1);
				
				results.add(cell.getText());
			}
			
			paginationHeader.findElement(By.xpath(".//a[@ng-click='nextPage()']")).click();
		}
		return results;
	}
	
	private int getShownTotelItemsNr() {
		WebElement topbar = driver.findElement(By.id("affixpagination"));
		WebElement totalDiv = topbar.findElement(By.xpath(".//div[@class='group total']"));
		String totalItemsString = totalDiv.getText().trim();
		    
		String itemsNr = totalItemsString.substring(totalItemsLabel.length());
		return new Integer(itemsNr.trim());
	}

	@Then("^I should see only records in the grid that contain '(.+?)' in column '(\\d+)'$")
	public void i_should_see_only_records_in_the_grid_that_contain_in_column(String filterstring, int column) throws Throwable {
		for (String operator : resultsPerFilter.keySet()) {
			for (String result : resultsPerFilter.get(operator).getResults()) {
				assertTrue("The result (" + result + ") does not " + operator + " " + filterstring + ".", filterService.isCorrect(operator, result, filterstring));
			}
		}
	}

	@Then("^the number of records shown equals the number shown for 'Totel Items'$")
	public void the_number_of_records_shown_equals_the_number_shown_for_Totel_Items() throws Throwable {
		for (String operator : resultsPerFilter.keySet()) {
			assertTrue("Number of rows in grid is different than the number displayed for 'Total Items.' " + resultsPerFilter.get(operator).getResults().size() + " != " + resultsPerFilter.get(operator).getNumberOfResultsDisplayed(), resultsPerFilter.get(operator).getNumberOfResultsDisplayed() == resultsPerFilter.get(operator).getResults().size());
		}
	}
}
