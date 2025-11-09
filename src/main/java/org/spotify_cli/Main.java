package org.spotify_cli;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.config.Config;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.repository.AlbumRepository;
import org.spotify_cli.repository.ArtistRepository;
import org.spotify_cli.repository.RepositoryResources;
import org.spotify_cli.repository.TrackRepository;
import org.spotify_cli.view.Menu;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Inicializamos los servicios de BBDD y de API
        RepositoryResources repositoryResources = new RepositoryResources(
                new ApiClient(),
                new DatabaseService()
        );
        // Inicializamos repositorios con los recursos ya cargados
        TrackRepository tr = new TrackRepository(repositoryResources);
        AlbumRepository alr = new AlbumRepository(repositoryResources, tr);
        ArtistRepository arr = new ArtistRepository(repositoryResources, alr);

        if (Config.getDatabaseInitData()) {
            // si la opcion esta activada, añade datos de muestra
            arr.addCascade("0jeYkqwckGJoHQhhXwgzk3");
            arr.addCascade("3bgsNtcf5d5h9jbQbohfBK");
            arr.addCascade("3GMBcFbI21Tbi1POVBmCwb");
        }

        // INICIA EL MENU pasandole todos los repositorios
        Menu.start(arr, alr, tr);
    }
}
