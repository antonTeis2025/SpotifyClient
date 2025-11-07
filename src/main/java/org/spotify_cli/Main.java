package org.spotify_cli;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.repository.ArtistRepository;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // DatabaseService db = new DatabaseService();
        // ApiClient api = new ApiClient();
        // System.out.println(api.getToken());
        // System.out.println(api.fetch("/artists/0jeYkqwckGJoHQhhXwgzk3/albums?limit=10"));
        // Artista a = api.fetchArtista("0jeYkqwckGJoHQhhXwgzk3");
        // List<Album> albumList =  api.fetchArtistsTop10Albums(a);
        // api.fetchAlbumsTracks(albumList.get(0)).stream()
        //        .forEach(System.out::println);

        ArtistRepository ar = new ArtistRepository(
                new ApiClient(),
                new DatabaseService()
        );

        ar.add("0jeYkqwckGJoHQhhXwgzk3");
        ar.getAll();
        System.out.println(ar.getById("0jeYkqwckGJoHQhhXwgzk3").toString());
    }
}
