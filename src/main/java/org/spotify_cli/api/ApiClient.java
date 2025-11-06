package org.spotify_cli.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.spotify_cli.config.Config;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.models.Track;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Devuelve un artista a partir de su ID
     * @param id ID del artista en la API de spotify
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Artista fetchArtista(String id) throws IOException, InterruptedException {
        System.out.println("[+] Obteniendo informacion del artista con ID " + id);
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

    /**
     * Devuelve los top 10 albumes de un artista
     * @param a Objeto artista
     * @return
     */
    public List<Album> fetchArtistsTop10Albums(Artista a) throws IOException, InterruptedException {
        System.out.println("[+] Obteniendo 10 ultimos albums de " + a.getName());
        List<Album> res = new ArrayList<>();
        String json = fetch(String.format("/artists/%s/albums?limit=10", a.getId()));
        JsonNode node = mapper.readTree(json).path("items");

        for (JsonNode item: node) {
            res.add(new Album(
                  item.path("id").asText(),  //  id;
                    a.getId(), // artist_id
                  item.path("name").asText(),  // name;
                  item.path("release_date").asText()  // release_date;
            ));
        }

        return res;
    }

    /**
     * Devuelve una lista de tracks de un album
     * @param a Objeto album
     * @return
     */
    public List<Track> fetchAlbumsTracks(Album a) throws IOException, InterruptedException {
        System.out.println("[+] Obteniendo tracks del album " + a.getName());
        List<Track> res = new ArrayList<>();
        String json = fetch(String.format("/albums/%s/tracks", a.getId()));
        JsonNode node = mapper.readTree(json);

        for (JsonNode item: node.at("/items")) {
            res.add(new Track(
                    item.at("/id").asText(), // id
                    a.getId(), // album_id
                    a.getArtist_id(), // artist_id
                    item.at("/duration_ms").asInt(), // duration
                    item.at("/name").asText() // titulo
            ));
        }

        return res;
    }

}
