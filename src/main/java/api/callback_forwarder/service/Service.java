package api.callback_forwarder.service;

import api.callback_forwarder.repository.Repository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class Service {
    @Inject
    Repository repository;
    private ObjectMapper objectMapper = new ObjectMapper();
    public Uni<UUID> createNewUser(String name, String email, String password){
        return repository.createNewUser(name, email, password).onItem().transform(id -> id);
    }

    public Uni<UUID> addBaseUrl(UUID userId, String baseUrl) {
        return repository.addBaseUrl(userId, baseUrl).onItem().transform(id -> id);
    }

    public Uni<UUID> addFwdUrl(String fwdUrl, UUID baseUrlId) {
        return repository.addFwdUrl(fwdUrl, baseUrlId).onItem().transform(id -> id);
    }

    public Uni<List<String>> getFwdUrls(UUID id) {
        return repository.getFwdUrls(id).onItem().transform(fwdUrls -> fwdUrls);
    }
    public Uni<Void> fwd(JsonNode body, String fwdUrl) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fwdUrl))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return Uni.createFrom().nullItem();
    }
}
