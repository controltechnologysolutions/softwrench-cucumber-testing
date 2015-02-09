package net.softwrench.features.filters.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.softwrench.features.filters.FilterService;
import net.softwrench.features.functinter.FilterOperator;

@Component
@Scope("cucumber-glue")
public class FilterServiceImpl implements FilterService {
	
	private Map<String, FilterOperator> filterOperators;
	
	@PostConstruct
	public void init() {
		filterOperators = new HashMap<String, FilterOperator>();
		filterOperators.put(CONTAINS, (String text, String filterstring) -> text.toLowerCase().contains(filterstring.toLowerCase()));
		filterOperators.put(NCONTAINS, (String text, String filterstring) -> !text.toLowerCase().contains(filterstring.toLowerCase()));
		filterOperators.put(STARTWITH, (String text, String filterstring) -> text.toLowerCase().startsWith(filterstring.toLowerCase()));
		filterOperators.put(ENDWITH, (String text, String filterstring) -> text.toLowerCase().endsWith(filterstring.toLowerCase()));
		filterOperators.put(EQ, (String text, String filterstring) -> text.toLowerCase().equals(filterstring.toLowerCase()));
		filterOperators.put(NEQ, (String text, String filterstring) -> !text.toLowerCase().equals(filterstring.toLowerCase()));
		filterOperators.put(BLANK, (String text, String filterstring) -> text.toLowerCase().equals(""));
		filterOperators.put(GT, (String text, String filterstring) -> text.toLowerCase().compareTo(filterstring.toLowerCase()) > 0);
		filterOperators.put(LT, (String text, String filterstring) -> text.toLowerCase().compareTo(filterstring.toLowerCase()) < 0);
		filterOperators.put(GTE, (String text, String filterstring) -> text.toLowerCase().compareTo(filterstring.toLowerCase()) >= 0);
		filterOperators.put(LTE, (String text, String filterstring) -> text.toLowerCase().compareTo(filterstring.toLowerCase()) <= 0);
	}
	
	/* (non-Javadoc)
	 * @see net.softwrench.features.filters.FilterService#isCorrect(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isCorrect(String operator, String text, String filterstring) {
		FilterOperator operatorMethod = filterOperators.get(operator);
		if (operatorMethod == null)
			return false;
		
		return operatorMethod.isCorrectResult(text, filterstring);
	}
}
