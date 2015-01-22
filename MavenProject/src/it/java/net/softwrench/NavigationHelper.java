package net.softwrench;

import org.openqa.selenium.WebDriver;

public interface NavigationHelper {

	public abstract void goToSRGrid(WebDriver driver);

	public abstract void makeSureImLoggedIn(String username, String password,
			WebDriver driver);

	public void makeSureImLoggedIn(WebDriver driver);

}