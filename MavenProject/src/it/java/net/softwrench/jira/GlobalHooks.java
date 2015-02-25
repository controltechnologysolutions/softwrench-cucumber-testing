package net.softwrench.jira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class GlobalHooks {
    private static boolean dunit = false;
    
    @Autowired
    private JiraIssueCreator creator;
    
    @Autowired
	private Environment env;

    @Before
    public void beforeAll(Scenario scenario) {
        if(!dunit) {
            Runtime.getRuntime().addShutdownHook(new JiraThread(creator, env.getProperty("jira.createIssues")));
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