package api.callback_forwarder.repository;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class Repository {
    @Inject
    PgPool client;
    public Uni<UUID> createNewUser(String name, String email, String password){
        String query = """
                INSERT INTO users (name, email, password)
                VALUES ($1, $2, $3)
                RETURNING id
                """;
       return client.preparedQuery(query).execute(Tuple.of(name, email, password)).map(row -> row.iterator().next().getUUID("id"));
    }

    public Uni<UUID> addBaseUrl(UUID userId, String baseUrl) {
        String query = """
                INSERT INTO base_urls (user_id, base_url)
                VALUES ($1, $2)
                RETURNING id
                """;
        return client.preparedQuery(query).execute(Tuple.of(userId, baseUrl)).map(row -> row.iterator().next().getUUID("id"));
    }

    public Uni<UUID> addFwdUrl(String fwdUrl, UUID baseUrlId) {
        String query = """
                INSERT INTO fwd_urls (fwd_url, base_url_id)
                VALUES ($1, $2)
                RETURNING id
                """;
        return client.preparedQuery(query).execute(Tuple.of(fwdUrl, baseUrlId)).map(row -> row.iterator().next().getUUID("id"));
    }

    public Uni<List<String>> getFwdUrls(UUID id) {
        String query = """
                SELECT fwd_url FROM fwd_urls WHERE base_url_id = $1
                """;
        List<String> fwdUrls = new ArrayList<>();
        return client.preparedQuery(query).execute(Tuple.of(id)).onItem().transform(rows -> {
            rows.forEach(row -> fwdUrls.add(row.getString("fwd_url")));
            return fwdUrls;
        });
    }
}
