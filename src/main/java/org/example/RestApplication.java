package org.example;

import org.example.protobuf.ProtobufMessageBodyReader;
import org.example.protobuf.ProtobufMessageBodyWriter;
import org.example.resources.HelloWorldResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("services")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Resources:
        classes.add(HelloWorldResource.class);

        classes.add(ProtobufMessageBodyWriter.class);
        classes.add(ProtobufMessageBodyReader.class);

        return classes;
    }

}
