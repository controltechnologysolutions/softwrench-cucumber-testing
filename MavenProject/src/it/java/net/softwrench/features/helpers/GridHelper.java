package net.softwrench.features.helpers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.paulhammant.ngwebdriver.WaitForAngularRequestsToFinish;

public interface GridHelper {

	public abstract List<String> getResults(int column);

}