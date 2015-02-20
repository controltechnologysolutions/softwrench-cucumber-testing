package net.softwrench.features.helpers;

public interface MessageDetector {

	/**
	 * Detect if there are any error message displayed on the current page.
	 * @return
	 */
	public abstract ErrorMessage detectErrorMessage();

}