package net.softwrench.features.sr.contexts;

import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class CreationContext {

	private WebElement newItemComposition;

	public WebElement getNewItemComposition() {
		return newItemComposition;
	}

	public void setNewItemComposition(WebElement newItem) {
		this.newItemComposition = newItem;
	}
	
	
}
