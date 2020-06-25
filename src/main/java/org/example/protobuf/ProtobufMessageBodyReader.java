package org.example.protobuf;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Provider
@Consumes({
        ProtobufMediaType.APPLICATION_PROTOBUF,
        MediaType.APPLICATION_JSON
})
public class ProtobufMessageBodyReader implements MessageBodyReader<Message> {
    private static final Logger LOGGER = Logger.getLogger(ProtobufMessageBodyReader.class.getName());

    private final Map<Class<Message>, Method> newBuilderCache = new ConcurrentHashMap<>();

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(aClass);
    }

    @Override
    public Message readFrom(Class<Message> aClass, Type type, Annotation[] annotations,
                            MediaType mediaType, MultivaluedMap<String, String> multivaluedMap,
                            InputStream inputStream) throws IOException {

        Message.Builder messageBuilder;
        try {
            Method newBuilder = this.newBuilderCache.computeIfAbsent(aClass, this::getNewBuilderMethod);
            messageBuilder = (Message.Builder) newBuilder.invoke(type);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            LOGGER.severe("Failed to instantiate builder for Protobuf message [clazz='" + aClass + "']");
            throw new InternalServerErrorException("Failed to instantiate builder for Protobuf message", ex);
        }

        if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            JsonFormat.parser().merge(reader, messageBuilder);
        } else {
            messageBuilder.mergeFrom(inputStream);
        }

        return messageBuilder.build();
    }

    private Method getNewBuilderMethod(Class<Message> messageClazz) {
        try {
            return messageClazz.getMethod("newBuilder");
        } catch (NoSuchMethodException ex) {
            LOGGER.severe("Failed to find method 'newBuilder' on Protobuf message [clazz='\"+ aClass + \"']");
            throw new InternalServerErrorException("Failed to find method 'newBuilder' on Protobuf message");
        }
    }
}
