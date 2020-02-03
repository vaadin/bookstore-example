# Bookstore App Starter for Vaadin

A project example for a Vaadin application that only requires a Servlet 3.1 container to run (no other JEE dependencies). The UI is built with Java only.

The easiest way of using it is via [https://vaadin.com/start](https://vaadin.com/start) - you can choose the package naming you want.

## Prerequisites

The project can be imported into the IDE of your choice, with Java 8 or 11 installed, as a Maven project.

## Project Structure

The project is following the standard [Maven project layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).

## Workflow

To compile the entire project, run "mvn install" in the project root.

Other basic workflow steps:

- getting started
- compiling the whole project
  - run `mvn install` in project root
- developing the application
  - edit code in src/main
  - run `mvn`
  - open http://localhost:8080/
- creating a production mode war
  - run `mvn package -Pproduction` 
- running in production mode
  - run `mvn jetty:run -Pproduction`
  - open http://localhost:8080/

### Running Integration Tests

Integration tests are implemented using TestBench. The tests take a few minutes to run and are therefore included in a separate Maven profile. To run the tests using Google Chrome, execute

`mvn verify -Pit`

and make sure you have a valid TestBench license installed. If the tests fail because of an old Chrome Driver or you want to use a different browser, you'll need to update the webdrivers.xml file in the project root.

Profile `it` adds the following parameters to run integration tests:
```sh
-Dwebdriver.chrome.driver=path_to_driver
-Dcom.vaadin.testbench.Parameters.runLocally=chrome
```

If you would like to run a separate test make sure you have added these parameters to VM Options of JUnit run configuration

### Branching information:
* `master` the latest version of the starter, using the latest platform version
