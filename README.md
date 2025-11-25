# Laitte JavaFX Project

This is a JavaFX application for a menu/login system connected to a PostgreSQL database. It includes features such as:

- User login
- Item menu with editable prices
- Database integration with PostgreSQL
- Animated UI elements

## Folder Structure
Laitte/
│
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  ├─ com/
│  │  │  │  └─ laitte/
│  │  │  │     ├─ LaitteMain/
│  │  │  │     │  ├─ Main.java
│  │  │  │     │  ├─ Database.java
│  │  │  │     │  └─ ...other controllers (if any)
│  │  │  │     └─ Managers/
│  │  │  │        ├─ LoginController.java
│  │  │  │        ├─ HomepageController.java
│  │  │  │        └─ ...other controllers
│  │  └─ resources/
│  │     ├─ FXML/
│  │     │  ├─ LoginScene.fxml
│  │     │  ├─ Homepage.fxml
│  │     │  └─ ...other .fxml files
│  │     ├─ CSS/
│  │     │  └─ style.css
│  │     └─ Images/
│  │        ├─ Logo.png
│  │        ├─ Home.png
│  │        └─ ...other images
│
├─ lib/                # Optional if using Maven dependencies
│  └─ postgresql-42.x.x.jar
│
├─ sql/
│  ├─ setup.sql
│  └─ updates.sql
│
└─ pom.xml             # Maven project file

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## **Prerequisites**

Before running the Laitte Inventory Management System, make sure you have the following installed and configured:

Java Development Kit (JDK)
    Version: Java 25 or higher
    Download: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
    Setup:
    Ensure JAVA_HOME environment variable points to your JDK installation.
    Add bin directory to your system PATH.

Maven
    Version: 3.8 or higher recommended
    Download: https://maven.apache.org/download.cgi
    Setup:
    Add Maven bin directory to your system PATH.
    Verify installation:
        mvn -v
JavaFX
    Version: 25.0.1 (matches the Maven dependency)
    Notes: No manual download is required if using Maven; the javafx-maven-plugin handles dependencies.
## **Database Setup**

1. Open PostgreSQL (or DBeaver) and connect to the `postgres` database.
2. Run the script `sql/setup.sql` to create the necessary tables and sample data:

## **Collaboration Guidelines**

Always push SQL scripts (setup.sql, updates.sql) when schema changes occur.
Avoid committing personal PostgreSQL database files; other members should run SQL scripts to recreate the DB.

## **Running the Project**
After cloning the repository:

1. Navigate to the project root:
    cd Laitte

2. Build the project with Maven:
    mvn clean compile

3. Run the application (JavaFX plugin will handle dependencies):
    mvn javafx:run


Alternatively, open the project in VS Code and use the Run button if your IDE supports Maven.

## Dependency Management

Maven handles:
    JavaFX runtime (controls & FXML)
    PostgreSQL JDBC driver

See the VS Code Java Dependency Extension for additional dependency management.
