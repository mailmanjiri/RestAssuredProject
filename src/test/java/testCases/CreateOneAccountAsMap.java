package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.Assert;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class CreateOneAccountAsMap extends GeneretBearerToken{

		 String baseURI;
		 String createOneAccountEndpoint;
		 String firstAccountId;
		 String createAccountBodyFilePath;
		 String readAllAccountsEndpoint;
		 String readOneAccountEndpoint;
		 Map <String, String> createPayloadMap;
		 
		 public CreateOneAccountAsMap()  {
		  baseURI = ConfigReader.getProperty("baseURI");
		  createOneAccountEndpoint= ConfigReader.getProperty("createOneAccountEndpoint");
		  createAccountBodyFilePath= "src\\main\\java\\data\\createAccountBody.json";
		  readAllAccountsEndpoint= ConfigReader.getProperty("readAllAccountsEndpoint");
		  readOneAccountEndpoint= ConfigReader.getProperty("readOneAccountEndpoint");
		  createPayloadMap= new HashMap <String, String>();
		 }
		 
		 public Map<String, String> createAccountBodyMap(){
			 createPayloadMap.put("account_name", "Techfios account abc"); 
			 createPayloadMap.put("account_number", "4545346"); 
			 createPayloadMap.put("description", "test account"); 
			 createPayloadMap.put("balance", "2000.00"); 
			 createPayloadMap.put( "contact_person", "xyz"); 
			 
			 
			return createPayloadMap;
			 
		 }
		 
		 @Test(priority=1)
		 public void createOneAccount() {
		
		  
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .header("Authorization", "Bearer " + bearerToken)
		   .body(createAccountBodyMap())
	       .log().all().
		  when()
		   .post(createOneAccountEndpoint).
		  then()
		   .log().all()
		   .extract().response();
		  
		  int statusCode = response.getStatusCode();
		  System.out.println("Status Code:" + statusCode);
		  Assert.assertEquals(statusCode, 201, "Status codes are NOT matching!");
		  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  System.out.println("Response Header Content-Type:" + responseHeaderContentType);
		  Assert.assertEquals(responseHeaderContentType, "application/json", "Status Content-Types are NOT matching!");
		  
		  long responseTimeInMilliSecs = response.getTimeIn(TimeUnit.MILLISECONDS);
		  System.out.println("Response Time InMilliSecs:" + responseTimeInMilliSecs);
		  
		  if(responseTimeInMilliSecs <=2000) {
		   System.out.println("Response time is within range.");
		  }else {
		   System.out.println("Response time is out of range!");
		  }   
		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body:" + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  
		 String message= jp.getString("message");
		 System.out.println("account message:" + message);
		 Assert.assertEquals(message, "Account created successfully.");
		 
		 
		  }
		 
		 @Test(priority=2)
		 public void readAllAccounts() {
		 
		  
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .header("Authorization", "Bearer " + bearerToken).
	       //.log().all().
		  when()
		   .get(readAllAccountsEndpoint).
		  then()
		  // .log().all()
		   .extract().response();
		  
		  int statusCode = response.getStatusCode();
		  System.out.println("Status Code:" + statusCode);
		  Assert.assertEquals(statusCode, 200, "Status codes are NOT matching!");
		  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  System.out.println("Response Header Content-Type:" + responseHeaderContentType);
		  Assert.assertEquals(responseHeaderContentType, "application/json", "Status Content-Types are NOT matching!");
		  
		  long responseTimeInMilliSecs = response.getTimeIn(TimeUnit.MILLISECONDS);
		  System.out.println("Response Time InMilliSecs:" + responseTimeInMilliSecs);
		  
		  if(responseTimeInMilliSecs <=2000) {
		   System.out.println("Response time is within range.");
		  }else {
		   System.out.println("Response time is out of range!");
		  }   
		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body:" + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  
		  firstAccountId = jp.getString("records[0].account_id");
		  System.out.println("First account id :" + firstAccountId);   
		  
		  if(firstAccountId != null) {
			  System.out.println("first account id is not null");
		  } else {
			  System.out.println("first account id is null");
		  }
		  
		 }
		 
		 
		 @Test(priority=3)
		 public void readOneAccount() {
		 
		  
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .auth().preemptive().basic("demo1@codefios.com", "abc123")
		   .queryParams("account_id", firstAccountId)
	       .log().all().
		  when()
		   .get(readOneAccountEndpoint).
		  then()
		   .log().all()
		   .extract().response();
		  
		  
		  
		  String actualresponseBody = response.getBody().asString();
		  JsonPath jp = new JsonPath(actualresponseBody);
		  
		
		 String actualAccountname= jp.getString("account_name");
		 System.out.println("actual name :" + actualAccountname);
		 
		 String actualDescription = jp.getString("description");
		 System.out.println("actual desciption:"+ actualDescription);
		  
		 String actualaccountNumber= jp.getString("account_number");
		 System.out.println("actual account number:" + actualaccountNumber);
		 
		 String actualBalance= jp.getString("balance");
		 System.out.println("actual balance"+ actualBalance);
		 
		 String actualContactPerson= jp.getString("contact_person");
		 System.out.println("actual contact person: " + actualContactPerson);
		 
		
		 String expectedAccountname= createAccountBodyMap().get("account_name");
		 System.out.println("expected account name" + expectedAccountname);
		 
		 String expectedDescription = createAccountBodyMap().get("description");
		 System.out.println("expected description" + expectedDescription);
		  
		 String expectedaccountNumber= createAccountBodyMap().get("account_number");
		 System.out.println("expected account number" + expectedaccountNumber);
		 
		 String expectedBalance= createAccountBodyMap().get("balance");
		 System.out.println("expected account balance" + expectedBalance);
		 
		 String expectedContactPerson= createAccountBodyMap().get("contact_person");
		 System.out.println("expected contact person: " + expectedContactPerson);
		 
		 Assert.assertEquals(actualAccountname, expectedAccountname);
		 Assert.assertEquals(actualDescription, expectedDescription);	
		 Assert.assertEquals(actualaccountNumber, expectedaccountNumber);
		 Assert.assertEquals(actualBalance, expectedBalance);
		 Assert.assertEquals(actualContactPerson, expectedContactPerson);
		  
		  
		 }
		  
		 
		 
	}
