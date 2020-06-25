package org.example.protobuf;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import java.util.List;

public class ProtobufMediaType {
    /**
     * The media-type for binary ProtocolBuffers-encoded objects.
     */
    public static final String APPLICATION_PROTOBUF = "application/x-protobuf";

    /**
     * The media-type {@value #APPLICATION_PROTOBUF}.
     */
    public static final MediaType APPLICATION_PROTOBUF_TYPE =
            new MediaType("application", "x-protobuf");

    /**
     * Determines the correct {@link MediaType} of the HTTP response from the headers of the HTTP request.
     *
     * @param requestHeaders The request headers.
     * @return the response MediaType
     */
    public MediaType get(HttpHeaders requestHeaders) {
        if (requestHeaders != null && requestHeaders.getAcceptableMediaTypes() != null) {
            List<MediaType> acceptableMediaTypes = requestHeaders.getAcceptableMediaTypes();

            if (acceptableMediaTypes.contains(ProtobufMediaType.APPLICATION_PROTOBUF_TYPE)) {
                return ProtobufMediaType.APPLICATION_PROTOBUF_TYPE;
            } else if (acceptableMediaTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
                return MediaType.APPLICATION_JSON_TYPE;
            }
        }

        return ProtobufMediaType.APPLICATION_PROTOBUF_TYPE;
    }
}
