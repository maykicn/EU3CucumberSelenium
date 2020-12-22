package com.vytrack.step_definitions;

import com.google.gson.internal.$Gson$Preconditions;
import com.vytrack.pages.*;
import com.vytrack.utilities.BrowserUtils;
import com.vytrack.utilities.ConfigurationReader;
import com.vytrack.utilities.DBUtils;
import com.vytrack.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class ContactsStepDefs {

    @Given("the user logged in as {string}")
    public void the_user_logged_in_as(String userType) {
        //go to login page
        Driver.get().get(ConfigurationReader.get("url"));
        //based on input enter that user information
        String username =null;
        String password =null;

        if(userType.equals("driver")){
            username = ConfigurationReader.get("driver_username");
            password = ConfigurationReader.get("driver_password");
        }else if(userType.equals("salesmanager")){
            username = ConfigurationReader.get("sales_manager_username");
            password = ConfigurationReader.get("sales_manager_password");
        }else if(userType.equals("storemanager")){
            username = ConfigurationReader.get("store_manager_username");
            password = ConfigurationReader.get("store_manager_password");
        }
        //send username and password and login
        new LoginPage().login(username,password);
    }

    @Then("the user should see following options")
    public void the_user_should_see_following_options(List<String> menuOptions) {
        BrowserUtils.waitFor(2);
        //get the list of webelement and convert them to list of string and assert
        List<String> actualOptions = BrowserUtils.getElementsText(new DashboardPage().menuOptions);

        Assert.assertEquals(menuOptions,actualOptions);
        System.out.println("menuOptions = " + menuOptions);
        System.out.println("actualOptions = " + actualOptions);
    }


    @When("the user logs in using following credentials")
    public void the_user_logs_in_using_following_credentials(Map<String,String> userInfo) {
        LoginPage loginPage=new LoginPage();
        loginPage.login(userInfo.get("username"),userInfo.get("password"));
        BrowserUtils.waitFor(3);
        String actualName=new DashboardPage().getUserName();
        String expectedName=userInfo.get("firstname")+" "+userInfo.get("lastname");
        Assert.assertEquals(expectedName,actualName);

    }

    @When("the user clicks the {string} from contacts")
    public void the_user_clicks_the_from_contacts(String email) {
        ContactsPage contactsPage=new ContactsPage();
        BrowserUtils.waitFor(5);
        contactsPage.getContactEmail(email).click();




    }

    @Then("the information should be same with database")
    public void the_information_should_be_same_with_database() {
        // get information from UI for actual
        BrowserUtils.waitFor(2);
        ContactInfoPage contactInfoPage=new ContactInfoPage();


        String actualFullName=contactInfoPage.contactFullName.getText();
        String actualEmail=contactInfoPage.email.getText();
        String actualPhone=contactInfoPage.phone.getText();

        System.out.println("actualFullName = " + actualFullName);
        System.out.println("actualEmail = " + actualEmail);
        System.out.println("actualPhone = " + actualPhone);




        //get information from database

        //we are getting only one row of result
        //query for retrieving first name last name email phone number
        String query="select concat(first_name,' ',last_name) as \"full_name\",e.email,phone\n" +
                "from orocrm_contact c join orocrm_contact_email e\n" +
                "on c.id = e.owner_id join orocrm_contact_phone p\n" +
                "on c.id = p.owner_id\n" +
                "where e.email='mbrackstone9@example.com'";

        //get info and save in the map
        Map<String, Object> rowMap = DBUtils.getRowMap(query);
        String expectedFullName=(String)rowMap.get("full_name");
        String expectedEmail=(String)rowMap.get("email");
        String expectedPhone=(String)rowMap.get("phone");

        System.out.println("expectedFullName = " + expectedFullName);
        System.out.println("expectedPhone = " + expectedPhone);
        System.out.println("expectedEmail = " + expectedEmail);


        //assertion

        Assert.assertEquals(expectedFullName,actualFullName);
        Assert.assertEquals(expectedEmail,actualEmail);
        Assert.assertEquals(expectedPhone,actualPhone);















    }



}