README
====

What you will need :
1) JDK 1.8.0_161 or higher (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2) Maven 3.5.2 or higher, latest version as I type this (https://maven.apache.org/download.cgi)
3) MyQSL (used MariaDB here, a CentOS packaging of MySQL, version 15.1)

Set-up:

1) Start the db server.
For example, on CentOS :
$> systemctl start mariadb

2) Create a database named "engage_bcc"
For example, on CentOS :
$> mysql
$> CREATE DATABASE engage_bcc;

Building the project:

1)Build the frontend by running "gulp dev" at the root directory
2)Copy the generated "static" directory under solution/src/main/resources
3)Build the backend by going under solution, and running "mvn clean install"
4)Run the server with "mvn spring-boot:run" (it will run the backend in a embedded tomcat server)

You can then access the app on localhost:8080
