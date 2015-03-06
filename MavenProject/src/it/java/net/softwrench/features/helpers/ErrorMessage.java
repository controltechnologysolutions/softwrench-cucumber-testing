package net.softwrench.features.helpers;

public class ErrorMessage {

	private String title;
	private String stacktrace;
	
	public ErrorMessage(String title, String stacktrace) {
		this.title = title;
		this.stacktrace = stacktrace;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStacktrace() {
		return stacktrace;
	}
	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}	
}
