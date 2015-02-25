package net.softwrench.jira.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import net.softwrench.jira.FailedTestInfo;
import net.softwrench.jira.JiraIssueCreator;
import net.softwrench.jira.ScenarioResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;


@Component
public class JiraIssueCreatorImpl implements JiraIssueCreator {

	private static final Logger logger = Logger
			.getLogger(JiraIssueCreatorImpl.class);
	
	private static final int JIRA_SUCCESS_CODE = 201;
	private static final int JIRA_SUCCESS_CODE_ATTACHMENTS = 200;

	private String featureIdField;
	private String featureIdFieldLong;

	@Autowired
	private Environment env;

	@PostConstruct
	public void init() {
		featureIdField = env.getProperty("jira.featureIdField");
		featureIdFieldLong = env.getProperty("jira.featureIdFieldLong");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.softwrench.jira.impl.JiraIssueCreator#createJiraIssue(net.softwrench
	 * .jira.ScenarioResult)
	 */
	@Override
	public void createJiraIssue(ScenarioResult scenario) {

		int existing = -1;
		try {
			existing = queryForScenario(scenario.getId());
		} catch (ClientProtocolException e1) {
			logger.error(
					"Couldn't check for existing Jira tickets. Abort ticket creation.",
					e1);
		} catch (IOException e1) {
			logger.error(
					"Couldn't check for existing Jira tickets. Abort ticket creation.",
					e1);
		} catch (URISyntaxException e) {
			logger.error(
					"Couldn't check for existing Jira tickets. Abort ticket creation.",
					e);
		}


		if (existing == 0) {

			String summary = "Test \"" + scenario.getScenarioName()
					+ "\" failed";
			StringBuffer description = new StringBuffer();
			StringBuffer stacktrace = new StringBuffer();
			List<byte[]> images = new ArrayList<byte[]>();
			for (FailedTestInfo info : scenario.getFailedTestInfos()) {
				description.append(info.getText() + "\n\n ========== \n\n");
				images.addAll(info.getImages());
				stacktrace.append(info.getStacktrace() + "\n\n");
			}

			JsonObjectBuilder issuetype = Json.createObjectBuilder().add(
					"name", env.getProperty("jira.issueType"));
			JsonObjectBuilder project = Json.createObjectBuilder().add("key",
					env.getProperty("jira.project"));
			JsonObjectBuilder fields = Json.createObjectBuilder()
					.add("project", project).add("summary", summary)
					.add("description", description.toString())
					.add("issuetype", issuetype)
					.add("customfield_10702", stacktrace.toString())
					.add(featureIdFieldLong, scenario.getId());
			JsonObjectBuilder payload = Json.createObjectBuilder().add(
					"fields", fields);
			StringWriter stWriter = new StringWriter();
			JsonWriter jsonWriter = Json.createWriter(stWriter);
			jsonWriter.writeObject(payload.build());
			jsonWriter.close();
			String jsonPayLoad = stWriter.toString();

			logger.info("Creating JIRA issue: " + summary);
			try {
				makeHttpPostRequest(jsonPayLoad, images, scenario.getScenarioName());

			} catch (MalformedURLException e) {
				logger.error("Error when creating Jira issue.", e);
			} catch (IOException e) {
				logger.error("Error when creating Jira issue.", e);
			} catch (Exception e) {
				logger.error("Exception was thrown: " + e.getMessage(), e);
			}
		}
	}

	private void makeHttpPostRequest(String jsonPayLoad, List<byte[]> images, String feature)
			throws ClientProtocolException, IOException, Exception {
		CloseableHttpClient httpClient = createHttpClient();
		
		

		HttpPost postRequest = new HttpPost(env.getProperty("jira.url")
				+ "issue/");

		StringEntity params = new StringEntity(jsonPayLoad);
		params.setContentType("application/json");
		postRequest.setEntity(params);
		HttpResponse response = executeRequest(httpClient, postRequest);
		logger.info(String.format("statusLine: %s%n", response.getStatusLine()));
		logger.info(String.format("statusCode: %d%n", response.getStatusLine()
				.getStatusCode()));
		if (response.getStatusLine().getStatusCode() != JIRA_SUCCESS_CODE) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
		}

