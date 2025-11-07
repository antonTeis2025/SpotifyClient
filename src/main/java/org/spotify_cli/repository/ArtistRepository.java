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


    private final AlbumRepository albumRepository;
    private final ApiClient api;
    private final DatabaseService db;

    public ArtistRepository(RepositoryResources repo, AlbumRepository albumRepository) {
        api = repo.getApi();
        db = repo.getDb();
        this.albumRepository = albumRepository;
    }

    /**
     * Añade tanto el artista como sus 10 primeros albumes con sus tracks
     * @param id
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Artista addCascade(String id) throws IOException, InterruptedException {
        Artista a = add(id);
        List<Album> artistAlbums = albumRepository.addAllCascade(a);
        return a;
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
        return a;
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
