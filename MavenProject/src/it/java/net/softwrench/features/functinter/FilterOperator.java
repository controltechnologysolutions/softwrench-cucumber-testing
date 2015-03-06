package net.softwrench.features.functinter;

@FunctionalInterface
public interface FilterOperator {
	public boolean isCorrectResult(String text, String filterstring);
}
