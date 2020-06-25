package org.example.protobuf;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import java.util.List;

public final class ResponseMediaType {

    private ResponseMediaType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines the correct {@link MediaType} of the HTTP response from the headers of the HTTP request.
     *
     * @param requestHeaders the request headers
     * @return the response MediaType
     */
    public static MediaType get(HttpHeaders requestHeaders) {

        // If the user accepts JSON we respond with JSON. In all other cases we respond with Protocol Buffers.

        if(requestHeaders != null && requestHeaders.getAcceptableMediaTypes() != null) {
            List<MediaType> acceptableMediaTypes = requestHeaders.getAcceptableMediaTypes();

            if(acceptableMediaTypes.contains(ProtobufMediaType.APPLICATION_PROTOBUF_TYPE)) {
                return ProtobufMediaType.APPLICATION_PROTOBUF_TYPE;
            } else if (acceptableMediaTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
                return MediaType.APPLICATION_JSON_TYPE;
            }
        }

        return ProtobufMediaType.APPLICATION_PROTOBUF_TYPE;
    }
}
