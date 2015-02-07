package net.softwrench.features.sr.util;

import java.util.List;

public class SearchResults {

	private int numberOfResultsDisplayed;
	private List<String> results;
	
	public SearchResults(int numberOfResultsDisplayed, List<String> results) {
		super();
		this.numberOfResultsDisplayed = numberOfResultsDisplayed;
		this.results = results;
	}
	public int getNumberOfResultsDisplayed() {
		return numberOfResultsDisplayed;
	}
	public void setNumberOfResultsDisplayed(int numberOfResultsDisplayed) {
		this.numberOfResultsDisplayed = numberOfResultsDisplayed;
	}
	public List<String> getResults() {
		return results;
	}
	public void setResults(List<String> results) {
		this.results = results;
	}
}
