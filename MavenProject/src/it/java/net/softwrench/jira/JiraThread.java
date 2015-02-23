package net.softwrench.jira;

import java.util.List;

public class JiraThread extends Thread {
	
	private JiraIssueCreator jiraCreator;
	
	public JiraThread(JiraIssueCreator creator) {
		jiraCreator = creator;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		List<ScenarioResult> results = ResultProvider.INSTANCE.getResults();
		
		for (ScenarioResult result : results) {
			if (!result.hasPassed())
				jiraCreator.createJiraIssue(result);
		}
		
	}

}
