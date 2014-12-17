package net.softwrench;

import java.util.List;

import net.softwrench.util.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface NavigationHelper {

	public abstract void goToSRGrid(WebDriver driver);

	public abstract void makeSureImLoggedIn(String username, String password,
			WebDriver driver);

}