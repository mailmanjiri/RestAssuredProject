package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.Assert;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class DeleteOneAccount extends GeneretBearerToken{

		 String baseURI;
		 String createOneAccountEndpoint;
		 String deleteAccountId;
		 String updateAccountBodyFilePath;
		 String readOneAccountEndpoint;
		 String deletOneAccountEndpoint;
		 
		 public DeleteOneAccount()  {
		  baseURI = ConfigReader.getProperty("baseURI");
		  deletOneAccountEndpoint= ConfigReader.getProperty("deletOneAccountEndpoint");
		 
		  readOneAccountEndpoint= ConfigReader.getProperty("readOneAccountEndpoint");
		  deleteAccountId= "592";
		 }
		 
		 @Test(priority=1)
		 public void deleteOneAccount() {
		
		  
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .header("Authorization", "Bearer " + bearerToken)
		   .queryParam("account_id", deleteAccountId)
	       .log().all().
		  when()
		   .delete(deletOneAccountEndpoint).
		  then()
		   .log().all()
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
		  
		 String message= jp.getString("message");
		 System.out.println("account message:" + message);
		 Assert.assertEquals(message, "Account deleted successfully.");
		 
		 
		  }
		 
		
		  @Test(priority=2)
		 public void readOneAccount() {
		 
			
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .auth().preemptive().basic("demo1@codefios.com", "abc123")
		   .queryParams("account_id", deleteAccountId)
	       .log().all().
		  when()
		   .get(readOneAccountEndpoint).
		  then()
		   .log().all()
		   .extract().response();
		  
		  
		  
		  String actualresponseBody = response.getBody().asString();
		  JsonPath jp = new JsonPath(actualresponseBody);
		  
		
		 String actualMessage= jp.getString("message");
		 System.out.println("actual message :" + actualMessage);
		 
		
		 int statusCode = response.getStatusCode();
		  System.out.println("Status Code:" + statusCode);
		  Assert.assertEquals(statusCode, 404, "Status codes are NOT matching!");
		 
		 
		 Assert.assertEquals(actualMessage, "No Record Found");
		
		  
		  
		 }
		  
		 
		 
	}
