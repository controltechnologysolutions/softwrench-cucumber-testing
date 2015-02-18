package net.softwrench.features.helpers.impl;

import java.util.ArrayList;
import java.util.List;

import net.softwrench.features.helpers.AngularHelper;
import net.softwrench.features.helpers.GridHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

@Component
@Scope("cucumber-glue")
public class GridHelperImpl implements GridHelper {
	
	@Autowired
	private RemoteWebDriver driver;
	
	@Autowired
	private AngularHelper angularHelper;
	
	@Value( "${sr.grid.oftotalpages.label}" )
	private String ofTotalLabel;

	@Override
	public List<String> getResults(int column) {
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
}
