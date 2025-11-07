package org.spotify_cli.repository;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;

import java.io.IOException;
import java.util.List;

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
    public Album add(Album album) throws IOException, InterruptedException {
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
    public List<Album> addAll(Artista a) throws IOException, InterruptedException {
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

}
