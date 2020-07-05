#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
BUILD_DIR="$(dirname "$DIR")/build/classes/java/main"
SRC_DIR="$(dirname "$DIR")/src/main/java"
JAR_FILE="CsvParser-1.0.jar"

mkdir -p $BUILD_DIR
javac -sourcepath $SRC_DIR -d $BUILD_DIR $SRC_DIR/org/muntasir/lab/CsvParser.java
mkdir -p lib
jar cfe CsvParser-1.0.jar org.muntasir.lab.CsvParser -C build/classes/java/main/ .

LIB_JUNIT="$(dirname "$DIR")/lib/junit-jupiter-api-5.6.2.jar"
if [ ! -f "$LIB_JUNIT" ]; then
  wget https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.6.2/junit-jupiter-api-5.6.2.jar -P lib/
fi

LIB_APIGUARDIAN="$(dirname "$DIR")/lib/apiguardian-api-1.1.0.jar"
if [ ! -f "$LIB_APIGUARDIAN" ]; then
  wget https://repo1.maven.org/maven2/org/apiguardian/apiguardian-api/1.1.0/apiguardian-api-1.1.0.jar -P lib/
fi

JUNIT_CONSOLE="$(dirname "$DIR")/lib/junit-platform-console-standalone-1.6.0.jar"
if [ ! -f "$JUNIT_CONSOLE" ]; then
  wget https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.6.0/junit-platform-console-standalone-1.6.0.jar -P lib/
fi

# Build test suite
TEST_BUILD="$(dirname "$DIR")/build/classes/java/test"
TEST_SRC="$(dirname "$DIR")/src/test/java"
mkdir -p $TEST_BUILD
javac -sourcepath $TEST_SRC -cp $JAR_FILE:$LIB_JUNIT:$LIB_APIGUARDIAN -d $TEST_BUILD $TEST_SRC/org/muntasir/lab/*.java

# Run Test Suit to validate
java -jar $JUNIT_CONSOLE -cp $JAR_FILE:$TEST_BUILD --scan-classpath
