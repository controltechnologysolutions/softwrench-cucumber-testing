package net.softwrench.jira;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cucumber.api.Scenario;

public class ResultProvider {

	public static final ResultProvider INSTANCE = new ResultProvider();
	
	private List<ScenarioResult> results;
	
	private ResultProvider() {
		results = new ArrayList<ScenarioResult>();
	}
	
	public void addScenario(Scenario sScenario) {
		ScenarioResult scenario = new ScenarioResult(sScenario.getName(), sScenario.getSourceTagNames());
		if (results.contains(scenario)) {
			scenario = results.get(results.indexOf(scenario));
		}
		else {
			results.add(scenario);
		}
		
		scenario.addToRuns();
	}
	
	public List<ScenarioResult> getResults() {
		return Collections.unmodifiableList(results);
	}
	
	public void addTestInfo(Scenario sScenario, String msg, String stacktrace, List<byte[]> images) {
		FailedTestInfo info= new FailedTestInfo(msg, images, stacktrace);
		
		ScenarioResult scenario = new ScenarioResult(sScenario.getName(), sScenario.getSourceTagNames());
		scenario = results.get(results.indexOf(scenario));
		scenario.addFailedTest(info);
		if (!sScenario.getStatus().equals("passed"))
			scenario.setFailed();
		
	}
}
