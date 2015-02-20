package net.softwrench.jira;

public class JiraThread extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		System.out.println("Results: " + ResultProvider.INSTANCE.getResults());
	}

}
