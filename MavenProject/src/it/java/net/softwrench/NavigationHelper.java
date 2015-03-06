package net.softwrench;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface NavigationHelper {

	public abstract void goToSRGrid(RemoteWebDriver driver);

	public abstract void makeSureImLoggedIn(String username, String password,
			WebDriver driver);

	public void makeSureImLoggedIn(RemoteWebDriver driver);

}