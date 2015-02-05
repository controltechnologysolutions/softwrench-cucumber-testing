package net.softwrench.features.helpers;

import org.openqa.selenium.OutputType;

public interface Reporter {

	public abstract void takeScreenshot(String msg);

	public abstract void addMessage(String msg);

	public abstract void takeScreenshot();

}