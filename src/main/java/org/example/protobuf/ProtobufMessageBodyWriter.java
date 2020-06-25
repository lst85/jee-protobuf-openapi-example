package org.example.protobuf;

import com.google.protobuf.Message;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Provider
@Produces({
        ProtobufMediaType.APPLICATION_PROTOBUF,
        MediaType.APPLICATION_JSON
})
public class ProtobufMessageBodyWriter implements MessageBodyWriter<Message> {

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(aClass);
    }

    @Override
    public void writeTo(Message message, Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap,
                        OutputStream outputStream) throws IOException {

        if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            ProtobufJsonWriter.printTo(message, writer);
            writer.flush(); // don't close! we must not close the outputstream!
            return;
        }

        message.writeTo(outputStream);
    }
}
