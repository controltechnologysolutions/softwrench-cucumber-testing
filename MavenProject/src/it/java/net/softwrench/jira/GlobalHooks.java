package net.softwrench.jira;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

public class GlobalHooks {
    private static boolean dunit = false;

    @Before
    public void beforeAll(Scenario scenario) {
        if(!dunit) {
            Runtime.getRuntime().addShutdownHook(new JiraThread());
            // do the beforeAll stuff...
            dunit = true;
        }
        
        ResultProvider.INSTANCE.addScenario(scenario);
        
    }
}