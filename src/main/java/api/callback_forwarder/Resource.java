package api.callback_forwarder;

import api.callback_forwarder.models.AddBaseUrlRequest;
import api.callback_forwarder.models.AddFwdUrlRequest;
import api.callback_forwarder.models.CreateUserRequest;
import api.callback_forwarder.service.Service;
import com.fasterxml.jackson.databind.JsonNode;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/callback-forwarder")
public class Resource {
    @Inject
    Service service;

    @POST
    @Path("/create-user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(CreateUserRequest createUserRequest) {
        return service.createNewUser(createUserRequest.getName(), createUserRequest.getEmail(), createUserRequest.getPassword())
                .onItem().transform(id -> Response.ok().entity(Map.of("id", id)).build());
    }

    @POST
    @Path("/add-base-url")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addBaseUrl(AddBaseUrlRequest addBaseURLRequest) {
        return service.addBaseUrl(addBaseURLRequest.getUserId(), addBaseURLRequest.getBaseUrl())
                .onItem().transform(id -> Response.ok().entity(Map.of("id", id)).build());
    }

    @POST
    @Path("/add-fwd-url")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addFwdUrl(AddFwdUrlRequest addFwdUrlRequest) {
        return service.addFwdUrl(addFwdUrlRequest.getFwdUrl(), addFwdUrlRequest.getBaseUrlId())
                .onItem().transform(id -> Response.ok().entity(Map.of("id", id)).build());
    }

    @POST
    @Path("/fwd/{id}")
    public Uni<Response> fwd(@PathParam("id") UUID id, JsonNode body){
        return service.getFwdUrls(id)
                .onItem().transformToUni(
                        urls -> {
                            for(String url: urls)
                            {
                                try {
                                    service.fwd(body, url);
                                } catch (IOException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            return Uni.createFrom().item(Response.ok().build());
                        }
                );
    }


}