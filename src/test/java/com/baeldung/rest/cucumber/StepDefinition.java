package com.baeldung.rest.cucumber;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class StepDefinition {

   // private static final String CREATE_PATH = "/create";
    private static final String APPLICATION_JSON = "application/json";

    private final InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("cucumber.json");
   
    private final String jsonString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();

  //  private final InputStream jsongetInputStream = this.getClass().getClassLoader().getResourceAsStream("cucumber.json");
    
  //  private final String jsongetString = new Scanner(jsongetInputStream, "UTF-8").useDelimiter("\\Z").next();

 private final InputStream jsonupdateInputStream = this.getClass().getClassLoader().getResourceAsStream("cucumberupdate.json");
    
    private final String jsonupdateString = new Scanner(jsonupdateInputStream, "UTF-8").useDelimiter("\\Z").next();

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @When("^users upload data on a project$")
    public void usersUploadDataOnAProject() throws IOException {
        

        HttpPost request = new HttpPost("https://petstore.swagger.io/v2/pet");

        StringEntity entity = new StringEntity(jsonString);
        request.addHeader("content-type", APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());
       
    }

//    @When("^users want to get information on the '(.+)' project$")
    @When("users want to get information on the {string} pets")
    public void usersGetInformationOnAProject(String projectName) throws IOException {
       
        HttpGet request = new HttpGet("https://petstore.swagger.io/v2/pet/findByStatus?status=available");
         request.addHeader("accept", APPLICATION_JSON);
        HttpResponse httpResponse = httpClient.execute(request);
        String responseString = convertResponseToString(httpResponse);
        
        assertThat(responseString, containsString("id"));
        
    }
    
    	@When("users update data on a project")
    	public void users_update_data_on_a_project() throws ClientProtocolException, IOException {
    		HttpPut request = new HttpPut("https://petstore.swagger.io/v2/pet");

            StringEntity entity = new StringEntity(jsonupdateString);
            request.addHeader("content-type", APPLICATION_JSON);
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);

            assertEquals(200, response.getStatusLine().getStatusCode());
    		
    		
    	}


    		@When("users want to delete pets")
    		public void users_want_to_delete_pets() throws ClientProtocolException, IOException {
    		HttpDelete request = new HttpDelete("https://petstore.swagger.io/v2/pet/100");

            request.addHeader("content-type", APPLICATION_JSON);
           
            HttpResponse response = httpClient.execute(request);

            assertEquals(200, response.getStatusLine().getStatusCode());
    		}


    	

    		@Then("the requested data is deleted")
    		public void the_requested_data_is_deleted() {
    		    // Write code here that turns the phrase above into concrete actions
    		}




    
    @Then("the server should handle it and return a success status")
    public void theServerShouldReturnASuccessStatus() {
    }

    @Then("the requested data is returned")
    public void theRequestedDataIsReturned() {
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }
}