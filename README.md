# CSV Parser

This software can generate JSON files from a CSV file.

These executable files:
* bin/build
* bin/clean
* bin/parse 

are tested on Mac OSX, and Ubuntu Linux.

### Installation
The installation requires internet connection, because it needs to download JUnit libraries for unit testing.

The build script requires following software:
* wget
* JDK 8 or above

installed and working.


To build using gradle:

```shell script
./gradlew build
```

To build and to download dependencies:

```shell script
bin/build
```

The outputs of build script are:
* CsvParser-1.0.jar
* lib/apiguardian-api-1.1.0.jar
* lib/junit-jupiter-api-5.6.2.jar
* lib/junit-platform-console-standalone-1.6.0.jar

The junit-platform-console-standalone-1.6.0.jar is required to launch a standalone JUnit application.
The apiguardian-api-1.1.0.jar is optional, but without it the compilation of test code will generate some warnings.

To clean, run following:

```shell script
bin/clean
```

To clean, using gradle:

```shell script
./gradlew clean
```


### Execution

```shell script
java -jar CsvParser-1.0.jar product.csv result
```

```shell script
./gradlew run --args="product.csv result"
```

Another convenient script is parse
```shell script
bin/parse product.csv result
```

## Project Structure

This project was built using Gradle, using recommended gradle directory structure.

Software code are put inside
*src/main/java*

The test code are inside *src/test/java*

Gradle wrapper is included repository, to ensure successful execution of application build and running using gradle.
