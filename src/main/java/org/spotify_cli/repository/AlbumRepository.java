package org.spotify_cli.repository;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AlbumRepository {

    private final ApiClient api;
    private final DatabaseService db;
    private final TrackRepository trackRepository;

    public AlbumRepository(RepositoryResources repositoryResources, TrackRepository trackRepository) {
        api = repositoryResources.getApi();
        db = repositoryResources.getDb();
        this.trackRepository = trackRepository;
    }

    /**
     * Añade un album
     * @param album
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private Album add(Album album) throws IOException, InterruptedException {
        System.out.println("[+] Añadiendo "+ album.toString());
        db.insert("INSERT INTO Album (id, artist_id, name, no_tracks, release_date) values (?, ?, ?, ?, ?)",
                album.getId(),
                album.getArtist_id(),
                album.getName(),
                album.getNo_tracks(),
                album.getRelease_date()
        );
        return album;
    }

    /**
     * Añade 10 primeros albumes de un artista
     * @param a
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<Album> addAll(Artista a) throws IOException, InterruptedException {
        System.out.println("[+] Añadiendo top 10 albumes de " + a.toString());
        List<Album> albums = api.fetchArtistsTop10Albums(a);
        albums.stream().forEach(
                album -> {
                    try {
                        add(album);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return albums;
    }

    /**
     * Añade los 10 primeros albumes de un artista con sus tracks
     * @param a
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Album> addAllCascade(Artista a) throws IOException, InterruptedException {
        List<Album> albums = addAll(a);
        albums.forEach( album ->  {
            try {
                trackRepository.addAlbumTracks(album);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return albums;
    }

    /**
     * Obtiene todos los albums de la BBDD local
     * @return
     */
    public List<Album> getAll() {
        List<Album> albums = new ArrayList<>();
        List<Map<String, Object>> filas = db.select("SELECT * FROM Album");
        filas.forEach(
                fila -> albums.add(parseAlbum(fila))
        );
        return albums;
    }

    public Album getById(String id) {
        System.out.println("[+] Obteniendo album con ID " + id);
        List<Map<String, Object>> artists = db.select("SELECT * FROM Album WHERE id = ?", id);
        AtomicReference<Album> a = new AtomicReference<>();
        artists.
                forEach(
                        fila -> {
                            a.set(parseAlbum(fila));
                        }
                );
        return a.get();
    }

    public Album getByName(String name) {
        System.out.println("[+] Obteniendo album con nombre " + name);
        List<Map<String, Object>> artists = db.select("SELECT * FROM Album WHERE name = ?", name);
        AtomicReference<Album> a = new AtomicReference<>();
        artists.
                forEach(
                        fila -> {
                            a.set(parseAlbum(fila));
                        }
                );
        return a.get();
    }

    public List<Album> getByArtist(Artista a) {
        System.out.println("[+] Obteniendo albums del artista " + a.getName());
        List<Album> albums = new ArrayList<>();

        List<Map<String, Object>> filas = db.select("SELECT * FROM Album WHERE artist_id = ?", a.getId());
        filas.forEach(
                fila -> albums.add(parseAlbum(fila))
        );
        return albums;
    }

    public Album delete(String id) {
        Album a = getById(id);
        db.delete("DELETE FROM Album WHERE id = ?", id);
        return a;
    }

    public Album update(Album entity, String id) {
        db.update("UPDATE Album SET name = ?, no_tracks = ?, release_date = ? WHERE id = ?",
                entity.getName(), entity.getNo_tracks(), entity.getRelease_date(), id);
        return entity;
    }


    private Album parseAlbum(Map<String, Object> fila) {
        return new Album(
                (String) fila.get("id"),
                (String) fila.get("artist_id"),
                (String) fila.get("name"),
                fila.get("release_date").toString(),
                (Integer) fila.get("no_tracks")
        );
    }
}
