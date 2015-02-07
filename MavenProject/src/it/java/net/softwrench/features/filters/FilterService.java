package net.softwrench.features.filters;

public interface FilterService {

	public abstract boolean isCorrect(String operator, String text,
			String filterstring);

}