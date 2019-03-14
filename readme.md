## A simple application for checking if your password(s) have been compromised online

[haveibeenpwned](https://haveibeenpwned.com) recently added the ability to check if your password has been part of any leaks that has occured. This is accomplished by sending 5 characters of your password hashed with [SHA-1](https://en.wikipedia.org/wiki/SHA-1) and comparing the full hash of your password against all potential matches that was returned from the API. If your password has been compromised, strongly consider changing your password asap

## How-to
### Java version
The project utilizes some new language features introduced in Java 11. You **will** need both a JDK and JRE capable of Java 11

### Build
The project is built with Maven. To build an executable jar, stand at the root of the project and run

```
mvn package
```

This will generate a jar file named "HaveIBeenPwned.jar" in target/

### Run
To run the program, you will first need a file with all of the passwords you want to check. Each password must be on seperate lines
> ---*passwords.txt*---

> Password1

> Password123

> correcthorsebatterystaple

Run the program with the follwing command

```
java -jar path/to/jar/HaveIBeenPwned.jar path/to/passwords/passwords.txt
```
To check several files at a time, just pass them as seperate command line arguments


### Output
If your password has been compromised, the program will generate an output with your password(s) along with the number of times your password has been compromised seperated by a : (colon).
#### Output from the [example file](#run) above
```
java -jar target/HaveIBeenPwned.jar passwords.txt 

Checking passwords from passwords.txt
-------------------------------------
Password1:111658
Password123:21961
correcthorsebatterystaple:114

```
