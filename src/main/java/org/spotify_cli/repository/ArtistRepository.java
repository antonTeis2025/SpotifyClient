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
        System.out.println("[+]     AÑADIENDO TOP 10 ALBUMS EN CASCADA (con sus tracks)...");
        List<Album> artistAlbums = addAlbumsCascade(a);
        System.out.println("[+]     AÑADIENDO TRACKS A LOS ALBUMS");
        artistAlbums.stream().forEach(
                album -> {
                    try {
                        addAlbumTracks(album);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return a;
    }
    // hacer otra clase AlbumRepository para mayor orden
    private List<Album> addAlbumsCascade(Artista a) throws IOException, InterruptedException {
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
    // lo mismo con esta, hacer repository
    private List<Track> addAlbumTracks(Album a) throws IOException, InterruptedException {
        List<Track> tracks = api.fetchAlbumsTracks(a);
        tracks.stream()
                .forEach(track -> {
                    db.insert("INSERT INTO Track (id, album_id, artist_id, duration, titulo) values (?, ?, ?, ?, ?)",
                            track.getId(),
                            track.getAlbum_id(),
                            track.getArtist_id(),
                            track.getDuration(),
                            track.getTitulo()
                    );
                });
        return tracks;
    }

    @Override
    public List<Artista> getAll() {
        System.out.println("[+] Obteniendo lista de artistas almacenada en BBDD");
        List<Artista> res = new ArrayList<>();
        List<Map<String, Object>> artists = db.select("SELECT * FROM Artista");

        artists.stream().forEach(
                fila -> {
                    res.add(parseArtista(fila));
                }
        );
        return res;
    }



    @Override
    public Artista getById(String s) {
        System.out.println("[+] Obteniendo artista con ID " + s);
        List<Map<String, Object>> artists = db.select("SELECT * FROM Artista WHERE id = ?", s);
        AtomicReference<Artista> a = new AtomicReference<>();
        artists.
            forEach(
                    fila -> {
                        a.set(parseArtista(fila));
                    }
            );
        return a.get();
    }

    @Override
    public Artista update(Artista entity, String s) {
        Artista a = new Artista(
                s,
                entity.getName(),
                entity.getListeners(),
                entity.getUrl()
        );

        System.out.println("[+] Actualizando artista " + s + " con datos " + a.toString());

        db.update("UPDATE Artista SET name = ?, listeners = ?, url = ? WHERE id = ?",
                a.getName(),
                a.getListeners(),
                a.getUrl(),
                a.getId()
        );
        return a;
    }

    @Override
    public Artista delete(String s) {
        // primero obtiene el artista para devolverlo
        Artista a = getById(s);
        System.out.println("[+] Borrando artista " + a.toString());
        db.delete("DELETE FROM Artista WHERE id = ?", s);
        return a;
    }

    private Artista parseArtista(Map<String, Object> fila) {
        return new Artista(
                (String) fila.get("id"),
                (String) fila.get("name"),
                (Integer) fila.get("listeners"),
                (String) fila.get("url")
        );
    }
}
