package net.softwrench.jira;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class GlobalHooks {
    private static boolean dunit = false;
    
    @Autowired
    private JiraIssueCreator creator;

    @Before
    public void beforeAll(Scenario scenario) {
        if(!dunit) {
            Runtime.getRuntime().addShutdownHook(new JiraThread(creator));
            // do the beforeAll stuff...
            dunit = true;
        }
        
        ResultProvider.INSTANCE.addScenario(scenario);      
    }
    
    @After
    public void after(Scenario scenario) {
    	// register if scenario failed
    	ResultProvider.INSTANCE.evaluateScenario(scenario);
    }
    
    
}