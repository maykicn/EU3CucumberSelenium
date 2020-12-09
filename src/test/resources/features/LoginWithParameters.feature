Feature: Login as different users

  Scenario: login as a driver user
    Given the user is on the login page
    When the user logs in using "User10" and "UserUser123"
    Then the user should be able to login
    And the title contains "Dashboard"


  Scenario: login as a driver
    Given the user is on the login page
    Then the user logged in as "storemanager"
    And the user should be able to login
    And the title contains "Dashboard"
    #driver,storemanager,salesmanager
   #you will have one step definition and it will handle differnet usertypes