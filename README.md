# Bookstore App Starter for Vaadin

A project example for a Vaadin application that only requires a Servlet 3.1 container to run (no other JEE dependencies). The UI is built with Java only.

The easiest way of using it is via [https://vaadin.com/start](https://vaadin.com/start) - you can choose the package naming you want.

## Prerequisites

The project can be imported into the IDE of your choice, with Java 21 installed, as a Maven project.

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
  - run `mvn package` 
- running in production mode
  - run `mvn jetty:run-war`
  - open http://localhost:8080/

### Running Integration Tests

Integration tests are implemented using TestBench. The tests take a few minutes to run and are therefore included in a separate Maven profile. To run the tests using Google Chrome, execute

```bash
mvn verify -Pit
```

and make sure you have a valid TestBench license installed. If the tests fail because of an old Chrome Driver or you want to use a different browser, you'll need to update the webdrivers.xml file in the project root.

### Running Load Tests

Record load tests with TestBench `testbench-converter-plugin` plugin and run
tests locally:

```bash
mvn verify -Plocal
```

```bash
# Record only without running load tests
mvn verify -Plocal,record-only

# record only with custom response check (response under 2s)
mvn clean verify -Plocal,record-only -Dk6.checks.custom="ALL|response under 2s|(r) => r.timings.duration < 2000"
```

Run recorded tests in remote server (requires `testbench-loadtest-support` on
runtime):

```bash
mvn verify -Premote -Dk6.appHost=staging.example.com
```

Load Test summary and error log is written to `target/k6/tests/report/` folder.

Try out how slow database affects the load test results by starting the server with `simulateSlowDB=true` system property.

```bash
# making a copy of jar to bookstore-example.jar
cp target/bookstore-example-1.0-SNAPSHOT.jar target/bookstore-example.jar
# start production jar package simulating slow SampleCrudView
java -DsimulateSlowDB=true -jar target/bookstore-example.jar
```
Load tests for SampleCrudView will fail due to threshold or custom checks. See examples below:
```bash 
# run load test on the server running on localhost
mvn verify -Premote -Dk6.appHost=localhost

# run with 50 VUs and 1m duration
mvn verify -Premote -Dk6.appHost=localhost -Dk6.vus=50 -Dk6.duration=1m

# run with 2s httpReqDurationP99 threshold 
mvn verify -Premote -Dk6.appHost=localhost -Dk6.vus=50 -Dk6.duration=1m -Dk6.threshold.httpReqDurationP99=2000

# allow checks to fail without aborting the test 
mvn verify -Premote -Dk6.appHost=localhost -Dk6.threshold.checksAbortOnFail=false

# run with warmup iteration before the actual load test
mvn verify -Premote -Dk6.appHost=localhost -Dwarmup=true

# run 2m stress test with 1000 VUs
mvn verify -Premote -Dk6.appHost=localhost -Dk6.vus=1000 -Dk6.duration=2m -Dk6.loadPattern=stress -Dk6.threshold.checksAbortOnFail=false
```


### Branching information:
* `vX` where X is the largest number is the latest version of the starter, using the latest platform version
