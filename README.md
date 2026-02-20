# Ecommerce Application 



## What I am building?
### The initial requirements

A friend has come to me with the intention of starting himself in the world of business. He wants to sell products online and have an inventory management system in a single solution. You suggest an existing third - party application, but he wants more bespoke. So he came to his rockstar developer pal, you!

The website will have **USERS** who can buy an array of **PRODUCTS** in different quantities. It will hold the **INVENTORY** of stock and allow users to make **ORDER** to ship to their **ADDRESS**.

The tech bit!

| Technology        | Use           |
|-------------------|---------------|
|  Angular/React    | Frontend      |    
|  Spring Boot      | Backend server |
|  MySQL            | Database      |


## Day wise Task - 

### DAY - 1 | Task - Setting up project [ecom-store]
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
  3. Provide an endpoint for users to Login and receive a JWT.
  4. Test our login endpoint.

### DAY - 6 | Task - Request Authentication
- Objectives
  1. Authenticate requests using the JWT token from the login process.
  2. Test that the user is being authenticated.