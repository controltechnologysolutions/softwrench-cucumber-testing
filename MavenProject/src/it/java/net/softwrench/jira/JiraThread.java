package net.softwrench.jira;

import java.util.List;

public class JiraThread extends Thread {
	
	private JiraIssueCreator jiraCreator;
	private String createIssues;
	
	public JiraThread(JiraIssueCreator creator, String createIssues) {
		jiraCreator = creator;
		this.createIssues = createIssues;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		if (createIssues.trim().equals("yes")) {
			List<ScenarioResult> results = ResultProvider.INSTANCE.getResults();
			
			for (ScenarioResult result : results) {
				if (!result.hasPassed())
					jiraCreator.createJiraIssue(result);
			}
		}
	}

}
