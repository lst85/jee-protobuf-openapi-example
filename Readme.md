Example how to use the protoc-gen-jsonschema to create a Java EE REST service that accepts both ProtoBuf and JSON and has OpenAPI specification. 

Maven: How to build and execute:
```
$ mvn clean install
$ mvn payara-micro:start

Open http://localhost:8080 in your browser.
```

Gradle: How to build and execute:
```
$ ./gradlew assemble
$ ./gradlew microStart

Open http://localhost:8080 in your browser.
```