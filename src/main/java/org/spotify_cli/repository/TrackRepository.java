package org.spotify_cli.repository;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.models.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    public List<Track> getAll() {
        System.out.println("[+] Obteniendo lista de tracks almacenada en BBDD");
        List<Track> res = new ArrayList<>();
        List<Map<String, Object>> tracks = db.select("SELECT * FROM Track");

        tracks.stream().forEach(
                fila -> {
                    res.add(parseTrack(fila));
                }
        );
        return res;
    }

    public Track getById(String id) {
        System.out.println("[+] Obteniendo track con ID " + id);
        List<Map<String, Object>> tracks = db.select("SELECT * FROM Track WHERE id = ?", id);
        AtomicReference<Track> a = new AtomicReference<>();
        tracks.
                forEach(
                        fila -> {
                            a.set(parseTrack(fila));
                        }
                );
        return a.get();
    }

    public List<Track> getByAlbum(Album a) {
        System.out.println("[+] Obteniendo lista de tracks del album " + a.getName());
        List<Track> res = new ArrayList<>();
        List<Map<String, Object>> tracks = db.select("SELECT * FROM Track WHERE album_id = ?", a.getId());

        tracks.stream().forEach(
                fila -> {
                    res.add(parseTrack(fila));
                }
        );
        return res;
    }

    public Track getByTitle(String title) {
        System.out.println("[+] Obteniendo track con titulo " + title);
        List<Map<String, Object>> tracks = db.select("SELECT * FROM Track WHERE titulo = ?", title);
        AtomicReference<Track> a = new AtomicReference<>();
        tracks.
                forEach(
                        fila -> {
                            a.set(parseTrack(fila));
                        }
                );
        return a.get();
    }

    public Track delete(String id) {
        Track t = getById(id);
        db.delete("DELETE FROM Track WHERE id = ?", id);
        return t;
    }
    public Track update(Track entity, String id) {
        db.update("UPDATE Track SET duration = ?, titulo = ? WHERE id = ?",
                entity.getDuration(), entity.getTitulo(), id);

        return entity;
    }

    private Track parseTrack(Map<String, Object> fila) {
        return new Track(
                (String) fila.get("id"),
                (String) fila.get("album_id"),
                (String) fila.get("artist_id"),
                (Integer) fila.get("duration"),
                (String) fila.get("titulo")
        );
    }
}
