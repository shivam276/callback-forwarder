package api.callback_forwarder.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.Path;

import java.util.UUID;

public class AddFwdUrlRequest {
    @JsonProperty("fwd_url")
    private final String fwdUrl;
    @JsonProperty("base_url_id")
    private final UUID baseUrlId;

    public AddFwdUrlRequest(String fwdUrl, UUID baseUrlId) {
        this.fwdUrl = fwdUrl;
        this.baseUrlId = baseUrlId;
    }

    public String getFwdUrl() {
        return fwdUrl;
    }

    public UUID getBaseUrlId() {
        return baseUrlId;
    }
}
