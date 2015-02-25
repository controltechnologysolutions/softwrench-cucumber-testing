package net.softwrench.features.helpers;

import net.softwrench.features.exceptions.UnexpectedErrorMessageException;
import cucumber.api.Scenario;

public interface MessageDetector {

	/**
	 * Detect if there are any error message displayed on the current page.
	 * @return
	 */
	public abstract ErrorMessage detectErrorMessage();

	public abstract void checkForErrorMessage(String page, String steps, Scenario scenario)
			throws UnexpectedErrorMessageException;

}