package org.example.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.protobuf.ProtobufMediaType;
import org.example.protobuf.ResponseMediaType;
import org.example.services.helloworld.HelloWorld.HelloWorldRequest;
import org.example.services.helloworld.HelloWorld.HelloWorldResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Tag(name = "HelloWorld Service")
@Path("helloworld")
@Consumes({
        MediaType.APPLICATION_JSON,
        ProtobufMediaType.APPLICATION_PROTOBUF
})
@Produces({
        MediaType.APPLICATION_JSON,
        ProtobufMediaType.APPLICATION_PROTOBUF
})
public class HelloWorldResource {

    private HttpHeaders httpHeaders;

    @Context
    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @POST
    @Operation(summary = "Returns the BoM positions matching the given request.", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/HelloWorldResponse")
                    ))
    })

    public Response helloWorld(@Parameter(schema = @Schema(
            implementation = Object.class,
            ref = "#/components/schemas/HelloWorldRequest"))
                                                 HelloWorldRequest request) {
        HelloWorldResponse response = HelloWorldResponse.newBuilder().setMessage("HelloWorld").build();
        return Response.ok(response)
                .type(ResponseMediaType.get(httpHeaders))
                .build();
    }
}