		int imageIdx = 0;
		if (images.size() > 0) {
			imageIdx++;
			
			// get create issue key
			JsonReader json = Json.createReader(response.getEntity().getContent());
			JsonObject jsonObj = json.readObject();
			String key = jsonObj.getString("key");
	
			for (byte[] image : images) {
				HttpPost postRequestAttachments = new HttpPost(
						env.getProperty("jira.url") + "issue/" + key + "/attachments");
				postRequestAttachments.setHeader("X-Atlassian-Token", "nocheck");
				logger.info("Adding attachments: " + env.getProperty("jira.url") + "issue/" + key + "/attachments");
				
				// save screenshot
				PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				Resource resource = resolver.getResource("classpath:/tmp/anchor.txt");
				String tmpFolder;
				tmpFolder = resource.getFile().getParentFile().getAbsolutePath();
				File imageFile = new File(tmpFolder + "/screenshot-" + feature.replace(" ", "-") + "-" + imageIdx + ".png");
				if (!imageFile.exists())
					imageFile.createNewFile();
				
				FileOutputStream stream = new FileOutputStream(imageFile);
				stream.write(image);
				stream.close();
				   
				// create message
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				
				FileBody fileBody = new FileBody(imageFile);
				builder.addPart("file", fileBody);
				
				HttpEntity entity = builder.build();
				postRequestAttachments.setEntity(entity);
				
				// submit
				response = executeRequest(httpClient, postRequestAttachments);
				
				logger.info("Status Line of attachment: " + response.getStatusLine());
				logger.info("Status code of attachment: " + response.getStatusLine()
						.getStatusCode());
				
				if (response.getStatusLine().getStatusCode() != JIRA_SUCCESS_CODE_ATTACHMENTS) {
					int code = response.getStatusLine().getStatusCode();
					httpClient.close();
					throw new RuntimeException("Failed : HTTP error code : "
							+ code);
				}
			}
		}
		
		httpClient.close();
		
	}

	private HttpResponse executeRequest(CloseableHttpClient httpClient,
			HttpUriRequest postRequest) throws IOException,
			ClientProtocolException {
		HttpClientContext localContext = HttpClientContext.create();
		try {
			postRequest.addHeader(new BasicScheme().authenticate(
					new UsernamePasswordCredentials(env
							.getProperty("jira.user"), env
							.getProperty("jira.password")), postRequest,
					localContext));
		} catch (AuthenticationException a) {
			a.printStackTrace();
		}

		HttpResponse response = httpClient.execute(postRequest);
		return response;
	}

	private CloseableHttpClient createHttpClient() {
		Credentials creds = new UsernamePasswordCredentials(
				env.getProperty("jira.user"), env.getProperty("jira.password"));
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, creds);
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setDefaultCredentialsProvider(credentialsProvider).build();
		return httpClient;
		
	}

	private int queryForScenario(String featureid)
			throws ClientProtocolException, IOException, URISyntaxException {
		CloseableHttpClient httpClient = createHttpClient();

		URIBuilder builder = new URIBuilder(env.getProperty("jira.url")
				+ "search");
		builder.addParameter("jql",
				"project=" + env.getProperty("jira.project") + " and "
						+ featureIdField + "~ \"" + featureid + "\"");
		URI uri = builder.build();
		logger.info("Connect to: " + uri);
		HttpGet postRequest = new HttpGet(uri);
		HttpResponse response = executeRequest(httpClient, postRequest);

		JsonReader json = Json.createReader(response.getEntity().getContent());
		JsonObject jsonObj = json.readObject();
		logger.info("Result: " + jsonObj.toString());
		int total = 0;

		JsonArray issues = jsonObj.getJsonArray("issues");
		if (issues == null)
			return 0;

		for (JsonValue v : issues) {
			JsonObject fields = ((JsonObject) v).getJsonObject("fields");
			String feature = fields.getString(featureIdFieldLong);
			// the jira search only allows the contains operator, so we have to
			// make
			// sure the feature is the same
			if (feature.equals(featureid))
				total++;
		}

		json.close();
		httpClient.close();

		return total;
	}
}
