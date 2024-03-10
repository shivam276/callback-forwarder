package api.callback_forwarder.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.Path;

import java.util.UUID;

public class AddBaseUrlRequest {
    @JsonProperty("user_id")
    private final UUID userId;
    @JsonProperty("base_url")
    private final String baseUrl;

    public AddBaseUrlRequest(UUID userId, String baseUrl) {
        this.userId = userId;
        this.baseUrl = baseUrl;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
