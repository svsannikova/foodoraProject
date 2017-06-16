# foodoraProject
You can download the latest version of SDK from Oracle's Java site: Java SE Downloads.
You will find the instructions for installing JDK in the downloaded files. Follow the given
instructions to install and configure the setup. Finally set the PATH and JAVA_HOME
environment variables to refer to the directory that contains Java and Javac.

install git

clone the project

Open a Terminal or Command Prompt window.

Change into the project directory like: cd C:\path\foodoraProject

Compile the project: mvn clean compile

Execute the project: mvn exec:java

Run tests: mvn test

for testing API:

GET/PUT/DELETE localhost:8080/myapp/customers/1
access: Admin(admin:admin), User(john.smith@gmail.com:password)

GET localhost:8080/myapp/customers/all
access: Admin(admin:admin)

