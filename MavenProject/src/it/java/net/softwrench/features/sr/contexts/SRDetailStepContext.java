package net.softwrench.features.sr.contexts;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class SRDetailStepContext {

	private String selectedId;
	private String selectedTitle;
	private int	rowClickedOn;
	
	public String getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}
	public String getSelectedTitle() {
		return selectedTitle;
	}
	public void setSelectedTitle(String selectedTitle) {
		this.selectedTitle = selectedTitle;
	}
	public int getRowClickedOn() {
		return rowClickedOn;
	}
	public void setRowClickedOn(int rowClickedOn) {
		this.rowClickedOn = rowClickedOn;
	}
	
	
}
