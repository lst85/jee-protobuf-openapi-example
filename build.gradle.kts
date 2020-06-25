import com.google.protobuf.gradle.*
import io.swagger.v3.plugins.gradle.tasks.*

plugins {
    war
    id("com.google.protobuf") version "0.8.12"
    id("fish.payara.micro-gradle-plugin") version "1.0.3"
    id("io.swagger.core.v3.swagger-gradle-plugin") version "2.1.2"
}

val protobufVersion = "3.6.1"
val payaraVersion = "5.194"

repositories {
    jcenter()
    maven {
        // Local repository contains protoc-gen-jsonschema binaries.
        url = uri("file://${project.projectDir}/ext/")
    }
}

dependencies {
    implementation("io.swagger.core.v3:swagger-annotations:2.1.2")
    implementation("com.google.protobuf:protobuf-java:${protobufVersion}")
    implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")

    providedCompile("jakarta.platform:jakarta.jakartaee-api:8.0.0")
    runtimeOnly("org.webjars:swagger-ui:3.18.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

protobuf {
    // Configure the protoc executable
    protoc {
        // Download from repositories
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("openapi") {
            artifact = "com.github.lst85:protoc-gen-jsonschema:0.9.3"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "openapi" plugin whose spec is defined above
                // Generate OpenAPI file from ProtoBuf definitions
                id("openapi") {
                    option("open_api")
                    option("open_api_template=${project.projectDir}/src/main/resources/openapi/openapi-template.json")
                    option("out_file=openapi-types.json")
                }
            }
        }
    }
}

tasks {
    // Takes the OpenAPI file that was generated from the ProtoBuf definitions and enhances it with the
    // service definitions from the Swagger/OpenAPI annotations
    named<ResolveTask>("resolve") {
        dependsOn(":generateProto")
        outputDir = File("${project.buildDir}/resources/main/META-INF/")
        outputFileName = "openapi"
        outputFormat = ResolveTask.Format.JSON
        openApiFile = File("${project.buildDir}/generated/source/proto/main/openapi/openapi-types.json")
        classpath = sourceSets.main.get().runtimeClasspath
        resourcePackages = setOf("org.example.resources")
        prettyPrint = true
    }

    named<War>("war") {
        dependsOn(":resolve")
    }
}

payaraMicro {
    payaraVersion = payaraVersion
    deployWar = true
}