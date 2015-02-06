package net.softwrench.features.sr.contexts;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class SearchResultsContext {

	private int numberOfResults;

	public int getNumberOfResults() {
		return numberOfResults;
	}

	public void setNumberOfResults(int numberOfResults) {
		this.numberOfResults = numberOfResults;
	}
	
}
