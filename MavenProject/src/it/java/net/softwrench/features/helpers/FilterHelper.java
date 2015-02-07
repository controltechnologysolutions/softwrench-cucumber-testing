package net.softwrench.features.helpers;

import java.util.List;

import net.softwrench.features.exceptions.NoSuchColumnException;
import net.softwrench.features.exceptions.NoSuchFilterException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

public interface FilterHelper {

	public abstract int getNrOfFilters(int columnnr) throws NoSuchColumnException ;

	public abstract List<WebElement> getFilterInputFields();

	public abstract boolean selectFilter(int columnnr, int filternr)
			throws NoSuchColumnException, NoSuchFilterException;

	public abstract String getFilterName(int columnnr, int filternr)
			throws NoSuchColumnException, NoSuchFilterException;

}