# abnamro_assignment
This project is standalone sprint boot application with integrated tomcat.Rest API is used to make API calls (more about api documentation can be found in swagger ui) .
Technolgy Stack used 

1. Spring Boot 3.0.5
2.h2 DataDB for data persistence.
3. springDoc for api documentation
4.Junit to write unit testing.


#swagger url for api documentation and testing
http://localhost:8080/swagger-ui/index.html#/
#h2 database url
http://localhost:8080/h2-console/
configuration for h2 database.
#spring.datasource.url=jdbc:h2:mem:crm;DB_CLOSE_DELAY=-1
#spring.datasource.driverClassName=org.h2.Driver
#spring.datasource.username=sa
#spring.datasource.password=

To run the application follow the below steps -
1. use any ide eclispe ,Intellij or STS and  import the project as new maven  project.
2. Maven build pom.xml.
3. Run  RecipeApplication.java as Java Application.
4.By default application will launch on localhost:8080
5.junit test cases can be find inside test folder.Right click on any test class and run as jUnit test.

Note : The application is build using java 17 ,please ensure to use java 17 or above.

