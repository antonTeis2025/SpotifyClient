package org.spotify_cli.repository;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Track;

import java.io.IOException;
import java.util.List;

public class TrackRepository {
    private final DatabaseService db;
    private final ApiClient api;

    public TrackRepository(RepositoryResources repositoryResources) {
        api = repositoryResources.getApi();
        db = repositoryResources.getDb();
    }

    public Track add(Track track) {
        System.out.println("[+] Añadiendo "+ track.toString());
        db.insert("INSERT INTO Track (id, album_id, artist_id, duration, titulo) values (?, ?, ?, ?, ?)",
                track.getId(),
                track.getAlbum_id(),
                track.getArtist_id(),
                track.getDuration(),
                track.getTitulo()
        );
        return track;
    }

    public List<Track> addAlbumTracks(Album a) throws IOException, InterruptedException {
        System.out.println("[+] Añadiendo todos los tracks de "+ a.toString());
        List<Track> tracks = api.fetchAlbumsTracks(a);
        tracks.stream()
                .forEach(track -> {
                    add(track);
                });
        return tracks;
    }

}
