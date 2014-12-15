package net.softwrench;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;
 
@RunWith(Cucumber.class)
@Cucumber.Options(format = { "json:target/report.json" }, features = "src/it/resources")
public class SoftWrenchIT {
}