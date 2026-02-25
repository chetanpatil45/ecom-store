# Ecommerce Application 



## What I am building?
### The initial requirements

A friend has come to me with the intention of starting himself in the world of business. He wants to sell products online and have an inventory management system in a single solution. You suggest an existing third - party application, but he wants more bespoke. So he came to his rockstar developer pal, you!

The website will have **USERS** who can buy an array of **PRODUCTS** in different quantities. It will hold the **INVENTORY** of stock and allow users to make **ORDER** to ship to their **ADDRESS**.

The tech bit!

| Technology    | Use            |
|---------------|----------------|
| Angular/React | Frontend       |    
| Spring Boot   | Backend server |
| MySQL         | Database       |


## Day wise Task - 

### DAY - 1 | Task - Setting up project [ecommerce-store]
- Objectives:
  1. Creating new project from start.spring.io [Spring initializer]
  2. Adding required dependency.
  3. Downloading that project and extracting zip and setup in intellij IDEA.
  4. Creating demo test controller to check everything working fine.

### DAY - 2 | Task - Setting up SQL Server/ MySQL. 
- Objectives:
  1. Set up SQL/Workbench Server.
  2. Make our spring app connect to the SQL Server/ Mysql. 
  3. Create a basic data object to test the SQL Server connection.

### DAY - 3 | Task - Creating Data Entities.
- Objectives
  1. Analyse the requirements for the data objects and their fields.
  2. Create these data objects in java.
  3. Make sure our spring boot app still starts successfully.
  4. Entities - `User`, `Product`, `Inventory`, `Order`, `Address`

### DAY - 4 | Task - User Registration
- Objectives 
  1. Add an endpoint for user Registration.
  2. Add validation on user Registration
  3. Test the Registration

### DAY - 5 | Task - User Login
- Objectives
  1. Add spring boot security to `ecom-store` application.
  2. Temporarily put an endpoint security bypass in.
  3. Provide an endpoint for users to Log in and receive a JWT.
  4. Test our login endpoint.

### DAY - 6 | Task - Request Authentication
- Objectives
  1. Authenticate requests using the JWT token from the login process.
  2. Test that the user is being authenticated.

### DAY - 7 | Task - Basic Starter Endpoints
- Objectives:
  1. Load some sample data into our database.
  2. Create some basic endpoints to get that data. 
  3. Test to ensure the correct data is presented.

### DAY - 8 | Task - Endpoint Security
- Objectives:
  1. Make all endpoints secured by authentication by default.
  2. Put exclusions to requiring authentication on specific endpoints.
  3. Test that our authentication on the endpoints work.

### DAY - 9 | Task - Email Security
- Objectives:
   1. Decide the email verification workflow.
   2. Download a tool to facilitate SMTP communication testing.
   3. Send an email at user registration with the verification link.
   4. Add endpoint to verify the user.
   5. Block logging in if the user is not verified.

### DAY - 10 | Unit Testing - I
- Objectives
  1. Build Unit test to test out code for us.
  2. Test cases for - UserService & RegisterUser(). 
  3. Aim for as high code coverage result while being realistic.

### DAY - 11 | Unit Testing - II
- Objectives 
  1. Build Unit test to test out code for us.
  2. Test cases for -  LoginUser(), JWTService, Encryption Service classes.
  3. Aim for as high code coverage result while being realistic.

### DAY - 12 | Unit Testing - III
- Objectives 
  1. Build Unit test to test out code for us.
  2. Test cases for - UserService & LoginUser().
  3. Aim for as high code coverage result while being realistic.