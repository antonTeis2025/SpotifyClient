package org.spotify_cli.repository;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;

import java.io.IOException;
import java.util.List;

public class ArtistRepository implements Repository<Artista, String> {

    private final ApiClient api;
    private final DatabaseService db;

    public ArtistRepository(ApiClient api, DatabaseService db) {
        this.api = api;
        this.db = db;
    }

    @Override
    public Artista add(String id) throws IOException, InterruptedException {
        Artista a = api.fetchArtista(id);
        db.insert("INSERT INTO Artista(id, name, listeners, url) values (?, ?, ?, ?)",
                a.getId(),
                a.getName(),
                a.getListeners(),
                a.getUrl()
        );
        System.out.println("[+] AÑADIDO A BD " + a.toString());
        System.out.println("[+] AÑADIENDO TOP 10 ALBUMS EN CASCADA (con sus tracks)...");
        addAlbumsCascade(a);
        return a;
    }
    // hacer otra clase AlbumRepository para mayor orden
    private void addAlbumsCascade(Artista a) throws IOException, InterruptedException {
        List<Album> albums = api.fetchArtistsTop10Albums(a);
        albums.stream().forEach(
                album -> {
                    db.insert("INSERT INTO Album (id, artist_id, name, no_tracks, release_date) values (?, ?, ?, ?, ?)",
                            album.getId(),
                            album.getArtist_id(),
                            album.getName(),
                            album.getNo_tracks(),
                            album.getRelease_date()
                    );
                }
        );
    }

    @Override
    public List<Artista> getAll() {
        return List.of();
    }

    @Override
    public Artista getById(String s) {
        return null;
    }

    @Override
    public Artista update(Artista entity, String s) {
        return null;
    }

    @Override
    public Artista delete(String s) {
        return null;
    }
}
