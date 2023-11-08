# TreeManager

Tree manager is a simple application for managing tree structured data. It allows the user to create, modify and delete
nodes of a managed tree. 
The application comes with:
* a backend server, which exposes REST API endpoints for node operations and stores the nodes in the database,
* a frontend application, which allows the user to perform operations and see the results on the interactive UI.

## Requirements

* Java: **17**
* Maven: **3.8.3**
* Node.js: **20.9.0**
* npm: **10.2.1**

## How to run

In order to run TreeManager, a user can use provided bash/batch scripts.

### TreeManager backend 

To install TreeManager backend application, run `run_TreeManager.sh install` on Linux or `run_TreeManager.bat install` 
on Windows.

To run the application, run `run_TreeManager.sh start` or `run_TreeManager.bat start` respectively, `start` operation 
will lock the terminal, to stop the application use `CTRL + C`.

Alternatively, a user can go to TreeManager directory and run:
* `mvn clean install`
* `java -jar target/TreeManager-<version>.jar`, where `<version>` depends on the generated jar version

By default, the backend application server listens on http://localhost:8080.

### TreeManagerUI

Similarly to the backend application, to install the UI, run `run_TreeManagerUI.sh install` or `run_TreeManagerUI.bat install`.

To run the UI application, run `run_TreeManagerUI.sh start` or `run_TreeManagerUI.bat start`. Same as in backend application,
the terminal will be locked and the application can be stopped with `CTRL + C`.

Alternatively, to start TreeManagerUI, go to TreeManagerUI directory and run:
* `npm run build`
* `npm run start`

By default, the UI application listens on http://localhost:4200.

## Project structure

The project contains:
* **TreeManager** - backend server application, implemented with Java 17 and Spring Boot.
* **TreeManagerUI** - frontend application, implemented with Angular

```bash
.
├── TreeManager/
│   └── src/main/java/com/treemanager/treemanager/
│       ├── node/ - contains classes responsible for handling the TreeNode related logic
│       │   ├── api/ - contains the controller and DTO entites
│       │   ├── domain/model/ - contains DB entity and JpaRepository
│       │   └── service/ - contains service classes that process certain business logic
│       └── tree/ - contains classes responsible for handling the tree (only getting the whole hierarchy)
│           ├── api/ - contains the controller
│           └── service/ - contains the service class for querying the tree structure from DB
└── TreeManagerUI/
    └── src/
        └── app/ - contains the root app component
            ├── node/ - contains the single node component
            └── tree/ - contains the tree component and related services for sending http requests and processing the tree state
```

## Database

For demonstration and development purposes, TreeManager uses the H2 in-memory database. However, migration to other DBMS 
shouldn't be a problem, since implemented entities and custom queries use basic SQL operations. 
Therefore, providing proper provider dependency and changing the application.properties should be enough to use other database.

## Further development

* parametrize backend/frontend addresses and ports and use them in source code
* use other DBMS
* dockerize backend and frontend applications and provide a docker-compose file for launching the whole application with docker
