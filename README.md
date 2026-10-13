# Understanding the code
## Setup Instructions 
- To run the project : `.\mvnw.cmd spring-boot:run`


### understanding the project strucutre.

- src/main/java

    Your application’s Java source code. Controllers and other Java classes will go here.

- src/main/resources

    Application configuration and non-Java resources.

- application.properties

    Configuration settings. Currently, it contains only spring.application.name=slow, which names the application.
- src/test/java

    Automated tests for the application. We will examine the generated test later.

- static and templates

    Places for static files and server-rendered HTML templates. We do not need either for our JSON API right now.
