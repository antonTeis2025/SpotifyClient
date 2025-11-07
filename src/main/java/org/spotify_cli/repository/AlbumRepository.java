package org.spotify_cli.repository;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;

import java.io.IOException;
import java.util.List;

public class AlbumRepository{

    private final ApiClient api;
    private final DatabaseService db;

    public AlbumRepository(RepositoryResources repositoryResources) {
        api = repositoryResources.getApi();
        db = repositoryResources.getDb();
    }

    public List<Album> addAll(Artista a) throws IOException, InterruptedException {
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
        return albums;
    }

}
