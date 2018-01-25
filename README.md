README
====

Dependencies :
--------------
1) JDK 1.8.0_161 or higher (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2) Maven 3.5.2 or higher, latest version as I type this (https://maven.apache.org/download.cgi)
3) MyQSL (used MariaDB here, a CentOS packaging of MySQL, version 15.1)

Set-up:
--------------
1) Start the db server.
For example, on CentOS :
>$> systemctl start mariadb
2) Create a database named "engage_bcc"
For example, on CentOS :
>$> mysql<br/>
>mysql> CREATE DATABASE engage_bcc;

Building and running the project:
--------------
1) Build the frontend by running "gulp dev" at the root directory. It will automatically build into the right folder.
2) Build the backend by going under solution, and running "mvn clean install"
3) Run the server with "mvn spring-boot:run" (it will run the backend in a embedded tomcat server)

You can then access the app on localhost:8080

In a production context, we would just deploy the jar on the server, and run it with "java -jar" ; spring-boot spares
us the trouble of installing Tomcat, as it is embedded.
All Tomcat related configuration should simply go in application.yml