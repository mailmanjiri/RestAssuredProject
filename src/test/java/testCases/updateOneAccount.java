package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.Assert;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class updateOneAccount extends GeneretBearerToken{

		 String baseURI;
		 String createOneAccountEndpoint;
		 String firstAccountId;
		 String updateAccountBodyFilePath;
		 String readAllAccountsEndpoint;
		 String readOneAccountEndpoint;
		 String updateOneaccountendPoint;
		 
		 public updateOneAccount()  {
		  baseURI = ConfigReader.getProperty("baseURI");
		  updateOneaccountendPoint= ConfigReader.getProperty("updateOneaccountendPoint");
		  updateAccountBodyFilePath= "src\\main\\java\\data\\updateAccountBody.json";
		  readOneAccountEndpoint= ConfigReader.getProperty("readOneAccountEndpoint");
		 }
		 
		 @Test(priority=1)
		 public void updateOneAccount() {
		
		  
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .header("Authorization", "Bearer " + bearerToken)
		   .body(new File(updateAccountBodyFilePath))
	       .log().all().
		  when()
		   .put(updateOneaccountendPoint).
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
		 Assert.assertEquals(message, "Account updated successfully.");
		 
		 
		  }
		 
		
		  @Test(priority=2)
		 public void readOneAccount() {
		 
			  File expectedResponseBody= new File(updateAccountBodyFilePath);
				 JsonPath jp2= new JsonPath(expectedResponseBody);
				 
				 String expectedAccountname= jp2.getString("account_name");
				 System.out.println("expected account name" + expectedAccountname);
				 
				 String expectedAccount_ID= jp2.getString("account_id");
				 System.out.println("expected account id" + expectedAccount_ID);
				 
				 String expectedDescription = jp2.getString("description");
				 System.out.println("expected description" + expectedDescription);
				  
				 String expectedaccountNumber= jp2.getString("account_number");
				 System.out.println("expected account number" + expectedaccountNumber);
				 
				 String expectedBalance= jp2.getString("balance");
				 System.out.println("expected account balance" + expectedBalance);
				 
				 String expectedContactPerson= jp2.getString("contact_person");
				 System.out.println("expected contact person: " + expectedContactPerson);
		  
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .auth().preemptive().basic("demo1@codefios.com", "abc123")
		   .queryParams("account_id", expectedAccount_ID)
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
		 
		 
		 
		 Assert.assertEquals(actualAccountname, expectedAccountname);
		 Assert.assertEquals(actualDescription, expectedDescription);	
		 Assert.assertEquals(actualaccountNumber, expectedaccountNumber);
		 Assert.assertEquals(actualBalance, expectedBalance);
		 Assert.assertEquals(actualContactPerson, expectedContactPerson);
		  
		  
		 }
		  
		 
		 
	}
