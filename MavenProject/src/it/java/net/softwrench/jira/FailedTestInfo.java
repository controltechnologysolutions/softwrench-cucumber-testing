package net.softwrench.jira;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FailedTestInfo {

	private StringBuffer message;
	private List<byte[]> images;
	private StringBuffer stacktrace;
	
	public FailedTestInfo(String msg, List<byte[]> scenarioImages, String scenarioStacktrace) {
		message = new StringBuffer();
		if (msg != null)
			message.append(msg);
		
		stacktrace = new StringBuffer();
		if (scenarioStacktrace != null)
			stacktrace.append(scenarioStacktrace);
		
		images = new ArrayList<byte[]>();
		if (images != null)
			images.addAll(scenarioImages);
	}
	
	public void addText(String msg) {
		message.append(msg);
		message.append("\n");
	}
	
	public void addImage(byte[] image) {
		images.add(image);
	}
	
	public void addException(String exception) {
		stacktrace.append(exception);
	}

	@Override
	public String toString() {
		return "FailedTestInfo [message=" + message + ", stacktrace="
				+ stacktrace + "]";
	}
	
	public String getText() {
		return message.toString();
	}
	
	public List<byte[]> getImages() {
		return Collections.unmodifiableList(images);
	}
	
	public String getStacktrace() {
		return stacktrace.toString();
	}
}
