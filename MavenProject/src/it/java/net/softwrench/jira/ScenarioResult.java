package net.softwrench.jira;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScenarioResult {

	private String scenarioName;
	private int runs;
	private boolean passed;
	private Collection<String> tags;
	private List<FailedTestInfo> failedTests;
	private String id;
	
	public ScenarioResult(String scenarioName, Collection<String> tags, String id) {
		this.scenarioName = scenarioName;
		this.tags = tags;
		this.id = id;
		runs = 0;
		passed = true;
		failedTests = new ArrayList<FailedTestInfo>();
	}
	
	public String getScenarioName() {
		return scenarioName;
	}
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
	
	public void addToRuns() {
		runs++;
	}
	
	public void setFailed() {
		passed = false;
	}
	
	public boolean hasPassed() {
		return passed;
	}
	
	public int getRuns() {
		return runs;
	}

	public Collection<String> getTags() {
		return tags;
	}

	public void setTags(Collection<String> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((scenarioName == null) ? 0 : scenarioName.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScenarioResult other = (ScenarioResult) obj;
		if (scenarioName == null) {
			if (other.scenarioName != null)
				return false;
		} else if (!scenarioName.equals(other.scenarioName))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScenarioResult [scenarioName=" + scenarioName + ", runs="
				+ runs + ", passed=" + passed + ", tags=" + tags + ", Infos=" + failedTests + "]";
	}
	
	
	public void addFailedTest(FailedTestInfo info) {
		failedTests.add(info);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public List<FailedTestInfo> getFailedTestInfos() {
		return Collections.unmodifiableList(failedTests);
	}
}
