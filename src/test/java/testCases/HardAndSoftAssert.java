package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.Assert;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

public class HardAndSoftAssert extends GeneretBearerToken{

		 String baseURI;
		 String createOneAccountEndpoint;
		 String firstAccountId;
		 String createAccountBodyFilePath;
		 String readAllAccountsEndpoint;
		 String readOneAccountEndpoint;
		 SoftAssert softAssert;
		 
		 public HardAndSoftAssert()  {
		  baseURI = ConfigReader.getProperty("baseURI");
		  createOneAccountEndpoint= ConfigReader.getProperty("createOneAccountEndpoint");
		  createAccountBodyFilePath= "src\\main\\java\\data\\createAccountBody.json";
		  readAllAccountsEndpoint= ConfigReader.getProperty("readAllAccountsEndpoint");
		  readOneAccountEndpoint= ConfigReader.getProperty("readOneAccountEndpoint");
		  softAssert= new SoftAssert();
		 
		 }
		 
		 @Test(priority=1)
		 public void createOneAccount() {
		
		  
		  Response response = 
		  
		  given()
		   .baseUri(baseURI)
		   .header("Content-Type","application/json")
		   .header("Authorization", "Bearer " + bearerToken)
		   .body(new File(createAccountBodyFilePath)).
	      // .log().all().
		  when()
		   .post(createOneAccountEndpoint).
		  then()
		   //.log().all()
		   .extract().response();
		  
		  //changed status code 200 instead of 201 but because it is soft assert rest of the code will get execute
		  int statusCode = response.getStatusCode();
		  System.out.println("Status Code:" + statusCode);
		  softAssert.assertEquals(statusCode, 200, "Status codes are NOT matching!");
		  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  System.out.println("Response Header Content-Type:" + responseHeaderContentType);
		  softAssert.assertEquals(responseHeaderContentType, "application/json", "Status Content-Types are NOT matching!");
		  
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
		 softAssert.assertEquals(message, "Account created successfully.");
		 
		 softAssert.assertAll();
		 
		  }
		 
		
		 
		 
	}
