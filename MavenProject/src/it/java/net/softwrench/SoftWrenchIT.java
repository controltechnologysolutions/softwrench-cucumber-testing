package net.softwrench;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
 
@RunWith(Cucumber.class)
@CucumberOptions(format = { "json:target/report.json" }, features = "src/it/resources", tags = "@details")
//@Cucumber.Options(format = { "json:target/report.json" }, features = "src/it/resources")
public class SoftWrenchIT {
}