A simple application for checking if your password(s) have been compromised online## A simple application for checking if your password(s) have been compromised online

[haveibeenpwned](https://haveibeenpwned.com) recently added the ability to check if your password has been part of any leaks that has occured. If your password has been compromised, strongly consider changing your password asap

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
java -jar path/to/jar/HaveIBeenPwned.jar path/to/passwords/password.txt
```

### Output
If your password has been compromised, the program will generate an output with your password(s) hashed in SHA-1 along with the number of times your password has been compromised seperated by a : (colon)
#### Output from the [example file](#run) above
```
java -jar target/HaveIBeenPwned.jar passwords.txt 

70CCD9007338D6D81DD3B6271621B9CF9A97EA00:111658
B2E98AD6F6EB8508DD6A14CFA704BAD7F05F6FB1:21961
BFD3617727EAB0E800E62A776C76381DEFBC4145:114

```