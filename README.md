[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin-flow/Lobby#?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# Bookstore App Starter for Vaadin Flow

A project example for a Vaadin application that only requires a Servlet 3.1 container to run (no other JEE dependencies). The UI is built with Java only.

The easiest way of using it is via [https://vaadin.com/start](https://vaadin.com/start) - you can choose the package naming you want.

## Prerequisites

The project can be imported into the IDE of your choice, with Java 8 installed, as a Maven project.

## Project Structure

The project consists of the following three modules:

- parent project: common metadata and configuration
- bookstore-starter-flow-ui: main application module, development time
- bookstore-starter-flow-backend: POJO classes and mock services being used in ui

## Workflow

To compile the entire project, run "mvn install" in the parent project.

Other basic workflow steps:

- getting started
- compiling the whole project
  - run `mvn install` in parent project
- developing the application
  - edit code in the ui module
  - run `mvn jetty:run` in ui module
  - open http://localhost:8080/
- creating a production mode war
  - run `mvn package -Dvaadin.productionMode ` in the ui module or in the parent module
- running in production mode
  - run `mvn jetty:run -Dvaadin.productionMode` in ui module
  - open http://localhost:8080/

### Branching information:
* `master` the latest version of the starter, using latest platform snapshot
* `v10` the version for Vaadin 10
* `v11` the version for Vaadin 11
* `v12` the version for Vaadin 12
* `v13` the version for Vaadin 13
