package net.softwrench.features.helpers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

public interface GridHelper {

	public abstract List<String> getResults(int column);

	public abstract int getNrOfPages();

	public abstract void goToPage(int pagenr);

	public abstract void clickOnRow(int rownumber);

	public abstract int getNrOfRowsOnPage();

	public abstract int getNrOfRecordsOnPage();

}