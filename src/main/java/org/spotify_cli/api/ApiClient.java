package org.spotify_cli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.spotify_cli.config.Config;
import org.spotify_cli.models.Artista;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final String baseUrl = "https://api.spotify.com/v1";

    @Getter
    private final String token;

    public ApiClient() throws IOException, InterruptedException {
        token = fetchTokenAPI();
    }

    /**
     * Esta funcion debe ejecutarse cada vez que se instancia la aplicacion
     */
    // TODO intentar hacer que justo calcule la ultima hora para aprovechar mejor los tokens.

    public String fetchTokenAPI() throws IOException, InterruptedException {
        String formData = "grant_type=" + URLEncoder.encode("client_credentials", StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(Config.getClient_id(), StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(Config.getClient_secret(), StandardCharsets.UTF_8);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> res = httpClient.send(
                req,
                HttpResponse.BodyHandlers.ofString()
        );


        if (res.statusCode()==200) {
            try {
                JsonNode node = mapper.readTree(res.body());
                System.out.println("[+] Token obtenido correctamente");
                return node.get("access_token").asText();
            } catch (Exception e) {
                System.err.println("[!] Respuesta del servidor: " + res.body());
                throw e;
            }
        } else {
            System.err.println("[!] Respuesta del servidor: " + res.body());
            throw new IOException("[!] No fue posible obtener token de usuario, saliendo...");
        }

    }


    public String fetch(String endpoint) throws IOException, InterruptedException {
        String url = baseUrl + endpoint;

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> res = httpClient.send(
                req,
                HttpResponse.BodyHandlers.ofString()
        );

        if (res.statusCode()==200) {
            System.out.println("[+] Request GET exitosa a " + url);
            return res.body();
        } else {
            System.err.println(res.body());
            throw new IOException("[!] Fallo al hacer peticion "+ url);
        }

    }

    public Artista fetchArtista(String id) throws IOException, InterruptedException {
        String json = fetch("/artists/" + id);
        JsonNode node = mapper.readTree(json);

        try {
            return new Artista(
                    node.at("/id").asText(), // id
                    node.at("/name").asText(), // nombre
                    node.at("/followers/total").asInt(), // listeners
                    node.at("/external_urls/spotify").asText() // url perfil
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
