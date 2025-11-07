package org.spotify_cli;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.repository.ArtistRepository;
import org.spotify_cli.repository.RepositoryResources;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Inicializamos los servicios de BBDD y de API
        RepositoryResources repositoryResources = new RepositoryResources(
                new ApiClient(),
                new DatabaseService()
        );
        // Inicializamos repositorio de artistas con los recursos ya cargados
        ArtistRepository ar = new ArtistRepository(repositoryResources);

        ar.add("0jeYkqwckGJoHQhhXwgzk3");
        ar.getAll();
        System.out.println(ar.getById("0jeYkqwckGJoHQhhXwgzk3").toString());
        ar.update(new Artista("", "Crusi", 42069, "http://google.es"), "0jeYkqwckGJoHQhhXwgzk3");
        System.out.println(ar.getById("0jeYkqwckGJoHQhhXwgzk3").toString());
        ar.delete("0jeYkqwckGJoHQhhXwgzk3");
    }
}
