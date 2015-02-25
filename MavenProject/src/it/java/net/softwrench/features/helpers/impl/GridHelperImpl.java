package net.softwrench.features.helpers.impl;

import java.util.ArrayList;
import java.util.List;

import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.GridHelper;
import net.softwrench.features.sr.contexts.SRDetailStepContext;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

import cucumber.api.PendingException;

@Component
@Scope("cucumber-glue")
public class GridHelperImpl implements GridHelper {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private AngularHelper angularHelper;
	

	@Autowired
	private SRDetailStepContext context;
	
	
	@Value( "${sr.grid.oftotalpages.label}" )
	private String ofTotalLabel;

	@Override
	public List<String> getResults(int column) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement grid = driver.findElement(By.id("listgrid"));
		WebElement tbody = grid.findElement(By.tagName("tbody"));
		
		WebElement paginationHeader = driver.findElement(By.id("affixpagination"));
		
		int totalPages = getNrOfPages();
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
		
		paginationHeader.findElement(By.xpath(".//a[@ng-click='selectPage(1)']")).click();
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		return results;
	}
	
	@Override
	public void goToPage(int pagenr) {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		WebElement paginationHeader = driver.findElement(By.id("affixpagination"));
		WebElement pageInput = paginationHeader.findElement(By.xpath(".//input[@ng-model='paginationData.pageNumber']"));
		pageInput.clear();
		pageInput.sendKeys(pagenr + "" + Keys.ENTER);
	}
	
	@Override
	public int getNrOfPages() {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		WebElement paginationHeader = driver.findElement(By.id("affixpagination"));
		WebElement currentPageElement = paginationHeader.findElement(By.xpath(".//input[@ng-model='paginationData.pageNumber']"));
		WebElement ofLabel = currentPageElement.findElement(By.xpath(".//following-sibling::label[1]"));
		
		int totalPages = new Integer(ofLabel.getText().substring(ofTotalLabel.length()).trim());
		return totalPages;
	}
	
	@Override
	public int getNrOfRowsOnPage() {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		WebElement grid = driver.findElement(By.id("listgrid"));
		WebElement tbody = grid.findElement(By.tagName("tbody"));
		List<WebElement> rows = tbody.findElements(By.tagName("tr"));
		return rows.size();
	}
	
	@Override
	public void clickOnRow(int rownumber) {
		List<WebElement> cells = driver.findElements(By.xpath("//tbody/tr[" + rownumber + "]/td[3]"));
		if (cells.size() > 0) {
			WebElement idCell = cells.get(0);
			// get id of SR
			context.setSelectedId(idCell.getText());
			// get title of SR
			WebElement titleCell = driver.findElement(By.xpath("//tbody/tr[" + rownumber + "]/td[4]"));
			context.setSelectedTitle(titleCell.getText());
			
			idCell.click();
		}
		else
			throw new PendingException("No data row " + rownumber + ".");
	}
	
	@Override
	public int getNrOfRecordsOnPage() {
		WaitForAngularRequestsToFinish.waitForAngularRequestsToFinish(driver);
		
		WebElement paginationHeader = driver.findElement(By.id("affixpagination"));
		WebElement pageSize = paginationHeader.findElement(By.id("pagesize"));
		int pageSizeInt = new Integer(pageSize.getAttribute("value"));
		return pageSizeInt;
	}
}
