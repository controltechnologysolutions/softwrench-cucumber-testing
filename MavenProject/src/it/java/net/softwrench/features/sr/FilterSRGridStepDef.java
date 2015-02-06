package net.softwrench.features.sr;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.Reporter;
import net.softwrench.features.helpers.WebElementFilter;
import net.softwrench.features.sr.contexts.SearchResultsContext;

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
	private Reporter reporter;
	
	@Autowired
	private SearchResultsContext searchResultContext;
	
	@Value( "${sr.grid.oftotalpages.label}" )
	private String ofTotalLabel;
	
	@Value( "${sr.grid.totalitems.label}" )
	private String totalItemsLabel;

	@When("^I enter '(.+?)' into column '(\\d+)'$")
	public void i_enter_into_column(String filterstring, int columnnr) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		WebElement header = driver.findElement(By.id("listgridthread"));
		List<WebElement> searchInputs = header.findElements(By.xpath(".//input[@type='search']"));
		searchInputs = angularHelper.applyFilter(searchInputs, we -> we.isDisplayed());
		
		if (searchInputs.size() < columnnr) {
			reporter.takeScreenshot("Column nr " + columnnr + " does not exist.");
			fail("Column nr " + columnnr + " does not exist.");
			return;
		}
		
		WebElement filterField = searchInputs.get(columnnr - 1);
		filterField.sendKeys(filterstring);
		filterField.sendKeys(Keys.ENTER);
	}

	@Then("^I should see only records in the grid that contain '(.+?)' in column '(\\d+)'$")
	public void i_should_see_only_records_in_the_grid_that_contain_in_column(String filterstring, int column) throws Throwable {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement grid = driver.findElement(By.id("listgrid"));
		WebElement tbody = grid.findElement(By.tagName("tbody"));
		
		
		
		int resultNr = 0;
		
		WebElement paginationHeader = driver.findElement(By.id("affixpagination"));
		WebElement currentPageElement = paginationHeader.findElement(By.xpath(".//input[@ng-model='paginationData.pageNumber']"));
		WebElement ofLabel = currentPageElement.findElement(By.xpath(".//following-sibling::label[1]"));
		
		int totalPages = new Integer(ofLabel.getText().substring(ofTotalLabel.length()).trim());
		
		// iterate over all pages
		for (int page = 1; page <= totalPages; page++) {
			WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
			
			// go through all results on the page
			List<WebElement> rows = tbody.findElements(By.tagName("tr"));
			if (rows.isEmpty()) {
				reporter.takeScreenshot();
				fail("There are no results for column " + column + " and filterstring " + filterstring);
				return;
			}
			
			for (WebElement row : rows) {
				resultNr++;
				List<WebElement> cells = angularHelper.applyFilter(row.findElements(By.tagName("td")), we -> we.isDisplayed());
				WebElement cell = cells.get(column - 1);
				
				// TODO adjust this for testing all filters
				assertTrue("The result (" + cell.getText() + ") does not contain " + filterstring + ".",cell.getText().toLowerCase().contains(filterstring.toLowerCase()));
			}
			
			paginationHeader.findElement(By.xpath(".//a[@ng-click='nextPage()']")).click();
		}
		searchResultContext.setNumberOfResults(resultNr);
	}

	@Then("^the number of records shown equals the number shown for 'Totel Items'$")
	public void the_number_of_records_shown_equals_the_number_shown_for_Totel_Items() throws Throwable {
	    WebElement topbar = driver.findElement(By.id("affixpagination"));
	    WebElement totalDiv = topbar.findElement(By.xpath(".//div[@class='group total']"));
	    String totalItemsString = totalDiv.getText().trim();
	    
	    String itemsNr = totalItemsString.substring(totalItemsLabel.length());
	    assertTrue("Number of rows in grid is different than the number displayed for 'Total Items.' " + searchResultContext.getNumberOfResults() + " != " + itemsNr, searchResultContext.getNumberOfResults() == new Integer(itemsNr.trim()));
	}
}
