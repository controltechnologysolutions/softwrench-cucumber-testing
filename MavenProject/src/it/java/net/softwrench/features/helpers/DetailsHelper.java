package net.softwrench.features.helpers;

import net.softwrench.features.exceptions.ElementNotShownException;
import net.softwrench.features.exceptions.NoSuchElementException;
import net.softwrench.features.exceptions.TextCouldNotBeFoundException;

import org.openqa.selenium.WebElement;

public interface DetailsHelper {

	public abstract WebElement findTab(String tabId);

	public abstract WebElement findButton(WebElement root, String buttontype);

	public abstract void clickOnTab(String tabid)
			throws NoSuchElementException;

	public abstract void clickOnNewItemButton(String button)
			throws NoSuchElementException, ElementNotShownException;

	public abstract HeaderInfo getRecordNr(String recordPrefix, String summaryPrefix)
			throws TextCouldNotBeFoundException;

	class HeaderInfo {
		public String recordNr;
		public String summary;
		
		public HeaderInfo(String recordNr, String summary) {
			this.recordNr = recordNr;
			this.summary = summary;
		}
	}
}