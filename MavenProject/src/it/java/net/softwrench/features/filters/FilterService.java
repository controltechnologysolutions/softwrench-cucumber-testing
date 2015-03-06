package net.softwrench.features.filters;

public interface FilterService {
	
	public final static String CONTAINS = "CONTAINS";
	public final static String NCONTAINS = "NCONTAINS";
	public final static String STARTWITH = "STARTWITH";
	public final static String ENDWITH = "ENDWITH";
	public final static String EQ = "EQ";
	public final static String NEQ = "NOTEQ";
	public final static String BLANK = "BLANK";
	public final static String GT = "GT";
	public final static String LT = "LT";
	public final static String GTE = "GTE";
	public final static String LTE = "LTE";

	public abstract boolean isCorrect(String operator, String text,
			String filterstring);

}