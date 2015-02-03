package net.softwrench.features.sr.contexts;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class DialogSelection {

	private String columnA;
	private String columnB;
	
	public String getColumnA() {
		return columnA;
	}
	public void setColumnA(String columnA) {
		this.columnA = columnA;
	}
	public String getColumnB() {
		return columnB;
	}
	public void setColumnB(String columnB) {
		this.columnB = columnB;
	}
}
